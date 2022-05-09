package cs321.search;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import cs321.common.ParseArgumentException;


/**
 * Searches a database for subsequences contained one on each line in a user input query file
 * Returns results from searching an sqlite database populated using a constructed binary BTree file
 * @author Andrew Sorensen
 *
 */
public class GeneBankSearchDatabase
{

	/**
	 * @param args -  see usage message for expected input
	 */
	public static void main(String[] args)
	{	

		//Parse args
		GeneBankSearchDatabaseArguments pArgs = parseArgumentsAndHandleExceptions(args);
		File queryFile = new File(pArgs.getQueryFile());		

		//Open scanner into query file
		Scanner queryParse = null;
		try {
			queryParse = new Scanner(queryFile);
			if (!queryParse.hasNext()) {
				queryParse.close();
				printUsageAndExit("Query file is empty");
			}
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(2);
		}


		//Build a stringbuilder and open SQL connection
		StringBuilder results = new StringBuilder();
		Connection connection = null;
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + pArgs.pathToDatabase());
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.


			//Run a query on the db for each line in the query file, appending result to stringbuilder if there is one
			ResultSet rs = null;
			while(queryParse.hasNext())
			{
				rs = statement.executeQuery("select * from sequences WHERE gene = '" + queryParse.next().toLowerCase().trim() + "'");

				if (rs.next()) {
					// read the result set and append to stringbuilder
					results.append(rs.getString("gene"));
					results.append(": ");
					results.append(rs.getInt("frequency"));
					results.append('\n');
				}
			}
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			printUsageAndExit(e.getMessage());
		}
		finally
		{
			try
			{
				//tidy up
				queryParse.close();
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e.getMessage());
			}
		}

		try {
			if(connection != null) {
				connection.close();
			}
			//Tidy up
			queryParse.close();
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		queryParse.close();
		//Print results of query
		String resultsToString = results.toString();
		if (resultsToString.length() == 0) {
			System.out.println("No results found. Exiting...");
		}
		System.out.print(resultsToString);
		dump(pArgs,results);

	}

	/**
	 * This method actually parses user input
	 * @param args -  user input arguments from CLI
	 * @return - an object containing the parsed user input
	 * @throws ParseArgumentException - upon unexpected/improper user input
	 */
	public static GeneBankSearchDatabaseArguments parseArguments(String[] args) throws ParseArgumentException
	{

		String pathToDB;
		String query;
		int debug = 0;

		//Check total number of arguments
		if (args.length < 2 || args.length > 3) {
			throw new ParseArgumentException
			("Incorrect number of arguments provided");
		}

		pathToDB = args[0];
		
		query = args[1];
		
		//Verify query file exists
		File gbkTest = new File(query);
		if (!(gbkTest.isFile() && gbkTest.exists())) {
			throw new ParseArgumentException("File " + gbkTest.toString() + " not found");
		}

		//Debug option included?
		if(args.length == 3) {
			debug = Integer.parseInt(args[2]);
			if (debug != 0 && debug != 1) {
				throw new ParseArgumentException
				("Debug value must be 0 or 1");
			}
		}


		GeneBankSearchDatabaseArguments argument = null;


		argument = new GeneBankSearchDatabaseArguments(pathToDB, query, debug);

		return argument;
	}

	/**
	 * Calls a method to parse user input, handles any exceptions
	 * @param args -  user input arguments from CLI
	 * @return - an object containing the parsed user input
	 */
	private static GeneBankSearchDatabaseArguments parseArgumentsAndHandleExceptions(String[] args)
	{
		GeneBankSearchDatabaseArguments geneBankSearchBTreeArguments = null;
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

	/**
	 * @param errorMessage - string containing cause for program termination
	 */
	private static void printUsageAndExit(String errorMessage)
	{
		System.out.println(errorMessage);
		System.out.println
		("Usage: java GeneBankSearchDataBase <path_to_SQLite_database> <query_file> [<debug_level>]");
		System.out.println
		("<btree path_to_SQLite_databasefile>: The path to the SQLite database to search."); 
		System.out.println
		("<query file>: the file containing gene subsequences to search the database for");
		System.out.println
		("[<debug level>]: the default level will be 0, 1 will produce a dump file of the results 'dbqdump'");

		System.exit(1);
	}


	/**
	 * This method handles creating an output dump file when the user has debug set to 1
	 * @param T - the GeneBankSearchBTreeArguments object that contains the user arguments for this program
	 * @param results - a StringBuilder containing the results of searching the BTree
	 */
	private static void dump(GeneBankSearchDatabaseArguments T, StringBuilder results) {
		int debug = T.getDebugLevel();

		if(debug == 1) {
			try {
				File dump = new File("dbqdump");//Create filename

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

}
