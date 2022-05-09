package cs321.btree;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Defines a BTree used to store gene subsequences as input from .gbk files
 * containing various gene sequences. Contains methods to insert to BTree, handle cache
 * operations, diskRead/diskWrite to binary file that is output from BTree creation and search
 * an existing BTree.
 * 
 * @author Zach Sullivan, Emily Thelander, Andrew Sorensen
 *
 */
public class BTree {

	// Metadata
	private BTreeNode root;
	private int k; //sequence length
	private int t; //degree
	private int numNodes, maxNodeSize;
	private String filename;
	private BTreeCache cache;



	/**
	 * This constructor builds a BTree from a binary data file
	 * @param binaryBTreeFileName - the name of the binary BTree file to create the BTree from.
	 */
	public BTree(String binaryBTreeFileName) {

		int bTreeMetadataSize = 20;
		cache = null;
		this.filename = binaryBTreeFileName;
		//Set up files/buffer
		File file = new File(binaryBTreeFileName);
		ByteBuffer buffer = ByteBuffer.allocate(bTreeMetadataSize);
		RandomAccessFile fileStream;

		//Open file and get data
		try {
			fileStream = new RandomAccessFile(file, "rw");
			fileStream.readFully(buffer.array(), 0, bTreeMetadataSize);
			fileStream.close();			

		}
		catch(Exception e) {
			System.err.println("BTree binary file constructor encountered an error:\n" + e.toString());
			System.exit(3);
		}

		//Pull data from buffer into memory
		long rootPointer = buffer.getLong();
		this.t = buffer.getInt();
		this.k = buffer.getInt();
		this.numNodes = buffer.getInt();


		//The root node
		//Node size is 25 bytes metadata (This node's offset : long,
		//isLeaf : char, numkeys : int, num children : int
		//Plus 2*t -1 objects containing a long (gene) and int (frequency) each
		//Plus 2*t longs representing child pointers
		int nodeMetadata = 8 + 2 + 4 + 4;//25		
		//Each object on file is a long representing the gene and an int representing the frequency
		int bytesPerObject = 8 + 4;//12
		int bytesPerChildPointer = 8;
		maxNodeSize = nodeMetadata + (2*t - 1) * bytesPerObject + 2 * t * bytesPerChildPointer;



		this.root = this.diskRead(rootPointer);		
	}


	/**
	 * This creates a BTree from scratch
	 * @param k - sequence length
	 * @param t - degree
	 * @param rootFileName
	 */
	public BTree(int k, int t, String sourceFileName) {

		int bTreeMetadataSize = 20;
		this.root = new BTreeNode(t, (long) bTreeMetadataSize, '1');
		this.k = k;
		this.t = t;
		cache = null;
		this.numNodes = 1;

		//The root node
		//Node size is 25 bytes metadata (This node's offset : long,
		//parent offset : long, isLeaf : char, numkeys : int, num children : int
		//Plus 2*t -1 objects containing a long (gene) and int (frequency) each
		//Plus 2*t longs representing child pointers
		int nodeMetadata = 8 + 8 + 2 + 4 + 4;//25

		//Each object on file is a long representing the gene and an int representing the frequency
		int bytesPerObject = 8 + 4;//12
		int bytesPerChildPointer = 8;
		maxNodeSize = nodeMetadata + (2*t - 1) * bytesPerObject + 2 * t * bytesPerChildPointer;
		filename = sourceFileName + ".btree.data." + k + "." + t;

		//Create file to write metadata
		File file = new File(filename);
		RandomAccessFile fileStream = null;
		ByteBuffer buffer = ByteBuffer.allocate(bTreeMetadataSize);
		buffer.putLong(root.getOffset());
		buffer.putInt(this.t);
		buffer.putInt(this.k);
		buffer.putInt(numNodes);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fileStream = new RandomAccessFile(file, "rw");//Creates file if it does not exist
			fileStream.write(buffer.array(), 0, bTreeMetadataSize);
			fileStream.close();

		}
		catch (Exception e) {
			System.err.println("BTree constructor error:\n" + e.toString());
			System.exit(3);
		}
		this.diskWrite(root);
	}


	/**
	 * This method inserts a TreeObject into the BTree, should be the only method called
	 * @param k - the tree object being inserted into the Tree
	 */
	public void bTreeInsert(TreeObject k) {

		BTreeNode r = root;
		if (r.getNumKeys() == (2 * t) - 1) {
			long offset = 20 + numNodes * maxNodeSize;//BTree metadata + skip all existing nodes
			BTreeNode s = new BTreeNode(this.t, offset, '0');
			numNodes++;
			root = s;
			s.setNumKeys(0);
			s.setChildPointer(r.getOffset(), 0);
			s.setNumChildren(1);
			BTreeSplitChild(s, 0, r);
			bTreeInsertNonFull(s, k);
		}
		else {
			bTreeInsertNonFull(r, k);
		}

	}

	/**
	 * This method splits a BTreeNode that is full
	 * @param x - The parent of the node we are splitting
	 * @param i - The index of the object to be moved from child y to parent x
	 * @param y - The i'th child of x, we are splitting y
	 */
	private void BTreeSplitChild(BTreeNode x, int i, BTreeNode y) {

		//20 bytes metadata for the tree itself plus the number of nodes * maxNode size
		long offset = 20 + numNodes * maxNodeSize;
		BTreeNode z = new BTreeNode(this.t, offset, y.isLeaf());//z will go to the right of y in the tree
		numNodes++; //There is a new node, track this for the offset calculation

		//Move right half of object (keys) from y to z
		for (int j = 0; j < t - 1; j++) {
			z.setNodeObject(j, y.getNodeObject(j + t));
		}
		y.setNumKeys(t -1);
		z.setNumKeys(t - 1);

		//Move child pointers
		if (y.isLeaf() == '0') {// y is not a leaf
			for (int j = 0; j < t; j++) {
				z.setChildPointer(y.getChildPointer(j + t), j);
			}
			z.setNumChildren(t);
			y.setNumChildren(t);
		}		

		//Shift children in x to insert z
		for (int j = x.getNumChildren(); j > i + 1; j--) {
			x.setChildPointer(x.getChildPointer(j - 1), j);
		}
		//update x metadata
		x.setChildPointer(z.getOffset(), i + 1);

		//Shift the objects (keys) in parent x
		for (int j = x.getNumKeys(); j > i ; j--) {
			x.setNodeObject(j, x.getNodeObject(j - 1));
		}
		x.setNodeObject(i, y.getNodeObject(t-1));
		x.setNumChildren(x.getNumChildren() + 1);
		x.setNumKeys(x.getNumKeys() + 1);
		diskWrite(z);
		diskWrite(y);
		diskWrite(x);		

	}

	/**
	 * This recursive method does the actual inserting. It inserts into a non-full node, which
	 * should be checked by insert before calling this method. If a duplicate object is encountered
	 * a new object is not inserted, instead the frequency count of the existing object is incremented
	 * @param x - The node to start searching when inserting object.
	 * @param k -  The actual object to insert
	 */
	private void bTreeInsertNonFull(BTreeNode x, TreeObject k) {
		int i = x.getNumKeys();
		boolean duplicate = false;
		BTreeNode child;

		if(x.isLeaf() != '0') {//x is a leaf
			//Search this node to see if we have a duplicated
			while( i > 0 && k.getKey() <= x.getNodeObject(i - 1).getKey()) {
				if(k.compareTo(x.getNodeObject(i-1)) == 0) {
					duplicate = true;
					x.getNodeObject(i-1).increaseFrequency();
					i = 0;
				} else {
					i--;
				}
			}

			i = x.getNumKeys();

			//If it was not a duplicate search for the correct place to insert this object
			if(duplicate != true) {
				while( i > 0 && k.getKey() <= x.getNodeObject(i - 1).getKey()) {

					x.setNodeObject(i, x.getNodeObject(i - 1));
					i--;
				}

				x.setNodeObject(i, k);
				x.setNumKeys(x.getNumKeys()+1);
			}

			diskWrite(x);

		} else {//x is not a leaf
			//Check to see if the object we are inserting is a duplicate of any in this node
			while( i > 0 && k.getKey() <= x.getNodeObject(i - 1).getKey()) {
				if(k.compareTo(x.getNodeObject(i-1)) == 0) {
					duplicate = true;
					x.getNodeObject(i-1).increaseFrequency();
					diskWrite(x);
					i = 0;
				} else {
					i--;
				}
			}

			//Object was not a duplicate
			if(duplicate != true) {
				//Get the i'th child of this node
				child = diskRead(x.getChildPointer(i));

				//Check if the child is full and split it
				if(child.getNumKeys() == 2*child.getDegree()-1) {
					BTreeSplitChild(x, i, child);

					//Check if the object that was just moved up to x in the split
					//Is a duplicate of the object we are inserting
					if (k.getKey() == x.getNodeObject(i).getKey()) {
						x.getNodeObject(i).increaseFrequency();
						diskWrite(x);
						return;
					}
					//The object we are inserting is not a duplicate of what was just moved up
					//Check if the object moved up is bigger than this one, if so go left
					//Else go right
					else if(k.getKey() > x.getNodeObject(i).getKey()) {
						i++;
						child = diskRead(x.getChildPointer(i));
					}
					bTreeInsertNonFull(child, k);
				} else {
					bTreeInsertNonFull(child, k);
				}
			} 
		}
	}

	/**
	 * Writes a node to the binary BTree file containing all the data on this BTree
	 * Completely handles interacting with cache to minimize actual diskreads/diskwrites
	 * when a cache is in use
	 * @param node - the node to write to file
	 */
	public void diskWrite(BTreeNode node) {

		//If we are using a cache, we will simply update that instead of Writing to disk
		if(cache != null) {
			//This node is not the updated version of the node we want to right,
			//but it will still match due to having the same offset (node.equals method)
			cache.removeObject(node);

			//Add the updated version of our node to cache, if it was in the cache before it has been removed
			BTreeNode wasInCache = cache.addObject(node);

			//Our node bumped another node out of the cache, write that node to file
			if (wasInCache != null) {
				this.diskWriteIgnoreCache(wasInCache);
			}
			return;//We are done, only the cache was updated, this node will be written 
			//To disk when it falls of cache or the  btree is closed
		}
		//Allocate buffer
		ByteBuffer buffer = ByteBuffer.allocate(maxNodeSize);

		//Put node metadata in buffer
		buffer.putLong(node.getOffset());
		buffer.putChar(node.isLeaf());
		buffer.putInt(node.getNumKeys());
		buffer.putInt(node.getNumChildren());

		//Write objects to buffer
		for(int i = 0; i < node.getNumKeys(); i++) {
			TreeObject current = node.getNodeObject(i);
			buffer.putLong(current.getKey());
			buffer.putInt(current.getFrequency());
		}

		//Move buffer pointer to start of children data section
		int jumpVal = 17 + 12*(2*t - 1) + 1; //the byte ahead of metadata plus max number of objects * objectSize
		buffer.position(jumpVal);

		//Write children to buffer
		for (int i = 0; i < node.getNumChildren(); i++) {
			buffer.putLong(node.getChildPointer(i));
		}

		//Attempt to write buffer to file
		try {
			File file = new File(this.filename);
			RandomAccessFile fileStream = new RandomAccessFile(file, "rw");
			fileStream.seek(node.getOffset());
			fileStream.write(buffer.array(), 0, this.maxNodeSize);
			fileStream.close();
		}
		catch(Exception e) {
			System.err.println("Btree.DiskWrite encoutered an error:\n" + e.toString());
		}

	}


	/**
	 * Reads a BTreeNode from disk. Handles all cache interactions when a cache is present
	 * to minimize the number of actual diskreads
	 * @param nodeOffset - The location of the node we want to read in the file
	 * @return - The node that was read from file, will error if the node doesn't exist
	 */
	public BTreeNode diskRead(long nodeOffset) {

		BTreeNode returnNode;

		if(cache != null) {
			//We don't actually know if this node is a leaf or not, but it doesn't matter here, we will not
			//add this actual node to cache, the node returned will be the node from cache that matched this offset
			returnNode = cache.removeObject(new BTreeNode(this.t, nodeOffset,'0'));
			if (returnNode != null) {//Our node was in cache
				//Add our node to the top of the cache, watching to see if a node from cache
				//is bumped out
				BTreeNode wasRemovedFromCache = cache.addObject(returnNode);
				if (wasRemovedFromCache != null) {
					//A node got bumped by this cache operation, write to disk avoiding cache.
					this.diskWriteIgnoreCache(wasRemovedFromCache);
				}

				return returnNode;
			}
		}
		//Create file, filestream, and buffer
		File file = new File(this.filename);
		RandomAccessFile fileStream = null;
		ByteBuffer buffer = ByteBuffer.allocate(maxNodeSize);

		//Attempt to actually open filestream into file
		try {
			fileStream = new RandomAccessFile(file, "r");
			fileStream.seek(nodeOffset);
			fileStream.readFully(buffer.array(), 0, this.maxNodeSize);//Read a node into the buffer
			fileStream.close();
		}
		catch (Exception e) {
			System.err.println("BTree.diskRead failure:\n" + e.toString());
		}

		//Get the offset stored on file
		long fileNodeOffset = buffer.getLong();

		//Verify it matches the node we are looking for
		if (fileNodeOffset != nodeOffset) {
			String error = "BTree.diskRead failure:\nOffsets do not match\n";
			error += "Argument offset: " + nodeOffset + ", Read Offset: " + fileNodeOffset;
			throw new IllegalStateException(error);
		}

		//Get the rest of the node metadata
		char isLeaf = buffer.getChar(); //Get a whole byte for the boolean
		int numKeys = buffer.getInt();
		int numChildren = buffer.getInt();

		//Build the node
		returnNode = new BTreeNode(this.t, nodeOffset, isLeaf);
		returnNode.setNumKeys(numKeys);
		returnNode.setNumChildren(numChildren);

		//Add the node objects
		TreeObject toAdd;
		for (int i = 0; i < numKeys; i++) {
			toAdd = new TreeObject(buffer.getLong(), this.k);
			toAdd.setFrequency(buffer.getInt());
			returnNode.setNodeObject(i, toAdd);
		}

		//Jump to the start of the children array (objects array may not have been full)
		int jumpVal = 17 + 12*(2*t - 1) + 1; //the byte ahead of metadata plus max number of objects * objectSize
		buffer.position(jumpVal);

		//Add all the children pointers
		for (int i = 0; i < numChildren; i++) {
			returnNode.setChildPointer(buffer.getLong(), i);
		}

		//If we are here, we need to add our object to the cache still
		if (cache != null) {
			//Add our node to cache
			BTreeNode wasRemovedFromCache = cache.addObject(returnNode);

			//If adding our node removed a node from the bottom
			if (wasRemovedFromCache != null) {
				//Write that node to disk
				this.diskWriteIgnoreCache(wasRemovedFromCache);
			}
		}

		return returnNode;
	}

	/**
	 * Call this method to create a cache in the BTree
	 * All other BTree methods will handle interacting with the cache after this
	 * @param size - Size of requested cache
	 */
	public void createCache(int size) {
		this.cache = new BTreeCache(size);
	}

	public BTreeNode getRoot() {
		return this.root;
	}

	public void setRoot(BTreeNode root) {
		this.root = root;
	}


	public int getNumNodes() {
		return this.numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	public void setDegree(int t) {
		this.t = t;
	}

	public int getDegree() {
		return this.t;
	}

	public void setSequenceLength(int k) {
		this.k = k;
	}

	public int getSequenceLength() {
		return this.k;
	}

	public void setFileName(String filename) {
		this.filename = filename;
	}

	public String getFileName() {
		return this.filename;
	}

	/**
	 * This method MUST be called when you are done working with a BTree.
	 * If the BTree has a cache, this will write the cache to the binary file on exit.
	 * This also ensures the BTree Metadata is correctly updated in the file on exit.
	 */
	public void close() {
		int treeMetaDataSize = 20;
		ByteBuffer buff = ByteBuffer.allocate(treeMetaDataSize);
		buff.putLong(root.getOffset());
		buff.putInt(this.getDegree());
		buff.putInt(this.getSequenceLength());
		buff.putInt(this.getNumNodes());

		//Try to write metadata to file
		File file = new File(this.getFileName());
		RandomAccessFile fs;

		try {
			fs = new RandomAccessFile(file, "rw");
			fs.write(buff.array(), 0, treeMetaDataSize);
			fs.close();
		}
		catch(Exception e) {
			System.err.println("BTree.close encountered an error:\n" + e.toString());
		}


		//If there is a cache, write to file on exit
		if(cache != null) {
			for(BTreeNode node : this.cache) {
				this.diskWriteIgnoreCache(node);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		recursiveToString(sb, this.root);
		return sb.toString();
	}

	/**
	 * Recursively searches the BTree to perform an in order traversal.
	 * Stores a printout of all BTree objects in order in a StringBuilder that is passed in
	 * @param sb - The StringBuilder used to store the printout of the BTree
	 * @param r - The node we are currently on, start with the root.
	 */
	public void recursiveToString(StringBuilder sb, BTreeNode r) {
		int i;
		//Make sure node isn't null
		if(r == null) {
			throw new NullPointerException();
		}

		//Iterate through all the objects in this node
		for (i = 0; i < r.getNumKeys(); i++) {
			//If this node is not a leaf
			if (r.isLeaf() == '0') {
				//Print subtree to the left of the i'th object
				recursiveToString(sb, this.diskRead(r.getChildPointer(i)));
			}
			//Print the i'th object of this node/all objects if node is a leaf
			sb.append(r.getNodeObject(i));
			sb.append('\n');
		}
		// This gets the rightmost child of a node
		if (r.isLeaf() == '0') {
			recursiveToString(sb, this.diskRead(r.getChildPointer(i)));
		}
	}

	/**
	 * This method is used only to write nodes to disk that just got bumped out of the cache
	 * OR to write a cache to disk upon closure of the BTree
	 * In this instance we do not want to add this node to the cache again or we will be in an infinite loop
	 * @param node - Node to write to disk without consideration to the cache
	 */
	private void diskWriteIgnoreCache(BTreeNode node) {
		//Allocate buffer
		ByteBuffer buffer = ByteBuffer.allocate(maxNodeSize);

		//Put node metadata in buffer
		buffer.putLong(node.getOffset());
		buffer.putChar(node.isLeaf());
		buffer.putInt(node.getNumKeys());
		buffer.putInt(node.getNumChildren());

		//Write objects to buffer
		for(int i = 0; i < node.getNumKeys(); i++) {
			TreeObject current = node.getNodeObject(i);
			buffer.putLong(current.getKey());
			buffer.putInt(current.getFrequency());
		}

		//Move buffer pointer to start of children data section
		int jumpVal = 17 + 12*(2*t - 1) + 1; //the byte ahead of metadata plus max number of objects * objectSize
		buffer.position(jumpVal);

		//Write children to buffer
		for (int i = 0; i < node.getNumChildren(); i++) {
			buffer.putLong(node.getChildPointer(i));
		}

		//Attempt to write buffer to file
		try {
			File file = new File(this.filename);
			RandomAccessFile fileStream = new RandomAccessFile(file, "rw");
			fileStream.seek(node.getOffset());
			fileStream.write(buffer.array(), 0, this.maxNodeSize);
			fileStream.close();
		}
		catch(Exception e) {
			System.err.println("Btree.DiskWrite encoutered an error:\n" + e.toString());
		}
	}

	/**
	 * This method recursively searches the BTree for a particular object
	 * @param x - The node to start recursing on, should start with root
	 * @param k - The long value of the sequence we are searching for
	 * @return - The TreeObject containing sequence k if found, null otherwise
	 */
	public TreeObject search(BTreeNode x, long k) {

		int i = 0;

		//Iterate through this node until we have searched everything or found an object that matches
		//Or the index of a child to search
		while (i < x.getNumKeys() && k > x.getNodeObject(i).getKey()) {
			i++;
		}

		//The while loop terminated because we found our object
		if (i < x.getNumKeys() && k == x.getNodeObject(i).getKey()) {
			return x.getNodeObject(i);
		}

		//The while loop terminated because we ran out of objects to search OR 
		//Found the index of the child we need to search
		//If we are not on a leaf, go to the i'th child
		if (x.isLeaf() == '0') {
			BTreeNode n = this.diskRead(x.getChildPointer(i));
			return search(n, k);
		}

		return null;
	}

}
