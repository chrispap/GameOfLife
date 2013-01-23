public class Cell{

	private boolean ON;
	private final int x,y;
	private Cell[][] parentGrid;
	
	public Cell(int x, int y, Cell[][] parentGrid,boolean alive) {
		super();
		this.x = x;
		this.y = y;
		this.parentGrid = parentGrid;
		ON = alive;
	}
	public void turnOn(){
		ON = true;
	}
	public void turnOff(){
		ON = false;
	}
	public boolean isOn(){
		return ON;
	}
	public Cell nextState(Cell[][] nextParentGrid) {
		int sum = countAliveNeighboors();
		
		if(sum>3) return new Cell(x,y,nextParentGrid,false);
		if(sum<2) return new Cell(x,y,nextParentGrid,false);
		if(sum==2)return new Cell(x,y,nextParentGrid,ON);
		if(sum==3)return new Cell(x,y,nextParentGrid,true);
		return null;
		
	}
	private int countAliveNeighboors() {
		int counter=0;
		int nbX;
		int nbY;
		
		for(nbX=x-1 ; nbX<=x+1 ; nbX++){
			for(nbY=y-1 ; nbY<=y+1 ; nbY++){
				if (nbX==x && nbY==y) continue;
				try{
					if (parentGrid[nbX][nbY].isOn()) counter++;
				} catch(ArrayIndexOutOfBoundsException exc){
				}
			}
		}
		
		return counter;
	}
}