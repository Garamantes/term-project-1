
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
public class MDParser {
	LinkedList<Node> nodeList = new LinkedList<Node>();

//============================================================================
	//String 한 줄을 받아와서 Symbol 과 PlainText 분리해서 토큰리스트에 저장하고 그 리스트를 리턴하는 메소드
	public List<Token> tokenize(String str){
		//토큰리스트 생성
		List<Token> tokenList = new ArrayList<Token>();	
		
		//패턴: (PlainText) 이거나 (Symbol) 들을 찾는다
		Pattern p = Pattern.compile("([a-zA-Z0-9]+)|(\\S+)");	//아마 그룹을 추가해서 판단해야할듯 (/+\\S+) | (/+\\S)
		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			for (int i = 1; i <= matcher.groupCount(); i++) {
				//Group1: 이 토큰이 plainText라면 PlainText토큰 노드를 생성해서 setContent하고(텍스트추가) 토큰리스트에 추가
		    	if(i==1 && matcher.group(i) != null){
			        T_plainText token = new T_plainText();
			        token.setContent(matcher.group(i));
			    	tokenList.add(token);
		        }
				//Group2: 이 토큰이 symbol이라면 MDSymbol토큰 노드를 생성해서 setContent하고(텍스트추가) 토큰리스트에 추가
		    	else if(i==2 && matcher.group(i) != null ){
		    		//Symbol 중 < 로 시작하는건 HTML로 처리해야되는데 일단 보류하고 여기선 plainText처럼 처리...
		    		if(matcher.group(i).charAt(0) == '<'){
		    			T_plainText token = new T_plainText();
				        token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		    		/*else if(matcher.group(i).contains("\\*")){ //backslash token구현 위함	    		
			    		T_symbol token = new T_symbol();
			        	token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}*/else {	//Symbol 토큰 만들어서 추가		    		
			    		//Token token = new T_symbol();
			    		if((matcher.group(i).charAt(0)== '\\'))
			    		{
			    			T_plainText token = new T_plainText();
			    			token.setContent(matcher.group(i).substring(1));
					    	tokenList.add(token);
			    		}
			    		else
			    		{
			    			T_symbol token =new T_symbol();
			    			token.setContent(matcher.group(i));
					    	tokenList.add(token);
			    		}
			        	
		    		}
		        }
		    }
		}
		//토큰리스트 리턴
		return tokenList;
	}
//============================================================================

	
	
	
//============================================================================
	//(temp)노드 하나를 받아와서, 그 노드가 어떤 노드인지 판별하고(헤더인지 리스트인지..)
	//적절한 노드를 생성해서 그걸 노드 리스트에 추가하는 함수
	public void addNodeToList(Node newNode){
		/*if(newNode.getTokenList().size()==0)
			System.out.print("error!!\n");
		else
			System.out.print("not null\n");*/
		//노드 안에 들어있는 토큰리스트 가져오기
		List<Token> tokenList = newNode.getTokenList();
		
		
			
		//토큰의 첫번째 요소가 Symbol인 경우와 PlainText인 경우가 있다.
		//첫번째 요소가 Symbol인 경우
		
		if((newNode.getTokenList().size()!=0) && (tokenList.get(0) instanceof T_symbol)){
			//------------------ Header Node ------------------
			
			//토큰의 첫번째 Symbol이 #종류인지 확인
			if( tokenList.get(0).getContent().equals("#") ||
				tokenList.get(0).getContent().equals("##") ||
				tokenList.get(0).getContent().equals("###") ||
				tokenList.get(0).getContent().equals("####") ||
				tokenList.get(0).getContent().equals("#####") ||
				tokenList.get(0).getContent().equals("######") )
			{
				//헤더노드 생성해서 정보 전달
				Header header = new Header();
			//1. 레벨 설정
				header.setLevel(tokenList.get(0).getContent());
			//2. 텍스트 설정
				String text = "";
				int size = tokenList.size();
				for(int i=1;i<size;i++){
					//PlainText 토큰만 헤더노드의 Text로 전달
					if(tokenList.get(i) instanceof T_plainText){
						text = text.concat(tokenList.get(i).getContent()+ " ");
					}
				}
				header.setText(text);
				
				//노드 리스트에 지금 만든 헤더 노드 삽입
				nodeList.add(header);
			}
			//토큰에 '=' 종류만 있고 뒤에는 아무것도 없는 경우
			else if(tokenList.get(0).getContent().charAt(0)=='=' && tokenList.size()==1){
				if(nodeList.getLast() instanceof TextNode){	// '='나오기 직전 노드가 텍스트노드인지 확인
					//헤더 노드 생성
					Header header = new Header();
					//1. 레벨 설정. (= 계통은 1)
					header.setLevel(1);
					//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
					header.setText(((TextNode)nodeList.getLast()).getContent());
					//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
					nodeList.removeLast();
					nodeList.add(header);
				}else{System.out.println("md syntax error"); }
			}
			//토큰에 '-' 종류만 있고 뒤에는 아무것도 없는 경우
			else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
				if(nodeList.getLast() instanceof TextNode){	// '-'나오기 직전 노드가 텍스트노드인지 확인
					//헤더 노드 생성
					Header header = new Header();
					//1. 레벨 설정. (= 계통은 1)
					header.setLevel(2);
					//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
					header.setText(((TextNode)nodeList.getLast()).getContent());
					//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
					nodeList.removeLast();
					nodeList.add(header);
				}else{System.out.println("md syntax error"); }
			}else if(tokenList.get(0).getContent().contains("\\") ||
					tokenList.get(0).getContent().contains("\\'") ||
					tokenList.get(0).getContent().contains("\\*") ||
					tokenList.get(0).getContent().contains("\\_") ||
					tokenList.get(0).getContent().contains("\\{") ||
					tokenList.get(0).getContent().contains("\\}") ||
					tokenList.get(0).getContent().contains("\\[") ||
					tokenList.get(0).getContent().contains("\\(") ||
					tokenList.get(0).getContent().endsWith("\\)") ||
					tokenList.get(0).getContent().endsWith("\\#") ||
					tokenList.get(0).getContent().endsWith("\\.") ||
					tokenList.get(0).getContent().endsWith("\\!")){//Backslash Esape 인지 check
				System.out.println("토큰리스트에 넣을꺼" + tokenList.get(0).getContent());
			}
		}
		//토큰의 첫번째 기호가 Plain Text인 경우
		else if(newNode.getTokenList().size()!=0 && tokenList.get(0) instanceof T_plainText)
		{
			//TextNode 생성
			TextNode textnode = new TextNode();
			String text = "";
			//단어단위로 연결된 토큰들을 한개의 문자열로 합침
			int size = tokenList.size();
			for(int i=0;i<size;i++){
				text = text.concat(tokenList.get(i).getContent()+ " ");
			}
			//textnode에 하나로 합친 문자열을 전달
			textnode.setContent(text);
			nodeList.add(textnode);
		}
		else{
			System.out.println("다른 노드는 아직 구현 안됨");
		}
		
	}
//============================================================================

	public void printAllNode(){
		for(int i=0;i<nodeList.size();i++)
			nodeList.get(i).printNodeInfo();
	}
	
	
	

}
