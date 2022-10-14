
/*
*  Name: Marin Maksutaj & Shyambhavi 
*  Course: CSC352
*  Description: This file contains the main classes and methods used to implement the Lil Lexi Document Editor.
*/

import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import org.eclipse.swt.graphics.*;


/**
 * LilLexiDoc is the class used to represent the document. It contains
 * the Composit pattern and the Glyphs that are added to the document.
 */
public class LilLexiDoc 
{
	private LilLexiUI ui;
	private Composition composition;
	private Compositor compositor;

	/**
	 * Constructor for LilLexiDoc
	 */
	public LilLexiDoc() 
	{
		composition = new Composition();
		compositor = new SimpleCompositor();
	
	}
	
	/**
	 * setter for the UI
	 */
	public void setUI(LilLexiUI ui) {this.ui = ui;}

	/**
	 * Method to add a char glyph to the document
	 */
	public void addCharGlyph(char c, RGB color, String fontName, int fontSize)
	{
		CharGlyph cg = new CharGlyph(c);
		cg.setColor(color);
		cg.setFont(fontName);
		cg.setSize(fontSize);
		composition.add(cg);
	}

	/**
	 * Method to add a rectangle glyph to the document. 
	 * It takes in the border and fill colors.
	 */
	public void addRectGlyph(RGB borderColor, RGB fillColor)
	{
		RectGlyph rg = new RectGlyph();
		rg.setFillColor(fillColor);
		rg.setBorderColor(borderColor);
		composition.add(rg);
	}

	/**
	 * Method to add a rectangle glyph to the document.
	 * It takes in the border and fill colors and the end point.
	 */
	public void addRectGlyph(Point endPoint, RGB borderColor, RGB fillColor) {
		RectGlyph rg = new RectGlyph();
		rg.setFillColor(fillColor);
		rg.setBorderColor(borderColor);
		rg.setEndPoint(endPoint);
		composition.add(rg);
	}
	
	/**
     * Method to add a triangle glyph to the document.
	 * It takes in the border and fill colors.
     */
    public void addTriangleGlyph(RGB borderColor, RGB fillColor)
    {
        TriangleGlyph tg = new TriangleGlyph();
        tg.setFillColor(fillColor);
        tg.setBorderColor(borderColor);
        composition.add(tg);
    }
    
	/**
	 * Method to add a triangle glyph to the document.
	 * It takes in the border and fill colors and the end point.	
	 */
    public void addTriangleGlyph(Point endPoint, RGB borderColor, RGB fillColor)
    {
        TriangleGlyph tg = new TriangleGlyph();
        tg.setFillColor(fillColor);
        tg.setBorderColor(borderColor);
        tg.setEndPoint(endPoint);
        composition.add(tg);
    }
	
	/**
     * Method to add a circle glyph to the document.
	 * It takes in the border and fill colors.
     */
    public void addCircleGlyph(RGB borderColor, RGB fillColor)
    {
        CircleGlyph cg = new CircleGlyph();
        cg.setFillColor(fillColor);
        cg.setBorderColor(borderColor);
        composition.add(cg);
    }

	/**
	 * Method to add a circle glyph to the document.
	 * It takes in the border and fill colors and the end point.
	 */
    public void addCircleGlyph(Point endPoint, RGB borderColor, RGB fillColor) {
        CircleGlyph cg = new CircleGlyph();
        cg.setFillColor(fillColor);
        cg.setBorderColor(borderColor);
        cg.setEndPoint(endPoint);
        composition.add(cg);
        
    }

	/**
	 * Method to add an image glyph to the document.
	 * It takes in the image path.
	 */
	public void addImageGlyph(String fileName) 
	{
		ImageGlyph ig = new ImageGlyph(fileName, 100, 100);
		composition.add(ig);
	} 

	/**
	 * Method to add an image glyph to the document.
	 * It takes in the image path and the end point.
	 */
	public void addImageGlyph(Point endPoint, String fileName) {
		ImageGlyph ig = new ImageGlyph(fileName, 100, 100);
		ig.setEndPoint(endPoint);
		composition.add(ig);
	}

	/**
	 * getter for the composition
	 */
	public Composition getComposition(){return composition;}

	/**
	 * getter for the compositor
	 */
	public Compositor getCompositor(){return compositor;}

	/**
	 * Method to set the composition
	 */
	public void setComposition(Composition composition){this.composition = composition;}

	/**
	 * Method to set the compositor
	 */
	public void setCompositor(Compositor compositor){this.compositor = compositor;}

	/**
	 * Method to backspace a glyph
	 */
	public void backspace()
	{
			compositor.backspace(composition);
	}

	/**
	 * Method to line break a glyph
	 */
	public void lineBreak()
	{
		compositor.lineBreak(composition);
	}

	/**
	 * toString method for the document
	 */
	public String toString()
	{
		String s = "LilLexiDoc: ";
		for (Glyph g: composition.getGlyphs())
		{
			s += g.toString();
		}
		return s;
	}

	/**
	 * draw method for the document. Calls the compose method of the compositor.
	 */
	public void draw()
	{
		compositor.addUI(ui);
		compositor.compose(composition);
		
	}

	/**
	 * Method to undo the last action
	 */
	public void undo() {
		compositor.undo(composition);
	}

	/**
	 * Method to redo the last action
	 */
	public void redo() {
		compositor.redo(composition);
	}
}




/**
 * Glyph class for the LilLexi project. It is an abstract class.
 * It is used by the other glyph classes to implement the methods.
 */
abstract class Glyph
{
	private Point pos;

	/**
	 * Constructor for the glyph class
	 */
	public Glyph() 
	{
		pos = new Point(0,0);
	}

	/**
	 * Constructor for the glyph class. It takes in the position of the glyph.
	 */
	public Glyph(Point pos) 
	{
		this.pos = pos;
	}

	/**
	 * getter for the position of the glyph
	 */
	public Point getPos(){return pos;}
}

/**
 * Compositor class for the LilLexi project. It is an interface.
 * It is used by the other compositor classes to implement the methods.
 */
interface Compositor
{
	public void addUI (LilLexiUI ui);
	public void compose(Composition c);
	public void backspace(Composition c);
	public void lineBreak(Composition c);
	public void undo(Composition c);
	public void redo (Composition c);
}

/**
 * Simple Compositor 
 * It is used to just draw the glyphs in the order they were added to the composition.
 */
class SimpleCompositor implements Compositor
{

	private LilLexiUI ui;
	private Point cursor;
	private List<Point> previousPositions;
	private int yOffSet;
	private List<Integer> previousYOffSets;
	
	/**
	 * Constructor for the Simple Compositor
	 */
	public SimpleCompositor () {
		this.cursor = new Point(0, 0);
		this.previousPositions = new ArrayList<Point>();
		this.yOffSet = 30;
		this.previousYOffSets = new ArrayList<Integer>();
	}

	/**
	 * Method to undo the last action
	 */
	public void undo (Composition c) {
		if (!TimeMachine.getInstance().canUndo()) {
			return;
		}
		TimeMachine.getInstance().undo();
		// perform a deep copy of the current time machine state, and set it as the composition
		Point newCursor = new Point(TimeMachine.getInstance().getCursor().getX(), 
		TimeMachine.getInstance().getCursor().getY());
		int newYOffSet = Integer.valueOf(TimeMachine.getInstance().getYOffSet());
		List<Glyph> newGlyphs = new ArrayList<Glyph>();
		for (Glyph g : TimeMachine.getInstance().getGlyphs()) {
			newGlyphs.add(g);
		}
		List<Integer> newPreviousYOffSets = new ArrayList<Integer>();
		for (Integer i : TimeMachine.getInstance().getPreviousYOffSets()) {
			newPreviousYOffSets.add(Integer.valueOf(i));
		}
		List<Point> newPreviousPositions = new ArrayList<Point>();
		for (Point p : TimeMachine.getInstance().getPreviousPositions()) {
			newPreviousPositions.add(new Point(p.getX(), p.getY()));
		}
		// set the composition to the new state
		this.cursor = newCursor;
		this.yOffSet = newYOffSet;
		c.setGlyphs(newGlyphs);
		this.previousYOffSets = newPreviousYOffSets;
		this.previousPositions = newPreviousPositions;
	}

	/**
	 * Method to redo the last action
	 */
	public void redo (Composition c) {
		if (!TimeMachine.getInstance().canRedo()) {
			return;
		}
		TimeMachine.getInstance().redo();
		// perform a deep copy of the current time machine state
		Point newCursor = new Point(TimeMachine.getInstance().getCursor().getX(), 
		TimeMachine.getInstance().getCursor().getY());
		int newYOffSet = Integer.valueOf(TimeMachine.getInstance().getYOffSet());
		List<Glyph> newGlyphs = new ArrayList<Glyph>();
		for (Glyph g : TimeMachine.getInstance().getGlyphs()) {
			newGlyphs.add(g);
		}
		List<Integer> newPreviousYOffSets = new ArrayList<Integer>();
		for (Integer i : TimeMachine.getInstance().getPreviousYOffSets()) {
			newPreviousYOffSets.add(Integer.valueOf(i));
		}
		List<Point> newPreviousPositions = new ArrayList<Point>();
		for (Point p : TimeMachine.getInstance().getPreviousPositions()) {
			newPreviousPositions.add(new Point(p.getX(), p.getY()));
		}
		// set the composition to the new state
		this.cursor = newCursor;
		this.yOffSet = newYOffSet;
		c.setGlyphs(newGlyphs);
		this.previousYOffSets = newPreviousYOffSets;
		this.previousPositions = newPreviousPositions;
	}

	/**
	 * Method to add the UI to the compositor
	 */
	public void addUI (LilLexiUI ui) {this.ui = ui;}

	/**
	 * Method to backspace the last glyph
	 */
	public void backspace (Composition c) {
		
		spellCheck(c); 
		if (cursor.getX() == 0 && cursor.getY() == 0) {
			return;
		}
		// get last element
		c.getGlyphs().remove(c.getGlyphs().size()-1);
		// get last position
		Point lastPosition = previousPositions.get(previousPositions.size()-1);
		// set cursor to last position
		cursor = new Point(lastPosition.getX(), lastPosition.getY());
		// remove last position
		previousPositions.remove(previousPositions.size()-1);
		// get last y offset
		int lastYOffSet = previousYOffSets.get(previousYOffSets.size()-1);
		// set y offset to last y offset
		yOffSet = lastYOffSet;
		// remove last y offset
		previousYOffSets.remove(previousYOffSets.size()-1);
	
	}

	/**
	 * Method to add a line break
	 */
	public void lineBreak (Composition c) {
		
		spellCheck(c);
		// add a new previous position
		previousPositions.add(new Point(cursor.getX(), cursor.getY()));
		// set cursor to next line
		previousYOffSets.add(yOffSet);
		CharGlyph emptySpace = new CharGlyph('\0');
		emptySpace.setColor(new RGB(0, 0, 0));
		emptySpace.setFont("Courier");
		emptySpace.setSize(24);
		emptySpace.setPos(cursor);
		c.getGlyphs().add(emptySpace);
		cursor = new Point(0, cursor.getY() + yOffSet);
		this.yOffSet = 30;
		
	}

	/**
	 * Method to compose the composition
	 */
	public void compose(Composition c)
	{
		
	    spellCheck(c);
		List<Glyph> glyphs = c.getGlyphs();
		for (Glyph g: glyphs)
		{
			if (g.getPos() != null)
			{
				continue;
			}
			previousPositions.add(new Point(cursor.getX(), cursor.getY()));
			previousYOffSets.add(yOffSet);

			if (g instanceof CharGlyph)
			{
				CharGlyph cg = (CharGlyph)g;
				if (yOffSet < cg.getSize() + 5) 
				{
					yOffSet = cg.getSize() + 5;
				}
				
				cg.setPos(new Point(cursor.getX(), cursor.getY()));
				if (cursor.getX() + cg.getSize() > ui.getWidth() - cg.getSize())
				{
					cursor.setX(0);
					cursor.setY(cursor.getY() + yOffSet);
					yOffSet = cg.getSize() + 5;
				}
				else
				{
					cursor.setX(cursor.getX() + cg.getSize() - cg.getSize()/4);
				}
				
			}
			else if (g instanceof RectGlyph)
			{
				RectGlyph rg = (RectGlyph)g;
				rg.setPos(new Point(cursor.getX(), cursor.getY()));
				// if rectangle has an endPoint, change it's width and height accordingly
				if (rg.getEndPoint() != null)
				{
					rg.setWidth(rg.getEndPoint().getX() - rg.getPos().getX());
					rg.setHeight(rg.getEndPoint().getY() - rg.getPos().getY());
				}
				if (yOffSet < rg.getHeight() + 10)
				{
					yOffSet = rg.getHeight() + 10;
				}
				if (cursor.getX() + rg.getWidth() > ui.getWidth())
				{
					cursor.setX(0);
					cursor.setY(cursor.getY() + yOffSet);
					yOffSet = 30;
				}
				else
				{
					cursor.setX(cursor.getX() + rg.getWidth() + 2);
				}
			}
			
			else if (g instanceof TriangleGlyph)
            {
			    TriangleGlyph tg = (TriangleGlyph)g;
			    tg.setPos(new Point(cursor.getX(), cursor.getY()));
                // if rectangle has an endPoint, change it's width and height accordingly
                if (tg.getEndPoint() != null)
                {
                    tg.setWidth(tg.getEndPoint().getX() - tg.getPos().getX());
                    tg.setHeight(tg.getEndPoint().getY() - tg.getPos().getY());
                }
                if (yOffSet < tg.getHeight() + 10)
                {
                    yOffSet = tg.getHeight() + 10;
                }
                if (cursor.getX() + tg.getWidth() > ui.getWidth())
                {
                    cursor.setX(0);
                    cursor.setY(cursor.getY() + yOffSet);
                    yOffSet = 30;
                }
                else
                {
                    cursor.setX(cursor.getX() + tg.getWidth() + 2);
                }
            }
			
			else if (g instanceof CircleGlyph)
            {
			    CircleGlyph cg = (CircleGlyph)g;
			    cg.setPos(new Point(cursor.getX(), cursor.getY()));
                // if rectangle has an endPoint, change it's width and height accordingly
                if (cg.getEndPoint() != null)
                {
                    cg.setRadius(cg.getEndPoint().getX() - cg.getPos().getX());
                }
                if (yOffSet < cg.getRadius() + 10)
                {
                    yOffSet = cg.getRadius() + 10;
                }
                if (cursor.getX() + cg.getRadius() > ui.getWidth())
                {
                    cursor.setX(0);
                    cursor.setY(cursor.getY() + yOffSet);
                    yOffSet = 30;
                }
                else
                {
                    cursor.setX(cursor.getX() + cg.getRadius() + 2);
                }
            }
			
			else if (g instanceof ImageGlyph)
			{
				ImageGlyph ig = (ImageGlyph)g;
				ig.setPos(new Point(cursor.getX(), cursor.getY()));
				if (ig.getEndPoint() != null)
				{
					ig.setWidth(ig.getEndPoint().getX() - ig.getPos().getX());
					ig.setHeight(ig.getEndPoint().getY() - ig.getPos().getY());
				}
				if (yOffSet < ig.getHeight() + 10)
				{
					yOffSet = ig.getHeight() + 10;
				}
				if (cursor.getX() + ig.getWidth() > ui.getWidth())
				{
					cursor.setX(0);
					cursor.setY(cursor.getY() + yOffSet);
					yOffSet = 30;
				}
				else
				{
					cursor.setX(cursor.getX() + ig.getWidth() + 2);
				}
			}
		}
		// add previous state in TimeMachine
		TimeMachine.getInstance().addState(cursor, previousPositions, yOffSet, 
		previousYOffSets, c.getGlyphs());
		
	}

	/**
	 * Method to spell check the text in the text editor.
	 */
	public void spellCheck(Composition c)
	{
		
		List <Glyph> glyphs = c.getGlyphs();
		String word = "";
		for (int i = 0 ; i < glyphs.size() ; i++) {
			Glyph g = glyphs.get(i);
			if (g instanceof CharGlyph) {
				CharGlyph cg = (CharGlyph)g;
				if (cg.getChar() == ' ' || cg.getChar() == '\0') {
					// check if word is misspelled
					if (word.length() > 0 && !SpellChecker.getInstance().isCorrect(word)) {
						// set every char glyph in the word to gramaticallyCorrect = false
						for (int j = i - word.length() ; j < i ; j++) {
							if (glyphs.get(j) instanceof CharGlyph) {
								CharGlyph cg2 = (CharGlyph)glyphs.get(j);
								cg2.setGramaticallyCorrect(false);
							}
						}
					} else {
						// set every char glyph in the word to gramaticallyCorrect = true
						for (int j = i - word.length() ; j < i ; j++) {
							if (glyphs.get(j) instanceof CharGlyph) {
								CharGlyph cg2 = (CharGlyph)glyphs.get(j);
								cg2.setGramaticallyCorrect(true);
							}
						}
					}
					word = "";
				}
				else {
					word += cg.getChar();
				}
			}
		}
	}
}



/*
 * Point class used to store the position of a glyph.
 */
class Point
{
	private int x;
	private int y;
	
	/**
	 * Constructor for Point class.
	 */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * getters and setters
	 */
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
}


/*
 * Composition class used to store the glyphs in a composition.
 */
class Composition
{
	private List<Glyph> glyphs;
	
	/**
	 * Constructor for Composition class.
	 */
	public Composition() 
	{
		glyphs = new ArrayList<Glyph>();
	}
	
	/**
	 * Add a glyph to the composition.
	 */
	public void add(Glyph g)
	{
		glyphs.add(g);
	}
	
	/**
	 * Get the glyphs in the composition.
	 */
	public List<Glyph> getGlyphs() {
		return this.glyphs;
	}

	/**
	 * Set the glyphs in the composition.
	 */
	public void setGlyphs(List<Glyph> glyphs) {
		this.glyphs = glyphs;
	}

}


/*
 * CharGlyph class used to store the character and position of a character glyph.
 */
class CharGlyph extends Glyph 
{
	private char c;
	private Point pos;
	private RGB color;
	private String font;
	private int size;
	private boolean gramaticallyCorrect;
	
	/**
	 * Constructor for CharGlyph class.
	 */
	public CharGlyph(char c) 
	{
		super();
		this.c = c;
		this.gramaticallyCorrect = true;
	}

	/**
	 * getters and setters
	 */
	public void setPos(Point pos){this.pos = pos;}
	public Point getPos(){return pos;}
	public RGB getColor(){return color;}
	public void setColor(RGB color){this.color = color;}
	public String getFont(){return font;}
	public void setFont(String font){this.font = font;}
	public int getSize(){return size;}
	public void setSize(int size){this.size = size;}
	public void setGramaticallyCorrect(boolean b){this.gramaticallyCorrect = b;}
	public boolean getGramaticallyCorrect() {return gramaticallyCorrect; }
	public char getChar(){return c;}

}


/**
 * ImageGlyph class used to store the image and position of an image glyph.
 */
class ImageGlyph extends Glyph
{
	private String fileName;
	private int width;
	private int height;
	private Image image;
	private Point pos;
	private Point endPoint;
	
	/**
	 * Constructor for ImageGlyph class.	
	 */
	public ImageGlyph(String fileName, int width, int height) 
	{
		super();
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		image = new Image(null, fileName);
	}
	
	/**
	 * getters and setters
	 */
	public String getFileName(){return fileName;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public Image getImage(){return image;}
	public void setPos(Point pos){this.pos = pos;}
	public Point getPos(){return pos;}
	public void setWidth(int width){this.width = width;}
	public void setHeight(int height){this.height = height;}
	public Point getEndPoint(){return endPoint;}
	public void setEndPoint(Point endPoint){this.endPoint = endPoint;}

}



/**
 * RectGlyph class used to store the position and dimensions of a rectangle glyph.
 */
class RectGlyph extends Glyph
{
	private Point pos;
	private int width;
	private int height;
	private Point endPoint;
	private RGB fillColor;
	private RGB borderColor;

	/**
	 * Constructor for RectGlyph class.
	 */
	public RectGlyph() 
	{
		super();
		width = 60;
		height = 60;
		fillColor = new RGB(255, 255, 255);
		borderColor = new RGB(0, 0, 0);
	}

	/**
	 * getters and setters
	 */
	public void setPos(Point pos){this.pos = pos;}
	public Point getPos(){return pos;}
	public void setWidth(int width){this.width = width;}
	public void setHeight(int height){this.height = height;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public void setEndPoint(Point p) {this.endPoint = p;}
	public Point getEndPoint() {return this.endPoint;}
	public RGB getBorderColor(){return borderColor;}
	public RGB getFillColor(){return fillColor;}
	public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
	public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
	
}

/**
 * CircleGlyph class used to store the position and dimensions of a circle glyph.
 */
class CircleGlyph extends Glyph
{
    private Point pos;
    private int radius;
    private Point endPoint;
    private RGB fillColor;
    private RGB borderColor;

	/**
	 * Constructor for CircleGlyph class.
	 */
    public CircleGlyph() 
    {
        super();
        radius = 60;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }

	/**
	 * getters and setters
	 */
    public void setPos(Point pos){this.pos = pos;}
    public Point getPos(){return pos;}
    public void setRadius(int radius){this.radius = radius;}
    public int getRadius(){return radius;}
    public void setEndPoint(Point p) {this.endPoint = p;}
    public Point getEndPoint() {return this.endPoint;}
    public RGB getBorderColor(){return borderColor;}
    public RGB getFillColor(){return fillColor;}
    public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
    public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
    
}

/**
 * TriangleGlyph class used to store the position and dimensions of a triangle glyph.
 */
class TriangleGlyph extends Glyph
{
    private Point pos;
    private int width;
    private int height;
    private Point endPoint;
    private RGB fillColor;
    private RGB borderColor;

	/**
	 * Constructor for TriangleGlyph class.
	 */
    public TriangleGlyph() 
    {
        super();
        width = 60;
        height = 60;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }

	/**
	 * getters and setters
	 */
    public void setPos(Point pos){this.pos = pos;}
    public Point getPos(){return pos;}
    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public void setEndPoint(Point p) {this.endPoint = p;}
    public Point getEndPoint() {return this.endPoint;}
    public RGB getBorderColor(){return borderColor;}
    public RGB getFillColor(){return fillColor;}
    public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
    public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
    
}


/**
 * SpellChecker class used to check the spelling of the words in the document.
 */
class SpellChecker 
{
	private static SpellChecker instance = null;
	private static final String DICTIONARY = "dictionary.txt";
	private Set<String> dict;
	
	/**
	 * getInstance method used to get the instance of the SpellChecker class.
	 */
	public static SpellChecker getInstance()
	{
		if (instance == null)
		{
			instance = new SpellChecker();
		}
		return instance;
	}
	
	/**
	 * Constructor for SpellChecker class.
	 */
	private SpellChecker() {
		dict = new HashSet<String>();
		// open the dictionary
				FileInputStream fis = null;
				try 
				{
					fis = new FileInputStream(DICTIONARY);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				try 
				{
					while ((line = br.readLine()) != null) 
					{
						dict.add(line.trim().toLowerCase());
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
	}
	
	/**
	 * check method used to check the spelling of the words in the document.
	 */
	public boolean isCorrect(String word)
	{
		// strip out punctuation at the end of the word
		if (word.length() > 0)
		{
			char lastChar = word.charAt(word.length() - 1);
			char firstChar = word.charAt(0);
			if (!Character.isLetter(lastChar))
			{
				word = word.substring(0, word.length() - 1);
			}
			if (!Character.isLetter(firstChar))
			{
				word = word.substring(1, word.length());
			}
		}
		return dict.contains(word.toLowerCase());	
	}
}


/**
 * TimeMachine class employs a Singleton design pattern. 
 * This class is used to implement the redo and undo functionality
 */
class TimeMachine {

	private List<Point> cursors;
	private List<List<Point>> previousPositions_lists;
	private List<Integer> yOffSets;
	private List<List<Integer>> previousYOffSets_lists;
	private List<List<Glyph>> glyphs_lists;
	int index;
	private static TimeMachine instance;
	
	/**
	 * getInstance method used to get the instance of the TimeMachine class.
	 */
	public static TimeMachine getInstance()
	{
		if (instance == null)
		{
			instance = new TimeMachine();
		}
		return instance;
	}
	
	/**
	 * Constructor for TimeMachine class.
	 */
	private TimeMachine()
	{
		cursors = new ArrayList<Point>();
		previousPositions_lists = new ArrayList<List<Point>>();
		yOffSets = new ArrayList<Integer>();
		previousYOffSets_lists = new ArrayList<List<Integer>>();
		glyphs_lists = new ArrayList<List<Glyph>>();
		index = 0;
		// the initial state is the empty document
		cursors.add(new Point(0, 0));
		previousPositions_lists.add(new ArrayList<Point>());
		yOffSets.add(0);
		previousYOffSets_lists.add(new ArrayList<Integer>());
		glyphs_lists.add(new ArrayList<Glyph>());
	}

	/**
	 * addState method used to add the state of the document to the TimeMachine.
	 */
	public void addState(Point cursor, List<Point> previousPositions, int yOffSet, 
	List<Integer> previousYOffSets, List<Glyph> glyphs)
	{
		// check if size is greater than 10, if it is replace oldest state with new one
		if (cursors.size() > 10)
		{
			cursors.remove(0);
			previousPositions_lists.remove(0);
			yOffSets.remove(0);
			previousYOffSets_lists.remove(0);
			glyphs_lists.remove(0);
			index--;
		}
	    
		if (index < cursors.size() - 1) 
		{
			// remove all the states after the current index
			cursors = cursors.subList(0, index + 1);
			previousPositions_lists = previousPositions_lists.subList(0, index + 1);
			yOffSets = yOffSets.subList(0, index + 1);
			previousYOffSets_lists = previousYOffSets_lists.subList(0, index + 1);
			glyphs_lists = glyphs_lists.subList(0, index + 1);
			// perform a deep copy of the lists
			List<Point> previousPositions_copy = new ArrayList<Point>();
			for (Point p : previousPositions)
			{
				previousPositions_copy.add(new Point(p.getX(), p.getY()));
			}
			List<Integer> previousYOffSets_copy = new ArrayList<Integer>();
			for (Integer i : previousYOffSets)
			{
				previousYOffSets_copy.add(Integer.valueOf(i));
			}
			List<Glyph> glyphs_copy = new ArrayList<Glyph>();
			for (Glyph g : glyphs)
			{
				glyphs_copy.add(g);
			}
			// add the new state
			cursors.add(new Point(cursor.getX(), cursor.getY()));
			previousPositions_lists.add(previousPositions_copy);
			yOffSets.add(Integer.valueOf(yOffSet));
			previousYOffSets_lists.add(previousYOffSets_copy);
			glyphs_lists.add(glyphs_copy);	
			index = cursors.size() - 1;
		}
		else
		{
			// perform a deep copy of the lists
			List<Point> previousPositions_copy = new ArrayList<Point>();
			for (Point p : previousPositions)
			{
				previousPositions_copy.add(new Point(p.getX(), p.getY()));
			}
			List<Integer> previousYOffSets_copy = new ArrayList<Integer>();
			for (Integer i : previousYOffSets)
			{
				previousYOffSets_copy.add(Integer.valueOf(i));
			}
			List<Glyph> glyphs_copy = new ArrayList<Glyph>();
			for (Glyph g : glyphs)
			{
				glyphs_copy.add(g);
			}
			// add the new state
			cursors.add(new Point(cursor.getX(), cursor.getY()));
			previousPositions_lists.add(previousPositions_copy);
			yOffSets.add(Integer.valueOf(yOffSet));
			previousYOffSets_lists.add(previousYOffSets_copy);
			glyphs_lists.add(glyphs_copy);	
			index++;
		}
	}

	/**
	 * undo method used to undo the last action.
	 */
	public void undo()
	{
		if (index > 0)
		{
			index--;
		}
	}

	/**
	 * redo method used to redo the last action.
	 */
	public void redo()
	{
		if (index < cursors.size() - 1)
		{
			index++;
		}
	}

	/**
	 * getCursor method used to get the cursor position.
	 */
	public Point getCursor()
	{
		return cursors.get(index);
	}

	/**
	 * getPreviousPositions method used to get the previous positions.
	 */
	public List<Point> getPreviousPositions()
	{
		return previousPositions_lists.get(index);
	}

	/**
	 * getYOffSet method used to get the yOffSet.
	 */
	public int getYOffSet()
	{
		return yOffSets.get(index);
	}

	/**
	 * getPreviousYOffSets method used to get the previous yOffSets.
	 */
	public List<Integer> getPreviousYOffSets()
	{
		return previousYOffSets_lists.get(index);
	}

	/**
	 * getGlyphs method used to get the glyphs.
	 */
	public List<Glyph> getGlyphs()
	{
		return glyphs_lists.get(index);
	}

	/**
	 * canUndo method used to check if the TimeMachine can undo.
	 */
	public boolean canUndo()
	{
		return index > 0;
	}

	/**
	 * canRedo method used to check if the TimeMachine can redo.
	 */
	public boolean canRedo()
	{
		return index < cursors.size() - 1;
	}
}