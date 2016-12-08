
public interface MDElementVisitor {
	public String visit(N_Header header);
	public String visit(N_TextNode textnode);
	public String visit(N_Blockquote blockquote);
	public String visit(N_newLine newLine);
	public String visit(N_Link link);
	public String visit(N_List list);
}
