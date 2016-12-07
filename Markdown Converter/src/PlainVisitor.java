import java.util.HashMap;
import java.util.LinkedList;



public class PlainVisitor implements MDElementVisitor{
	public String startHtml(){
		String str="<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n";
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
	public String visit(N_Hr hr){
		
		return "<hr>";
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
		
		if(link.urlList.get(link.getLinkKey()) != null){
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
	

}
