package cs321.create;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * 
 * Not the kind you'd expect...
 * @author Zade
 *
 */
public class stripper {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.print("Usage: stripper <filename to strip>");
			System.exit(1);
		}
		File file = new File(args[0]);
		Scanner fs =  null;
		
		try {
			fs = new Scanner(file);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		String nextLine = null;
		while (fs.hasNext()) {
			nextLine = fs.nextLine();
			//nextLine.replaceAll("\r\n", "\n");
			sb.append(nextLine);
			sb.append('\n');
		}
		
		try {
            File dump = new File(args[0] + ".new");//Create filename

            if(dump.exists() && dump.isFile()) {//Delete currently existing file
                dump.delete();
            }
            
            dump.createNewFile();

            FileWriter dumpOutWriter = new FileWriter(dump); //Write it

            dumpOutWriter.write(sb.toString());
            dumpOutWriter.close();
        }
        catch (Exception e){
            System.err.println(e.toString());
        }
		

	}

}
