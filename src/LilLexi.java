/*
*  Course: CSC335
*  Description: Main class for the LilLexi application.
* 				This is where the document is connected to the GUI, and vice versa.
*/
public class LilLexi
{
	static private LilLexiDoc currentDoc = null;

	/**
	 * Main method for the LilLexi application.
	 * Author:  Marin Maksutaj
	 */
	public static void main(String args[])
	{
		if (currentDoc == null)
			currentDoc = new LilLexiDoc();
		LilLexiUI lexiUI = new LilLexiUI();
		currentDoc.setUI(lexiUI);
		lexiUI.setCurrentDoc( currentDoc );
		currentDoc.setUI(lexiUI);
		
		LilLexiControl lexiControl = new LilLexiControl( currentDoc );
		lexiUI.setController( lexiControl );
		
		lexiUI.start();
	} 
}


