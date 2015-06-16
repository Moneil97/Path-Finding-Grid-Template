import java.awt.Color;


public enum CellTypes {

	START(Color.green), END(Color.red), BARRIER(Color.black), EMPTY(Color.white);
	
	Color color;
	
	CellTypes(Color c){
		color = c;
	}
	
}
