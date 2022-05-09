package cs321.btree;

/**
 * This class defines a BTreeNode object to hold all metadata, TreeObjects, and
 * child pointers for a node inside a BTree
 * @author Emily Thelander, Andrew Sorensen, Zach Sullivan
 *
 */
public class BTreeNode {


	//Instance variablse
	private char isLeaf;
	private int degree, maxKeys;
	private int numKeys, numChildren;
	private TreeObject[] nodeObjects;
	private long[] childPointers;
	private long offset;

	/**
	 * Constructor
	 * @param degree - The degree of the BTree this node exists in
	 * @param offset - This nodes offset in the binary BTree file the BTree is written to
	 * @param isLeaf - '0' if not a leaf else '1'
	 */
	public BTreeNode(int degree, long offset, char isLeaf) {
		this.degree = degree;
		this.offset = offset;
		this.isLeaf = isLeaf;
		this.numKeys = 0;
		this.maxKeys = (degree * 2) - 1;
		this.numChildren = 0;
		nodeObjects = new TreeObject[maxKeys];
		childPointers = new long[maxKeys + 1];
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getNumKeys() {
		return numKeys;
	}

	public void setNumKeys(int numKeys) {
		this.numKeys = numKeys;
	}

	public void incrementNumKeys() {
		numKeys++;
	}

	public TreeObject getNodeObject(int index) {
		TreeObject obj = nodeObjects[index];
		return obj;
	}

	public void setNodeObject(int index, TreeObject obj) {
		nodeObjects[index] = obj;
	}

	public long getChildPointer(int i) {
		return childPointers[i];
	}

	public void setChildPointer(long node, int i) {
		childPointers[i] = node;
	}

	public char isLeaf() {
		return isLeaf;
	}

	public int getNumChildren() {
		return numChildren;
	}

	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < this.getNumKeys(); i++ ){
			sb.append(this.nodeObjects[i].toString());
			sb.append('\n');
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof BTreeNode))
			return false;
		if (this.getOffset() == ((BTreeNode) obj).getOffset())
			return true;
		return false;
	}


}
