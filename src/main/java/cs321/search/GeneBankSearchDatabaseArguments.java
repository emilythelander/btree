package cs321.search;

/**
 * 
 * An object to contain the parsed user input from the CLI for the associated driver program:
 * GeneBankSearchDatabase.java
 * @author Andrew Sorensen
 *
 */
public class GeneBankSearchDatabaseArguments
{	
	//Instance variables
	private final String pathToDatabase;
	private final String queryFile;
	private final int debugLevel;

	/**
	 * Constructor
	 * @param pathToDatabase - the name of the db/path to the db to search
	 * @param queryFile - path to query file to use when searching the db
	 * @param debugLevel - default to zero, option 1 will cause dump file to be generated for results
	 */
	public GeneBankSearchDatabaseArguments(String pathToDatabase, String queryFile, int debugLevel)
	{
		this.pathToDatabase = pathToDatabase;
		this.queryFile = queryFile;
		this.debugLevel = debugLevel;
	}
	public String pathToDatabase() {
		return pathToDatabase;
	}

	public String getQueryFile() {
		return queryFile;
	}

	public int getDebugLevel() {
		return debugLevel;
	}

	@Override
	public boolean equals(Object obj)
	{
		//this method was generated using an IDE
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		GeneBankSearchDatabaseArguments other = (GeneBankSearchDatabaseArguments) obj;
		if (debugLevel != other.debugLevel)
		{
			return false;
		}
		if (pathToDatabase == null)
		{
			if (other.pathToDatabase != null)
			{
				return false;
			}
		}
		else
		{
			if (!pathToDatabase.equals(other.pathToDatabase))
			{
				return false;
			}
		}
		if (queryFile == null)
		{
			if (other.queryFile != null)
			{
				return false;
			}
		}
		else
		{
			if (!queryFile.equals(other.queryFile))
			{
				return false;
			}
		}
		return true;
	}
}
