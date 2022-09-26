/**
 * Lil Lexi Document Model
 * 
 */
import java.util.List;
import java.util.ArrayList;

/**
 * LilLexiDoc
 */
public class LilLexiDoc 
{
	private LilLexiUI ui;
	private List<Glyph> glyphs;
	
	/**
	 * Ctor
	 */
	public LilLexiDoc() 
	{
		glyphs = new ArrayList<Glyph>();
	}
	
	/**
	 * setUI
	 */
	public void setUI(LilLexiUI ui) {this.ui = ui;}

	/**
	 * add a char
	 */
	public void add(char c) 
	{
		glyphs.add(new Glyph(c));
		ui.updateUI();
	}
	
	/**
	 * gets
	 */
	public List<Glyph> getGlyphs(){return glyphs;}
}




/**
 * Glyph
 */
class Glyph 
{
	private char c;
	
	public Glyph(char c) 
	{
		this.c = c;
	}

	public char getChar() {return c;}
	public void setChar(char c) {this.c = c;}
}





