import java.util.HashMap;

public class N_Link extends Node{
	private String linkText;
	private String linkKey; //hashmap key
	private boolean isImage = false;
	public HashMap<String, String[]> urlList;
	
	//public N_Link(){}
	public N_Link(HashMap<String,String[]> urlList){
		this.urlList = urlList;
		}
	
	//setter
	public void setLinkText(String str){this.linkText = str;}
	public void setLinkKey(String str){this.linkKey = str;}
	public void setImage(boolean val){this.isImage = val;}
	//getter
	public String getLinkText(){return this.linkText;}
	public String getLinkKey(){return this.linkKey;}
	public String getLinkUrl(){return this.urlList.get(linkKey)[0];}
	public String getLinkTitle(){return this.urlList.get(linkKey)[1];}
	public boolean getImage(){return this.isImage;}
	
	public void printNodeInfo(){
		System.out.println("[Link Node]");
		if(urlList.get(linkKey)!=null)
			System.out.println("["+linkText+"] ("+urlList.get(linkKey)[0]+") Title: "+urlList.get(linkKey)[1]);
		else
			System.out.println("url for"+"["+linkText+"] is not provided");
	}
	
	public String accept(MDElementVisitor visitor){
		return visitor.visit(this);
	}
}
