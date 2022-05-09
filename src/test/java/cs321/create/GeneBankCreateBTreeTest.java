package cs321.create;

import cs321.common.ParseArgumentException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GeneBankCreateBTreeTest
{
    private String[] args;
    private GeneBankCreateBTreeArguments expectedConfiguration;
    private GeneBankCreateBTreeArguments actualConfiguration;

    @Test
    public void parse4CorrectArgumentsTest() throws ParseArgumentException, IOException
    {
    	
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[4];
        args[0] = "0";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";

        expectedConfiguration = new GeneBankCreateBTreeArguments(false, 20, "fileNameGbk.gbk", 13, 0, 0);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    
    @Test (expected = ParseArgumentException.class)
    public void parse4IncorrectArgumentsTest_CacheSelected() throws ParseArgumentException, IOException
    {
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[4];
        args[0] = "1";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";

        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        fail("parse4IncorrectArgumentsTest_CacheSelected did not throw expected exception");
    }
    
    @Test (expected = NumberFormatException.class)
    public void parse4IncorrectArgumentsTest_WrongDataType() throws ParseArgumentException, IOException
    {
    	
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[4];
        args[0] = "cat";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";

        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        fail("parse4IncorrectArgumentsTest_WrongDataType did not throw expected exception");
    }

    @Test
    public void parse5CorrectArgumentsTest() throws ParseArgumentException, IOException
    {
    	
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[5];
        args[0] = "1";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "100";

        expectedConfiguration = new GeneBankCreateBTreeArguments(true, 20, "fileNameGbk.gbk", 13, 100, 0);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    
    @Test
    public void parse5CorrectArgumentsTest_NoCache() throws ParseArgumentException, IOException
    {
    	
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[5];
        args[0] = "0";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "1";

        expectedConfiguration = new GeneBankCreateBTreeArguments(false, 20, "fileNameGbk.gbk", 13, 0, 1);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    
    @Test (expected = ParseArgumentException.class)
    public void parse5IncorrectArgumentsTest_NoCache() throws ParseArgumentException, IOException
    {
    	
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[6];
        args[0] = "0";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "100";
        args[5] = "1";

        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        fail("parse5IncorrectArgumentsTest_NoCache did not throw expected exception");
    }

    @Test
    public void parse6CorrectArgumentsTest() throws ParseArgumentException, IOException
    {
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
    	
        args = new String[6];
        args[0] = "1";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "500";
        args[5] = "1";

        expectedConfiguration = new GeneBankCreateBTreeArguments(true, 20, "fileNameGbk.gbk", 13, 500, 1);
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }
    
    @Test (expected = ParseArgumentException.class)
    public void parse6IncorrectArgumentsTest_BadDebug() throws ParseArgumentException, IOException
    {
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
        args = new String[6];
        args[0] = "1";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "500";
        args[5] = "4";
        
        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        fail("parse6IncorrectArgumentsTest_BadDebug did not throw expected exception");
    }
    
    @Test (expected = ParseArgumentException.class)
    public void parse7IncorrectArgumentsTest() throws ParseArgumentException, IOException
    {
    	File test = new File("fileNameGbk.gbk");
    	if (test.isFile() && test.exists()) {
    		test.delete();
    		test.createNewFile();
    	}
    	else {
    		test.createNewFile();
    	}
    	
        args = new String[7];
        args[0] = "1";
        args[1] = "20";
        args[2] = "fileNameGbk.gbk";
        args[3] = "13";
        args[4] = "500";
        args[5] = "1";
        args[6] = "1";

        actualConfiguration = GeneBankCreateBTree.parseArguments(args);
        fail("parse7IncorrectArgumentsTest did not throw expected exception");
    }
    
    @Test
    public void test3_6_dump_withCache() throws Exception
    {
    	
        args = new String[6];
        args[0] = "1";
        args[1] = "0";
        args[2] = "./data/files_gbk/test3.gbk";
        args[3] = "6";
        args[4] = "500";
        args[5] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test3.gbk.btree.dump.6"};
    	stripper.main(stripArg);
    	
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test3.gbk.btree.dump.6.new";
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
    
    @Test
    public void test3_6_dump_withoutCache() throws Exception
    {
    	
        args = new String[5];
        args[0] = "0";
        args[1] = "0";
        args[2] = "./data/files_gbk/test3.gbk";
        args[3] = "6";
        args[4] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test3.gbk.btree.dump.6"};
    	stripper.main(stripArg);
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test3.gbk.btree.dump.6.new";
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
    public void test3_7_dump_withoutCache() throws Exception
    {
    	
        args = new String[5];
        args[0] = "0";
        args[1] = "0";
        args[2] = "./data/files_gbk/test3.gbk";
        args[3] = "7";
        args[4] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test3.gbk.btree.dump.7"};
    	stripper.main(stripArg);
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test3.gbk.btree.dump.7.new";
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
    public void test3_7_dump_withCache() throws Exception
    {
    	
        args = new String[6];
        args[0] = "1";
        args[1] = "0";
        args[2] = "./data/files_gbk/test3.gbk";
        args[3] = "7";
        args[4] = "500";
        args[5] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test3.gbk.btree.dump.7"};
    	stripper.main(stripArg);
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test3.gbk.btree.dump.7.new";
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
    public void test0_4_dump_withCache() throws Exception
    {
    	
        args = new String[6];
        args[0] = "1";
        args[1] = "0";
        args[2] = "./data/files_gbk/test0.gbk";
        args[3] = "4";
        args[4] = "500";
        args[5] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test0.gbk.btree.dump.4"};
    	stripper.main(stripArg);
    	
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test0.gbk.btree.dump.4.new";
    	File dump = new File(dumpName);
    	File expectedDump = new File(expectedDumpName);
    	
    	if (!(dump.exists() && dump.isFile())) {
    		fail("test0_4_dump_withCache: dump file not created");
    	}
    	
    	long result = Files.mismatch(dump.toPath(), expectedDump.toPath());
    	
    	if (result != -1) {
    		fail("test0_4_dump_withCache file mismatch: " + result);
    	}    
    	
    }
    
    @Test
    public void test0_4_dump_withoutCache() throws Exception
    {
    	
        args = new String[5];
        args[0] = "0";
        args[1] = "0";
        args[2] = "./data/files_gbk/test0.gbk";
        args[3] = "4";
        args[4] = "2";
    	GeneBankCreateBTree.main(args);
    	
    	
    	//Create dump file stripped of '/r' chars
    	String[] stripArg = {"./data/files_gbk_expected_results/test0.gbk.btree.dump.4"};
    	stripper.main(stripArg);
    	
    	
    	String dumpName = "dump";
    	String expectedDumpName = "./data/files_gbk_expected_results/test0.gbk.btree.dump.4.new";
    	File dump = new File(dumpName);
    	File expectedDump = new File(expectedDumpName);
    	
    	if (!(dump.exists() && dump.isFile())) {
    		fail("test0_4_dump_withoutCache: dump file not created");
    	}
    	
    	long result = Files.mismatch(dump.toPath(), expectedDump.toPath());
    	
    	if (result != -1) {
    		fail("test0_4_dump_withoutCache file mismatch: " + result);
    	}    
    	
    }

}
