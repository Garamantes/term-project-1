package mdconverter;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class N_ListTest {

	private N_List nodelist;
	private LinkedList<Node> list;
	
	String line1 = "ul";
	String line2 = "ol";
	String line3 = "\tHello";

	@Before
	public void setup() {
		nodelist = new N_List(line1);
		nodelist = new N_List(line2);
		nodelist = new N_List(line3);
		list = new LinkedList<Node>();
		
		System.out.println("setup");
	}


	@After
	public void teardown() {
		System.out.println("teardown");
	}

	@Test
	public void test() {
		list.add(new N_TextNode("njnkkl"));
		assertTrue("Yes",nodelist.equals("\tHello"));
		System.out.println("test");
	}

}
