package cs321.search;

import cs321.common.ParseArgumentException;
import cs321.create.GeneBankCreateBTree;
import cs321.create.stripper;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GeneBankSearchBTreeTest
{
	private String[] args;
	private GeneBankSearchBTreeArguments expectedConfiguration;
	private GeneBankSearchBTreeArguments actualConfiguration;

	@Test
	public void parse3CorrectArgumentsTest() throws ParseArgumentException, IOException
	{
		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[3];
		args[0] = "0";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";

		expectedConfiguration = new GeneBankSearchBTreeArguments(false, "./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 0, 0);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test (expected = ParseArgumentException.class)
	public void parse3IncorrectArgumentsTest_noSuchFile_BTree() throws ParseArgumentException, IOException
	{

		args = new String[3];
		args[0] = "0";
		args[1] = "NoSuchFile";
		args[2] = "./data/queries/query7";

		expectedConfiguration = new GeneBankSearchBTreeArguments(false, "./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 0, 0);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test (expected = ParseArgumentException.class)
	public void parse3IncorrectArgumentsTest_noSuchFile_Query() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[3];
		args[0] = "0";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "NoSuchFile";

		expectedConfiguration = new GeneBankSearchBTreeArguments(false, "./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 0, 0);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}


	@Test (expected = ParseArgumentException.class)
	public void parse3IncorrectArgumentsTest() throws ParseArgumentException
	{
		args = new String[3];
		args[0] = "1";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";

		expectedConfiguration = GeneBankSearchBTree.parseArguments(args);
		fail("parse3IncorrectArgumentsTest did not throw expected exception");
	}

	@Test
	public void parse4CorrectArgumentsTestWithCache() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[4];
		args[0] = "1";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";
		args[3] = "500";

		expectedConfiguration = new GeneBankSearchBTreeArguments(true, "./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 500, 0);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test
	public void parse4CorrectArgumentsTestWithoutCache() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[4];
		args[0] = "0";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";
		args[3] = "0";//debug

		expectedConfiguration = new GeneBankSearchBTreeArguments(false, "./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 0, 0);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test
	public void parse5CorrectArgumentsTest() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[5];
		args[0] = "1";
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";
		args[3] = "500";
		args[4] = "1";

		expectedConfiguration = new GeneBankSearchBTreeArguments(true,"./data/files_gbk/test3.gbk.btree.data.6.102","./data/queries/query7", 500, 1);
		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test (expected = NumberFormatException.class)
	public void parse5IncorrectArgumentsTest() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[5];
		args[0] = "cat";//Not an int
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";
		args[3] = "500";
		args[4] = "0";

		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		fail("parse5IncorrectArgumentsTest did not throw expected error");
	}

	@Test (expected = ParseArgumentException.class)
	public void parse6IncorrectArgumentsTest() throws ParseArgumentException, IOException
	{

		File test = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		if (test.isFile() && test.exists()) {
			test.delete();
			test.createNewFile();
		}
		else {
			test.createNewFile();
		}
		args = new String[6];
		args[0] = "cat";//Not an int
		args[1] = "./data/files_gbk/test3.gbk.btree.data.6.102";
		args[2] = "./data/queries/query7";
		args[3] = "500";
		args[4] = "0";
		args[5] = "0";

		actualConfiguration = GeneBankSearchBTree.parseArguments(args);
		fail("parse5IncorrectArgumentsTest did not throw expected error");
	}

	@Test
	public void test3_7_dump_withCache() throws Exception
	{


		File bTreeBinary = new File("./data/files_gbk/test3.gbk.btree.data.7.102");
		//Make sure the Btree file is generated
		args = new String[6];
		args[0] = "1";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "7";
		args[4] = "500";
		args[5] = "2";
		GeneBankCreateBTree.main(args);

		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query7_result"};
		stripper.main(stripArg);

		//Create query file
		args = new String[5];
		args[0] = "1";
		args[1] = bTreeBinary.toString();
		args[2] = "./data/queries/query7";
		args[3] = "500";
		args[4] = "1";        
		GeneBankSearchBTree.main(args);



		String dumpName = "qdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query7_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_7_dump_withoutCache: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_7_dump_withoutCache file mismatch: " + result);
		}    

	}

	@Test
	public void test3_7_dump_withoutCache() throws Exception
	{   	

		File bTreeBinary = new File("./data/files_gbk/test3.gbk.btree.data.7.102");
		//Make sure the Btree file is generated
		args = new String[5];
		args[0] = "0";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "7";
		args[4] = "2";
		GeneBankCreateBTree.main(args);


		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query7_result"};
		stripper.main(stripArg);

		//Create query file
		args = new String[4];
		args[0] = "0";
		args[1] = bTreeBinary.toString();
		args[2] = "./data/queries/query7";
		args[3] = "1";    
		GeneBankSearchBTree.main(args);



		String dumpName = "qdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query7_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_7_dump_withoutCache: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_7_dump_withoutCache file mismatch: " + result);
		}    

	}

	@Test
	public void test3_6_dump_withoutCache() throws Exception
	{   	

		File bTreeBinary = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		//Make sure the Btree file is generated
		args = new String[5];
		args[0] = "0";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "6";
		args[4] = "2";
		GeneBankCreateBTree.main(args);


		//Create query file
		args = new String[4];
		args[0] = "0";
		args[1] = bTreeBinary.toString();
		args[2] = "./data/queries/query6";
		args[3] = "1";    
		GeneBankSearchBTree.main(args);

		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query6_result"};
		stripper.main(stripArg);

		String dumpName = "qdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query6_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_6_dump_withoutCache: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_6_dump_withoutCache file mismatch: " + result);
		}    

	}

	@Test
	public void test3_6_dump_withCache() throws Exception
	{   	

		File bTreeBinary = new File("./data/files_gbk/test3.gbk.btree.data.6.102");
		//Make sure the Btree file is generated
		args = new String[6];
		args[0] = "1";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "6";
		args[4] = "500";
		args[5] = "2";
		GeneBankCreateBTree.main(args);


		//Create query file
		args = new String[5];
		args[0] = "1";
		args[1] = bTreeBinary.toString();
		args[2] = "./data/queries/query6";
		args[3] = "500";
		args[4] = "1";
		GeneBankSearchBTree.main(args);

		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query6_result"};
		stripper.main(stripArg);

		String dumpName = "qdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query6_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_6_dump_withCache: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_6_dump_withCache file mismatch: " + result);
		}    

	}

}
