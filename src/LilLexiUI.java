/**
 * UI for Lil Lexi
 * 
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import java.util.List;


/**
 * LilLexiUI
 * 
 */
public class LilLexiUI
{
	private LilLexiDoc currentDoc;
	private LilLexiControl lexiControl;
	private Display display;
	private Shell shell;
	private Label statusLabel;
	private Canvas canvas;
	private int canvasWidth;
	private int canvasHeight;
	private Point rectangleEndPosition;
	private RGB fontColor;
	private RGB rectangleFillColor;
	private RGB rectangleBorderColor;
	private boolean darkMode;
	private String fontName;
	private int fontSize;
	
	/**
	 * Ctor
	 */
	public LilLexiUI() 
	{
		//---- create the window and the shell
		Display.setAppName("Lil Lexi");
		display = new Display();  
		shell = new Shell(display);
	    shell.setText("Lil Lexi");
		shell.setSize(810,900);
		shell.setLayout( new GridLayout());
		rectangleEndPosition = new Point(10,10);
		// set default fontColor to blue
		fontColor = new RGB(0,0,255);
		// set default rectangleFillColor to transparent
		rectangleFillColor = new RGB(255,255,255);
		// set default rectangleBorderColor to black
		rectangleBorderColor = new RGB(0,0,0);
		darkMode = false;
		fontName = "Courier";
		fontSize = 24;
	}
	
	public int getWidth() {return this.canvasWidth;}
	public int getHeight() {return this.canvasHeight;}
		
	/**
	 * start the editor
	 */
	public void start()
	{	
		//---- create widgets for the interface
	    Composite upperComp = new Composite(shell, SWT.NO_FOCUS);
	    Composite lowerComp = new Composite(shell, SWT.NO_FOCUS);
	    
	    //---- canvas for the document
		canvas = new Canvas(upperComp, SWT.NONE);
		canvasWidth = 800;
		canvasHeight = 800;
		canvas.setSize(canvasWidth, canvasHeight);

		canvas.addPaintListener(e -> {
			System.out.println("PaintListener");
			Rectangle rect = shell.getClientArea();
			if (darkMode) {
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			} else {
				e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE)); 
			}
            e.gc.fillRectangle(rect.x, rect.y, rect.width, rect.height);
            e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE)); 
    		Font font = new Font(display, fontName, fontSize, SWT.BOLD);
    		e.gc.setFont(font);
			// iterate over the glyphs and draw them
			List<Glyph> glyphs = currentDoc.getComposition().getGlyphs();
			for (Glyph g : glyphs) {
				if (g instanceof CharGlyph) {
					CharGlyph cg = (CharGlyph) g;
					// set the font color
					e.gc.setForeground(new Color(display, cg.getColor()));
					Font f = new Font(display, cg.getFont(), cg.getSize(), SWT.BOLD);
					e.gc.setFont(f);
					e.gc.drawString(""+cg.getChar(), cg.getPos().getX(), cg.getPos().getY() + 10);
				}
				if (g instanceof RectGlyph) {
					RectGlyph rg = (RectGlyph) g;
					// set the rectangle fill color
					e.gc.setBackground(new Color(display, rg.getFillColor()));
					// set the rectangle border color
					e.gc.setForeground(new Color(display, rg.getBorderColor()));
					// make border width 2
					e.gc.setLineWidth(2); // without this the border would be default width 1 but not visible when filled for some reason. TODO: decide what to do
					e.gc.drawRectangle(rg.getPos().getX(), rg.getPos().getY() + 10, rg.getWidth(), rg.getHeight());
					e.gc.fillRectangle(rg.getPos().getX(), rg.getPos().getY() + 10, rg.getWidth(), rg.getHeight());
					// reset fill color to transparent
					if (darkMode) {
						e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
					} else {
						e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE)); 
					}
				}
				if (g instanceof ImageGlyph) {
					ImageGlyph ig = (ImageGlyph) g;
					//e.gc.drawImage(ig.getImage(), ig.getPos().getX(), ig.getPos().getY() + 10);
					// draw image with width and height
					e.gc.drawImage(ig.getImage(), 0, 0, ig.getImage().getBounds().width, ig.getImage().getBounds().height, ig.getPos().getX(), ig.getPos().getY() + 10, ig.getWidth(), ig.getHeight());
				}
			}
		});	
		
        canvas.addMouseListener(new MouseListener() {
            public void mouseDown(MouseEvent e) {
            	System.out.println("mouseDown in canvas");
				// change rectangleEndPosition to the mouse position
				if (rectangleEndPosition == null) {
					rectangleEndPosition = new Point(e.x, e.y);
				}
				else {
					rectangleEndPosition.setX(e.x);
					rectangleEndPosition.setY(e.y);
				}
            } 
            public void mouseUp(MouseEvent e) {} 
            public void mouseDoubleClick(MouseEvent e) {} 
        });
        
        canvas.addKeyListener(new KeyListener() {
        	public void keyPressed(KeyEvent e) {
				// check if key is backspace
				rectangleEndPosition = null;
				if (e.keyCode == SWT.BS) {
					lexiControl.backspace();
					updateUI();
					return;
			}
				// check for enter key
				if (e.keyCode == SWT.CR) {
					lexiControl.lineBreak();
					updateUI();
					return;
				}
        		System.out.println("key " + e.character);
        		lexiControl.addCharGlyph(e.character, fontColor, fontName, fontSize);
        		updateUI();
        	}
        	public void keyReleased(KeyEvent e) {}
        });

		Slider slider = new Slider (canvas, SWT.VERTICAL);
		Rectangle clientArea = canvas.getClientArea ();
		slider.setBounds (clientArea.width - 40, clientArea.y + 10, 32, clientArea.height);
		slider.addListener (SWT.Selection, event -> {
			String string = "SWT.NONE";
			switch (event.detail) {
				case SWT.DRAG: string = "SWT.DRAG"; break;
				case SWT.HOME: string = "SWT.HOME"; break;
				case SWT.END: string = "SWT.END"; break;
				case SWT.ARROW_DOWN: string = "SWT.ARROW_DOWN"; break;
				case SWT.ARROW_UP: string = "SWT.ARROW_UP"; break;
				case SWT.PAGE_DOWN: string = "SWT.PAGE_DOWN"; break;
				case SWT.PAGE_UP: string = "SWT.PAGE_UP"; break;
			}
			System.out.println ("Scroll detail -> " + string);
		});
		        
        //---- status label
        lowerComp.setLayout(new RowLayout());
        statusLabel = new Label(lowerComp, SWT.NONE);		

		FontData[] fD = statusLabel.getFont().getFontData();
		fD[0].setHeight(24);
		statusLabel.setFont( new Font(display,fD[0]));
		// set the color
		statusLabel.setForeground(new Color(display, fontColor));
		statusLabel.setText("Ready to edit!");
		
		//---- main menu
		Menu menuBar, fileMenu, insertMenu, helpMenu;
		MenuItem fileMenuHeader, insertMenuHeader, helpMenuHeader, fileExitItem, fileSaveItem, helpGetHelpItem;
		MenuItem insertImageItem, insertRectItem;

		menuBar = new Menu(shell, SWT.BAR);
		
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("File");
		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

	    fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveItem.setText("Save");
	    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileExitItem.setText("Exit");

		// add a color picker for the font color
		MenuItem fileFontColorItem = new MenuItem(fileMenu, SWT.PUSH);
		fileFontColorItem.setText("Font Color");
		fileFontColorItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ColorDialog cd = new ColorDialog(shell);
				cd.setText("Choose a font color");
				cd.setRGB(statusLabel.getForeground().getRGB());
				RGB newColor = cd.open();
				if (newColor != null) {
					statusLabel.setForeground(new Color(display, newColor));
					fontColor = newColor;
				}
			}
		});

		// add a font picker
		MenuItem fileFontItem = new MenuItem(fileMenu, SWT.PUSH);
		fileFontItem.setText("Font Family");
		fileFontItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FontDialog fd = new FontDialog(shell);
				fd.setText("Choose a font");
				fd.setRGB(statusLabel.getForeground().getRGB());
				fd.setFontList(statusLabel.getFont().getFontData());
				FontData newFont = fd.open();
				if (newFont != null) {
					statusLabel.setFont(new Font(display, newFont));
					fontName = newFont.getName();
					updateUI();
				}
			}
		});

		// add a font size picker
		MenuItem fileFontSizeItem = new MenuItem(fileMenu, SWT.PUSH);
		fileFontSizeItem.setText("Font Size");
		fileFontSizeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FontDialog fd = new FontDialog(shell);
				fd.setText("Choose a font size");
				fd.setRGB(statusLabel.getForeground().getRGB());
				fd.setFontList(statusLabel.getFont().getFontData());
				FontData newFont = fd.open();
				if (newFont != null) {
					statusLabel.setFont(new Font(display, newFont));
					fontSize = newFont.getHeight();
					updateUI();
				}
			}
		});

		// add a color picker for the rectangle fill color
		MenuItem fileRectFillColorItem = new MenuItem(fileMenu, SWT.PUSH);
		fileRectFillColorItem.setText("Rectangle Fill Color");
		fileRectFillColorItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ColorDialog cd = new ColorDialog(shell);
				cd.setText("Choose a rectangle fill color");
				cd.setRGB(rectangleFillColor);
				RGB newColor = cd.open();
				if (newColor != null) {
					rectangleFillColor = newColor;
				}
			}
		});

		// add a color picker for the rectangle border color
		MenuItem fileRectBorderColorItem = new MenuItem(fileMenu, SWT.PUSH);
		fileRectBorderColorItem.setText("Rectangle Border Color");
		fileRectBorderColorItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				ColorDialog cd = new ColorDialog(shell);
				cd.setText("Choose a rectangle border color");
				cd.setRGB(rectangleBorderColor);
				RGB newColor = cd.open();
				if (newColor != null) {
					rectangleBorderColor = newColor;
				}
			}
		});

		// add a dark mode toggle
		MenuItem fileDarkModeItem = new MenuItem(fileMenu, SWT.PUSH);
		fileDarkModeItem.setText("Dark Mode");
		fileDarkModeItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (darkMode) {
					darkMode = false;
				} else {
					darkMode = true;
				}
				updateUI();
			}
		});

		insertMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		insertMenuHeader.setText("Insert");
		insertMenu = new Menu(shell, SWT.DROP_DOWN);
		insertMenuHeader.setMenu(insertMenu);

	    insertImageItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertImageItem.setText("Image");
	    insertRectItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertRectItem.setText("Rectangle");

		insertRectItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (rectangleEndPosition != null) {
					lexiControl.addRectGlyph(rectangleEndPosition, rectangleBorderColor, rectangleFillColor);
					rectangleEndPosition = null;
					updateUI();
					return;
				}
				lexiControl.addRectGlyph(rectangleBorderColor, rectangleFillColor); 
				updateUI();
			}
		});

		// when the user clicks insert image, open a file dialog
		insertImageItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.jpg", "*.png", "*.gif"});
				String result = dialog.open();
				if (result != null) {
					lexiControl.addImageGlyph(result);
					updateUI();
				}
			}
		});


	    helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    helpMenuHeader.setText("Help");
	    helpMenu = new Menu(shell, SWT.DROP_DOWN);
	    helpMenuHeader.setMenu(helpMenu);

	    helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
	    helpGetHelpItem.setText("Get Help");
	    
	    fileExitItem.addSelectionListener(new SelectionListener() {
	    	public void widgetSelected(SelectionEvent event) {
	    		shell.close();
	    		display.dispose();
	    	}
	    	public void widgetDefaultSelected(SelectionEvent event) {
	    		shell.close();
	    		display.dispose();
	    	}
	    });

		// when the user clicks save, open a file dialog, and save the canvas as a png
		// TODO: this part does not work. Image doesn't save. Fix it.
	    fileSaveItem.addSelectionListener(new SelectionListener() {
	    	public void widgetSelected(SelectionEvent event) {
	    		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
	    		dialog.setFilterExtensions(new String[] {"*.png"});
	    		String result = dialog.open();
	    		if (result != null) {
					// save the canvas as a png
					// do it here
					Image image = new Image(display, canvasWidth, canvasHeight);
					GC gc = new GC(image);
					gc.copyArea(image, 0, 0);
					ImageLoader loader = new ImageLoader();
					loader.data = new ImageData[] {image.getImageData()};
					loader.save(result, SWT.IMAGE_PNG);
					gc.dispose();
					image.dispose();
	    		}
	    	}
	    	public void widgetDefaultSelected(SelectionEvent event) {
	    		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
	    		dialog.setFilterExtensions(new String[] {"*.png"});
	    		String result = dialog.open();
	    		if (result != null) {
	    			// save the canvas as a png
					// do it here
					Image image = new Image(display, canvasWidth, canvasHeight);
					GC gc = new GC(image);
					gc.copyArea(image, 0, 0);
					ImageLoader loader = new ImageLoader();
					loader.data = new ImageData[] {image.getImageData()};
					loader.save(result, SWT.IMAGE_PNG);
					gc.dispose();
					image.dispose();
	    		}
	    	}    		
	    });

	    helpGetHelpItem.addSelectionListener(new SelectionListener() {
	    	public void widgetSelected(SelectionEvent event) {
	    	}
	    	public void widgetDefaultSelected(SelectionEvent event) {
	    	}	    		
	    });	
	    
        Menu systemMenu = Display.getDefault().getSystemMenu();
        MenuItem[] mi = systemMenu.getItems();
        mi[0].addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event){
            	System.out.println("About");
            }
        });
	    
	    shell.setMenuBar(menuBar);
	      
		//---- event loop
		shell.open();
		while( !shell.isDisposed())
			if(!display.readAndDispatch()){}
		display.dispose();
	} 

	/**
	 * updateUI
	 */
	public void updateUI()
	{
		System.out.println("updateUI");
		canvas.redraw();
	}
	
	/**
	 * setCurrentDoc
	 */
	public void setCurrentDoc(LilLexiDoc cd) { currentDoc = cd; }
	/**
	 * setController
	 */
	public void setController(LilLexiControl lc) { lexiControl = lc; }
	
}


