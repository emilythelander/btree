package cs321.search;

import cs321.common.ParseArgumentException;
import cs321.create.GeneBankCreateBTree;
import cs321.create.stripper;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;

public class GeneBankSearchDatabaseTest
{

	private String[] args;
	private GeneBankSearchDatabaseArguments expectedConfiguration;
	private GeneBankSearchDatabaseArguments actualConfiguration;


	@Test
	public void parse2CorrectArgumentsTest() throws ParseArgumentException
	{
		args = new String[2];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";

		expectedConfiguration = new GeneBankSearchDatabaseArguments("test.db","./data/queries/query7", 0);
		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test (expected = ParseArgumentException.class)
	public void parseIncorrectArgumentsTest_tooManyArgs() throws ParseArgumentException
	{
		args = new String[4];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";
		args[2] = "0";
		args[3] = "0";

		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		fail("parseIncorrectArgumentsTest_tooManyArgs did not produce expected exception");
	}

	@Test
	public void parse3CorrectArgumentsTest_debug0() throws ParseArgumentException
	{
		args = new String[3];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";
		args[2] = "0";

		expectedConfiguration = new GeneBankSearchDatabaseArguments("test.db","./data/queries/query7", 0);
		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test
	public void parse3CorrectArgumentsTest_debug1() throws ParseArgumentException
	{
		args = new String[3];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";
		args[2] = "1";

		expectedConfiguration = new GeneBankSearchDatabaseArguments("test.db","./data/queries/query7", 1);
		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		assertEquals(expectedConfiguration, actualConfiguration);
	}

	@Test (expected = ParseArgumentException.class)
	public void parse3IncorrectArgumentsTest_debug2() throws ParseArgumentException
	{
		args = new String[3];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";
		args[2] = "2";

		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		fail("parse3IncorrectArgumentsTest_debug2 did not throw expected Exception");
	}

	@Test (expected = NumberFormatException.class)
	public void parse3IncorrectArgumentsTest_debugNAN() throws ParseArgumentException
	{
		args = new String[3];
		args[0] = "test.db";
		args[1] = "./data/queries/query7";
		args[2] = "jim";

		actualConfiguration = GeneBankSearchDatabase.parseArguments(args);
		fail("parse3IncorrectArgumentsTest_debugNAN did not throw expected Exception");
	}

	@Test
	public void test3_7_dump() throws Exception
	{   	
		//Make sure the Btree file is generated
		args = new String[5];
		args[0] = "0";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "7";
		args[4] = "1";
		GeneBankCreateBTree.main(args);

		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query7_result"};
		stripper.main(stripArg);

		//Create dbquery dump file
		args = new String[3];
		args[0] = "./data/files_gbk/test3.gbk.7.db";
		args[1] = "./data/queries/query7";
		args[2] = "1";    
		GeneBankSearchDatabase.main(args);



		String dumpName = "dbqdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query7_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_7_dump: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_7_dump file mismatch: " + result);
		}    

	}

	@Test
	public void test3_6_dump() throws Exception
	{ 
		//Make sure the Btree file is generated
		args = new String[5];
		args[0] = "0";
		args[1] = "0";
		args[2] = "./data/files_gbk/test3.gbk";
		args[3] = "6";
		args[4] = "1";
		GeneBankCreateBTree.main(args);


		//Create dump file stripped of '/r' chars
		String[] stripArg = {"./data/files_gbk_expected_results/test3_query6_result"};
		stripper.main(stripArg);

		//Create dbquery dump file
		args = new String[3];
		args[0] = "./data/files_gbk/test3.gbk.6.db";
		args[1] = "./data/queries/query6";
		args[2] = "1";    
		GeneBankSearchDatabase.main(args);



		String dumpName = "dbqdump";
		String expectedDumpName = "./data/files_gbk_expected_results/test3_query6_result.new";
		File dump = new File(dumpName);
		File expectedDump = new File(expectedDumpName);

		if (!(dump.exists() && dump.isFile())) {
			fail("test3_6_dump: dump file not created");
		}

		long result = Files.mismatch(dump.toPath(), expectedDump.toPath());

		if (result != -1) {
			fail("test3_6_dump file mismatch: " + result);
		}    

	}

}
