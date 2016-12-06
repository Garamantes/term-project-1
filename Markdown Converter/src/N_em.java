//package github_parser;

import java.util.LinkedList;

public class N_em extends Node{
private String text;
public LinkedList<Node> list = new LinkedList<Node>();

	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.text;
	}
	
	public void printNodeInfo(){
		System.out.println("[Emphasis Code]");
	}
	
	public void addToList(String text){
		N_TextNode textnode = new N_TextNode();
		textnode.setContent(text);
		this.list.add(textnode);
	}
	
	public void addNewParagraph(){
		addToList("<em>");
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
}
