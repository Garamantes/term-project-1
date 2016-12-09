

import java.util.LinkedList;

public class N_Hr extends Node{
	public LinkedList<Node> list = new LinkedList<Node>();
	
	public void printNodeInfo(){
		System.out.println("[Horizental Rule Node]");
	}
	
	public void addToList(String text){
		N_TextNode textnode = new N_TextNode();
		textnode.setContent(text);
		this.list.add(textnode);
	}
	
	public void addNewParagraph(){
		addToList("<hr>");
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
}
