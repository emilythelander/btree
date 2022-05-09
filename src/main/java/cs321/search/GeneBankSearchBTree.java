package cs321.search;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import cs321.btree.*;
import cs321.common.ParseArgumentException;
import cs321.create.GeneSequenceMapper;

/**
 * This driver program searches an existing BTree Binary file for the existence
 * of all subsequences specified on each line of an input file
 * @author Andrew Sorensen, Zach Sullivan
 *
 */
public class GeneBankSearchBTree
{

	/**
	 * @param args - see usage message for expected input
	 */
	public static void main(String[] args)
	{
		GeneBankSearchBTreeArguments pArgs = parseArgumentsAndHandleExceptions(args);
		File queryFile = new File(pArgs.getQueryFile());
		BTree tree = null;
		Scanner queryParse = null;
		try {
			queryParse = new Scanner(queryFile);
			tree = new BTree(pArgs.getBtreeFile());
			if (!queryParse.hasNext()) {
				queryParse.close();
				printUsageAndExit("Query file is empty");
			}
			String firstSequence = queryParse.next();
			int sequenceLen = tree.getSequenceLength();
			//Enforce sequence length comparison
			if(firstSequence.length() != sequenceLen) {
				queryParse.close();
				printUsageAndExit("Sequence length of query file does not match BTree");
			}
			queryParse = new Scanner(queryFile);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(2);
		}

		if(pArgs.isUseCache()) {
			tree.createCache(pArgs.getCacheSize());
		}

		StringBuilder queryResults = new StringBuilder();
		TreeObject result;
		while (queryParse.hasNext()) {
			String geneToSearch = queryParse.next().toUpperCase().trim();
			result = tree.search(tree.getRoot(), GeneSequenceMapper.encodeGeneSequence(geneToSearch));

			if (result == null) {//Object not found
				//queryResults.append(geneToSearch);
				//queryResults.append(": 0\n");//Append 0
			}
			else {//Object found
				queryResults.append(result.toString());
				queryResults.append("\n");//Append newline
			}
		}
		//Tidy up
		tree.close();
		queryParse.close();

		dump(pArgs, queryResults);

		//Print results of query
		String resultsToString = queryResults.toString();
		if (resultsToString.length() == 0) {
			System.out.println("No results found. Exiting...");
		}
		System.out.print(resultsToString);

	}

	/**
	 * This method handles creating an output dump file when the user has debug set to 1
	 * @param T - the GeneBankSearchBTreeArguments object that contains the user arguments for this program
	 * @param results - a StringBuilder containing the results of searching the BTree
	 */
	private static void dump(GeneBankSearchBTreeArguments T, StringBuilder results) {
		int debug = T.getDebugLevel();

		if(debug == 1) {
			try {
				File dump = new File("qdump");//Create filename

				if(dump.exists() && dump.isFile()) {//Delete currently existing file
					dump.delete();
				}

				dump.createNewFile();

				FileWriter dumpOutWriter = new FileWriter(dump); //Write it

				dumpOutWriter.write(results.toString());
				dumpOutWriter.close();
			}
			catch (Exception e){
				System.err.println(e.toString());
			}
		}
	}

	/**
	 * Actually parses user input from CLI
	 * @param args - from the main method, should contain user selected options from CLI
	 * @return - an object containing the results of parsing the user input
	 * @throws ParseArgumentException - upon input that doesn't match expecation
	 */
	public static GeneBankSearchBTreeArguments parseArguments(String[] args) throws ParseArgumentException
	{
		int cache;
		int cacheSize = 0;
		Boolean cacheBoo = false;
		String btree;
		String query;
		int debug = 0;

		//Check total number of arguments
		if (args.length < 3 || args.length > 5) {
			throw new ParseArgumentException
			("Incorrect number of arguments provided");
		}


		// Parse cache option
		cache = Integer.parseInt(args[0]);
		if (cache != 0 && cache != 1) { //Enforce cache option
			throw new ParseArgumentException
			("Illegal argument: cache must be 0 or 1.");
		}

		if (cache == 1) {
			//Must have 4 args minimum with cache
			if (args.length < 4) {
				throw new ParseArgumentException("Too few arguments provided for using a cache");
			}
			cacheBoo = true;
			cacheSize = Integer.parseInt(args[3]);

			if (cacheSize < 100 || cacheSize > 500) {
				throw new ParseArgumentException
				("Illegal argument: cache size must be specified between 100 and 500 inclusive.");
			}
			//Debug option provided?
			if(args.length == 5) {
				debug = Integer.parseInt(args[4]);
				if (debug != 0 && debug != 1) {
					throw new ParseArgumentException
					("Illegal argument: debug must be 0 or 1");
				}
			}

		} else if (cache == 0) {

			//4 arguments maximum with no cache
			if (args.length > 4 ) {
				throw new ParseArgumentException("Too many arguments provided without cache usage");
			}

			//debug option provided?
			if (args.length == 4) {
				debug = Integer.parseInt(args[3]);

				if (debug != 0 && debug != 1) {
					throw new ParseArgumentException
					("Illegal argument: debug must be 0 or 1");
				}
			}
		}	


		btree = args[1];
		if (!btree.contains(".btree.data.")) {
			throw new ParseArgumentException
			("Illegal argument: must be a .btree.data.<k>.<t> file.");
		}
		
		//Verify file exists
		File gbkTest = new File(btree);
		if (!(gbkTest.isFile() && gbkTest.exists())) {
			throw new ParseArgumentException("File " + gbkTest.toString() + " not found");
		}

		query = args[2];
		
		//Verify File exists
		gbkTest = new File(query);
		if (!(gbkTest.isFile() && gbkTest.exists())) {
			throw new ParseArgumentException("File " + gbkTest.toString() + " not found");
		}
		
		GeneBankSearchBTreeArguments argument = new GeneBankSearchBTreeArguments(cacheBoo, btree, query, cacheSize, debug);

		return argument;
	}

	/**
	 * Calls a method that actually parses user input, handles any exceptions
	 * @param args - from main method, should contain user options from CLI
	 * @return - an object containing parsed user input
	 */
	private static GeneBankSearchBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
	{
		GeneBankSearchBTreeArguments geneBankSearchBTreeArguments = null;
		try
		{
			geneBankSearchBTreeArguments = parseArguments(args);
		}
		catch (ParseArgumentException e)
		{
			printUsageAndExit(e.getMessage());
		}
		catch(NumberFormatException e) {
			printUsageAndExit(e.getMessage());
		}
		return geneBankSearchBTreeArguments;
	}

	/*
	 * @param errorMessage - String containing a message about why we are exiting
	 */
	private static void printUsageAndExit(String errorMessage)
	{
		System.out.println(errorMessage);
		System.out.println
		("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]\n");
		System.out.println
		("<0/1(no/with Cache)>: determines whether or not a cache will be used, if a cache is used a cache size must be specified.");
		System.out.println
		("<btree file>: The binary btree file containing gene bank subsequences to be queried."); 
		System.out.println
		("<query file>: the file containing gene subsequences to search the btree for");
		System.out.println
		("[<cache size>]: the size of the cache. Must be between 100 and 500.");
		System.out.println
		("[<debug level>]: the default level will be 0 - output of query on standard output, 1 - create dump file of output 'qdump'");

		System.exit(1);
	}

}

