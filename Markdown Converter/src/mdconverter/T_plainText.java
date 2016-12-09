package mdconverter;
/*
 * 일반 텍스트 토큰.
 */

public class T_plainText extends Token{
	public T_plainText(){}
	public T_plainText(String str){
		this.setContent(str);
	}
}
