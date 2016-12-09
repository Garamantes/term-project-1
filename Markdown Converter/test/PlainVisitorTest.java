package mdconverter;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlainVisitorTest {
	
	private PlainVisitor testVisitor;
	
	
	@Before
	public void setup(){
		testVisitor = new PlainVisitor();
		System.out.println("setup");
	}
	
	@After
	public void teardeown(){
		System.out.println("done");
	}
	
	@Test
	public void test() {
		
		N_Blockquote bq = new N_Blockquote();
		bq.getList().add(new N_TextNode("text"));
		
		bq.getList().add(new N_Header());
		bq.getList().add(new N_newLine());
		bq.getList().add(new N_Blockquote());
		bq.getList().add(new N_Link(new HashMap<>()));
		bq.getList().add(new N_List("ol"));
		bq.getList().add(new N_emphasis());
		bq.getList().add(new N_Hr());
		bq.getList().add(new Node());
		
		

		
		
		
		testVisitor.visit(bq);
		
		HashMap<String,String[]> urlList = new HashMap<>();
		String[] value = {"one", "two"};
		urlList.put("key", value);
		N_Link link = new N_Link(urlList);
		testVisitor.visit(link);		
		link.setLinkKey("key");
		link.setLinkText("text");
					
		testVisitor.visit(link);
		link.setImage(true);
		testVisitor.visit(link);
		
		
		N_List list = new N_List("ul");
		list.addToList("1");
		testVisitor.visit(list);

		N_List list2 = new N_List("ol");
		list2.addToList("1");
		list2.addToList(bq);

		N_List listnode = new N_List("ul");
		listnode.addToList(list2);
		
		testVisitor.visit(list2);
		testVisitor.visit(listnode);
		
		
	}

}