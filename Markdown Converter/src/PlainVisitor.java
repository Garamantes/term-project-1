import java.util.HashMap;
import java.util.LinkedList;

public class PlainVisitor implements MDElementVisitor{
	public String startHtml(){
		String str="<!DOCTYPE html>\n<html>\n<head>\n<meta content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n";
		return str;
	}
	
	
	public String endHtml(){
		String str="\n</body>\n</html>";
		return str;
	}
	
	@Override
	public String visit(N_Header header) {
		int level = header.getLevel();
		String text = header.getText();
		String str = "<h"+level+">"+text+"</h"+level+">";
		return str+"\n";
	}

	@Override
	public String visit(N_TextNode textnode) {
		String str = textnode.getContent();
		return str+"\n";
	}
	
	public String visit(N_newLine newLine){
		return "<br>\n";
	}



	public String visit(N_Blockquote blockquote){
		LinkedList<Node> list = blockquote.getList();
		String str = new String();
		str = str.concat("<blockquote>\n");
	
		for(int i=0;i<list.size();i++){
	
			if(list.get(i) instanceof N_TextNode){
				str = str.concat(visit((N_TextNode)list.get(i)));
			}else if(list.get(i) instanceof N_Header){
				str = str.concat(visit((N_Header)list.get(i)));
			}else if(list.get(i) instanceof N_newLine){
				str = str.concat(visit((N_newLine)list.get(i)));
			}else if(list.get(i) instanceof N_Blockquote){
				str = str.concat(visit((N_Blockquote)list.get(i)));
			}else{}

		}
		
		str = str.concat("</blockquote>");
		return str;
	}

	@Override
	public String visit(N_Link link) {
		String str = new String();
		if(link.getImage() && link.urlList.get(link.getLinkKey()) != null){
			str = "<img src=\""
					+link.urlList.get(link.getLinkKey())[0]
					+"\" title = \""
					+link.urlList.get(link.getLinkKey())[1]
					+"\" alt = \""
					+link.getLinkText()
					+ "\"/>\n";
		}
		else if(link.urlList.get(link.getLinkKey()) != null){
			str = "<a href=\""
					+link.urlList.get(link.getLinkKey())[0]
					+"\" title = \""
					+link.urlList.get(link.getLinkKey())[1]
					+ "\">"
					+link.getLinkText()
					+"</a>"+"\n";
		}
		else{
			str = "["+link.getLinkText()+"] ["+link.getLinkKey()+"] ";
		}
		
		
		return str;
	}

	@Override
	public String visit(N_List list) {
		
		String str = new String();
		if(list.getListType().equals("ul")){
			str = str.concat("<ul>");
		}
		else
			str = str.concat("<ol>");
		
		for(int i=0;i<list.getList().size();i++){
			str = str.concat("<li>");
			if(list.getNewLine()==true) str = str.concat("<p>");
			
			if(list.getList().get(i) instanceof N_TextNode){
				str = str.concat(visit((N_TextNode)list.getList().get(i)));
			}
			else if(list.getList().get(i) instanceof N_Blockquote){
				str = str.concat(visit((N_Blockquote)list.getList().get(i)));
			}
			else if(list.getList().get(i) instanceof N_List){
				str = str.concat(visit((N_List)list.getList().get(i)));
			}
			if(list.getNewLine()==true) str = str.concat("</p>");
			str = str.concat("</li>");
		}
		
		if(list.getListType().equals("ul")){
			str = str.concat("</ul>");
		}
		else
			str = str.concat("</ol>");
		
		
		return str+"\n";
		
	}
	
	
public String visit(N_emphasis em){
		
		String text = em.getText();
		String str = "<em>"+text+"</em> ";
		return str;
	}

@Override
public String visit(N_Hr hr){
	
	return "<hr>";
}
	

}
