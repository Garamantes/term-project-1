package mdconverter;
import java.io.*;

import org.w3c.tidy.Tidy;

public class HtmlValidator {

	public int checkHtml(String fileName){
		Tidy tidy = new Tidy();
		OutputStream out = null;
		try {

			//CMD ¿ë
			File upOne = new File(System.getProperty("user.dir")).getParentFile();
			String filepath = upOne.getAbsolutePath();
			InputStream in = new FileInputStream(filepath+"/src/"+fileName);

			tidy.parse(in,out);
			in.close();
			return tidy.getParseErrors();
		}  catch (IOException e) {e.printStackTrace();}

		//Code should not reach here
		return -1;
	}
}
