import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		MDParser myTestParser = new MDParser();
		
		//아래와 같은 String 3줄이 있다고 가정하고 dummyMDList에 삽입
		ArrayList<String> dummyMDList = new ArrayList<String>();
		String str1 = "## This is <H2> header #"; 	dummyMDList.add(str1);
		String str2 = "This is H1 header";			dummyMDList.add(str2);
		String str3 = "=======================";	dummyMDList.add(str3);
		String str4 = "This is normal text";		dummyMDList.add(str4);
		/*	결과적으로 
		 * "This is <H2> header" 라는 내용이 있는 level 2 헤더 노드 하나랑
		 * "This is H1 header"라는 내용이 있는 level 1 헤더 노드가 생성되면 됨.
		 */
		
		for(int i=0;i<dummyMDList.size();i++){
			//그냥 일반 노드 하나를 만들고,
			Node tempNode = new Node();
			
			//dummyMDList에 있는 스트링 한줄을 myTestParser에 있는 함수인 tokenize를 사용해서 토큰리스트를 만들고
			//그걸 tempNode에 넣는다.
			tempNode.setToken(myTestParser.tokenize(dummyMDList.get(i)));
			
			//addNodeToList 메소드를 이용해서 tempNode가 어떤 노드여야 하는지 확인하고
			//적절한 타입의 노드를 생성해서 nodeList에 추가한다.
			myTestParser.addNodeToList(tempNode);
		}
		
		//myTestParser 안에 있는 nodeList에 어떤 노드들이 들어가 있는지 확인.
		myTestParser.printAllNode();
		
		
	}
}
