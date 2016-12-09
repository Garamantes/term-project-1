package mdconverter;


public interface MDElement {
	public String accept(MDElementVisitor visitor);
}
