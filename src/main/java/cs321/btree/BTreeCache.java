package cs321.btree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Defines a cache to be used by BTree.java to increase the efficiency of building and
 * searching a BTree. The intent is to use this to minimize the number of diskReads and diskWrites.
 * @author Andrew Sorensen
 *
 */
public class BTreeCache implements Iterable<BTreeNode>{

	private int size;
	private LinkedList<BTreeNode> cache;

	/**
	 * Constructor - creates a new cache for the BTree
	 * @param size - the desired size for the new cache
	 */
	public BTreeCache(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Cache size cannot be negative");
		}
		this.size = size;
		this.cache = new LinkedList<BTreeNode>();
	}

	/**
	 * Method to add an object to the cache. Removes the last object
	 * 		if the cache has become to large. Returns null if it removed nothing
	 * 		else returns the object that was removed to make space
	 * @param toAdd - The object to add to the Cache
	 * @return - Null if nothing was removed from the cache, the object that was
	 * 		removed otherwise.
	 */
	public BTreeNode addObject(BTreeNode toAdd) {
		BTreeNode retval = null;
		if(toAdd == null) {
			throw new NullPointerException();
		}

		// Remove tail object if list is too large
		if(this.cache.size() >= this.size) {
			retval = this.cache.removeLast();
		}
		this.cache.addFirst(toAdd);
		return retval;
	}

	/**
	 * clearCache - clears the cache
	 */
	public void clearCache() {
		this.cache.clear();
	}

	/**
	 * removeObject - Removes an object from a a cache
	 * 		Uses a list iterator.
	 * @param objectToRemove - object to remove from cache
	 * @return - Object if object was in cache (and removed), null otherwise
	 */
	public BTreeNode removeObject(BTreeNode objectToRemove) {
		if (objectToRemove == null) {
			throw new NullPointerException();
		}
		BTreeNode retVal = null;
		boolean foundObject = false;
		// See if the object is in Cache;
		ListIterator<BTreeNode> listIterator = cache.listIterator();
		BTreeNode currentObject = null;
		while( !foundObject && listIterator.hasNext() ) {
			currentObject =  listIterator.next();
			if (currentObject.equals(objectToRemove)) {
				foundObject = true;
			}
		}		
		if (foundObject) {
			listIterator.remove();
			retVal = currentObject;
		}		
		return retVal;
	}

	@Override
	public Iterator<BTreeNode> iterator() {
		return this.cache.iterator();
	}




}
