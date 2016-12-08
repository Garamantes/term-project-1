
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;

import org.omg.Messaging.SyncScopeHelper;


public class MDParser {
	LinkedList<Node> nodeList = new LinkedList<Node>();
	List<Token> tokenList = new LinkedList<Token>();
	public HashMap<String, String[]> urlList = new HashMap<String,String[]>(); //[0]: url , [1]:title
	boolean bqFlag = false;

//=========tokenize: String을 받아서 토큰리스트 리턴==============================
	public List<Token> tokenize(String str){
		List<Token> tokenList = new ArrayList<Token>();
		
		//만약 String 이 \n만 있다면 NewLine 토큰 생성
		if(str.length()==0){
			T_newLine token = new T_newLine();
			token.setContent("\n");
			tokenList.add(token);
			return tokenList;
		}
		
		//패턴: (PlainText) 이거나 (Symbol) 들을 찾는다
		Pattern p = Pattern.compile("(\\_{1,2}[a-zA-Z0-9 ]+\\_{1,2})|"	//Group1 : Emphasis _~~_ or __~~__
								  + "(\\*{1,2}[a-zA-Z0-9 ]+\\*{1,2})|"	//Group2 : Emphasis *~~* or **~~**
								  + "(!\\[[a-zA-Z0-9 ]+\\])"			//Group3 : Image ![~~~~]
								  + "|(\\[[a-zA-Z0-9 ]+\\])"			//Group4 : Link [~~~~]
								  + "|(\\(.*\\))"						//Group5 : Link (http://~~)
								  + "|(\\w+)"							//Group6 : text
								  + "|(\\S+)");							//Group7 : symbol

		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			//i=그룹번호. 1~7까지 중에 맞는 그룹을 매칭해서 토큰을 생성, 추가한다.
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if(i==1 && matcher.group(i)!=null){	tokenList.add(new T_emphasis(matcher.group(i)));}
				else if(i==2 && matcher.group(i)!=null){tokenList.add(new T_emphasis(matcher.group(i)));}
				else if(i==3 && matcher.group(i)!=null){tokenList.add(new T_image(matcher.group(i)));}
				else if(i==4 && matcher.group(i)!=null){tokenList.add(new T_link(matcher.group(i)));}
				else if(i==5 && matcher.group(i)!=null){
					if(tokenList.get(tokenList.size()-1) instanceof T_link)
						tokenList.add(new T_link(matcher.group(i)));
					else
						tokenList.add(new T_plainText(matcher.group(i)));
					}
				else if(i==6 && matcher.group(i)!=null){tokenList.add(new T_plainText(matcher.group(i)));}
				else if(i==7 && matcher.group(i)!=null){
					//Symbol 중 < 로 시작하는건 HTML로 처리해야되는데 일단 보류하고 여기선 plainText처럼 처리...
					if(matcher.group(i).charAt(0) == '<'){tokenList.add(new T_plainText(matcher.group(i)));}
					else if(matcher.group(i).charAt(0)=='[' && matcher.group(i).charAt(1)==']'){
						tokenList.add(new T_link("[]"));
					}
					//Backslash escape
		    		else if(matcher.group(i).charAt(0)=='\\'){
		    			//시작이 '\'로 시작하고, 그 뒤에 오는 문자가 backslash escape 중에 하나라면 '\' 지우고 뒷부분 plainText로
		    			//ex) '\*' -> *를 plaintext 토큰으로
		    			if(backslashEsc(matcher.group(i).charAt(1))){
			    			tokenList.add(new T_plainText(String.valueOf(matcher.group(i).charAt(1))));
			    			
			    			//혹시 '\**' 이런게 왔으면 '\*'뒤에 있는 다른 symbol은 symbol 토큰으로.
			    			if(matcher.group(i).length()>2){tokenList.add(new T_symbol(matcher.group(i).substring(2,matcher.group().length())));}
			    		
			    		//'\' 뒤에 오는 문자가 backslash escape에 해당되는 문자가 아니라면 그냥 text 처리
		    			}else{tokenList.add(new T_plainText(matcher.group(i)));}
		    		}	
					//'<'나 '\' 같은 케이스가 아니라면 그냥 일반 symbol 토큰으로 처리
		    		else{tokenList.add(new T_symbol(matcher.group(i)));}
				}
				else{ //matcher.group(i)==null 이거나 패턴이 없는 경우
				}
			}
		}
		//토큰리스트 리턴
		return tokenList;
	}
//============================================================================

	
	
	
//==========addNodeToList: 노드랑 리스트를 받아서 노드를 리스트에 삽입===================
	public void addNodeToList(Node tempNode, LinkedList<Node> nodeList){
		List<Token> tokenList = tempNode.getTokenList();
		if(tempNode instanceof N_Blockquote){
			nodeList.add(tempNode);
			return;
		}
		//첫 토큰 : Symbol
		if(tokenList.get(0) instanceof T_symbol){
			//if header
			if(header(tempNode, nodeList) == true)
				return;
			//if blockquote
			else if(blockquote(tempNode, nodeList) == true){
				return;
			}else if(hr(tempNode,nodeList) == true){
				return;
			}
				
			}
		//첫 토큰 : Plain Text
		else if(tokenList.get(0) instanceof T_plainText)
		{
			int textStart=0;
			for(int i=0; i<tokenList.size();i++){
				//문자열의 중간에 링크 토큰이 나오는 경우
				if(tokenList.get(i) instanceof T_link && !(tokenList.get(i+1) instanceof T_link)){
					addTextNode(tempNode, nodeList, textStart, i-1);
					textStart = setLink(tempNode,nodeList,textStart);
				}
				else if(tokenList.get(i) instanceof T_image){
					
				}//중간에 em이 올 경우
				else if(tokenList.get(i) instanceof T_emphasis){
					
					String text = tokenList.get(i-1).getContent();
					if(text!="\n")
					{
						nodeList.add(new N_TextNode(text));
					}
					
					N_emphasis em =new N_emphasis();
					
					String emSymbol;
					emSymbol=tokenList.get(i).getContent().replaceAll("\\*", "");
					em.setText(emSymbol);
					nodeList.add(em);
					textStart = i+1;
				}
			}
			
			//text token 처리 
			addTextNode(tempNode,nodeList,textStart,tokenList.size());
		}
		//첫 토큰 : New Line
		else if(tokenList.get(0) instanceof T_newLine){
			N_newLine newLine = new N_newLine();
			nodeList.add(newLine);
		}
		//첫 토큰 : Link
		else if(tokenList.get(0) instanceof T_link){
			int textStart=0;
			if((tokenList.get(1) instanceof T_link)){
				textStart = setLink(tempNode,nodeList,textStart);
				for(int i=textStart; i<tokenList.size();i++){
					if(tokenList.get(i) instanceof T_link){
						System.out.println("textStart = "+textStart+"i = "+i);
						addTextNode(tempNode, nodeList, textStart, i);
						textStart = setLink(tempNode,nodeList,textStart);
						i = textStart;
					}
				}
				addTextNode(tempNode,nodeList,textStart,tokenList.size());
			}
			//[~~] : http://~~~ 이런 형태
			else if(tokenList.get(1).getContent().charAt(0)==':'){
				String linkText = ((T_link)tokenList.get(0)).getText();
				String temp = new String();
				for(int i=2;i<tokenList.size();i++)
					temp = temp.concat(tokenList.get(i).getContent());
				String [] split;
				split = temp.split("[()\"\']");
				String[] val = new String[2];
				val[0] = split[0];
				if(split.length<2)
					val[1] = "";
				else
					val[1] = split[1];
				
				urlList.put(linkText, val);
				
			}
		}
		//첫 토큰 : em
		else if(tokenList.get(0) instanceof T_emphasis){
			//em(tempNode,nodeList);
			N_emphasis em =new N_emphasis();
			String text ="";
			String emSymbol;
			emSymbol=tokenList.get(0).getContent().replaceAll("\\*", "");
			em.setText(emSymbol);
			nodeList.add(em);
			for(int i=0; i<tokenList.size()-1;i++){
				text =text.concat(tokenList.get(i+1).getContent()+ " ");
			}
			nodeList.add(new N_TextNode(text));
		}
		
		else{
			System.out.println("다른 토큰는 아직 구현 안됨");
		}
		
	}
	
	
	
	
//==========================================================================//
//==========================================================================//
//							노드별 메소드										//
//==========================================================================//
//==========================================================================//
	
	
	
//==========header: 토큰리스트랑 노드리스트 받아서 삽입하기===================
	public void addTextNode(Node tempNode, LinkedList<Node> nodeList, int start, int end){
		List<Token> tokenList = tempNode.getTokenList();
		String text = new String();	
		
		//Text token의 범위 안에서 한개의 String으로 합침
		for(int i=start;i<end;i++)
			text = text.concat(tokenList.get(i).getContent()+ " ");
		
		nodeList.add(new N_TextNode(text));
	}

	
	
	public boolean header(Node tempNode, LinkedList<Node> nodeList){
		//----------------------------------- Header Node -------------------------------------------
		List<Token> tokenList = tempNode.getTokenList();
		//토큰의 첫번째 Symbol이 #종류인지 확인
		if( tokenList.get(0).getContent().equals("#") ||
			tokenList.get(0).getContent().equals("##") ||
			tokenList.get(0).getContent().equals("###") ||
			tokenList.get(0).getContent().equals("####") ||
			tokenList.get(0).getContent().equals("#####") ||
			tokenList.get(0).getContent().equals("######") )
		{
			//헤더노드 생성해서 정보 전달
			N_Header header = new N_Header();
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
			return true;
		}
		//토큰에 '=' 종류만 있고 뒤에는 아무것도 없는 경우
		else if(tokenList.get(0).getContent().charAt(0)=='=' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '='나오기 직전 노드가 텍스트노드인지 확인
				//헤더 노드 생성
				N_Header header = new N_Header();
				//1. 레벨 설정. (= 계통은 1)
				header.setLevel(1);
				//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false; }
		}
		//토큰에 '-' 종류만 있고 뒤에는 아무것도 없는 경우
		else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '-'나오기 직전 노드가 텍스트노드인지 확인
				//헤더 노드 생성
				N_Header header = new N_Header();
				//1. 레벨 설정. (= 계통은 1)
				header.setLevel(2);
				//2. 텍스트 설정 (직전 노드에서 텍스트를 가져와서 그걸 사용)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//이제 직전노드(텍스트만 있는)는 필요없으니까 지우고 노드리스트에 헤더노드 삽입
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false;}
		}
		else //code shouldn't reach here
			return false;
	}
	
	//em함수
	public boolean em(Node newNode, LinkedList<Node> nodeList){/************************************************************************************************/
		List<Token> tokenList = newNode.getTokenList();
		N_emphasis em =new N_emphasis();
		String text = "";
		
		text=tokenList.get(0).getContent().replaceAll("\\*", "");
		em.setText(text);
		nodeList.add(em);
		
		return true;
	}
	
	public boolean hr(Node newNode, LinkedList<Node> nodeList){
		List<Token> tokenList = newNode.getTokenList();
		if(tokenList.get(0).getContent().contains("***")||
				tokenList.get(0).getContent().contains("---")/*||
				tokenList.get(0).getContent().contains("* * *")||
				tokenList.get(0).getContent().contains("- - -")*/||
				tokenList.get(0).getContent().contains("** *")||
				tokenList.get(0).getContent().contains("* **")||
				tokenList.get(0).getContent().contains("-- -")||
				tokenList.get(0).getContent().contains("- --")){
			//last nodelist를 바라봐야한다
			N_Hr hr = new N_Hr();
			nodeList.add(hr);
			hr.addNewParagraph();
			
			return true;
		}
		else{
			//System.out.println("hr아님");
			return false;
		}
	}
	
	public boolean blockquote(Node newNode, LinkedList<Node> nodeList){
		List<Token> tokenList = newNode.getTokenList();
		Node lastNode;
		//blockquote가 아니면 false리턴
		if(tokenList.get(0).getContent().equals(">") != true){	
			return false;
		}
		
		if(tokenList.get(0).getContent().equals(">") == true)// && tokenList.get(1).getContent().equals(">") == true){
		{
			
			//이번 노드가 nested BQ인 경우
			if(tokenList.size()>1 && tokenList.get(1).getContent().equals(">") == true)
			{
				//이전에 BQ없는데 처음부터 nested BQ인 경우
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					addNodeToList(bq, nodeList);
					addNodeToList(newNode,nodeList);	//아래 경우로 만드는 것
					return true;
				}
				//마지막 노드가 BQ이고 그 안에서 nest 하는 경우
				else if(nodeList.size()>0 && ((lastNode=nodeList.getLast()) instanceof N_Blockquote))
				{
					if(tokenList.size()==1){
						((N_Blockquote)lastNode).addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						T_plainText token = new T_plainText("<br>");
						node.getTokenList().add(token);
						addNodeToList(node, ((N_Blockquote)lastNode).getList());
						return true;
					}
				}
				else
					return false;
			}
			//Nested BQ 아닌 경우
			else
			{
				// 문서의 처음이 BQ거나   || BQ가 새로 등장하는 경우
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					if(tokenList.size()==1){
						bq.addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						addNodeToList(node, bq.getList());
						addNodeToList(bq, nodeList);	
						return true;
					}
				}
				//마지막 노드가 BQ였던 경우
				else if(nodeList.size()>0 && ((lastNode=nodeList.getLast()) instanceof N_Blockquote))
				{
					//> 만 있는 빈 칸
					if(tokenList.size()==1){
						((N_Blockquote)lastNode).addNewParagraph();
						return true;
					}else{
						tokenList.remove(0);
						Node node = new Node(tokenList);
						T_plainText token = new T_plainText("<br>");
						node.getTokenList().add(token);
						addNodeToList(node, ((N_Blockquote)lastNode).getList());
						return true;
					}			
				}
				else
					return false;
			
			}
		}
		return false;
	}
					
					
	public boolean backslashEsc(char c){
		if(c=='\\' || c=='\'' || c=='*' || c=='_' || c=='{' || c=='}' || 
		   c=='[' || c==']' || c=='(' || c==')' || c=='#' || c=='.' || c=='!'){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public int setLink(Node tempnode, LinkedList<Node> nodeList, int startIndex){
		List<Token> tokenList = tempnode.getTokenList();

		//Link Token 위치 검색
		while(!(tokenList.get(startIndex) instanceof T_link)) startIndex++;
		System.out.println(">> link start: "+startIndex);
		//[~~~~][~~~] 이렇게 2개가 연달아 있는 경우
		if(((T_link)tokenList.get(startIndex+1)).getType().equals("text") ){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex+1)).getText());
			nodeList.add(linkNode);
			
			//다음 TextToken이 시작하는 위치 리턴
			return startIndex+2;
		}
		//[~~~~](~~~) 이런 경우
		else if(((T_link)tokenList.get(startIndex+1)).getType().equals("url") ){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			//[~~~](~~~) 이런 경우, linkText와 linkKey 는 동일
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex)).getText());
			nodeList.add(linkNode);
			
			//Hashmap에 key, url, title추가
			String key = linkNode.getLinkKey();
			String url = ((T_link)tokenList.get(startIndex+1)).getUrl();
			String title = ((T_link)tokenList.get(startIndex+1)).getTitle();
			String[] val = new String[2];
			val[0] = url.toLowerCase(); val[1] = title;
			urlList.put(key,val);
			
			//다음 TextToken이 시작하는 위치 리턴
			return startIndex+2;
		}
		//[~~~][] 이런 경우
		else if(((T_link)tokenList.get(startIndex+1)).getType().equals("empty")){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex)).getText().toLowerCase());
			nodeList.add(linkNode);
			//다음 TextToken이 시작하는 위치 리턴
			return startIndex+2;
		}
		else
			return -1;
		
	}
	
	
	
	
	
	
	public void printAllNode(){
		for(int i=0;i<nodeList.size();i++)
			nodeList.get(i).printNodeInfo();
	}
	
	
	public String concatString(List<Token> tokenList, int startIndex){
		String text = new String();
		for(int i=startIndex;i<tokenList.size();i++){
			text = text.concat(tokenList.get(i).getContent()+ " ");
		}
		return text;
	}

}
