package cs321.btree;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;

public class BTreeTest
{

	////////////////////////////////////////////////////////
	// XXX Basic Constructor tests
	////////////////////////////////////////////////////////	

	@Test
	public void basicConstructorTest() {
		String testName = "basicConstructorTest";
		int k = 3;
		int t = 2;
		BTree test = new BTree(k, t, "ConstructorTest.gbk");

		//test Degree
		if (test.getDegree() != t) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != k) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 1) {
			fail(testName + " numNodes");
		}

		//numChildren
		if(test.getRoot().getNumChildren() != 0) {
			fail(testName + " numChildren");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 0) {
			fail(testName + " numKeys");
		}

		//isleaf
		if (test.getRoot().isLeaf() == 0) {
			fail(testName + " isLeaf");
		}

		String readTestFilename = test.getFileName();
		if (!readTestFilename.equals("ConstructorTest.gbk.btree.data." + k + "." + t)) {
			fail(testName + " fileName before read");
		}

		///////////////////////////////////////////////////////////////////////////////////
		//////////////// REPEAT WITH OTHER CONSTRUCTOR TO TEST READ/WRITE ////////////////
		//////////////////////////////////////////////////////////////////////////////////

		//read from file
		test = new BTree(readTestFilename);

		//test Degree
		if (test.getDegree() != t) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != k) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 1) {
			fail(testName + " numNodes");
		}

		//numChildren
		if(test.getRoot().getNumChildren() != 0) {
			fail(testName + " numChildren");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 0) {
			fail(testName + " numKeys");
		}

		//isleaf
		if (test.getRoot().isLeaf() == 0) {
			fail(testName + " isLeaf");
		}

		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		}    		

	}

	////////////////////////////////////////////////////////
	// XXX Degree 2 tests
	////////////////////////////////////////////////////////
	
	@Test
	public void degree2Insert12Objects() {
		String testName = "degree2nsert12Objects";
		int degree = 2;
		int sequenceLength = 3;
		BTree test = new BTree(sequenceLength, degree, "degree2Tests.gbk");
		
		//Insert items 4 - 9
		TreeObject toInsert;
		for (int i = 4; i <= 9; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//Insert items 0 - 3
		for (int i = 0; i <= 3; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//Insert items 10 - 11
		for (int i = 10; i <= 11; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//duplicate root object
		toInsert = new TreeObject(5, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 4
		toInsert = new TreeObject(4, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 7
		toInsert = new TreeObject(7, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 11
		toInsert = new TreeObject(11, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 9) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}



		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 5) {
			fail(testName + " root: object did not match");
		}
		
		
		//Test child nodes
		BTreeNode childNode;
		
		
		
		//Left child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 1 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 3 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Left child of roots left child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 0 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Middle Child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}



		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 2 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
				
		//Right child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(2));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " M child of(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 4 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test M child of nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Right child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 7 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		if(childNode.getNodeObject(1).getKey() != 9 || childNode.getNodeObject(1).getFrequency() != 1) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		///Left child of roots right child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test L child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 6 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Middle child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 8 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}	
		
		
		
		
		
		//Right child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(2));		
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 10 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 11 || childNode.getNodeObject(1).getFrequency() != 2) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}

		test.close();
		
		//Repeat the whole test from file read
		String readTestFilename = test.getFileName();		
		test = new BTree(readTestFilename);
		
		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 9) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}



		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 5) {
			fail(testName + " root: object did not match");
		}
				
		
		//Left child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 1 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 3 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Left child of roots left child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 0 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Middle Child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 2 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
				
		//Right child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(2));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " M child of(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 4 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test M child of nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Right child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 7 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		if(childNode.getNodeObject(1).getKey() != 9 || childNode.getNodeObject(1).getFrequency() != 1) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		///Left child of roots right child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test L child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 6 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Middle child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 8 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}	
		
		
		
		
		
		//Right child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(2));		
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 10 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 11 || childNode.getNodeObject(1).getFrequency() != 2) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		
		
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		
		
	}
	
	@Test
	public void degree2Insert12ObjectsWithCache() {
		String testName = "degree2Insert12ObjectsWithCache";
		int degree = 2;
		int sequenceLength = 3;
		BTree test = new BTree(sequenceLength, degree, "degree2Tests.gbk");
		test.createCache(5);
		
		//Insert items 4 - 9
		TreeObject toInsert;
		for (int i = 4; i <= 9; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//Insert items 0 - 3
		for (int i = 0; i <= 3; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//Insert items 10 - 11
		for (int i = 10; i <= 11; i++) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}
		
		//duplicate root object
		toInsert = new TreeObject(5, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 4
		toInsert = new TreeObject(4, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 7
		toInsert = new TreeObject(7, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//Duplicate object 11
		toInsert = new TreeObject(11, sequenceLength);
		test.bTreeInsert(toInsert);
		
		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 9) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}



		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 5) {
			fail(testName + " root: object did not match");
		}
		
		
		//Test child nodes
		BTreeNode childNode;
		
		
		
		//Left child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 1 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 3 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Left child of roots left child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 0 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Middle Child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}



		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 2 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
				
		//Right child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(2));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " M child of(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 4 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test M child of nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Right child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 7 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		if(childNode.getNodeObject(1).getKey() != 9 || childNode.getNodeObject(1).getFrequency() != 1) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		///Left child of roots right child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test L child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 6 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Middle child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 8 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}	
		
		
		
		
		
		//Right child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(2));		
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 10 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 11 || childNode.getNodeObject(1).getFrequency() != 2) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}

		test.close();
		
		//Repeat the whole test from file read
		String readTestFilename = test.getFileName();		
		test = new BTree(readTestFilename);
		
		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 9) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}



		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 5) {
			fail(testName + " root: object did not match");
		}
				
		
		//Left child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 1 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 3 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Left child of roots left child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 0 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
		
		
		//Middle Child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " L child of(0): isLeaf");
		}
		
		if(childNode.getNodeObject(0).getKey() != 2 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child of(0): objects or frequency in child 0 did not match");
		}
		
				
		//Right child of roots left child
		childNode = test.diskRead(test.getRoot().getChildPointer(0));
		childNode = test.diskRead(childNode.getChildPointer(2));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child of(0): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child of(0): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " M child of(0): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 4 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test M child of nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Right child of root
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 3) {
			fail(testName + " test child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '0') {
			fail(testName + " child(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 7 || childNode.getNodeObject(0).getFrequency() != 2) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		if(childNode.getNodeObject(1).getKey() != 9 || childNode.getNodeObject(1).getFrequency() != 1) {
			fail(testName + " test child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		///Left child of roots right child
		childNode = test.diskRead(childNode.getChildPointer(0));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test L child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test L child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test L child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 6 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test L child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		//Middle child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(1));
		
		if(childNode.getNumKeys() != 1) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 8 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}	
		
		
		
		
		
		//Right child of roots right child
		childNode = test.diskRead(test.getRoot().getChildPointer(1));
		childNode = test.diskRead(childNode.getChildPointer(2));		
		
		if(childNode.getNumKeys() != 2) {
			fail(testName + " test M child nodes(1): numKeys");
		}

		//numChildren
		if(childNode.getNumChildren() != 0) {
			fail(testName + " test M child nodes(1): numChildren");
		}

		//isLeaf
		if(childNode.isLeaf() != '1') {
			fail(testName + " test M child nodes(1): isLeaf");
		}
		
		//Test objects
		if(childNode.getNodeObject(0).getKey() != 10 || childNode.getNodeObject(0).getFrequency() != 1) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		//Test objects
		if(childNode.getNodeObject(1).getKey() != 11 || childNode.getNodeObject(1).getFrequency() != 2) {
			fail(testName + " test M child nodes(1): objects or frequency in child 1 did not match");
		}
		
		
		
		
		
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		
		
	}

	////////////////////////////////////////////////////////
	// XXX Degree 3 tests
	////////////////////////////////////////////////////////
	@Test
	public void degree3InsertSixObjects() {
		//Test a degree 3 BTree which should cause 1 split.
		//No duplicate elements involved
		String testName = "degree3InsertSixObjects";
		int degree = 3;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree3Tests.gbk");

		TreeObject toInsert;

		//Root should have 2
		//Root.c0 should contain 0,1
		//Root.c1 should contain 3,4,5
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 3) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 1) {
			fail(testName + " root: Frequency");
		}

		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 2) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 2) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 1) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 3,4,5
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 3) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 
	}
	
	@Test
	public void degree3InsertSixObjectsWithCache() {
		//Test a degree 3 BTree which should cause 1 split.
		//No duplicate elements involved
		String testName = "degree3InsertSixObjectsWithCache";
		int degree = 3;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree3Tests.gbk");
		test.createCache(500);

		TreeObject toInsert;

		//Root should have 2
		//Root.c0 should contain 0,1
		//Root.c1 should contain 3,4,5
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 3) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 1) {
			fail(testName + " root: Frequency");
		}

		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 2) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 2) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 1) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 3,4,5
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 3) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 
	}



	@Test
	public void degree3InsertSixDuplicates_TestFrequency() {
		//Test a degree 3 BTree which should cause 1 split.
		//No duplicate elements involved
		String testName = "degree3InsertSixObjects";
		int degree = 3;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree3Tests.gbk");

		TreeObject toInsert;

		//Root should have 2
		//Root.c0 should contain 0,1
		//Root.c1 should contain 3,4,5
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//duplicate the insertions
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 3) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}

		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 2) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 2) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 2) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 3,4,5
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 3) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 

	}
	
	@Test
	public void degree3InsertSixDuplicates_TestFrequencyWithCache() {
		//Test a degree 3 BTree which should cause 1 split.
		//No duplicate elements involved
		String testName = "degree3InsertSixDuplicates_TestFrequencyWithCache";
		int degree = 3;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree3Tests.gbk");
		test.createCache(500);

		TreeObject toInsert;

		//Root should have 2
		//Root.c0 should contain 0,1
		//Root.c1 should contain 3,4,5
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//duplicate the insertions
		for (int i = 0; i < 6; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 3) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 1) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 2) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}

		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object
		if (test.getRoot().getNodeObject(0).getKey() != 2) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 2) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 2) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 3,4,5
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 3) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 

	}

	////////////////////////////////////////////////////////
	// XXX Degree 4 tests
	////////////////////////////////////////////////////////

    @Test
	public void degree4InsertTwelveObjects() {
		//Test a degree 4 BTree which should cause 2 split.
		//No duplicate elements involved
		String testName = "degree4InsertTwelveObjects";
		int degree = 4;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree4Tests.gbk");

		TreeObject toInsert;

		//Root should have 3,7
		//Root.c0 should contain 0,1,2
		//Root.c1 should contain 4,5,6
        //Root.c2 should contain 7,8,9,10,11
		for (int i = 0; i < 12; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 4) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 2) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 3) {
			fail(testName + " root: numChildren");
		}

		//Frequency
		if(test.getRoot().getNodeObject(0).getFrequency() != 1) {
			fail(testName + " root: Frequency");
		}

		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object index 0
		if (test.getRoot().getNodeObject(0).getKey() != 3) {
			fail(testName + " root: object did not match");
		}

        //Test root object index 1
		if (test.getRoot().getNodeObject(1).getKey() != 7) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1,2
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 1) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 4,5,6
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 6) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
            case 2://child 1
				//num objects
				if(childNode.getNumKeys() != 4) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 8,9,10,11
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 8) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 9) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 10) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
                    case 3:
						if(childNode.getNodeObject(j).getKey() != 11) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 1) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 
	}

	@Test
	public void degree4InsertTwelveDuplicates_TestFrequency() {
		//Test a degree 3 BTree which should cause 1 split.
		//No duplicate elements involved
		String testName = "degree4InsertTwelveDuplicates_TestFrequency";
		int degree = 4;
		int sequenceLength = 2;

		BTree test = new BTree(sequenceLength, degree, "degree4Tests.gbk");

		TreeObject toInsert;

		//Root should have 3,7
		//Root.c0 should contain 0,1,2
		//Root.c1 should contain 4,5,6
        //Root.c2 should contain 7,8,9,10,11
		for (int i = 0; i < 12; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//duplicate the insertions
		for (int i = 0; i < 12; i++ ) {
			toInsert = new TreeObject(i, sequenceLength);
			test.bTreeInsert(toInsert);
		}

		//test Degree
		if (test.getDegree() != degree) {
			fail(testName + " getDegree");
		}

		//test Sequence Length
		if (test.getSequenceLength() != sequenceLength) {
			fail(testName + " sequenceLength");
		}

		//numNodes
		if(test.getNumNodes() != 4) {
			fail(testName + " numNodes");
		}

		//numKeys
		if(test.getRoot().getNumKeys() != 2) {
			fail(testName + " root: numKeys");
		}


		//numChildren
		if(test.getRoot().getNumChildren() != 3) {
			fail(testName + " root: numChildren");
		}

		//Frequency index 0
		if(test.getRoot().getNodeObject(0).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}

		//Frequency index 1
		if(test.getRoot().getNodeObject(1).getFrequency() != 2) {
			fail(testName + " root: Frequency");
		}
		//isLeaf
		if(test.getRoot().isLeaf() != '0') {
			fail(testName + " Root: isLeaf");
		}

		//Test root object index 0
		if (test.getRoot().getNodeObject(0).getKey() != 3) {
			fail(testName + " root: object did not match");
		}

        //Test root object index 1
		if (test.getRoot().getNodeObject(1).getKey() != 7) {
			fail(testName + " root: object did not match");
		}

		//Test child nodes
		BTreeNode childNode;
		for (int i = 0; i < test.getRoot().getNumChildren(); i++) {
			childNode = test.diskRead(test.getRoot().getChildPointer(i));
			switch(i) {
			case 0://child 0
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(0): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(0): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(0): isLeaf");
				}

				//Test child 0 order, should be 0,1,2
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					if(childNode.getNodeObject(j).getKey() != j || childNode.getNodeObject(j).getFrequency() != 2) {
						fail(testName + " test child nodes(0): objects or frequency in child 0 did not match");
					}
				}
				break;
			case 1://child 1
				//num objects
				if(childNode.getNumKeys() != 3) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 4,5,6
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 4) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 5) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 6) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
            case 2://child 1
				//num objects
				if(childNode.getNumKeys() != 4) {
					fail(testName + " test child nodes(1): numKeys");
				}

				//numChildren
				if(childNode.getNumChildren() != 0) {
					fail(testName + " test child nodes(1): numChildren");
				}

				//isLeaf
				if(childNode.isLeaf() != '1') {
					fail(testName + " child(1): isLeaf");
				}

				//Test child 1 order. Should be 8,9,10,11
				for (int j = 0; j < childNode.getNumKeys(); j++) {
					switch(j) {
					case 0:
						if(childNode.getNodeObject(j).getKey() != 8) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 1:
						if(childNode.getNodeObject(j).getKey() != 9) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					case 2:
						if(childNode.getNodeObject(j).getKey() != 10) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
                    case 3:
						if(childNode.getNodeObject(j).getKey() != 11) {
							fail(testName + " test child nodes(1): objects in child 1 did not match");
						}
						//frequency
						if(childNode.getNodeObject(j).getFrequency() != 2) {
							fail(testName + " test child nodes(1): frequency");
						}
						break;
					default:
						fail(testName + " test child nodes: child 1 had too many objects");
						break;
					}
				}
				break;
			default:
				fail(testName + " test child nodes: root had too many children");

			}
		}
		
		String readTestFilename = test.getFileName();
		//remove file
		File file = new File(readTestFilename);    	
		if (file.exists() && file.isFile()) {
			file.delete();
		} 
	}


	// HINT:
	//  instead of checking all intermediate states of constructing a tree
	//  you can check the final state of the tree and
	//  assert that the constructed tree has the expected number of nodes and
	//  assert that some (or all) of the nodes have the expected values
	@Test
	public void btreeDegree4Test()
	{
		//        //TODO instantiate and populate a bTree object
		//        int expectedNumberOfNodes = TBD;
		//
		//        // it is expected that these nodes values will appear in the tree when
		//        // using a level traversal (i.e., root, then level 1 from left to right, then
		//        // level 2 from left to right, etc.)
		//        String[] expectedNodesContent = new String[]{
		//                "TBD, TBD",      //root content
		//                "TBD",           //first child of root content
		//                "TBD, TBD, TBD", //second child of root content
		//        };
		//
		//        assertEquals(expectedNumberOfNodes, bTree.getNumberOfNodes());
		//        for (int indexNode = 0; indexNode < expectedNumberOfNodes; indexNode++)
		//        {
		//            // root has indexNode=0,
		//            // first child of root has indexNode=1,
		//            // second child of root has indexNode=2, and so on.
		//            assertEquals(expectedNodesContent[indexNode], bTree.getArrayOfNodeContentsForNodeIndex(indexNode).toString());
		//        }
	}
}
