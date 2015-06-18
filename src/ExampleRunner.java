import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ExampleRunner extends JFrame implements Runnable{
	
	JPanel canvas;
	PathFinder pathFinder;
	
	public ExampleRunner(){
		
		int width = 800, height = 600;
		int rows = height/50;
		int cols = width/50;
		
		Cell[][] cells = new Cell[rows][cols];
		
		int size = 50;
		
		for (int r=0; r < rows; r++)
			for (int c=0; c < cols; c++)
				cells[r][c] = new Cell(c*size, r*size, r,c, size);
		
		for (int i=0; i < rows-1;i++)
			cells[i][1].setType(CellTypes.BARRIER);
		for (int i=1; i < rows;i++)
			cells[i][3].setType(CellTypes.BARRIER);
		for (int i=0; i < rows/2;i++)
			cells[i][8].setType(CellTypes.BARRIER);
		
		pathFinder = new PathFinder(cells, cells[0][0], cells[rows-1][cols-1], true, false);
		
		this.add(canvas = new JPanel(){
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				for (Cell[] row : cells)
					for (Cell cell : row)
						if (cell != null) cell.draw(g);
			}
		});
		
		canvas.setPreferredSize(new Dimension(cols*50, rows*50));
		
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				
				List<Cell> fastest;
				
				for (Cell[] row : cells)
					for (Cell cell : row)
						if (cell.click(e)){
							//if (!pathFinder.quickHasPath())
							if ((fastest = pathFinder.calculateShortestPath()) != null){
								update(fastest);
								
							}
							else{
								cell.click(e);
							}
							break;
						}
				
				
				
			}
			
			private void update(List<Cell> fastest){
				for (Cell[] row : cells)
					for (Cell cell : row)
						cell.setPartOfPath(false);
				
				for (Cell cell : fastest){
					cell.setPartOfPath(true);
				}
			}
			
		});
		
		for (Cell cell : pathFinder.calculateShortestPath()){
			cell.setPartOfPath(true);
		}
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		new Thread(this).start();
	}

	public static void say(Object s){
		System.out.println(s);
	}

	public static void main(String[] args) {
		new ExampleRunner();
	}
	
	@Override
	public void run() {
		while (true){
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
//	private void print2D(Object[][] array){
//	for (Object[] row : array)
//		System.out.println(Arrays.toString(row));
//}
}
