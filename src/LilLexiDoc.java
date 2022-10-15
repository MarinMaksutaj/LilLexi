/*
*  Course: CSC335
*  Description: Document class for the LilLexi application.
* 				This is where the glyphs are stored.
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
 * LilLexiDoc class. It contains the composition and compositor.
 */
public class LilLexiDoc 
{
	private LilLexiUI ui;
	private Composition composition;
	private Compositor compositor;
	/**
	 * Constructor for the LilLexiDoc class.
	 * Author:  Marin Maksutaj
	 */
	public LilLexiDoc() 
	{
		composition = new Composition();
		compositor = new SimpleCompositor();
	
	}
	
	/**
	 * setter for the UI.
	 * Author:  Shyambhavi
	 */
	public void setUI(LilLexiUI ui) {this.ui = ui;}

	/**
	 * Method to add a char glyph to the document. 
	 * Author:  Shyambhavi
	 */
	public void addCharGlyph(char c, RGB color, String fontName, int fontSize)
	{
		CharGlyph cg = new CharGlyph(c);
		cg.setColor(color);
		cg.setFont(fontName);
		cg.setSize(fontSize);;
		composition.add(cg);
	}

	/**
	 * Method to add a rect glyph to the document.
	 * Author:  Marin Maksutaj
	 */
	public void addRectGlyph(RGB borderColor, RGB fillColor, Integer size)
	{
		RectGlyph rg = new RectGlyph(size);
		rg.setFillColor(fillColor);
		rg.setBorderColor(borderColor);
		composition.add(rg);
	}

	
	/**
     * Method to add the triangle glyph to the document.
	 * Author:  Marin Maksutaj
     */
    public void addTriangleGlyph(RGB borderColor, RGB fillColor, Integer size)
    {
        TriangleGlyph tg = new TriangleGlyph(size);
        tg.setFillColor(fillColor);
        tg.setBorderColor(borderColor);
        composition.add(tg);
    }
    
	
	/**
     * Method to add the circle glyph to the document.
	 * Author:  Shyambhavi
     */
    public void addCircleGlyph(RGB borderColor, RGB fillColor, Integer size)
    {
        CircleGlyph cg = new CircleGlyph(size);
        cg.setFillColor(fillColor);
        cg.setBorderColor(borderColor);
        composition.add(cg);
    }


	/**
	 * Method to add an image glyph to the document.
	 * Author:  Marin Maksutaj
	 */
	public void addImageGlyph(String fileName, Integer size) 
	{
		ImageGlyph ig = new ImageGlyph(fileName, size, size);
		composition.add(ig);
	} 


	/*
	 * Getters for the composition and compositor.
	 * Author: Shyambhavi
	 */
	public Composition getComposition(){return composition;}
	public Compositor getCompositor(){return compositor;}

	/*
	 * Setters for the composition and compositor.
	 * Author: Marin Maksutaj
	 */
	public void setComposition(Composition composition){this.composition = composition;}
	public void setCompositor(Compositor compositor){this.compositor = compositor;}

	/**
	 * Method to read the file and add the glyphs to the document.
	 * Author:  Marin Maksutaj
	 */
	public void backspace()
	{
			compositor.backspace(composition);
	}

	/*
	 * Method to break the line.
	 * Author: Shyambhavi
	 */
	public void lineBreak()
	{
		compositor.lineBreak(composition);
	}

	/**
	 * ToString method for the document.
	 * Author:  Marin Maksutaj
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
	 * Draw method for the document.
	 * Author:  Marin Maksutaj
	 */
	public void draw()
	{
		compositor.addUI(ui);
		compositor.compose(composition);
		
	}

	/*
	 * Undo method for the document.
	 * Author: Shyambhavi
	 */
	public void undo() {
		compositor.undo(composition);
	}

	/*
	 * Redo method for the document.
	 * Author: Shyambhavi
	 */
	public void redo() {
		compositor.redo(composition);
	}
}


/*
 * Glyph abstract class for the LilLexi application.
 */
abstract class Glyph
{
	private Point pos;

	/**
	 * Constructor for the Glyph class.
	 * Author:  Marin Maksutaj
	 */
	public Glyph() 
	{
		pos = new Point(0,0);
	}

	/**
	 * Constructor for the Glyph class.
	 * Author:  Shyambhavi
	 */
	public Glyph(Point pos) 
	{
		this.pos = pos;
	}
	/*
	 * Getter for the position.
	 * Author: Marin Maksutaj
	 */
	public Point getPos(){return pos;}
}

/**
 * Compositor interface for the LilLexi application.
 */
interface Compositor
{
	/**
	 * Method to compose the glyphs.
	 * Author:  Marin Maksutaj
	 */
	public void addUI (LilLexiUI ui);
	public void compose(Composition c);
	public void backspace(Composition c);
	public void lineBreak(Composition c);
	public void undo(Composition c);
	public void redo (Composition c);
}

/**
 * Simple Compositor 
 * It is used to just draw the glyphs in the order they were added to the composition
 */
class SimpleCompositor implements Compositor
{
	private LilLexiUI ui;
	private Point cursor;
	private List<Point> previousPositions;
	private int yOffSet;
	private List<Integer> previousYOffSets;
	
	/**
	 * Constructor for the SimpleCompositor class.
	 * Author:  Shyambhavi
	 */
	public SimpleCompositor () {
		this.cursor = new Point(0, 0);
		this.previousPositions = new ArrayList<Point>();
		this.yOffSet = 30;
		this.previousYOffSets = new ArrayList<Integer>();
	}

	/**
	 * Method to undo the last action.
	 * Author:  Shyambhavi
	 */
	public void undo (Composition c) {
		if (!TimeMachine.getInstance().canUndo()) {
			return;
		}
		System.out.println("undoin");
		TimeMachine.getInstance().undo();
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
	 * Method to redo the last action.
	 * Author:  Marin Maksutaj
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
	 * Method to add the UI.
	 * Author:  Marin Maksutaj
	 */
	public void addUI (LilLexiUI ui) {this.ui = ui;}

	/**
	 * Method to backsapce the last glyph.
	 * Author:  Marin Maksutaj
	 */
	public void backspace (Composition c) {
		
		spellCheck(c); 
		System.out.println("Size of glyphs " + c.getGlyphs().size());
		System.out.println("size of previous positions: " + previousPositions.size());
		System.out.println("size of previous y offsets: " + previousYOffSets.size());
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
	 * Method to line break.
	 * Author:  Shyambhavi
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
	 * Method to compose a composition.
	 * Author:  Marin Maksutaj
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
		TimeMachine.getInstance().addState(cursor, previousPositions, yOffSet, 
		previousYOffSets, c.getGlyphs());
		
	}

	/**
	 * Method to spell check a composition.
	 * Author:  Shyambhavi
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
					if (word.length() > 0 && !SpellChecker.getInstance().isCorrect(word)) {
						for (int j = i - word.length() ; j < i ; j++) {
							if (glyphs.get(j) instanceof CharGlyph) {
								CharGlyph cg2 = (CharGlyph)glyphs.get(j);
								cg2.setGramaticallyCorrect(false);
							}
						}
					} else {
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
 * Point class used to keep track of the glyph's position.
 */
class Point
{
	private int x;
	private int y;
	
	/**
	 * Constructor for the Point class.
	 * Author:  Marin Maksutaj
	 */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getters and setters for the Point class.
	 * Author:  Marin Maksutaj
	 */
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
}


/*
 * Composition class used to keep track of the composition's glyphs.
 */
class Composition
{
	private List<Glyph> glyphs;
	
	/**
	 * Constructor for the Composition class.
	 * Author:  Shyambhavi
	 */
	public Composition() 
	{
		glyphs = new ArrayList<Glyph>();
	}
	
	/**
	 * Add a glyph to the composition.
	 * Author:  Shyambhavi
	 */
	public void add(Glyph g)
	{
		glyphs.add(g);
	}
	
	/**
	 * Get glyphs from the composition.
	 * Author:  Shyambhavi
	 */
	public List<Glyph> getGlyphs() {
		return this.glyphs;
	}


	/*
	 * Set glyphs for the composition.
	 * Author:  Marin Maksutaj
	 */
	public void setGlyphs(List<Glyph> glyphs) {
		this.glyphs = glyphs;
	}

}


/*
 * CharGlyph class used to keep track of the character's glyph.
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
	 * Constructor for the CharGlyph class.
	 * Author:  Shyambhavi
	 */
	public CharGlyph(char c) 
	{
		super();
		this.c = c;
		this.gramaticallyCorrect = true;
	}

	/**
	 * Getters and setters for the CharGlyph class.
	 * Author:  Shyambhavi
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
 * Image Glyph class used to keep track of the image's glyph.
 */
class ImageGlyph extends Glyph
{
	private String fileName;
	private int width;
	private int height;
	private Image image;
	private Point pos;
	
	/**
	 * Constructor for the ImageGlyph class.
	 * Author:  Marin Maksutaj
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
	 * Getters and setters for the ImageGlyph class.
	 * Author:  Marin Maksutaj
	 */
	public String getFileName(){return fileName;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public Image getImage(){return image;}
	public void setPos(Point pos){this.pos = pos;}
	public Point getPos(){return pos;}
	public void setWidth(int width){this.width = width;}
	public void setHeight(int height){this.height = height;}

}



/**
 * Rectangle Glyph class used to keep track of the rectangle's glyph.
 */
class RectGlyph extends Glyph
{
	private Point pos;
	private int width;
	private int height;
	private RGB fillColor;
	private RGB borderColor;
	

	/**
	 * Constructor for the RectGlyph class.
	 * Author:  Marin Maksutaj
	 */
	public RectGlyph(Integer size) 
    {
        super();
        width = size;
        height = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
        System.out.println(1);
    }

	/**
	 * Getters and setters for the RectGlyph class.
	 * Author:  Marin Maksutaj
	 */
	public void setPos(Point pos){this.pos = pos;}
	public Point getPos(){return pos;}
	public void setWidth(int width){this.width = width;}
	public void setHeight(int height){this.height = height;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public RGB getBorderColor(){return borderColor;}
	public RGB getFillColor(){return fillColor;}
	public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
	public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
	
}

/**
 * Circle Glyph class used to keep track of the circle's glyph.
 */
class CircleGlyph extends Glyph
{
    private Point pos;
    private int radius;
    private RGB fillColor;
    private RGB borderColor;

	/**
	 * Constructor for the CircleGlyph class.
	 * Author:  Shyambhavi
	 */
    public CircleGlyph(Integer size) 
    {
        super();
        radius = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }


	/**
	 * Getters and setters for the CircleGlyph class.
	 * Author:  Shyambhavi
	 */
    public void setPos(Point pos){this.pos = pos;}
    public Point getPos(){return pos;}
    public void setRadius(int radius){this.radius = radius;}
    public int getRadius(){return radius;}
    public RGB getBorderColor(){return borderColor;}
    public RGB getFillColor(){return fillColor;}
    public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
    public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
    
}

/**
 * Triangle Glyph class used to keep track of the triangle's glyph.
 */
class TriangleGlyph extends Glyph
{
    private Point pos;
    private int width;
    private int height;
    private RGB fillColor;
    private RGB borderColor;

	/**
	 * Constructor for the TriangleGlyph class.
	 * Author:  Marin Maksutaj
	 */
    public TriangleGlyph(Integer size) 
    {
        super();
        width = size;
        height = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }

	/**
	 * Getters and setters for the TriangleGlyph class.
	 * Author:  Marin Maksutaj
	 */
    public void setPos(Point pos){this.pos = pos;}
    public Point getPos(){return pos;}
    public void setWidth(int width){this.width = width;}
    public void setHeight(int height){this.height = height;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public RGB getBorderColor(){return borderColor;}
    public RGB getFillColor(){return fillColor;}
    public void setBorderColor(RGB borderColor){this.borderColor = borderColor;}
    public void setFillColor(RGB fillColor){this.fillColor = fillColor;}
    
}


/**
 * SpellChecker class used to check the spelling of the words. Uses a singleton pattern.
 */
class SpellChecker 
{
	private static SpellChecker instance = null;
	private static final String DICTIONARY = "dictionary.txt";
	private Set<String> dict;
	
	/**
	 * getter for the instance.
	 * Author:  Shyambhavi
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
	 * Constructor for the SpellChecker class.
	 * Author:  Marin Maksutaj
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
	 * Method used to check the spelling of the word.
	 * Author:  Marin Maksutaj
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
 * This class is used to implement the redo and undo functionality.
 * Uses a singleton pattern.
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
	 * getter for the instance.
	 * Author:  Shyambhavi
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
	 * Constructor for the TimeMachine class.
	 * Author:  Marin Maksutaj
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
	 * Method used to add a new state to the time machine.
	 * Author:  Marin Maksutaj
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
	 * Method used to undo the last action.
	 * Author:  Shyambhavi
	 */
	public void undo()
	{
		if (index > 0)
		{
			index--;
		}
		
	}

	/**
	 * Method used to redo the last action.
	 * Author:  Shyambhavi
	 */
	public void redo()
	{
		if (index < cursors.size() - 1)
		{
			index++;
		}
	}

	/**
	 * Method used to get the cursor position.
	 * Author:  Marin Maksutaj
	 */
	public Point getCursor()
	{
		return cursors.get(index);
	}

	/**
	 * Method used to get the previous positions.
	 * Author:  Marin Maksutaj
	 */
	public List<Point> getPreviousPositions()
	{
		return previousPositions_lists.get(index);
	}

	/**
	 * Method used to get the y offset.
	 * Author:  Marin Maksutaj
	 */
	public int getYOffSet()
	{
		return yOffSets.get(index);
	}

	/**
	 * Method used to get the previous y offsets.
	 * Author: Shyambhavi
	 */
	public List<Integer> getPreviousYOffSets()
	{
		return previousYOffSets_lists.get(index);
	}

	/**
	 * Method used to get the glyphs.
	 * Author:  Shyambhavi
	 */
	public List<Glyph> getGlyphs()
	{
		return glyphs_lists.get(index);
	}

	/**
	 * Method used to check if we can undo.
	 * Author:  Marin Maksutaj
	 */
	public boolean canUndo()
	{
		return index > 0;
	}

	/**
	 * Method used to check if we can redo.
	 * Author:  Marin Maksutaj
	 */
	public boolean canRedo()
	{
		return index < cursors.size() - 1;
	}
}