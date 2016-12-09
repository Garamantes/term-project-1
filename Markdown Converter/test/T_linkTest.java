import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class T_linkTest {

	private T_link tlink;
	
	String line = "[]";
	String line2 = "[~~~]";
	String line3 = "(http:~~~)";
	String line4 = "(http:~~~ \"sffd\")";
	

	@Before
	public void setup() {
		tlink = new T_link(line);
		tlink = new T_link(line2);
		tlink = new T_link(line3);
		tlink = new T_link(line4);
		System.out.println("Set up!!");
	}


	@After
	public void teardown() {
		System.out.println("Tear down!");
	}

	@Test
	public void T_link() {

		System.out.println("Test");
	}

}
