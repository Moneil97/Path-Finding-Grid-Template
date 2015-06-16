import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JOptionPane;

public class PathFinder{

	Cell[][] grid;
	Cell end, start;
	List<Cell> shortest = new ArrayList<Cell>();
	
	public PathFinder(Cell[][] grid, Cell start, Cell end) {
		
		this.grid = grid;
		
		this.start = start;
		
		if (start == null){
			JOptionPane.showMessageDialog(null, "No Available Start Point");
			return;
		}
		
		start.setType(CellTypes.START);
		
		this.end = end;
		
		if (end == null){
			JOptionPane.showMessageDialog(null, "No Available End Point");
			return;
		}
		
		end.setType(CellTypes.END);
		
	}
	
	public List<Cell> calculateShortestPath(){
		
		for (Cell[] row : grid)
			for (Cell cell : row)
				cell.reset();
		
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();
		List<Cell> closed = new ArrayList<Cell>();
		
		long startTime = System.nanoTime();

		start.gCost = 0;
		start.hCost = getCostBetween(start, end);
		open.add(start);

		while (true){
		
			Cell current = open.poll();
			
			if (current == null){
				//JOptionPane.showMessageDialog(null, "No Path Found");
				return null;
			}
			
			closed.add(current);
			
			if (current.getType() == CellTypes.END){
				
				shortest.clear();
				
				Cell cell = end;
				shortest.add(end);
				
				do{
					
					cell = cell.parentCell;
					shortest.add(0, cell);

				}while(cell.parentCell != null);
				
				
				say("It took: " + ((System.nanoTime() - startTime)/1000000000.0) + " sec");
				
				return shortest;
			}
			
			
			for (int r=Math.max(0, current.getRow()-1); r <= Math.min(grid.length-1, current.getRow()+1); r++){
				for (int c=Math.max(0, current.getCol()-1); c <= Math.min(grid[r].length-1 ,current.getCol()+1); c++){
					
					Cell neighbor = grid[r][c];
					
					if (checkIfCorner(current, neighbor))
						continue;
					
					if ((neighbor.getType() == CellTypes.EMPTY || neighbor.getType() == CellTypes.END) && !closed.contains(neighbor)){
						
						neighbor.hCost = getCostBetween(neighbor, end);
						
						double newGCost = current.gCost + getCostBetween(current, neighbor);
						if (neighbor.gCost > newGCost){
							neighbor.gCost = newGCost;
							neighbor.parentCell = current;
							if (!open.contains(neighbor))
								open.add(neighbor);
						}
						
						open.remove(neighbor);
						neighbor.fCost = neighbor.gCost + neighbor.hCost;
						open.add(neighbor);
						
					}
				}
			}
			
		}
		
	}
	
	//gCost -dist from start //needs to get gCost of parent and add to it
	//hCost -dist to end //should stay same
	//fCost -total

	private boolean checkIfCorner(Cell current, Cell neighbor) {
		
		List<Cell> currentBarriers = new ArrayList<Cell>();
		
		for (Cell cell : getAdjacent(current)){
			if (cell.getType() == CellTypes.BARRIER)
				currentBarriers.add(cell);
		}
		
		if (currentBarriers.size() < 1)
			return false;
		
		List<Cell> neighborBarriers = new ArrayList<Cell>();
		
		for (Cell cell : getAdjacent(neighbor)){
			if (cell.getType() == CellTypes.BARRIER)
				neighborBarriers.add(cell);
		}
		
		if (neighborBarriers.size() < 1)
			return false;
		
		int similar = 0;
		
		for (Cell a : currentBarriers)
			for (Cell b : neighborBarriers)
				if (a == b){
					similar++;
					if (similar >= 1)
						return true;
					break;
				}
		
		return false;
		
	}
	
	private List<Cell> getAdjacent(Cell cell){
		List<Cell> cells = new ArrayList<Cell>();
		int row = cell.getRow(), col = cell.getCol();
		
		int r[] = {row,row,row+1,row-1};
		int c[] = {col+1,col-1,col,col};
			
			for (int i=0; i < 4; i++)
				if (r[i] >= 0 && r[i] < grid.length && c[i] >= 0 && c[i] < grid[r[i]].length)
					cells.add(grid[r[i]][c[i]]);
		
		return cells;
	}
	
	private double getCostBetween(Cell a, Cell b){
		int height = Math.abs(a.getRow() - b.getRow());
		int length = Math.abs(a.getCol() - b.getCol());
		double distance = Math.sqrt(length*length + height*height);
		return distance;
	}
	
	public boolean quickHasPath(){
		List<Cell> open = new ArrayList<Cell>();
		List<Cell> closed = new ArrayList<Cell>();
		
		long startTime = System.nanoTime();

		start.gCost = 0;
		start.hCost = getCostBetween(start, end);
		open.add(start);

		while (true){
		
			if (open.isEmpty())
				return false;
			
			Cell current = open.remove(0);

			closed.add(current);
			
			if (current.getType() == CellTypes.END){
				say("It took: " + ((System.nanoTime() - startTime)/1000000000.0) + " sec");
				return true;
			}
			
			for (int r=Math.max(0, current.getRow()-1); r <= Math.min(grid.length-1, current.getRow()+1); r++){
				for (int c=Math.max(0, current.getCol()-1); c <= Math.min(grid[r].length-1 ,current.getCol()+1); c++){
					
					Cell neighbor = grid[r][c];
					
					if (checkIfCorner(current, neighbor))
						continue;
					
					if ((neighbor.getType() == CellTypes.EMPTY || neighbor.getType() == CellTypes.END) && !closed.contains(neighbor)){
						
						if (!open.contains(neighbor))
							open.add(neighbor);
						
					}
				}
			}
			
		}
	}
	
	
//	private void print2D(Object[][] array){
//		for (Object[] row : array)
//		    System.out.println(Arrays.toString(row));
//	}
	
	public void say(Object s) {
		System.out.println(this.getClass().getName() + ": " + s);
	}

}
