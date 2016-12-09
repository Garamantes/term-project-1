package mdconverter;
import java.util.LinkedList;

public class N_List extends Node{
	private LinkedList<Node> list = new LinkedList<Node>();
	private String ListType;
	private boolean newLine = false;
	
	public N_List(){}
	public N_List(String str){
		if(str.equals("ul"))
			this.ListType = "ul";
		else if(str.equals("ol"))
			this.ListType = "ol";
	}
	
	
	public void addToList(String str){
		N_TextNode node = new N_TextNode(str);
		this.list.add(node);
	}
	public void addToList(Node node){list.add(node);}	//blockquote 노드가 들어올 수 있기 때문에
	public void addToList(N_List listNode){	this.list.add(listNode);}
	
	//\t 을 사용해서 원래 있던 문장에 붙여야 하는 경우
	public void concatToList(String str){
		if(this.list.getLast() instanceof N_TextNode){
			String tempStr = ((N_TextNode)this.list.getLast()).getContent().concat(str);
			this.list.removeLast();
			this.addToList(tempStr);
		
		}
	}
	

	
	public void setNewLine(boolean val){this.newLine = val;	}
	public boolean getNewLine(){return this.newLine;}
	public String getListType(){return this.ListType;}	//ul인지 ol인지
	public LinkedList<Node> getList(){	return this.list;}
	
	public void printNodeInfo(){
		System.out.println("[List Node]");
		System.out.println("List Type : "+getListType());
		for(int i=0;i<list.size();i++){
			if(list.get(i) instanceof N_TextNode)
				System.out.print(((N_TextNode)list.get(i)).getContent()+"--");
		}
		System.out.println();
	}
	

	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
}
