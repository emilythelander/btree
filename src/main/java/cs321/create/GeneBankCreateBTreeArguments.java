package cs321.create;

/**
 * Object used to stored user input as parsed in the associated driver file:
 * GeneBankCreateBTree.java
 * @author Zach Sullivan, Andrew Sorensen
 *
 */
public class GeneBankCreateBTreeArguments
{	
	//Instance variables
    private final boolean useCache;
    private final int degree;
    private final String gbkFileName;
	private final int subsequenceLength;
    private final int cacheSize;
    private final int debugLevel;

    /**
     * Constructor
     * @param useCache - whether or not the user requested a BTree cache
     * @param degree - the requested degree of the BTree
     * @param gbkFileName - The file name of the .gbk to turn into a BTree
     * @param subsequenceLength - length of gene subsequences in this tree
     * @param cacheSize - The requested size of the cache
     * @param debugLevel - To run in debug mode or not
     */
    public GeneBankCreateBTreeArguments(boolean useCache, int degree, String gbkFileName, int subsequenceLength, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
        this.gbkFileName = gbkFileName;
        this.subsequenceLength = subsequenceLength;
        this.cacheSize = cacheSize;
        this.debugLevel = debugLevel;
    }

    public int getDebug() {
        return this.debugLevel;
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
        GeneBankCreateBTreeArguments other = (GeneBankCreateBTreeArguments) obj;
        if (cacheSize != other.cacheSize)
        {
            return false;
        }
        if (debugLevel != other.debugLevel)
        {
            return false;
        }
        if (degree != other.degree)
        {
            return false;
        }
        if (gbkFileName == null)
        {
            if (other.gbkFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!gbkFileName.equals(other.gbkFileName))
            {
                return false;
            }
        }
        if (subsequenceLength != other.subsequenceLength)
        {
            return false;
        }
        if (useCache != other.useCache)
        {
            return false;
        }
        return true;
    }
    
    public boolean isUseCache() {
		return useCache;
	}

	public int getDegree() {
		return degree;
	}

	public String getGbkFileName() {
		return gbkFileName;
	}

	public int getSubsequenceLength() {
		return subsequenceLength;
	}

	public int getCacheSize() {
		return cacheSize;
	}

    @Override
    public String toString()
    {
        //this method was generated using an IDE
        return "GeneBankCreateBTreeArguments{" +
                "useCache=" + useCache +
                ", degree=" + degree +
                ", gbkFileName='" + gbkFileName + '\'' +
                ", subsequenceLength=" + subsequenceLength +
                ", cacheSize=" + cacheSize +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
