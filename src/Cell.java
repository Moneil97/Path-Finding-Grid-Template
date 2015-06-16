import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Cell implements Comparable<Cell>{

	int x, y, size;
	private CellTypes type = CellTypes.EMPTY;
	private int row;
	private int col;
	double gCost = Double.MAX_VALUE, hCost = Double.MAX_VALUE, fCost = Double.MAX_VALUE;
	Cell parentCell;
	boolean drawable;
	boolean partOfPath = false;
	
	//Use if not drawing
	public Cell(int row, int col) {
		drawable = false;
		this.setRow(row);
		this.setCol(col);
	}
	
	//Use if drawing
	public Cell(int x, int y, int row, int col, int size) {
		drawable = true;
		this.x = x;
		this.y = y;
		this.setRow(row);
		this.setCol(col);
		this.size = size;
	}
	
	public void draw(Graphics2D g){
		
		if (!drawable){
			System.err.println(this + " cannot be drawn");
			return;
		}
		
		g.setColor(type.color);
		if (partOfPath && type==CellTypes.EMPTY)
			g.setColor(Color.orange);
		g.fillRect(x, y, size, size);
		g.setColor(Color.black);
		g.drawRect(x, y, size, size);
		
//		if (gCost < Double.MAX_VALUE)
//			g.drawString(String.format("%.1f", gCost), x+2, y+12);
//		
//		if (hCost < Double.MAX_VALUE)
//			g.drawString(String.format("%.1f", hCost), x+size-20, y+12);
//		
//		if (fCost < Double.MAX_VALUE)
//			g.drawString(String.format("%.1f", fCost), x+size/2-10, y+size/2);
		
	}
	
	public CellTypes getType() {
		return type;
	}

	public void setType(CellTypes type) {
		this.type = type;
	}
	
	public boolean contains(MouseEvent e) {
		return new Rectangle(x, y, size, size).contains(e.getPoint());
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public void setPartOfPath(boolean b) {
		partOfPath = b;
	}
	
	@Override
	public String toString() {
		
		return "Cell: [row: " + row + " col: " + col + "type: " + type + "]";
		
//		if (drawable)
//			return "Cell: [x: " + x + " y: " + y + " row: " + row + " col: " + col + " type: " + type + " gCost: " + gCost + " hCost: " + hCost + " fCost: " + fCost + "]";
//		else
//			return "Cell: [row: " + row + " col: " + col + " type: " + type + " gCost: " + gCost + " hCost: " + hCost + " fCost: " + fCost + "]";
	}
	
	public void reset(){
		gCost = hCost = fCost = Double.MAX_VALUE;
	}

	@Override
	public int compareTo(Cell other) {
		if (other.fCost > this.fCost)
			return -1;
		if (other.fCost < this.fCost)
			return 1;
		
		return 0;
	}

	public boolean click(MouseEvent e) {
		
		if (new Rectangle(x, y, size, size).contains(e.getPoint())){
			if (type == CellTypes.EMPTY)
				type = CellTypes.BARRIER;
			else if (type == CellTypes.BARRIER)
				type = CellTypes.EMPTY;
			return true;
		}
		return false;
		
	}

}