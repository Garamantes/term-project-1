
public class PlainVisitor implements MDElementVisitor{

	public void startHtml(){
		System.out.println("<!DOCTYPE html>\n<html>\n<body>\n");
	}
	
	public void endHtml(){
		System.out.println("\n</body>\n</html>");
	}
	
	@Override
	public void visit(Header header) {
		int level = header.getLevel();
		String text = header.getText();
		System.out.println("<h"+level+">"+text+"</h"+level+">");
	}

	@Override
	public void visit(TextNode textnode) {
		System.out.println(textnode.getContent());
	}

}
