
public class PlainVisitor implements MDElementVisitor{

	public String startHtml(){
		String str="<!DOCTYPE html>\n<html>\n<body>\n";
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
		return str;
	}

	@Override
	public String visit(N_TextNode textnode) {
		String str = textnode.getContent();
		return str;
	}

}
