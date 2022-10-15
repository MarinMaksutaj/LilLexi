/*
*  Course: CSC335
*  Description: Controller class for the LilLexi application.
* 				This is where the GUI sends commands to the document.
*/
import org.eclipse.swt.graphics.RGB;

public class LilLexiControl 
{
	private LilLexiDoc currentDoc;

	/**
	 * LilLexiControl constructor.
	 * Author:  Shyambhavi
	 */
	public LilLexiControl( LilLexiDoc doc )
	{
		this.currentDoc = doc;
	}
	
	/**
	 * Method to add a char glyph to the document. Takes the char,
	 *  the RGB color, font name and font size.
	 * Author:  Shyambhavi
	 */
	void addCharGlyph( char c, RGB color, String fontName, int fontSize)
	{
		currentDoc.addCharGlyph( c, color, fontName, fontSize );
		currentDoc.draw();
	}
	/*
	 * Method to add a rect glyph to the document. Takes the RGB color,
	 * the fill color and size.
	 * Author:  Marin Maksutaj
	 */
	void addRectGlyph(RGB borderColor, RGB fillColor, Integer size) 
	{	
		currentDoc.addRectGlyph(borderColor, fillColor, size);
		currentDoc.draw();
	}
	
	/*
	 * Method to add the triangle glyph to the document. Takes the border color,
	 * the fill color and size.
	 * Author:  Marin Maksutaj
	 */
	void addTriangleGlyph(RGB borderColor, RGB fillColor, Integer size) 
    {   
        currentDoc.addTriangleGlyph(borderColor, fillColor, size);
        currentDoc.draw();
    }
	
	/*
	 * Method to add the cirlce glyph to the document.
	 * Author:  Shyambhavi
	 */
	void addCircleGlyph(RGB borderColor, RGB fillColor, Integer size) 
    {   
        currentDoc.addCircleGlyph(borderColor, fillColor, size);
        currentDoc.draw();
    }

	/*
	 * Method to add the image glyph to the document.
	 * Author:  Marin Maksutaj
	 */
	void addImageGlyph( String fileName , Integer size) 
	{	
		currentDoc.addImageGlyph(fileName, size);
		currentDoc.draw();
	}

	/*
	 * Method to backspace the last glyph in the document.
	 * Author:  Shyambhavi
	 */
	void backspace() 
	{	
		currentDoc.backspace();
		currentDoc.draw();
	}

	/*
	 * Method to line break the document.
	 * Author:  Marin Maksutaj
	 */
	void lineBreak() 
	{	
		currentDoc.lineBreak();
		currentDoc.draw();
	}

	/*
	 * Method to undo the last action in the document.
	 * Author:  Shyambhavi
	 */
	void undo() 
	{	
		currentDoc.undo();
	}

	/*
	 * Method to redo the last undo in the document.
	 * Author:  Marin Maksutaj
	 */
	void redo() 
	{	
		currentDoc.redo();
	}

	/*
	 * Method to quit the application.
	 * Author:  Shyambhavi
	 */
	void  quitEditor() 
	{ 
		System.exit(0); 
	}	
}






