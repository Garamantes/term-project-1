import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		MDParser myTestParser = new MDParser();
		
		//�Ʒ��� ���� String 3���� �ִٰ� �����ϰ� dummyMDList�� ����
		ArrayList<String> dummyMDList = new ArrayList<String>();
		String str1 = "## This is <H2> header #"; 	dummyMDList.add(str1);
		String str2 = "This is H1 header";			dummyMDList.add(str2);
		String str3 = "=======================";	dummyMDList.add(str3);
		String str4 = "This is normal text";		dummyMDList.add(str4);
		/*	��������� 
		 * "This is <H2> header" ��� ������ �ִ� level 2 ��� ��� �ϳ���
		 * "This is H1 header"��� ������ �ִ� level 1 ��� ��尡 �����Ǹ� ��.
		 */
		
		for(int i=0;i<dummyMDList.size();i++){
			//�׳� �Ϲ� ��� �ϳ��� �����,
			Node tempNode = new Node();
			
			//dummyMDList�� �ִ� ��Ʈ�� ������ myTestParser�� �ִ� �Լ��� tokenize�� ����ؼ� ��ū����Ʈ�� �����
			//�װ� tempNode�� �ִ´�.
			tempNode.setToken(myTestParser.tokenize(dummyMDList.get(i)));
			
			//addNodeToList �޼ҵ带 �̿��ؼ� tempNode�� � ��忩�� �ϴ��� Ȯ���ϰ�
			//������ Ÿ���� ��带 �����ؼ� nodeList�� �߰��Ѵ�.
			myTestParser.addNodeToList(tempNode);
		}
		
		//myTestParser �ȿ� �ִ� nodeList�� � ������ �� �ִ��� Ȯ��.
		myTestParser.printAllNode();
		
		
	}
}
