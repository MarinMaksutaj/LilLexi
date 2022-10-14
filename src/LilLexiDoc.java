/**
 * Lil Lexi Document Model
 * 
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
 * LilLexiDoc uses the Glyph class and Composite pattern
 */
public class LilLexiDoc 
{
	private LilLexiUI ui;
	private String docName;
	private int pageWidth;
	private int pageHeight;
	private int numColumns;
	private Point cursorPosition;
	private Composition composition;
	private Compositor compositor;
	/**
	 * Ctor
	 */
	public LilLexiDoc() 
	{
		docName = "Untitled";
		pageWidth = 800;
		pageHeight = 800;
		numColumns = 40;
		cursorPosition = new Point(0,0);
		composition = new Composition();
		compositor = new SimpleCompositor();
	
	}
	
	/**
	 * setUI
	 */
	public void setUI(LilLexiUI ui) {this.ui = ui;}

	/**
	 * add a char
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
	 * add a rectangle
	 */
	public void addRectGlyph(RGB borderColor, RGB fillColor, Integer size)
	{
		RectGlyph rg = new RectGlyph(size);
		rg.setFillColor(fillColor);
		rg.setBorderColor(borderColor);
		composition.add(rg);
	}

	
	/**
     * add a triangle
     */
    public void addTriangleGlyph(RGB borderColor, RGB fillColor, Integer size)
    {
        TriangleGlyph tg = new TriangleGlyph(size);
        tg.setFillColor(fillColor);
        tg.setBorderColor(borderColor);
        composition.add(tg);
    }
    
	
	/**
     * add a circle
     */
    public void addCircleGlyph(RGB borderColor, RGB fillColor, Integer size)
    {
        CircleGlyph cg = new CircleGlyph(size);
        cg.setFillColor(fillColor);
        cg.setBorderColor(borderColor);
        composition.add(cg);
    }


	/**
	 * add an image
	 */
	public void addImageGlyph(String fileName, Integer size) 
	{
		ImageGlyph ig = new ImageGlyph(fileName, size, size);
		composition.add(ig);
	} 

	public String getDocName(){return docName;}
	public int getPageWidth(){return pageWidth;}
	public int getPageHeight(){return pageHeight;}
	public int getNumColumns(){return numColumns;}
	public Point getCursorPosition(){return cursorPosition;}
	public Composition getComposition(){return composition;}
	public Compositor getCompositor(){return compositor;}
	public void setDocName(String docName){this.docName = docName;}
	public void setPageWidth(int pageWidth){this.pageWidth = pageWidth;}
	public void setPageHeight(int pageHeight){this.pageHeight = pageHeight;}
	public void setNumColumns(int numColumns){this.numColumns = numColumns;}
	public void setCursorPosition(Point cursorPosition){this.cursorPosition = cursorPosition;}
	public void setComposition(Composition composition){this.composition = composition;}
	public void setCompositor(Compositor compositor){this.compositor = compositor;}
	public void backspace()
	{
			compositor.backspace(composition);
	}
	public void lineBreak()
	{
		compositor.lineBreak(composition);
	}

	/**
	 * toString
	 */
	public String toString()
	{
		String s = "LilLexiDoc: " + docName + " " + pageWidth + " " + pageHeight + " " + numColumns + " " + cursorPosition;
		for (Glyph g: composition.getGlyphs())
		{
			s += g.toString();
		}
		return s;
	}

	/**
	 * draw
	 */
	public void draw()
	{
		compositor.addUI(ui);
		compositor.compose(composition);
		
	}

	public void undo() {
		compositor.undo(composition);
	}

	public void redo() {
		compositor.redo(composition);
	}
}


/**
 * Point
 */




/**
 * Glyph
 */
abstract class Glyph
{
	private Point pos;

	/**
	 * Ctor
	 */
	public Glyph() 
	{
		pos = new Point(0,0);
	}
	public Glyph(Point pos) 
	{
		this.pos = pos;
	}
	public Point getPos(){return pos;}
}

/**
 * Compositor
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
 * It is used to just draw the glyphs in the order they were added to the composition
 */
class SimpleCompositor implements Compositor
{
	/**
	 * compose
	 */
	private LilLexiUI ui;
	private Point cursor;
	private List<Point> previousPositions;
	private int yOffSet;
	private List<Integer> previousYOffSets;
	
	public SimpleCompositor () {
		this.cursor = new Point(0, 0);
		this.previousPositions = new ArrayList<Point>();
		this.yOffSet = 30;
		this.previousYOffSets = new ArrayList<Integer>();
	}

	public void undo (Composition c) {
		if (!TimeMachine.getInstance().canUndo()) {
			return;
		}
		System.out.println("undoin");
		TimeMachine.getInstance().undo();
		// perform a deep copy of the current time machine state
		Point newCursor = new Point(TimeMachine.getInstance().getCursor().getX(), TimeMachine.getInstance().getCursor().getY());
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

	public void redo (Composition c) {
		if (!TimeMachine.getInstance().canRedo()) {
			return;
		}
		TimeMachine.getInstance().redo();
		// perform a deep copy of the current time machine state
		Point newCursor = new Point(TimeMachine.getInstance().getCursor().getX(), TimeMachine.getInstance().getCursor().getY());
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

	public void addUI (LilLexiUI ui) {this.ui = ui;}
	public void backspace (Composition c) {
		
		spellCheck(c); // should we put it somewhere else?
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
	public void compose(Composition c)
	{
		
	    spellCheck(c);
		List<Glyph> glyphs = c.getGlyphs();
		//System.out.println(glyphs.size());
		//System.out.println(cursor.getX());
		//System.out.println(cursor.getY());
		for (Glyph g: glyphs)
		{
			if (g.getPos() != null)
			{
				//System.out.println("Non null pos:" + g.getPos().getX() + " " + g.getPos().getY());
				//System.out.println("hello");
				continue;
			}
			previousPositions.add(new Point(cursor.getX(), cursor.getY()));
			previousYOffSets.add(yOffSet);
			
			// check what type of glyph it is and draw it
			// if it is a character glyph, draw it, move the cursor by a fixed amount and continue
			// if it is a rectangle glyph, draw it and continue, move the cursor by a the width of the rectangle
			// if it is an image glyph, draw it and continue, move the cursor by a the width of the image
			// if cursor reaches the end of the line, move it to the next line
			// if cursor reaches the end of the page, don't draw anything

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
		// add previous state in TimeMachine
		TimeMachine.getInstance().addState(cursor, previousPositions, yOffSet, previousYOffSets, c.getGlyphs());
		
	}

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




class Point
{
	private int x;
	private int y;
	
	/**
	 * Ctor
	 */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * gets
	 */
	public int getX(){return x;}
	public int getY(){return y;}
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
}


class Composition
{
	private List<Glyph> glyphs;
	
	/**
	 * Ctor
	 */
	public Composition() 
	{
		glyphs = new ArrayList<Glyph>();
	}
	
	/**
	 * add
	 */
	public void add(Glyph g)
	{
		glyphs.add(g);
	}
	
	/**
	 * insert
	 */
	public void insert(Glyph g)
	{
		glyphs.add(g);
	}
	
	public List<Glyph> getGlyphs() {
		return this.glyphs;
	}

	public void  removeLast() {
		glyphs.remove(glyphs.size() - 1);
	}

	public void setGlyphs(List<Glyph> glyphs) {
		this.glyphs = glyphs;
	}

}


class CharGlyph extends Glyph 
{
	private char c;
	private Point pos;
	private RGB color;
	private String font;
	private int size;
	private boolean gramaticallyCorrect;
	
	/**
	 * Ctor
	 */
	public CharGlyph(char c) 
	{
		super();
		this.c = c;
		this.gramaticallyCorrect = true;
	}

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
	
	/**
	 * gets
	 */
	public char getChar(){return c;}

}


/**
 * Image Glyph
 */
class ImageGlyph extends Glyph
{
	private String fileName;
	private int width;
	private int height;
	private Image image;
	private Point pos;
	
	/**
	 * Ctor
	 */
	public ImageGlyph(String fileName, int width, int height) 
	{
		super();
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		// create the image using SWT
		image = new Image(null, fileName);
	}
	
	/**
	 * gets
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
 * Rectangle Glyph
 */
class RectGlyph extends Glyph
{
	private Point pos;
	private int width;
	private int height;
	private RGB fillColor;
	private RGB borderColor;
	
	public RectGlyph(Integer size) 
    {
        super();
        width = size;
        height = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
        System.out.println(1);
    }

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
 * Circle Glyph
 */
class CircleGlyph extends Glyph
{
    private Point pos;
    private int radius;
    private RGB fillColor;
    private RGB borderColor;

    public CircleGlyph(Integer size) 
    {
        super();
        radius = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }

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
 * Triangle Glyph 
 */
class TriangleGlyph extends Glyph
{
    private Point pos;
    private int width;
    private int height;
    private RGB fillColor;
    private RGB borderColor;

    public TriangleGlyph(Integer size) 
    {
        super();
        width = size;
        height = size;
        fillColor = new RGB(255, 255, 255);
        borderColor = new RGB(0, 0, 0);
    }

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


class SpellChecker 
{
	private static SpellChecker instance = null;
	
	private static final String DICTIONARY = "dictionary.txt";
	private Set<String> dict;
	
	public static SpellChecker getInstance()
	{
		if (instance == null)
		{
			instance = new SpellChecker();
		}
		return instance;
	}
	
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
	
	public static TimeMachine getInstance()
	{
		if (instance == null)
		{
			instance = new TimeMachine();
		}
		return instance;
	}
	
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

	public void addState(Point cursor, List<Point> previousPositions, int yOffSet, List<Integer> previousYOffSets, List<Glyph> glyphs)
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

	public void undo()
	{
		if (index > 0)
		{
			index--;
		}
		
		// print the current glyph list
	}

	public void printGlyphs() {
	    System.out.println("size undo: " + cursors.size());
        System.out.println("undo index: " + index);
		System.out.println("==================================");
		for (int i = 0; i < glyphs_lists.size() ; i++) {
			for (Glyph g : glyphs_lists.get(i)) {
			    CharGlyph cg = (CharGlyph)g;
				System.out.print(cg.getChar());
			}
			System.out.println();
		}
		System.out.println("==================================");
	}

	public void redo()
	{
		if (index < cursors.size() - 1)
		{
			index++;
		}
	}

	public Point getCursor()
	{
		return cursors.get(index);
	}

	public List<Point> getPreviousPositions()
	{
		return previousPositions_lists.get(index);
	}

	public int getYOffSet()
	{
		return yOffSets.get(index);
	}

	public List<Integer> getPreviousYOffSets()
	{
		return previousYOffSets_lists.get(index);
	}

	public List<Glyph> getGlyphs()
	{
		return glyphs_lists.get(index);
	}

	public boolean canUndo()
	{
		return index > 0;
	}

	public boolean canRedo()
	{
		return index < cursors.size() - 1;
	}
}