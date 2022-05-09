package cs321.search;

/**
 * Defines an object to contain parsed user input for the accompanying driver class:
 * GeneBankSearchBTree.java
 * @author Andrew Sorensen
 *
 */
public class GeneBankSearchBTreeArguments
{	

	//Instance variables
	private final boolean useCache;
	private final String btreeFile;
	private final String queryFile;
	private final int cacheSize;
	private final int debugLevel;

	/**
	 * @param useCache - whether or not a user has requested to use a cache
	 * @param btreeFile - name that represents the binary BTree file we will be querying
	 * @param queryFile - the name of the source file containing one gene subsequence to query per line
	 * @param cacheSize - size of cache if requested
	 * @param debugLevel - default to zero for no debug, 1 to create output dump file
	 */
	public GeneBankSearchBTreeArguments(boolean useCache, String btreeFile, String queryFile, int cacheSize, int debugLevel)
	{
		this.useCache = useCache;
		this.btreeFile = btreeFile;
		this.queryFile = queryFile;
		this.debugLevel = debugLevel;
		this.cacheSize = cacheSize;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public String getBtreeFile() {
		return btreeFile;
	}

	public String getQueryFile() {
		return queryFile;
	}

	public int getCacheSize() {
		return cacheSize;
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
		GeneBankSearchBTreeArguments other = (GeneBankSearchBTreeArguments) obj;
		if (cacheSize != other.cacheSize)
		{
			return false;
		}
		if (debugLevel != other.debugLevel)
		{
			return false;
		}
		if (btreeFile == null)
		{
			if (other.btreeFile != null)
			{
				return false;
			}
		}
		else
		{
			if (!btreeFile.equals(other.btreeFile))
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
		if (useCache != other.useCache)
		{
			return false;
		}
		return true;
	}
}
