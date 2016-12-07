/*
 * Link token�� 3������ ����
 * 1. [~~~~] 		Text�� ���� ����	-> text
 * 2. (http:~~~) 	url�� ���� ����	-> url
 * 3. [] 			����ִ� ����		-> empty
 * 
 * 
 */


public class T_link extends Token{
	private String linkText;	// [linkText]
	private String linkUrl;	// (linkUrl)
	private String linkTitle="";	// "linkTitle"
	private String type;		// text, url, empty
	
	public T_link(){}
	public T_link(String str){
		if(str.charAt(0)=='['){
			if(str.charAt(1)==']')
				this.type = "empty";
			else{
				this.type = "text";
				this.linkText = str.substring(1,str.length()-1);
			}
		}
		else{
			str = str.replaceAll("[()]", "");
			String [] temp = str.split("\"");
			this.linkUrl = temp[0];
			if(temp.length>=2){
				this.linkTitle = temp[1];
			}
			this.type = "url";
		}
	}
	
	//getter
	public String getType(){return this.type;}
	public String getText(){return this.linkText;}
	public String getUrl(){return this.linkUrl;}
	public String getTitle(){return this.linkTitle;}
	
	
	
	
}
