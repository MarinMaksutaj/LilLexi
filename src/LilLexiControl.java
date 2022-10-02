

/**
 * Controller
 */
import org.eclipse.swt.graphics.RGB;

public class LilLexiControl 
{
	private LilLexiDoc currentDoc;

	/**
	 * LilLexiControl
	 */
	public LilLexiControl( LilLexiDoc doc )
	{
		this.currentDoc = doc;
	}
	
	/**
	 * selectCard  user selects a card
	 */
	void addCharGlyph( char c, RGB color, String fontName, int fontSize)
	{
		currentDoc.addCharGlyph( c, color, fontName, fontSize );
		currentDoc.draw();
	}
	void addRectGlyph(RGB borderColor, RGB fillColor) 
	{	//System.out.println("adding something");
		currentDoc.addRectGlyph(borderColor, fillColor);
		currentDoc.draw();
	}

	void addRectGlyph(Point endPoint, RGB borderColor, RGB fillColor)
	{	//System.out.println("adding rectangle glyph");
		currentDoc.addRectGlyph(endPoint, borderColor, fillColor);
		currentDoc.draw();
	}

	void addImageGlyph( String fileName ) 
	{	//System.out.println("adding image glyph");
		currentDoc.addImageGlyph(fileName);
		currentDoc.draw();
	}

	void backspace() 
	{	//System.out.println("backspacing");
		currentDoc.backspace();
		currentDoc.draw();
	}

	void lineBreak() 
	{	//System.out.println("line breaking");
		currentDoc.lineBreak();
		currentDoc.draw();
	}

	/**
	 * quitEditor  user quits
	 */
	void  quitEditor() 
	{ 
		System.exit(0); 
	}	
}






