package cs321.btree;
import cs321.create.GeneSequenceMapper;

/**
 * This class defines an object that acts as a 'key' to be stored inside a
 * BTreeNode. Primarily used to hold encoded gene subsequence and frequency of that subsequence
 * @authors Zach Sullivan, Andrew Sorensen
 *
 */
public class TreeObject
{	
	//Instance Variables
	private long object;
	private int frequency;
	private int k;

	/**
	 * Constructor - creates a new Tree Object
	 * @param DNA - the long that represents the encoded gene subsequence
	 * @param k - The length of subsequence of the encoded gene contained in DNA
	 */
	public TreeObject(long DNA, int k) {
		this.object = DNA;
		this.frequency = 1;
		this.k = k;
	}

	public long getKey() {
		return this.object;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void increaseFrequency() {
		this.frequency++;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * Compares the encoded DNA subsequences in two TreeObjects
	 * to determine their ordering
	 * @param A - The tree object to compare to
	 * @return -1 if this object goes after A
	 * 			0 if they are equal
	 * 			1 if A goes after this object
	 */
	public int compareTo(TreeObject A) {

		if (this.getKey() > A.getKey()) {
			return 1;
		} else if (this.getKey() == A.getKey()) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(GeneSequenceMapper.decodeGeneSequence(this.object, this.k));
		sb.append(": ");
		sb.append(this.frequency);

		return sb.toString();
	}
}
