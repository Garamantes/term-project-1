import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.tidy.Tidy;

public class HtmlValidator {

	public int checkHtml(String fileName){
		Tidy tidy = new Tidy();
		OutputStream out = null;
		try {
			InputStream in = new FileInputStream("./"+fileName);
			
			tidy.parse(in,out);
			return tidy.getParseErrors();
		} catch (FileNotFoundException e) {e.printStackTrace();	}
		
		//Code should not reach here
		return -1;
	}
}
