package mdconverter;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class MDParserTest {

	private MDParser testParser1;
	private MDParser testParser2;
	private MDParser testParser3;
	
	
	@Before
	public void setup() {
		testParser1 = new MDParser();
		testParser2 = new MDParser();
		System.out.println("setup!!");
	}


	@After
	public void teardown() {
		System.out.println("teardown!!!");
	}

	
	
	
	@Test
	public void test() {
		ArrayList<String> testCases = new ArrayList<String>();
		testCases.add("Normal Text");		//Plain Text
		testCases.add("> blockquote");		
		testCases.add("# header");		//Header
		testCases.add("## header");
		testCases.add("### header");
		testCases.add("#### header");
		testCases.add("##### header");
		testCases.add("###### header");
		testCases.add("*emphasis*");		
		testCases.add("emphasis _in_ middle");		//Header
		testCases.add("emphasis at **back**");			//Code
		testCases.add("*****");		//emphasis
		testCases.add("_emphasis_");		//emphasis
		testCases.add("![ImageTest]"
				+ "(http://seoin.godohosting.com/seoin/wp-content/uploads/2013/05/%EB%A9%94%EC%9D%B83.jpg \"HGU\""); //List
		testCases.add("");
		testCases.add("Header Test");
		testCases.add("===========");
		testCases.add("Header Test 2");
		testCases.add("=========== fdf");
		testCases.add("Header Test");
		testCases.add("------------");
		testCases.add("Header Test 2");
		testCases.add("------------ fdf");
		testCases.add("*");
		testCases.add("===========");
		testCases.add("*");
		testCases.add("------------");
		testCases.add("1. List1");
		testCases.add("2. List2");
		testCases.add("* UL1");
		testCases.add("+ UL2");
		testCases.add("* UL1");
		testCases.add("- UL2");
		testCases.add("* UL1");
		testCases.add("* UL2");
		testCases.add("1. List1");
		testCases.add("* UL2");
		testCases.add("1. List1");
		testCases.add("+ UL2");
		testCases.add("1. List1");
		testCases.add("- UL2");
		testCases.add("'code'");
		testCases.add("This is [an example](http://example.com/ \"Title \") inline link .");
		testCases.add("Text (http://example.com/ \"Title \")");
		testCases.add("<adsf");
		testCases.add("[*");
		testCases.add("\\t");
		testCases.add("    ");
		testCases.add("I get 10 times more traffic from [ Google ] [1] than from [ Yahoo ] [] or [ MSN ] [3].");
		testCases.add("[ Google ] [1] than from [ Yahoo ] [] or [ MSN ] [3].");
		testCases.add("[1]: http://google.com/ \"Google\"");
		testCases.add("[2]: http://google.com/");
		testCases.add("![Alt text] (/path/to/img.jpg)");
		testCases.add("![ Alt text ][ id]");
		testCases.add("![ Alt text ][]");
		testCases.add("1. This is a list item with two paragraphs . Lorem ipsum dolor");
		testCases.add("\t sit amet , consectetuer adipiscing elit . Aliquam hendrerit");
		testCases.add("");
		testCases.add("\t sit amet , consectetuer adipiscing elit . Aliquam hendrerit");
		testCases.add("2. Suspendisse id sem consectetuer libero luctus adipiscing .");
		testCases.add("+ UL1");
		testCases.add("* UL2");
		testCases.add("1. List1");
		testCases.add("2. List2");
		testCases.add("\t> bq in list");
		testCases.add("> ## This is a header .");
		testCases.add("> ");
		testCases.add("> 1. This is the first list item .");
		testCases.add("> 2. This is the second list item .");
		testCases.add("> > Here ¡¯s some example code :");
		testCases.add("");
		testCases.add("endBQ");
		testCases.add(">");
		testCases.add(">");
		testCases.add("endLine");
		testCases.add("");
		testCases.add("");
		testCases.add("\\t After Newline");

		
		
		for(int i=0;i<testCases.size();i++){
			Node tempNode = new Node();
			tempNode.setToken(testParser1.tokenize(testCases.get(i)));
			testParser1.addNodeToList(tempNode, testParser1.nodeList);
			//System.out.println(tempNode.getTokenList().get(0).getContent());
		}
		
		///*
		testParser1.nodeList.clear();
		testCases.clear();
			
		System.out.println(testCases.size());
		testCases.add("> >");
		testCases.add(">");
		testCases.add("");
		testCases.add("::");
		
		for(int i=0;i<testCases.size();i++){
			Node tempNode = new Node();
			tempNode.setToken(testParser1.tokenize(testCases.get(i)));
			testParser1.addNodeToList(tempNode, testParser1.nodeList);
			//System.out.println(tempNode.getTokenList().get(0).getContent());
		}
		//*/
		
		N_newLine node = new N_newLine();
		Node tempNode = new Node();
		tempNode.setToken(testParser1.tokenize("\t text"));
		testParser1.addNodeToList(tempNode, testParser1.nodeList);

		
		String temp = "\'*_{}[]()#.!";
		for(int i=0;i<temp.length();i++)
			testParser1.backslashEsc(temp.charAt(i));
		
		temp = "*emTest*";
		Node newNode = new Node();
		node.setToken(testParser1.tokenize(temp));
		testParser1.em(newNode, testParser1.nodeList);
		
		
		
	
	}
	

}
