

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
	void addRectGlyph(RGB borderColor, RGB fillColor, Integer size) 
	{	System.out.println("adding rectangle");
		currentDoc.addRectGlyph(borderColor, fillColor, size);
		currentDoc.draw();
	}
	
	void addTriangleGlyph(RGB borderColor, RGB fillColor, Integer size) 
    {   System.out.println("adding tri1");
        currentDoc.addTriangleGlyph(borderColor, fillColor, size);
        currentDoc.draw();
    }
	
	void addCircleGlyph(RGB borderColor, RGB fillColor, Integer size) 
    {   //System.out.println("adding something");
        currentDoc.addCircleGlyph(borderColor, fillColor, size);
        currentDoc.draw();
    }


	void addImageGlyph( String fileName , Integer size) 
	{	//System.out.println("adding image glyph");
		currentDoc.addImageGlyph(fileName, size);
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

	void undo() 
	{	
		currentDoc.undo();
	}

	void redo() 
	{	
		currentDoc.redo();
	}

	/**
	 * quitEditor  user quits
	 */
	void  quitEditor() 
	{ 
		System.exit(0); 
	}	
}






