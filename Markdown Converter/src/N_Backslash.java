import java.util.LinkedList;

//package github_parser;

public class N_Backslash extends Node{
	public LinkedList<Node> list = new LinkedList<Node>();
	
	
	
	public void printNodeInfo(){
		System.out.println("[Back Slash Escape Code]*****************************");
	}
	
	public String accept(MDElementVisitor visitor){
		//return visitor.visit(this);
		return "123";
	}
}
