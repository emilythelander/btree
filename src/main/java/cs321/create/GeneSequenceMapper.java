package cs321.create;

/**
 * 
 * This class contains static methods to encode a gene sequence string to a long, decode
 * a long to a gene sequence string, and find the complement of a gene sequence string
 * 
 * @author Andrew Sorensen
 *
 */
public class GeneSequenceMapper {	
	
	
	/**
	 * This method checks a long two bits at a time starting from the two least significant bits.
	 * It bitwise ands these two bits with 1's and bit-shifts the result back to the right. The result
	 * will be a two digit binary number, decoded as follows:
	 * 				A = 00
     * 				T = 11
     * 				C = 01
     * 				G = 10
	 * 
	 * @param sequence - The long that represents a given gene sequence. The bits of this long
	 * 						read from the most significant bit (left) to the least significant bit (right)
	 * 						should correspond to the encoding of the gene string read left to right.
	 * @param sequenceLength - The length of the sequence contained inside the long (k in our BTree)
	 * @return - The decoded gene sequence as a string
	 */
	public static String decodeGeneSequence(long sequence, int sequenceLength) {
		
		StringBuilder sb = new StringBuilder();
		long currentChar;		
		//Iterate through all the twice the number of bits as there are characters in the sequence
		//moving two bits at a time
		for (int i = 2*sequenceLength - 2; i >= 0; i -= 2) {
			//Current Gene Character is the sequence bitwise ANDed with two 1's. The ones move
			// left by i (i increments by two) each iteration. 1&1 = 1, 1&0 = 0
			currentChar = (sequence & (0b11L << i)) >> i;//Shift result back so it is between 0-3
			switch((int) currentChar) {
				case 0: //the two bits of sequence & 11 == 00, meaning A
					sb.append('a');
					break;
				case 1://C
					sb.append('c');
					break;
				case 2://G
					sb.append('g');
					break;
				case 3://T
					sb.append('t');
					break;
				default:
						throw new IllegalStateException("Gene Sequence Decode encountered an unexpected error");
					
			}
		}
		return sb.toString();
	}
	
	/**
	 * This method produces a long whose bits read left to right represent the an equivalent gene sequence
	 * read left to right where the genes are encoded as follows:
	 * 				A = 00
     * 				T = 11
     * 				C = 01
     * 				G = 10
     *      	This method relies on bit shifting to calculate the correct number. It starts by using the 
     *			character at the end of the string (highest indexed char) to become the least significant bit
     *			of the long resultant long.
     *			It bitwise ORs zero with the associated binary encoding for the character, changing
     *			those two bits to be the characters encoding.
     * 
     * @param sequence - The gene sequence to encode
     * @return - A long value, the binary representation of which corresponds to a gene.
     */
    public static long encodeGeneSequence(String sequence) {
    	long result = 0;
    	//Avoid having to calculate the length every call
    	int Length = sequence.length() - 1;
    	for (int i = 0; i <= Length; i++) {
    		switch(sequence.charAt(Length - i)) {//Starts with chars at end of sequence string
    			case 'A':
    				//A == 0
    				break;
    			case 'T':
    				//T == 3, Insert binary 11 into the appropriate position in the long
    				result |= (0b11L << 2*i);
    				break;
    			case 'C':
    				//C == 1, Insert binary 01 into the appropriate position int the long
    				result |= (0b01L << 2*i);
    				break;
    			case 'G':
    				//G
    				result |= (0b10L << 2*i);
    				break;
    			default:
    				throw new IllegalArgumentException("Character is not valid for a gene sequence");
    		}
    	}
    	return result;
    	
    }
    
    /**
     * This method takes a string representing a gene sequence and returns the complement
     * @param sequence - A string that represents the gene sequence to get the complement of
     * @return - The String representation of a gene sequence's complement
     */
    public static String getComplementString(String sequence) {
    	StringBuilder sb = new StringBuilder();
    	int length = sequence.length();
    	
    	for (int i = 0; i < length; i++) {
    		switch(sequence.charAt(i)) {
    			case 'A':
    				sb.append('T');
    				break;
    			case 'T':
    				sb.append('A');
    				break;
    			case 'G':
    				sb.append('C');
    				break;
    			case 'C':
    				sb.append('G');
    				break;
    		}
    	}
    	
    	return sb.toString();
    	
    }    

}
