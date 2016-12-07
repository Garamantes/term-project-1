package assignment2;

// testing program for methods within jTidy library 

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.w3c.tidy.Tidy;
import java.io.IOException;

public class JtidyMain {
	
	public static void main(String[] args)
	{
		File file = new File("sample.html");
		InputStream in = null;
		
		try{
			in = new FileInputStream(file);
			Tidy tidy = new Tidy();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			tidy.parse(in, out);
				
			tidy.getErrout();
			FileOutputStream file2 = new FileOutputStream(new File("sample2.html"));
			
			out.writeTo(file2);
			System.out.println(out.toString());
			
		} catch(IOException e){
			e.printStackTrace();
		}
			
	}
}
