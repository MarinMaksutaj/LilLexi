/*
*  Course: CSC335
*  Description: UI class for the LilLexi application.
*/
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import java.util.List;


/**
 * LilLexiUI main class.
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
	private RGB fontColor;
	private boolean darkMode;
	private String fontName;
	private int fontSize;
	private boolean spellCheck;
	
	/**
	 * Constructor for LilLexiUI.
	 */
	public LilLexiUI() 
	{
		Display.setAppName("Lil Lexi");
		display = new Display();  
		shell = new Shell(display);
	    shell.setText("Lil Lexi");
		shell.setSize(810,900);
		shell.setLayout( new GridLayout());
		// set default fontColor to blue
		fontColor = new RGB(0,0,255);
		darkMode = false;
		fontName = "Courier";
		fontSize = 24;
		spellCheck = false;
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
		canvas = new Canvas(upperComp, SWT.V_SCROLL);
		canvasWidth = 800;
		canvasHeight = 800;
		canvas.setSize(canvasWidth, canvasHeight);

		canvas.addPaintListener(e -> {
		    int vSelection = canvas.getVerticalBar().getSelection();
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

			List<Glyph> glyphs = currentDoc.getComposition().getGlyphs();
			for (Glyph g : glyphs) {
				if (g instanceof CharGlyph) {
					CharGlyph cg = (CharGlyph) g;
					// set the font color
					e.gc.setForeground(new Color(display, cg.getColor()));
					Font f = new Font(display, cg.getFont(), cg.getSize(), SWT.BOLD);
					e.gc.setFont(f);
					// if the char is gramatically incorrect, draw a red line under it
					if (!cg.getGramaticallyCorrect() && spellCheck) { 
						e.gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
						e.gc.drawLine(cg.getPos().getX(), cg.getPos().getY() + cg.getSize() - vSelection + 5, 
								cg.getPos().getX() + cg.getSize(), cg.getPos().getY() + cg.getSize() - vSelection + 5);
					}
					e.gc.drawString(""+cg.getChar(), cg.getPos().getX(), cg.getPos().getY() + 5 - vSelection); // 5 is so that chars don't overlap with end of rect
				}
				if (g instanceof RectGlyph) {	
				    System.out.println("rect");
					RectGlyph rg = (RectGlyph) g;
					// set the rectangle fill color
					e.gc.setBackground(new Color(display, rg.getFillColor()));
					// set the rectangle border color
					e.gc.setForeground(new Color(display, rg.getBorderColor()));
					// make border width 2
					e.gc.setLineWidth(2); // without this the border would be default width 1 but not visible when filled for some reason. TODO: decide what to do
					e.gc.drawRectangle(rg.getPos().getX(), rg.getPos().getY() + 10 - vSelection , rg.getWidth(), rg.getHeight());
					e.gc.fillRectangle(rg.getPos().getX(), rg.getPos().getY() + 10 - vSelection , rg.getWidth(), rg.getHeight());
					// reset fill color to transparent
					if (darkMode) {
						e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
					} else {
						e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE)); 
					}
				}
				
				if (g instanceof TriangleGlyph) {
				    System.out.println("triangle");
				    TriangleGlyph tg = (TriangleGlyph) g;
                    // set the rectangle fill color
                    e.gc.setBackground(new Color(display, tg.getFillColor()));
                    // set the rectangle border color
                    e.gc.setForeground(new Color(display, tg.getBorderColor()));
                    e.gc.setLineWidth(2); 
                    int x1= tg.getPos().getX() + (tg.getWidth()/2);
                    int y1= tg.getPos().getY() + 10;
                    
                    int x2= tg.getPos().getX();
                    int y2= tg.getPos().getY() + 10 + tg.getHeight();
                    
                    int x3= tg.getPos().getX() + tg.getWidth();
                    int y3= tg.getPos().getY() + 10 + tg.getHeight();
                    
                    e.gc.drawPolygon(new int[] {x1,y1 - vSelection ,x2,y2 - vSelection ,x3,y3 - vSelection });
                    e.gc.fillPolygon(new int[] {x1,y1 - vSelection ,x2,y2 - vSelection ,x3,y3 - vSelection });
                    
                    
                    // reset fill color to transparent
                    if (darkMode) {
                        e.gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
                    } else {
                        e.gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE)); 
                    }
                }
				
				
				if (g instanceof CircleGlyph) {
				    CircleGlyph cg = (CircleGlyph) g;
                    // set the rectangle fill color
                    e.gc.setBackground(new Color(display, cg.getFillColor()));
                    // set the rectangle border color
                    e.gc.setForeground(new Color(display, cg.getBorderColor()));
                    e.gc.setLineWidth(2);
                    e.gc.drawOval(cg.getPos().getX(), cg.getPos().getY() + 10 - vSelection , cg.getRadius(), cg.getRadius());
                    e.gc.fillOval(cg.getPos().getX(), cg.getPos().getY() + 10 - vSelection , cg.getRadius(), cg.getRadius());
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
					e.gc.drawImage(ig.getImage(), 0, 0, ig.getImage().getBounds().width, ig.getImage().getBounds().height, ig.getPos().getX(), ig.getPos().getY() + 10 - vSelection , ig.getWidth(), ig.getHeight());
				}
			}
		});	
		
        
        canvas.addKeyListener(new KeyListener() {
        	public void keyPressed(KeyEvent e) {
				// if key is SHIFT or CTRL or ALT or CAPS_LOCK or NUM_LOCK or SCROLL_LOCK or INSERT, ignore
				if (e.keyCode == SWT.SHIFT || e.keyCode == SWT.CTRL || e.keyCode == SWT.ALT || e.keyCode == SWT.CAPS_LOCK || e.keyCode == SWT.NUM_LOCK || e.keyCode == SWT.SCROLL_LOCK || e.keyCode == SWT.INSERT) {
					return;
				}
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

        final ScrollBar vBar = canvas.getVerticalBar();       
        Rectangle size = canvas.getBounds();
        vBar.setMaximum(size.height/2);
        vBar.setMinimum(0);
		
        vBar.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	        canvas.redraw();
	      }
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
		Menu menuBar, toolsMenu, insertMenu, helpMenu;
		MenuItem toolsMenuHeader, insertMenuHeader, helpMenuHeader, toolsExitItem, helpGetHelpItem;
		MenuItem insertImageItem, insertRectItem, insertCircleItem, insertTriangleItem;

		menuBar = new Menu(shell, SWT.BAR);
		
		toolsMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		toolsMenuHeader.setText("Tools");
		toolsMenu = new Menu(shell, SWT.DROP_DOWN);
		toolsMenuHeader.setMenu(toolsMenu);

		// add a color picker for the font color
		MenuItem toolsFontColorItem = new MenuItem(toolsMenu, SWT.PUSH);
		toolsFontColorItem.setText("Font Color");
		toolsFontColorItem.addListener(SWT.Selection, new Listener() {
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
		MenuItem toolsFontItem = new MenuItem(toolsMenu, SWT.PUSH);
		toolsFontItem.setText("Font Family");
		toolsFontItem.addListener(SWT.Selection, new Listener() {
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
		MenuItem toolsFontSizeItem = new MenuItem(toolsMenu, SWT.PUSH);
		toolsFontSizeItem.setText("Font Size");
		toolsFontSizeItem.addListener(SWT.Selection, new Listener() {
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
		
		// add a dark mode toggle
        MenuItem toolsDarkModeItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsDarkModeItem.setText("Dark Mode");
        toolsDarkModeItem.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (darkMode) {
                    darkMode = false;
                } else {
                    darkMode = true;
                }
                updateUI();
            }
        });
        
        MenuItem toolsUndoItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsUndoItem.setText("Undo");
        toolsUndoItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                lexiControl.undo();
                updateUI();
            }
        });

        MenuItem toolsRedoItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsRedoItem.setText("Redo");
        toolsRedoItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                lexiControl.redo();
                updateUI();
            }
        });
        
        MenuItem spellCheckItem = new MenuItem(toolsMenu, SWT.PUSH);
        spellCheckItem.setText("Spell Checker");
     // Spell check menu item. Toggle spell check on and off.
        spellCheckItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (spellCheck) {
                    spellCheck = false;
                } else {
                    spellCheck = true;
                }
                updateUI();
            }
        });
		
		toolsExitItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsExitItem.setText("Exit");

		insertMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		insertMenuHeader.setText("Insert");
		insertMenu = new Menu(shell, SWT.DROP_DOWN);
		insertMenuHeader.setMenu(insertMenu);

	    insertImageItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertImageItem.setText("Image");
	    insertRectItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertRectItem.setText("Rectangle");
	    insertCircleItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertCircleItem.setText("Circle");
	    insertTriangleItem = new MenuItem(insertMenu, SWT.PUSH);
	    insertTriangleItem.setText("Triangle");

		insertRectItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			    InputDialog dialog = new InputDialog(shell);
                dialog.open("shape");
                
                Integer size = dialog.getSize();
                RGB borderColor = dialog.getBorderColor();
                if(borderColor==null) {
                    borderColor = new RGB(0,0,0);
                }
                
                RGB fillColor = dialog.getFillColor();
                if(fillColor==null) {
                    fillColor = new RGB(255,255,255);
                }
				lexiControl.addRectGlyph(borderColor, fillColor, size); 
				updateUI();
			}
		});
		
		insertTriangleItem.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                InputDialog dialog = new InputDialog(shell);
                dialog.open("shape");
                Integer size = dialog.getSize();
                
                RGB borderColor = dialog.getBorderColor();
                if(borderColor==null) {
                    borderColor = new RGB(0,0,0);
                }
                
                RGB fillColor = dialog.getFillColor();
                if(fillColor==null) {
                    fillColor = new RGB(255,255,255);
                }
                
                lexiControl.addTriangleGlyph(borderColor, fillColor, size); 
                updateUI();
            }
        });
		
		insertCircleItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                InputDialog dialog = new InputDialog(shell);
                dialog.open("shape");
                Integer size = dialog.getSize();
                
                RGB borderColor = dialog.getBorderColor();
                if(borderColor==null) {
                    borderColor = new RGB(0,0,0);
                }
                
                RGB fillColor = dialog.getFillColor();
                if(fillColor==null) {
                    fillColor = new RGB(255,255,255);
                }
                
                lexiControl.addCircleGlyph(borderColor, fillColor, size); 
                updateUI();
            }
        });


		// when the user clicks insert image, open a file dialog
		insertImageItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
			    InputDialog sizeDialog = new InputDialog(shell);
                sizeDialog.open("image");
                Integer size = sizeDialog.getSize();
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.jpg", "*.png", "*.gif"});
				String result = dialog.open();
				if (result != null) {
					lexiControl.addImageGlyph(result, size);
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
	    
	    toolsExitItem.addSelectionListener(new SelectionListener() {
	    	public void widgetSelected(SelectionEvent event) {
	    		shell.close();
	    		display.dispose();
	    	}
	    	public void widgetDefaultSelected(SelectionEvent event) {
	    		shell.close();
	    		display.dispose();
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
	
	class InputDialog extends Dialog {
	    Integer size;
	    RGB borderColor;
	    RGB fillColor;

	    /**
	     * @param parent
	     */
	    public InputDialog(Shell parent) {
	      super(parent);
	    }

	    /**
	     * @param parent
	     * @param style
	     */
	    public InputDialog(Shell parent, int style) {
	      super(parent, style);
	    }
	    
	    public Integer getSize() {
	        return size;
	    }
	    
	    public RGB getBorderColor() {
            return borderColor;
        }
	    
	    public RGB getFillColor() {
            return fillColor;
        }
	    
	    /**
	     * Makes the dialog visible.
	     * 
	     * @return
	     */
	    public void open(String type) {
	      Shell parent = getParent();
	      final Shell shell =
	      new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	      shell.setText("Input size");

	      shell.setLayout(new GridLayout(2, true));

	      Label label = new Label(shell, SWT.NULL);
	      label.setText("Please enter size (Integer):");

	      final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
	      
	      
	      if(type.equals("shape")) {
	          
	          final Button buttonBorderColor = new Button(shell, SWT.PUSH);
	          buttonBorderColor.setText("Select Border Color");
	          
	          final Button buttonFillColor = new Button(shell, SWT.PUSH);
	          buttonFillColor.setText("Select Fill Color");
	          
	          ColorDialog cdBorder = new ColorDialog(shell);
	          cdBorder.setText("Choose a border color");
	          
	          ColorDialog cdFill = new ColorDialog(shell);
	          cdFill.setText("Choose a fill color");
	          
	          buttonBorderColor.addListener(SWT.Selection, new Listener() {
	              public void handleEvent(Event event) {
	                 borderColor = cdBorder.open();
	              }
	            });
	        
	          buttonFillColor.addListener(SWT.Selection, new Listener() {
	            public void handleEvent(Event event) {
	               fillColor = cdFill.open();
	            }
	          });
	          
	      }
	      
	      

	      final Button buttonOK = new Button(shell, SWT.PUSH);
	      buttonOK.setText("Ok");
	      buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	      
	      
          
	      text.addListener(SWT.Modify, new Listener() {
	        public void handleEvent(Event event) {
	          try {
	            size = Integer.valueOf(text.getText());
	            buttonOK.setEnabled(true);
	          } catch (Exception e) {
	            buttonOK.setEnabled(false);
	          }
	        }
	      });
	      

	      buttonOK.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	          shell.dispose();
	        }
	      });

	      
	      shell.addListener(SWT.Traverse, new Listener() {
	        public void handleEvent(Event event) {
	          if(event.detail == SWT.TRAVERSE_ESCAPE)
	            event.doit = false;
	        }
	      });

	      text.setText("");
	      shell.pack();
	      shell.open();

	      Display display = parent.getDisplay();
	      while (!shell.isDisposed()) {
	        if (!display.readAndDispatch())
	          display.sleep();
	      }

	    }

	  }

	
}


