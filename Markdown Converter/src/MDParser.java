
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

//=========tokenize: String�� �޾Ƽ� ��ū����Ʈ ����==============================
	public List<Token> tokenize(String str){
		List<Token> tokenList = new ArrayList<Token>();
		
		//���� String �� \n�� �ִٸ� NewLine ��ū ����
		if(str.length()==0){
			T_newLine token = new T_newLine();
			token.setContent("\n");
			tokenList.add(token);
			return tokenList;
		}
		
		//����: (PlainText) �̰ų� (Symbol) ���� ã�´�
		Pattern p = Pattern.compile("(\\_{1,2}[a-zA-Z0-9 ]+\\_{1,2})|"	//Group1 : Emphasis _~~_ or __~~__
								  + "(\\*{1,2}[a-zA-Z0-9 ]+\\*{1,2})|"	//Group2 : Emphasis *~~* or **~~**
								  + "(!\\[[a-zA-Z0-9 ]+\\])"			//Group3 : Image ![~~~~]
								  + "|(\\[[a-zA-Z0-9 ]+\\])"			//Group4 : Link [~~~~]
								  + "|(\\(.*\\))"						//Group5 : Link (http://~~)
								  + "|(\\w+)"							//Group6 : text
								  + "|(\\S+)");							//Group7 : symbol

		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			//i=�׷��ȣ. 1~7���� �߿� �´� �׷��� ��Ī�ؼ� ��ū�� ����, �߰��Ѵ�.
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
					//Symbol �� < �� �����ϴ°� HTML�� ó���ؾߵǴµ� �ϴ� �����ϰ� ���⼱ plainTextó�� ó��...
					if(matcher.group(i).charAt(0) == '<'){tokenList.add(new T_plainText(matcher.group(i)));}
					else if(matcher.group(i).charAt(0)=='[' && matcher.group(i).charAt(1)==']'){
						tokenList.add(new T_link("[]"));
					}
					//Backslash escape
		    		else if(matcher.group(i).charAt(0)=='\\'){
		    			//������ '\'�� �����ϰ�, �� �ڿ� ���� ���ڰ� backslash escape �߿� �ϳ���� '\' ����� �޺κ� plainText��
		    			//ex) '\*' -> *�� plaintext ��ū����
		    			if(backslashEsc(matcher.group(i).charAt(1))){
			    			tokenList.add(new T_plainText(String.valueOf(matcher.group(i).charAt(1))));
			    			
			    			//Ȥ�� '\**' �̷��� ������ '\*'�ڿ� �ִ� �ٸ� symbol�� symbol ��ū����.
			    			if(matcher.group(i).length()>2){tokenList.add(new T_symbol(matcher.group(i).substring(2,matcher.group().length())));}
			    		
			    		//'\' �ڿ� ���� ���ڰ� backslash escape�� �ش�Ǵ� ���ڰ� �ƴ϶�� �׳� text ó��
		    			}else{tokenList.add(new T_plainText(matcher.group(i)));}
		    		}	
					//'<'�� '\' ���� ���̽��� �ƴ϶�� �׳� �Ϲ� symbol ��ū���� ó��
		    		else{tokenList.add(new T_symbol(matcher.group(i)));}
				}
				else{ //matcher.group(i)==null �̰ų� ������ ���� ���
				}
			}
		}
		//��ū����Ʈ ����
		return tokenList;
	}
//============================================================================

	
	
	
//==========addNodeToList: ���� ����Ʈ�� �޾Ƽ� ��带 ����Ʈ�� ����===================
	public void addNodeToList(Node tempNode, LinkedList<Node> nodeList){
		List<Token> tokenList = tempNode.getTokenList();
		if(tempNode instanceof N_Blockquote){
			nodeList.add(tempNode);
			return;
		}
		//ù ��ū : Symbol
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
		//ù ��ū : Plain Text
		else if(tokenList.get(0) instanceof T_plainText)
		{
			int textStart=0;
			for(int i=0; i<tokenList.size();i++){
				//���ڿ��� �߰��� ��ũ ��ū�� ������ ���
				if(tokenList.get(i) instanceof T_link && !(tokenList.get(i+1) instanceof T_link)){
					addTextNode(tempNode, nodeList, textStart, i-1);
					textStart = setLink(tempNode,nodeList,textStart);
				}
				else if(tokenList.get(i) instanceof T_image){
					
				}//�߰��� em�� �� ���
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
			
			//text token ó�� 
			addTextNode(tempNode,nodeList,textStart,tokenList.size());
		}
		//ù ��ū : New Line
		else if(tokenList.get(0) instanceof T_newLine){
			N_newLine newLine = new N_newLine();
			nodeList.add(newLine);
		}
		//ù ��ū : Link
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
			//[~~] : http://~~~ �̷� ����
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
		//ù ��ū : em
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
			System.out.println("�ٸ� ��ū�� ���� ���� �ȵ�");
		}
		
	}
	
	
	
	
//==========================================================================//
//==========================================================================//
//							��庰 �޼ҵ�										//
//==========================================================================//
//==========================================================================//
	
	
	
//==========header: ��ū����Ʈ�� ��帮��Ʈ �޾Ƽ� �����ϱ�===================
	public void addTextNode(Node tempNode, LinkedList<Node> nodeList, int start, int end){
		List<Token> tokenList = tempNode.getTokenList();
		String text = new String();	
		
		//Text token�� ���� �ȿ��� �Ѱ��� String���� ��ħ
		for(int i=start;i<end;i++)
			text = text.concat(tokenList.get(i).getContent()+ " ");
		
		nodeList.add(new N_TextNode(text));
	}

	
	
	public boolean header(Node tempNode, LinkedList<Node> nodeList){
		//----------------------------------- Header Node -------------------------------------------
		List<Token> tokenList = tempNode.getTokenList();
		//��ū�� ù��° Symbol�� #�������� Ȯ��
		if( tokenList.get(0).getContent().equals("#") ||
			tokenList.get(0).getContent().equals("##") ||
			tokenList.get(0).getContent().equals("###") ||
			tokenList.get(0).getContent().equals("####") ||
			tokenList.get(0).getContent().equals("#####") ||
			tokenList.get(0).getContent().equals("######") )
		{
			//������ �����ؼ� ���� ����
			N_Header header = new N_Header();
		//1. ���� ����
			header.setLevel(tokenList.get(0).getContent());
		//2. �ؽ�Ʈ ����
			String text = "";
			int size = tokenList.size();
			for(int i=1;i<size;i++){
				//PlainText ��ū�� �������� Text�� ����
				if(tokenList.get(i) instanceof T_plainText){
					text = text.concat(tokenList.get(i).getContent()+ " ");
				}
			}
			header.setText(text);
			//��� ����Ʈ�� ���� ���� ��� ��� ����
			nodeList.add(header);
			return true;
		}
		//��ū�� '=' ������ �ְ� �ڿ��� �ƹ��͵� ���� ���
		else if(tokenList.get(0).getContent().charAt(0)=='=' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '='������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
				//��� ��� ����
				N_Header header = new N_Header();
				//1. ���� ����. (= ������ 1)
				header.setLevel(1);
				//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false; }
		}
		//��ū�� '-' ������ �ְ� �ڿ��� �ƹ��͵� ���� ���
		else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
			if(nodeList.getLast() instanceof N_TextNode){	// '-'������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
				//��� ��� ����
				N_Header header = new N_Header();
				//1. ���� ����. (= ������ 1)
				header.setLevel(2);
				//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
				header.setText(((N_TextNode)nodeList.getLast()).getContent());
				//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
				nodeList.removeLast();
				nodeList.add(header);
				return true;
			}else{System.out.println("md syntax error"); return false;}
		}
		else //code shouldn't reach here
			return false;
	}
	
	//em�Լ�
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
			//last nodelist�� �ٶ�����Ѵ�
			N_Hr hr = new N_Hr();
			nodeList.add(hr);
			hr.addNewParagraph();
			
			return true;
		}
		else{
			//System.out.println("hr�ƴ�");
			return false;
		}
	}
	
	public boolean blockquote(Node newNode, LinkedList<Node> nodeList){
		List<Token> tokenList = newNode.getTokenList();
		Node lastNode;
		//blockquote�� �ƴϸ� false����
		if(tokenList.get(0).getContent().equals(">") != true){	
			return false;
		}
		
		if(tokenList.get(0).getContent().equals(">") == true)// && tokenList.get(1).getContent().equals(">") == true){
		{
			
			//�̹� ��尡 nested BQ�� ���
			if(tokenList.size()>1 && tokenList.get(1).getContent().equals(">") == true)
			{
				//������ BQ���µ� ó������ nested BQ�� ���
				if(nodeList.size()<=0 || ( nodeList.size()>0 && !((lastNode=nodeList.getLast()) instanceof N_Blockquote)))
				{
					N_Blockquote bq = new N_Blockquote();
					addNodeToList(bq, nodeList);
					addNodeToList(newNode,nodeList);	//�Ʒ� ���� ����� ��
					return true;
				}
				//������ ��尡 BQ�̰� �� �ȿ��� nest �ϴ� ���
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
			//Nested BQ �ƴ� ���
			else
			{
				// ������ ó���� BQ�ų�   || BQ�� ���� �����ϴ� ���
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
				//������ ��尡 BQ���� ���
				else if(nodeList.size()>0 && ((lastNode=nodeList.getLast()) instanceof N_Blockquote))
				{
					//> �� �ִ� �� ĭ
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

		//Link Token ��ġ �˻�
		while(!(tokenList.get(startIndex) instanceof T_link)) startIndex++;
		System.out.println(">> link start: "+startIndex);
		//[~~~~][~~~] �̷��� 2���� ���޾� �ִ� ���
		if(((T_link)tokenList.get(startIndex+1)).getType().equals("text") ){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex+1)).getText());
			nodeList.add(linkNode);
			
			//���� TextToken�� �����ϴ� ��ġ ����
			return startIndex+2;
		}
		//[~~~~](~~~) �̷� ���
		else if(((T_link)tokenList.get(startIndex+1)).getType().equals("url") ){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			//[~~~](~~~) �̷� ���, linkText�� linkKey �� ����
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex)).getText());
			nodeList.add(linkNode);
			
			//Hashmap�� key, url, title�߰�
			String key = linkNode.getLinkKey();
			String url = ((T_link)tokenList.get(startIndex+1)).getUrl();
			String title = ((T_link)tokenList.get(startIndex+1)).getTitle();
			String[] val = new String[2];
			val[0] = url.toLowerCase(); val[1] = title;
			urlList.put(key,val);
			
			//���� TextToken�� �����ϴ� ��ġ ����
			return startIndex+2;
		}
		//[~~~][] �̷� ���
		else if(((T_link)tokenList.get(startIndex+1)).getType().equals("empty")){
			N_Link linkNode = new N_Link(urlList);
			linkNode.setLinkText(((T_link)tokenList.get(startIndex)).getText());
			linkNode.setLinkKey(((T_link)tokenList.get(startIndex)).getText().toLowerCase());
			nodeList.add(linkNode);
			//���� TextToken�� �����ϴ� ��ġ ����
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
