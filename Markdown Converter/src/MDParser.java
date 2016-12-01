
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
public class MDParser {
	//�̰� private?? public??
	LinkedList<Node> nodeList = new LinkedList<Node>();

//============================================================================
	//String �� ���� �޾ƿͼ� Symbol �� PlainText �и��ؼ� ��ū����Ʈ�� �����ϰ� �� ����Ʈ�� �����ϴ� �޼ҵ�
	public List<Token> tokenize(String str){
		//��ū����Ʈ ����
		List<Token> tokenList = new ArrayList<Token>();
		
		if(str.length()==0){
			T_newLine token = new T_newLine();
			token.setContent("\n");
			tokenList.add(token);
			return tokenList;
		}
		
		
		
		//����: (PlainText) �̰ų� (Symbol) ���� ã�´�
		Pattern p = Pattern.compile("([a-zA-Z0-9]+)|(\\S+)");
		Matcher matcher = p.matcher(str);
		
		while(matcher.find()){
			for (int i = 1; i <= matcher.groupCount(); i++) {
				//Group1: �� ��ū�� plainText��� PlainText��ū ��带 �����ؼ� setContent�ϰ�(�ؽ�Ʈ�߰�) ��ū����Ʈ�� �߰�
		    	if(i==1 && matcher.group(i) != null){
			        T_plainText token = new T_plainText();
			        token.setContent(matcher.group(i));
			    	tokenList.add(token);
		        }
				//Group2: �� ��ū�� symbol�̶�� MDSymbol��ū ��带 �����ؼ� setContent�ϰ�(�ؽ�Ʈ�߰�) ��ū����Ʈ�� �߰�
		    	else if(i==2 && matcher.group(i) != null ){
		    		//Symbol �� < �� �����ϴ°� HTML�� ó���ؾߵǴµ� �ϴ� �����ϰ� ���⼱ plainTextó�� ó��...
		    		if(matcher.group(i).charAt(0) == '<'){
		    			T_plainText token = new T_plainText();
				        token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		    		else{	//Symbol ��ū ���� �߰�		    		
			    		T_symbol token = new T_symbol();
			        	token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}
		        }
		    	//Group3
		    }
		}
		//��ū����Ʈ ����
		return tokenList;
	}
//============================================================================

	
	
	
//============================================================================
	//(temp)��� �ϳ��� �޾ƿͼ�, �� ��尡 � ������� �Ǻ��ϰ�(������� ����Ʈ����..)
	//������ ��带 �����ؼ� �װ� ��� ����Ʈ�� �߰��ϴ� �Լ�
	public void addNodeToList(Node newNode){
		//��� �ȿ� ����ִ� ��ū����Ʈ ��������
		List<Token> tokenList = newNode.getTokenList();
		
		//��ū�� ù��° ��Ұ� Symbol�� ���� PlainText�� ��찡 �ִ�.
		//ù��° ��Ұ� Symbol�� ���
		if(tokenList.get(0) instanceof T_symbol){
//----------------------------------- Header Node -------------------------------------------
			
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
				}else{System.out.println("md syntax error"); }
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
				}else{System.out.println("md syntax error"); }
			}
			
			
//----------------------------------- Blockquote Node -----------------------------------------------
			else if(tokenList.get(0).getContent().equals(">") == true){		
				int bqLevel = 0;
				for(int i=0;i<tokenList.size();i++){
					if(tokenList.get(i).getContent().equals(">") == true){
						bqLevel++;
					}
				}
				System.out.println(bqLevel);
				if(nodeList.size()>0 && nodeList.getLast() instanceof N_Blockquotes){
					if(tokenList.size()==1){
						((N_Blockquotes)nodeList.getLast()).addNewParagraph();
					}
					else{
						String text = new String();
						for(int i=1;i<tokenList.size();i++){
							text = text.concat(tokenList.get(i).getContent()+ " ");
						}
						((N_Blockquotes)nodeList.getLast()).addToTextList(text);
					}
				}else{
					N_Blockquotes bq = new N_Blockquotes();
					String text = new String();
					for(int i=1;i<tokenList.size();i++){
						text = text.concat(tokenList.get(i).getContent()+ " ");
					}
					bq.addToTextList(text);
					nodeList.add(bq);
				}
			}
					
//---------------------------------------------------------------------------------------------------

		}
//---------------------------------------------------------------------------------------------------

//----------------------------------- Plain Text Node -----------------------------------------------
		//��ū�� ù��° ��ȣ�� Plain Text�� ���
		else if(tokenList.get(0) instanceof T_plainText)
		{
			if(nodeList.size() > 0 && nodeList.getLast() instanceof N_Blockquotes){
				String text = new String();
				for(int i=1;i<tokenList.size();i++){
					text = text.concat(tokenList.get(i).getContent()+ " ");
				}
				((N_Blockquotes)nodeList.getLast()).addToTextList(text);
			}
			else{
				//TextNode ����
				N_TextNode textnode = new N_TextNode();
				String text = "";
				//�ܾ������ ����� ��ū���� �Ѱ��� ���ڿ��� ��ħ
				int size = tokenList.size();
				for(int i=0;i<size;i++){
					text = text.concat(tokenList.get(i).getContent()+ " ");
				}
				//textnode�� �ϳ��� ��ģ ���ڿ��� ����
				textnode.setContent(text);
				nodeList.add(textnode);
			}
		}
//---------------------------------------------------------------------------------------------------

		
//-------------------- Empty Line -----------------------------------------------
		//T_newLine ��ū�� ���
		else if(tokenList.get(0) instanceof T_newLine){
			N_newLine newLine = new N_newLine();
			nodeList.add(newLine);
		}
				
				
//---------------------------------------------------------------------------------------------------
		
		

		else{
			System.out.println("�ٸ� ���� ���� ���� �ȵ�");
		}
		
	}
//============================================================================

	public void printAllNode(){
		for(int i=0;i<nodeList.size();i++)
			nodeList.get(i).printNodeInfo();
	}
	
	
	

}
