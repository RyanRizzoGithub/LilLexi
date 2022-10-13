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
 * PURPOSE:
 * This class is responsible for housing all information about that current
 * state of the document
 * 
 * METHODS:
 * + LilLexiDoc():						Constructor
 * + setUI(LilLexiUI):					void
 * + add(char):							void
 * + remove(char):						void
 * + undo():							void
 * + redo():							void
 * + getUI():							LilLexiUI
 * + getCurrFont():						String
 * + getCurrcolor():					String
 * + getCurrSize():						String
 * + getCurrSizeMargin():				String
 * + getCurrEdgeMargin():				String
 * + getCurrIndex():					int
 * + getDepth():						int
 * + getRowHeight():					int
 * + getRows():							int
 * + getGlyphs():						List<Glyph>
 * + getImages():						String[][]
 * + getRects():						String[][]
 * + getLines():						String[][]
 * + getCircles():						String[][]
 * + getTriangles():					String[][]
 * + getNumNewline():					int
 * + getRowWidth():						int
 * + getColumnWidth():					int
 * + setCurrFont(String):				void
 * + setCurrColor(String):				void
 * + setCurrSize(String):				void
 * + setCurrSizeMargin(String):			void
 * + setCurrEdgeMargin(String):			void
 * + setCurrIndex(int):					void
 * + setColumnWidth(int):				void
 * + setRowsWidth(int):					void
 * + setRowHeight(int):					void
 * + setDepth(int):						void
 * + setRowWidth(int):					void
 * + setRows(int):						void
 * + setPages(int):						void
 * + addImage(String[]):				void
 * + addRect(String[]):					void
 * + addLine(String[]):					void
 * + addCircle(String[]):				void
 * + addTriangle(String[]):				void
 * + addNewline():						void
 * + removeNewline():					void
 */
public class LilLexiDoc {
	private LilLexiUI ui;
	private List<Glyph> glyphs;
	private HashMap<Integer, Character> undoMemo;
	private HashMap<Integer, Character> redoMemo;
	private String currFont;
	private String currColor;
	private String currSideMargin;
	private String currEdgeMargin;
	private int currSize;
	private int index;
	private int depth;
	private int columnWidth;
	private int rowHeight;
	private int rows;
	private int pages;
	private int rowsWidth;
	private int numNewline;
	private int[] rowWidth;
	private String[][] images;
	private String[][] rects;
	private String[][] lines;
	private String[][] circles;
	private String[][] triangles;
	
	
	/**
	 * Constructor
	 */
	public LilLexiDoc() {
		glyphs = new ArrayList<Glyph>();
		glyphs.add(new Glyph('|', currFont, currSize, currColor));

		undoMemo = new HashMap<Integer, Character>();
		redoMemo = new HashMap<Integer, Character>();
		index = 0;
		currSideMargin = "1/2''";
		currEdgeMargin = "1/2''";
		currFont = "Courier";
		currColor = "Black";
		currSize = 12;
		numNewline = 0;
		depth = 0;		
		columnWidth = -1;
		rowHeight = -1;
		rows = 0;
		rowsWidth = 0;
		pages = 2;
		
		rowWidth = new int[1000];
		for (int i=0; i<1000; i++) {
			rowWidth[i] = -1;
		}
		
		images = new String[100][5];
		rects = new String[100][5];
		lines = new String[100][4];
		circles = new String[100][4];
		triangles = new String[100][6];
		for (int i=0; i<100; i++) {
			String[] tempImage = new String[3];
			tempImage[0] = "";
			tempImage[1] = "";
			tempImage[2] = "";
			String[] tempRect = new String[5];
			tempRect[0] = "";
			tempRect[1] = "";
			tempRect[2] = "";
			tempRect[3] = "";
			tempRect[4] = "";
			String[] tempLineCircle = new String[4];
			tempLineCircle[0] = "";
			tempLineCircle[1] = "";
			tempLineCircle[2] = "";
			tempLineCircle[3] = "";
			String[]  tempTriangle = new String[6];
			tempTriangle[0] = "";
			tempTriangle[1] = "";
			tempTriangle[2] = "";
			tempTriangle[3] = "";
			tempTriangle[4] = "";
			tempTriangle[5] = "";
			images[i] = tempImage;
			rects[i] = tempRect;
			lines[i] = tempLineCircle;
			circles[i] = tempLineCircle;
			triangles[i] = tempTriangle;
		}
	}
	
	/* - - - - - - SET UI - - - - - - - - - - - - - - - - - - - - - - 
	 * This function lets us set the user interface for this document
	 * 
	 * @param ui, the LilLexiUI object being used
	 */
	public void setUI(LilLexiUI ui) {
		this.ui = ui;
	}

	/* - - - - - - ADD - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for adding a single Glyph
	 * to the document.
	 * 
	 * @param c, the character being added to the document
	 */
	public void add(char c) {
		// Add the character to the list
		if (glyphs.size() > 0) {
			glyphs.remove(index);
		}
		glyphs.add(index, new Glyph(c, currFont, currSize, currColor));
		
		
		glyphs.add(index + 1, new Glyph('|', currFont, currSize, currColor));
		
		// Add the location & character to the undo tracker
		undoMemo.put(index, c);
		
		// Update
		index++;
		ui.updateUI();
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
	
	/* - - - - - - REDO - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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
	
	/* - - - - - - GET UI - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current user interface
	 * object
	 * 
	 * @return ui, the LilLexiUI object
	 */
	public LilLexiUI getUI() {
		return this.ui;
	}
	
	/* - - - - - - GET CURR FONT - - - - - - - - - - - - - - - - - - - - - - - -
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
	
	/* - - - - - - GET CURR SIDE MARGIN - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current size of the side margins
	 * 
	 * @return currSideMargin, the int which represents the size of the side margins
	 */
	public String getCurrSideMargin() {
		return this.currSideMargin;
	}
	
	/* - - - - - - GET CURR EDGE MARGIN - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the current size of the edge margins
	 * 
	 * @return currEdgeMargin, the int which represents the size of the edge margins
	 */
	public String getCurrEdgeMargin() {
		return this.currEdgeMargin;
	}
	
	/* - - - - - - GET CURR INDEX - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the index of the cursor
	 * 
	 * @return index, the int which represents the location of the cursor
	 */
	public int getCurrIndex() {
		return this.index;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public int getRowHeight() {
		return this.rowHeight;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	
	/* - - - - - - GET GLYPHS - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for returning the list of Glyphs on the document
	 * 
	 * @return glyphs, the list of Glyph objects
	 */
	public List<Glyph> getGlyphs(){
		return glyphs;
	}
	
	public String[][] getImages(){
		return images;
	}
	
	public String[][] getRects(){
		return rects;
	}
	
	public String[][] getLines(){
		return lines;
	}
	
	public String[][] getCircles(){
		return circles;
	}
	
	public String[][] getTriangles(){
		return triangles;
	}
	
	public int getNumNewline() {
		return numNewline;
	}
	
	public int[] getRowWidth() {
		return rowWidth;
	}
	public int getRowsWidth() {
		return this.rowsWidth;
	}
	public int getColumnWidth() {
		return this.columnWidth;
	}
	public int getPages() {
		return this.pages;
	}
	
	
	/* - - - - - - SET CURR FONT - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current font value
	 * 
	 * @param font, the String which represents the font we want to use
	 */
	public void setCurrFont(String font) {
		this.currFont = font;
	}
	
	/* - - - - - - SET CURR COLOR - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * This function is responsible for setting the current color value
	 * 
	 * @param color, the String which represents the color we want to use
	 */
	public void setCurrColor(String color) {
		this.currColor = color;
	}
	
	/* - - - - - - SET CURR SIZE - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current font size value
	 * 
	 * @param size, the int which represents the font size we want to use
	 * 
	 */
	public void setCurrSize(int size) {
		this.currSize = size;
	}
	
	/* - - - - - - SET CURR SIDE MARGIN - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current size of the side margins
	 * 
	 * @param size, the string which represents the size of the size margins
	 */
	public void setCurrSideMargin(String size) {
		this.currSideMargin = size;
	}
	
	/* - - - - - - SET CURR EDGE MARGIN - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the current size of the edge margins
	 * 
	 * @param size, the string which represents the size of the edge margins
	 */
	public void setCurrEdgeMargin(String size) {
		this.currEdgeMargin = size;
	}
	
	/* - - - - - - SET CURR INDEX - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * This function is responsible for setting the index (or the location
	 * of the cursor), for use in the document
	 * 
	 * @param i, the integer which represents the current index of the cursor
	 */
	public void setCurrIndex(int i) {
		if (i > glyphs.size()) {
			i = glyphs.size();
		} else {
			index = i;
		}
	}
	
	public void setColumnWidth(int i) {
		this.columnWidth = i;
	}
	
	public void setRowsWidth(int i) {
		this.rowsWidth = i;
	}
	
	public void setRowHeight(int i) {
		this.rowHeight = i;
	}
	
	public void setDepth(int i) {
		this.depth = i * (pages);
	}
	public void setRowWidth(int row, int i) {
		this.rowWidth[row] = i;
	}
	public void setRows(int i) {
		this.rows = i;
	}
	
	public void addImage(String[] imageInfo) {
		
	}
	public void addRect(String[] rectInfo) {
		
	}
	public void addLine(String[] lineInfo) {
		
	}
	public void addCircle(String[] circleInfo) {
		
	}
	public void addTriangle(String[] triangleInfo) {
		
	}
	public void addNewline() {
		numNewline++;
	}
	public void removeNewline() {
		numNewline--;
	}
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
	
	public void setFont(String font) {
		this.font = font;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
}




