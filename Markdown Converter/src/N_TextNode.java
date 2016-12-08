/*
 * �ؽ�Ʈ���.
 * �����ϰ� �ؽ�Ʈ�� ����ִ� ���
 * MD�� ������ �Ϲ� �ؽ�Ʈ�� ���۵Ǵ� ��� �׳� ���ڿ� �ϳ��� ��� ������ ��.
 * �߰��� HTML�� ���� ���� ���� ����.
 */

public class N_TextNode extends Node{
	private String text;
	
	public N_TextNode(){}
	public N_TextNode(String str){
		setContent(str);
	}
	
	public void setContent(String text){
		this.text = text;
	}
	public String getContent(){
		return text;
	}
	
	public void printNodeInfo(){
		System.out.println("[Text Node]");
		System.out.println("Text : "+ this.text);
	}
	
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
	

}
