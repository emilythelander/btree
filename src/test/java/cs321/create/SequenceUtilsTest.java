package cs321.create;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequenceUtilsTest
{
    @Test
    public void longToDNAStringTest() throws Exception
    {
    	
    	//Single 'A' test (k = 1)
    	long encodedGene = 0b00;
    	String expectedResult = "a";
    	String actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 1);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Single A");
    	}
    	
    	//Single 'G' test (k = 1)
    	encodedGene = 0b10;
    	expectedResult = "g";
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 1);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Single G");
    	}
    	//Single 'T' test (k = 1)
    	encodedGene = 0b11;
    	expectedResult = "t";
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 1);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Single T");
    	}
    	//Single 'C' test (k = 1)
    	encodedGene = 0b01;
    	expectedResult = "c";
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 1);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Single C");
    	}
    	
    	//Max A test (k = 31)
    	encodedGene = 0;
    	expectedResult = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".toLowerCase();
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 31);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Max A");
    	}
    	
    	//Max C test (k = 31)
    	encodedGene = 0b01010101010101010101010101010101010101010101010101010101010101L;
    	expectedResult = "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC".toLowerCase();
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 31);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Max C");
    	}
    	
    	//Max G test (k = 31)
    	encodedGene = 0b10101010101010101010101010101010101010101010101010101010101010L;
    	expectedResult = "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG".toLowerCase();
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 31);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Max G");
    	}
    	
    	//Max T test (k = 31)
    	encodedGene = 0b11111111111111111111111111111111111111111111111111111111111111L;
    	expectedResult = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT".toLowerCase();
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, 31);
    	if (!actualResult.equals(expectedResult)) {
    		fail("Max T");
    	}
    	
    	//Arbitrary Test test
    	encodedGene = 0b001101101001110001L;
    	expectedResult = "ATCGGCTAC".toLowerCase();
    	actualResult = GeneSequenceMapper.decodeGeneSequence(encodedGene, expectedResult.length());
    	if (!actualResult.equals(expectedResult)) {
    		fail("Arbitrary");
    	}
    	
	}

    @Test
    public void DNAStringToLongTest() throws Exception
    {
    	// Single 'A' test (k=1)
    	String toTest = "A";
    	long expectedResult = 0b00;
    	long actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Single A");
    	}
    	
    	// Single 'C' test (k=1)
    	toTest = "C";
    	expectedResult = 0b01;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Single C");
    	}
    	
    	// Single 'G' test (k=1)
    	toTest = "G";
    	expectedResult = 0b10;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Single G");
    	}
    	
    	// Single 'T' test (k=1)
    	toTest = "T";
    	expectedResult = 0b11;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Single T");
    	}
    	
    	//Random Test
    	toTest = "ATCGGCTA";
    	expectedResult = 0b0011011010011100;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Arbitrary test");
    	}
    	
    	//Max Length 'A' Test (k = 31)
    	toTest = "A";//set toTest to A
    	//Add 30 more A's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'A';
    	}
    	
    	expectedResult = 0;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Maximum length A");
    	}
    	
    	//Max Length 'T' Test (k = 31)
    	toTest = "T";//set toTest to T
    	//Add 30 more T's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'T';
    	}
    	expectedResult = 0b11111111111111111111111111111111111111111111111111111111111111L;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Max Length T");
    	}
    	
    	//Max Length 'C' Test (k = 31)
    	toTest = "C";//set toTest to C
    	//Add 30 more C's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'C';
    	}
    	expectedResult = 0b01010101010101010101010101010101010101010101010101010101010101L;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Max Length C");
    	}
    	
    	//Max Length 'G' Test (k = 31)
    	toTest = "G";//set toTest to G
    	//Add 30 more G's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'G';
    	}
    	expectedResult = 0b10101010101010101010101010101010101010101010101010101010101010L;
    	actualResult = GeneSequenceMapper.encodeGeneSequence(toTest);
    	if (!(actualResult == expectedResult)) {
    		fail("Max Length G");
    	}
    	
    	
	}

	@Test
	public void getComplementTest() throws Exception
	{
    	//Max Length 'G' String Test (k = 31)
    	String toTest = "G";//set toTest to G
    	//Add 30 more G's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'G';
    	}
    	String expectedResult = "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC";
    	String actualResult = GeneSequenceMapper.getComplementString(toTest);
    	if (!(actualResult.equals(expectedResult))) {
    		fail("Max Length G complement");
    	}
    	
    	//Max Length 'T' String Test (k = 31)
    	toTest = "T";//set toTest to T
    	expectedResult = "A";
    	//Add 30 more T's/A's
    	for (int i = 0; i < 30; i++) {
    		toTest += 'T';
    		expectedResult += "A";
    	}
    	actualResult = GeneSequenceMapper.getComplementString(toTest);
    	if (!(actualResult.equals(expectedResult))) {
    		fail("Max Length T complement");
    	}
    	
    	
    	//All combo test
    	toTest = "TGAC";
    	expectedResult = "ACTG";
    	actualResult = GeneSequenceMapper.getComplementString(toTest);
    	if (!(actualResult.equals(expectedResult))) {
    		fail("All combo string complement test");
    	}
    	
		
	}
}
