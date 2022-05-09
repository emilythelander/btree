package cs321.create;

import cs321.btree.*;
import cs321.common.ParseArgumentException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class creates a btree binary file containing all subsequences
 * of requested length as parsed from an input .gbk file
 * 
 * @authors Zach Sullivan, Emily Thelander, Andrew Sorensen
 *
 */
public class GeneBankCreateBTree
{
	private static BTree btree;
	/**
	 * @param args - see usage message
	 */
	public static void main(String[] args)
	{	
		long time = System.currentTimeMillis();
		//Parse args
		GeneBankCreateBTreeArguments pArgs = parseArgumentsAndHandleExceptions(args);
		System.out.println("Creating BTree...");

		//Parse file and get sequences of Genes
		ArrayList<StringBuilder> sequences = (ArrayList<StringBuilder>) ParseFile(pArgs.getGbkFileName());

		//Calculate optimal degree if user input was 0
		if (pArgs.getDegree() == 0) {

			//long + char + int  + int : offset + isLeaf + numKeys + numChildren
			int nodeMetaData = 8 + 2 + 4 + 4;

			//Need to solve the equation:
			// 18 + 8(2t) + 12(2t-1) <= 4096
			int optimalDegree = (4096 - nodeMetaData + 12);//leaves us with 16t + 24t <= 4090
			optimalDegree /= 40; //divide the 40 away leaving us with the floor of t (integer division)
			btree = new BTree(pArgs.getSubsequenceLength(), optimalDegree, pArgs.getGbkFileName());
			System.out.println("Optimal degree t = " + optimalDegree + " selected for you.");
		}
		else {// user provided degree
			btree = new BTree(pArgs.getSubsequenceLength(), pArgs.getDegree(), pArgs.getGbkFileName());
		}

		//If we need to be using a cache then use it
		if (pArgs.isUseCache()) {
			btree.createCache(pArgs.getCacheSize());
		}

		//Break all sequences down into subsequences and insert them into the BTree
		for (StringBuilder builder :  sequences) {
			encodeSubSequenceAndInsert(btree, builder.toString(), pArgs.getSubsequenceLength());
		}

		//Close the BTree to write potential cache and any remaining metadata to file
		btree.close();
		//Check if we need a dump file, if so make one
		dump(pArgs);

		
		if (pArgs.getDebug() != 2) {
			createDataBase(pArgs);
		}
		System.out.println("Program Complete");
		time  = (System.currentTimeMillis() - time) / 1000;
		System.out.println("Time elapsed (seconds): " + time);
	}

	/**
	 * This method handles creating an output dump file when the user has debug set to 1
	 * @param T - the GeneBankCreateBTreeArguments object that contains the user arguments for this program
	 */
	public static void dump(GeneBankCreateBTreeArguments T) {
		int debug = T.getDebug();

		if(debug == 1 || debug == 2) {//check debug option
			try {
				System.out.println("Creating dump file...");
				File dump = new File("dump");//Create filename

				if(dump.exists() && dump.isFile()) {//Delete currently existing file
					dump.delete();
				}

				dump.createNewFile();

				FileWriter dumpOutWriter = new FileWriter(dump); //Write it

				dumpOutWriter.write(btree.toString());
				dumpOutWriter.close();
				System.out.println("Created dump file: 'dump'");
			}
			catch (Exception e){
				System.err.println(e.toString());
			}
		}
	}

	/**
	 * Calls method to parse user input, prints error and usage message upon error
	 * @param args - the user argument inputs from the command line 
	 * @return - An object containing the parsed user input that matches the expected input
	 */
	private static GeneBankCreateBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
	{
		GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = null;
		try
		{
			geneBankCreateBTreeArguments = parseArguments(args);
		}
		catch (ParseArgumentException e)
		{
			printUsageAndExit(e.getMessage());
		}
		catch (NumberFormatException e) {
			printUsageAndExit(e.getMessage());
		}
		return geneBankCreateBTreeArguments;
	}

	/**
	 * Does what it says on the tin
	 * @param errorMessage - the error message from the exception that is causing the system to exit
	 */
	private static void printUsageAndExit(String errorMessage)
	{
		System.out.println(errorMessage);
		System.out.println
		("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]\n");
		System.out.println
		("<0/1(no/with Cache)>: determines whether or not a cache will be used, if a cache is used a cache size must be specified.");
		System.out.println
		("<degree>: the degree of the bTree node, if 0 is specified then the optimal degree will be determined.");
		System.out.println
		("<gbk file>: The gene bank sequence file that wants to be analyzed."); 
		System.out.println
		("<sequence length>: the squence length that must be between 1 and 31.");
		System.out.println
		("[<cache size>]: the size of the cache must be between 100 and 500.");
		System.out.println
		("[<debug level>]: the default level will be 0, a value of 1 will produce a dump file. A value of 2 will produce a dump file and no database");

		System.exit(1);
	}

	/**
	 * This method does the actual parsing of the input arguments
	 * 
	 * @param args - the user argument inputs from the command line 
	 * @return - An object containing the parsed user inputs according to expectations
	 * @throws ParseArgumentException - user input did not match expectations
	 */
	public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws ParseArgumentException
	{
		int cache;
		int cacheSize = 0;
		Boolean cacheBoo = false;
		int degree;
		String gbk;
		int sequence;
		int debug = 0;

		//Check number of args
		if (args.length < 4 || args.length > 6)
		{
			throw new ParseArgumentException
			("Incorrect number of arguments provided");
		}

		//Get cache option
		cache = Integer.parseInt(args[0]);
		if (cache != 0 && cache != 1) {
			throw new ParseArgumentException
			("Illegal argument: cache must be 0 or 1.");
		}

		if (cache == 1) {//cache requested

			if(args.length < 5) {//Need at min 5 args if cache is requested
				throw new ParseArgumentException
				("Cache option requested but no cache size provided");
			}
			cacheBoo = true;//Mark we are using a cache

			//Get the cache size
			cacheSize = Integer.parseInt(args[4]);

			//Enforce cache size constraints
			if (cacheSize < 100 || cacheSize > 500) {
				throw new ParseArgumentException
				("Illegal argument: cache size must be specified between 100 and 500 inclusive.");
			}

			//No Cache Requested
		} else if (cache == 0) {
			//if debug arg is present parse it
			if (args.length == 5) {
				debug = Integer.parseInt(args[4]);

				// Enforce debug constraint
				if (debug != 0 && debug != 1 && debug !=2) {
					throw new ParseArgumentException
					("Illegal argument: debug must be 0,1 or 2.");
				}
			} 

			//Too many arguments
			if (args.length == 6) {
				throw new ParseArgumentException
				("Illegal argument: too many arguments, cache size shouldn't be input if cache = 0.");
			} 
		}

		//Get the degree
		degree = Integer.parseInt(args[1]);
		if (degree < 0) {
			throw new ParseArgumentException
			("Illegal argument: degree must >= 0.");
		}	

		//get the gbk file name
		gbk = args[2];
		if (!gbk.contains(".gbk")) {
			throw new ParseArgumentException
			("Illegal argument: must be a .gbk file.");
		}
		
		File gbkTest = new File(gbk);
		if (!(gbkTest.isFile() && gbkTest.exists())) {
			throw new ParseArgumentException("File " + gbkTest.toString() + " not found");
		}

		//get the subsequence length
		sequence = Integer.parseInt(args[3]);
		if (sequence < 1 || sequence > 31) {
			throw new ParseArgumentException
			("Illegal argument: sequence length must be between 1 and 31.");
		}

		//If a cache was requested, see if a debug option is also provided
		if (cache == 1 && args.length == 6) {
			debug = Integer.parseInt(args[5]);

			if (debug != 0 && debug != 1 && debug !=2) {
				throw new ParseArgumentException
				("Illegal argument: debug must be 0 or 1.");
			}
		}

		//Build the object that contains the parsed args
		GeneBankCreateBTreeArguments argument = new GeneBankCreateBTreeArguments( cacheBoo, degree, gbk, sequence, cacheSize, debug);

		return argument;
	}

	/**
	 * Parses the input file to get only valid gene sequences
	 * @param fileName - The filename containing the gene sequences
	 * @return - An arrayList of Stringbuilders, where each stringbuilder contains a valid subsequence
	 */
	private static List<StringBuilder> ParseFile(String fileName) {
		StringBuilder tempstr = new StringBuilder("");
		List<StringBuilder> list = new ArrayList<StringBuilder>();

		try {
			File file = new File(fileName);
			Scanner fileScan = new Scanner(file);
			while (fileScan.hasNext()) {  //outer loop to go through entire file
				String line = fileScan.next();
				if (line.contains("ORIGIN")) { //we've found the start and can start recording
					while (!line.contains("//")) { //stop recording when we hit this
						line = fileScan.next();
						char[] newChar = line.toCharArray();
						for (int i = 0; i < newChar.length; i++) {
							newChar[i] = Character.toUpperCase(newChar[i]); //case standardization
							if (newChar[i] == 'A' || newChar[i] == 'T' || newChar[i] == 'C' || newChar[i] == 'G') { //what we want, add to SB
								tempstr.append(newChar[i]);
							} else if (newChar[i] == 'N') { //stop, add existing sequence to final list, reset list and keep going
								list.add(tempstr);
								tempstr = new StringBuilder("");
							} else {
								continue; //numbers, spaces, anything else we don't want we just skip
							}
						}
					}
					list.add(tempstr);
					tempstr = new StringBuilder("");
				}
			}
			fileScan.close();

		} catch (Exception e) {
			System.out.println("Some issue reading file contents in\n:" + e.toString());
		}
		return list;
	}

	/**
	 * Takes a gene sequence and breaks it into subsequences of specified length before encoding
	 * them and inserting them into the BTree
	 * @param tree - the BTree to insert the encoded subsequences into
	 * @param string - The sequence to be broken into subsequences
	 * @param sequenceLength - How long of subsequences to break the sequence into
	 */
	private static void encodeSubSequenceAndInsert(BTree tree, String string, int sequenceLength) {
		//Break substring into sequences
		// This loop needs to stop iterating before the "sliding window" reads past the last char in the string
		TreeObject toInsert;
		for (int i = 0; i< string.length() - sequenceLength + 1; i++) {
			//Break the sequence up into appropriate subsequences
			String subSeq = string.substring(i, i + sequenceLength);

			//Encode the subsequence
			long encodedSubSeq = GeneSequenceMapper.encodeGeneSequence(subSeq);

			//Make a tree object and insert it
			toInsert = new TreeObject(encodedSubSeq, sequenceLength);
			tree.bTreeInsert(toInsert);

		}

	}

	/**
	 * This method creates a database containing the contents of the BTree
	 */
	private static void createDataBase(GeneBankCreateBTreeArguments pArgs) {
		Connection connection = null;
		try
		{
			System.out.println("Creating database (will take a moment)...");
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + pArgs.getGbkFileName() + "." + pArgs.getSubsequenceLength() + ".db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.

			statement.executeUpdate("drop table if exists sequences");
			statement.executeUpdate("create table sequences (gene string, frequency integer)");

			// In order traverse and insert
			recursiveInOrderInsert(statement, btree.getRoot());
			System.out.println("Created database: " +  pArgs.getGbkFileName() + "." + pArgs.getSubsequenceLength() + ".db");

		}
		catch(SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * This recursive method does an in order walk through the BTree in this class, inserting
	 * each element into the database this program creates.
	 * @param statement - an SQL statement object connected to the database we are inserting into
	 * @param r - The root node of the BTree we are reading from
	 * @throws SQLException
	 */
	private static void recursiveInOrderInsert(Statement statement, BTreeNode r) throws SQLException {
		if(r == null) {//Check for errors
			throw new NullPointerException();
		}
		int i;
		//For each key in the node
		for (i = 0; i < r.getNumKeys(); i++) {
			//If we aren't at a leaf go to i'th child and recursively call this method
			if (r.isLeaf() == '0') {
				recursiveInOrderInsert(statement, btree.diskRead(r.getChildPointer(i)));
			}
			//The i'th child is done or we are not a leaf, insert the next node object into the db
			statement.executeUpdate("insert into sequences values('" + 
					GeneSequenceMapper.decodeGeneSequence(r.getNodeObject(i).getKey(), btree.getSequenceLength()) + 
					"'," + r.getNodeObject(i).getFrequency() + ")");
		}
		//This makes sure we get the right most child of each node
		if (r.isLeaf() == '0') {
			recursiveInOrderInsert(statement, btree.diskRead(r.getChildPointer(i)));
		}
	}
}
