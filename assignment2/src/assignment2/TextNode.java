package assignment2;

/*
 * �ؽ�Ʈ���.
 * �����ϰ� �ؽ�Ʈ�� ����ִ� ���
 * MD�� ������ �Ϲ� �ؽ�Ʈ�� ���۵Ǵ� ��� �׳� ���ڿ� �ϳ��� ��� ������ ��.
 * �߰��� HTML�� ���� ���� ���� ����.
 */

public class TextNode extends Node{
	private String text;
	
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

}
