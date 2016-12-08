import java.util.LinkedList;

//package link;

public class N_emphasis extends Node{
	public LinkedList<Node> list = new LinkedList<Node>();
	private String text;
	
	public void addToList(String text){
		N_TextNode textnode = new N_TextNode();
		textnode.setContent(text);
		this.list.add(textnode);
	}
	
	public void addToList(N_emphasis em){
		this.list.add(em);
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.text;
	}
}
