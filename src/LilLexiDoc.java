/**
 * AUTHOR(S):	Ryan Rizzo
 * CLASS:		CSC 335
 * FILE:		LilLexiDoc.java
 * DATE:		10/13/22
 */
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * LilLexiDoc
 */
public class LilLexiDoc 
{
	private LilLexiUI ui;
	private List<Glyph> glyphs;
	private HashMap<Integer, Character> undoMemo;
	private HashMap<Integer, Character> redoMemo;
	private String currFont;
	private String currColor;
	private int currSize;
	private int index;
	
	
	/**
	 * Constructor
	 */
	public LilLexiDoc() {
		glyphs = new ArrayList<Glyph>();
		undoMemo = new HashMap<Integer, Character>();
		redoMemo = new HashMap<Integer, Character>();
		index = 0;
		currFont = "Courier";
		currColor = "Black";
		currSize = 12;
	}
	
	/*
	 * setUI
	 */
	public void setUI(LilLexiUI ui) {this.ui = ui;}

	/* - - - - - - ADD - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for adding a single Glyph
	 * to the document.
	 */
	public void add(char c) {
		// Add the character to the list
		glyphs.add(index, new Glyph(c, currFont, currSize, currColor));
		
		// Add the location & character to the undo tracker
		undoMemo.put(index, c);
		
		// Update
		ui.updateUI();
		index++;
	}
	/* - - - - - - REMOVE - - - - - - - - - - - - - - - - - - - - - - - 
	 * This function is responsible for removing a single Glyph
	 * to the document.
	 */
	public void remove() {
		// Check that there is at least one character
		if (index >= 1) {
			// Remove the character from the list
			glyphs.remove(index - 1);
			
			// Remove action from the undo tracker
			undoMemo.remove(index - 1);
			
			// Update
			ui.updateUI();
			index--;
		}
	}
	
	/* - - - - - - UNDO - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for triggering the internal function
	 * for the undo button. Here we "remove" whatever most previous action
	 * the user made.
	 */
	public void undo() {
		// Check if there are undo actions to take
		if (undoMemo.get(index - 1) != null) {
			// Remove the character from the document
			glyphs.remove(index - 1);
		
			// Change undo & redo trackers
			char c = undoMemo.get(index - 1);
			undoMemo.remove(index - 1);
			redoMemo.put(index - 1, c);
			
			// Update
			ui.updateUI();
			index--;
		}
	}
	
	/* - - - - - - REDO - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for triggering the internal function
	 * for the redo button. Here we "replace" whatever action was most
	 * previously undone.
	 */
	public void redo() {
		// Check if there are redo actions to take
		if (redoMemo.get(index) != null) {
			// Add the character back to the document
			char c = redoMemo.get(index);
			glyphs.add(index, new Glyph(c, currFont, currSize, currColor));
		
			// Change undo & redo trackers
			redoMemo.remove(redoMemo.size() - 1);
				undoMemo.put(index, c);
		
			// Update
			ui.updateUI();
			index++;
		}
	}
	
	/* - - - - - - SET INDEX - - - - - - - - - - - - - - - - - - - - - - - 
	 * This function is responsible for setting the index (or the location
	 * of the cursor), for use in the document
	 * 
	 * @param i, the integer which represents the current index of the cursor
	 */
	public void setIndex(int i) {
		index = i;
	}
	
	/* - - - - - - GET CURR FONT - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current font being used
	 * 
	 * @return currFont, the String which represents the font being used
	 */
	public String getCurrFont() {
		return this.currFont;
	}
	
	/* - - - - - - GET CURR COLOR - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current color being used
	 * 
	 * @return currColor, the String which represents the color being used
	 */
	public String getCurrColor() {
		return this.currColor;
	}
	
	/* - - - - - - GET CURR SIZE - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current font size being used
	 * 
	 * @return currColor, the int which represents the font size being used
	 */
	public int getCurrSize() {
		return this.currSize;
	}
	
	/* - - - - - - SET CURR FONT - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current font value
	 * 
	 * @param font, the String which represents the font we want to use
	 */
	public void setCurrFont(String font) {
		this.currFont = font;
	}
	
	/* - - - - - - SET CURR COLOR - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current color value
	 * 
	 * @param color, the String which represents the color we want to use
	 */
	public void setCurrColor(String color) {
		this.currColor = color;
	}
	
	/* - - - - - - SET CURR SIZE - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current font size value
	 * 
	 * @param size, the int which represents the font size we want to use
	 * 
	 */
	public void setCurrSize(int size) {
		this.currSize = size;
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
	private String font;
	private String color;
	private int size;
	
	public Glyph(char c, String font, int size, String color) {
		this.c = c;
		this.font = font;
		this.size = size;
		this.color = color;
	}

	public char getChar() {
		return c;
	}
	
	public String getFont() {
		return font;
	}
	
	public int getSize() {
		return size;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setChar(char c) {
		this.c = c;
	}
}





