
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
public class MDParser {
	LinkedList<Node> nodeList = new LinkedList<Node>();

//============================================================================
	//String �� ���� �޾ƿͼ� Symbol �� PlainText �и��ؼ� ��ū����Ʈ�� �����ϰ� �� ����Ʈ�� �����ϴ� �޼ҵ�
	public List<Token> tokenize(String str){
		//��ū����Ʈ ����
		List<Token> tokenList = new ArrayList<Token>();	
		
		//����: (PlainText) �̰ų� (Symbol) ���� ã�´�
		Pattern p = Pattern.compile("([a-zA-Z0-9]+)|(\\S+)");	//�Ƹ� �׷��� �߰��ؼ� �Ǵ��ؾ��ҵ� (/+\\S+) | (/+\\S)
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
		    		/*else if(matcher.group(i).contains("\\*")){ //backslash token���� ����	    		
			    		T_symbol token = new T_symbol();
			        	token.setContent(matcher.group(i));
				    	tokenList.add(token);
		    		}*/else {	//Symbol ��ū ���� �߰�		    		
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
		//��ū����Ʈ ����
		return tokenList;
	}
//============================================================================

	
	
	
//============================================================================
	//(temp)��� �ϳ��� �޾ƿͼ�, �� ��尡 � ������� �Ǻ��ϰ�(������� ����Ʈ����..)
	//������ ��带 �����ؼ� �װ� ��� ����Ʈ�� �߰��ϴ� �Լ�
	public void addNodeToList(Node newNode){
		/*if(newNode.getTokenList().size()==0)
			System.out.print("error!!\n");
		else
			System.out.print("not null\n");*/
		//��� �ȿ� ����ִ� ��ū����Ʈ ��������
		List<Token> tokenList = newNode.getTokenList();
		
		
			
		//��ū�� ù��° ��Ұ� Symbol�� ���� PlainText�� ��찡 �ִ�.
		//ù��° ��Ұ� Symbol�� ���
		
		if((newNode.getTokenList().size()!=0) && (tokenList.get(0) instanceof T_symbol)){
			//------------------ Header Node ------------------
			
			//��ū�� ù��° Symbol�� #�������� Ȯ��
			if( tokenList.get(0).getContent().equals("#") ||
				tokenList.get(0).getContent().equals("##") ||
				tokenList.get(0).getContent().equals("###") ||
				tokenList.get(0).getContent().equals("####") ||
				tokenList.get(0).getContent().equals("#####") ||
				tokenList.get(0).getContent().equals("######") )
			{
				//������ �����ؼ� ���� ����
				Header header = new Header();
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
				if(nodeList.getLast() instanceof TextNode){	// '='������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
					//��� ��� ����
					Header header = new Header();
					//1. ���� ����. (= ������ 1)
					header.setLevel(1);
					//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
					header.setText(((TextNode)nodeList.getLast()).getContent());
					//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
					nodeList.removeLast();
					nodeList.add(header);
				}else{System.out.println("md syntax error"); }
			}
			//��ū�� '-' ������ �ְ� �ڿ��� �ƹ��͵� ���� ���
			else if(tokenList.get(0).getContent().charAt(0)=='-' && tokenList.size()==1){
				if(nodeList.getLast() instanceof TextNode){	// '-'������ ���� ��尡 �ؽ�Ʈ������� Ȯ��
					//��� ��� ����
					Header header = new Header();
					//1. ���� ����. (= ������ 1)
					header.setLevel(2);
					//2. �ؽ�Ʈ ���� (���� ��忡�� �ؽ�Ʈ�� �����ͼ� �װ� ���)
					header.setText(((TextNode)nodeList.getLast()).getContent());
					//���� �������(�ؽ�Ʈ�� �ִ�)�� �ʿ�����ϱ� ����� ��帮��Ʈ�� ������ ����
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
					tokenList.get(0).getContent().endsWith("\\!")){//Backslash Esape ���� check
				System.out.println("��ū����Ʈ�� ������" + tokenList.get(0).getContent());
			}
		}
		//��ū�� ù��° ��ȣ�� Plain Text�� ���
		else if(newNode.getTokenList().size()!=0 && tokenList.get(0) instanceof T_plainText)
		{
			//TextNode ����
			TextNode textnode = new TextNode();
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
