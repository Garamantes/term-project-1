import java.util.ArrayList;

public class N_Blockquotes extends Node{
	private ArrayList<String> textList = new ArrayList<String>();
	private Node nestedNode;
	private int bqLevel;
	
	
	
	public void addToTextList(String text){
		this.textList.add(text);
	}
	
	public void addNewParagraph(){
		this.textList.add("</p>\n<p>");
	}
	
	public void deleteTextList(int index){
		this.textList.remove(index);
	}
	
	public int getListSize(){
		return textList.size();
	}
	
	public void setBQLevel(int bqLevel){
		this.bqLevel = bqLevel;
	}
	
	public int getBQLevel(){
		return this.bqLevel;
	}
	
	
	public void nestNode(Node newNode){
		this.nestedNode = newNode;
	}
	
	public void printNodeInfo(){
		System.out.println("[Blockquote Node]");
		System.out.print("text: ");
		for(int i=0;i<textList.size();i++){
			System.out.println(textList.get(i));
		}
	}
	
}
