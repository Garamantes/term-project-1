import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;


public class PlainVisitorTest {

	private PlainVisitor visitor;
	private LinkedList<Node> list;
	private N_TextNode textnode; 
	//String line1 = "</blockquote>";
	//String line1="Hello";
	
	@Before
	public void setup() {
		visitor = new PlainVisitor();
		textnode = new N_TextNode();
		list = new LinkedList<Node>();
		System.out.println("setup");
	}

	@After
	public void teardown() {
		
			System.out.println("teardown!");
	}

	@Test
	public void test1() {
		//list.add(new N_TextNode("njnkkl"));
		System.out.println("test!");
	}

	
}
