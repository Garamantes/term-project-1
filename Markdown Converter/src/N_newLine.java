
public class N_newLine extends Node{

	public void printNodeInfo(){
		System.out.println("[New Line Node]");
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
	
}
