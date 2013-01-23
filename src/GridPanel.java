import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class GridPanel extends Panel implements Runnable{
	private static final long serialVersionUID = 1L;
	private  Color cellColor = Color.GRAY;
	private  Color lineColor = Color.LIGHT_GRAY;
	private  Color aliveCellColor = new Color(128,249,245);
	
	private  Graphics bufferGraphics;
	private  Image emptyGrid;
	
	public GridPanel() {
		super();
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent evt){
				int x = evt.getX();
				int y = evt.getY(); 
				
				x = x/GameOfLife.cellSize;
				y = y/GameOfLife.cellSize;
				
				if(x<0 || x>GameOfLife.gridWidth-1 || y<0 || y>GameOfLife.gridHeight-1) return;
				
				if ((evt.getModifiers() & InputEvent.ALT_MASK) != 0) turnOff(x,y);
				else turnOn(x,y);
			}
			private void turnOn(int x, int y) {
				GameOfLife.grid[x][y].turnOn();
			}
			private void turnOff(int x, int y) {
				GameOfLife.grid[x][y].turnOff();
			}
		});
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent evt){
				int x = evt.getX();
				int y = evt.getY(); 
				
				x = x/GameOfLife.cellSize;
				y = y/GameOfLife.cellSize;
				
				if ((evt.getModifiers() & InputEvent.ALT_MASK) != 0) turnOff(x,y);
				else turnOn(x,y);
			}
			private void turnOn(int x, int y) {
				GameOfLife.grid[x][y].turnOn();
			}
			private void turnOff(int x, int y) {
				GameOfLife.grid[x][y].turnOff();
			}
		});
	}

	public Dimension getPreferredSize(){
		return new Dimension(
				GameOfLife.gridWidth*GameOfLife.cellSize,
				GameOfLife.gridHeight*GameOfLife.cellSize);
	}

	public void paint(Graphics g){
		emptyGrid = createImage(getPreferredSize().width,getPreferredSize().height);
		bufferGraphics = emptyGrid.getGraphics();
		drawEmptyGrid(bufferGraphics);
		drawAliveCells(bufferGraphics);
		g.drawImage(emptyGrid,0,0,this);
	}
	public void update(Graphics g)
    {	
        paint(g); 
    }

	private void drawAliveCells(Graphics g){
		int x,y;
		Color oldColor = g.getColor();
		for (x=0;x<GameOfLife.gridWidth;x++){
			for (y=0;y<GameOfLife.gridHeight;y++){
				g.setColor(aliveCellColor);
				if (GameOfLife.grid[x][y].isOn()){ 				
					g.fillRect(x*GameOfLife.cellSize, y*GameOfLife.cellSize, GameOfLife.cellSize, GameOfLife.cellSize);
					g.setColor(lineColor);
					g.drawRect(x*GameOfLife.cellSize, y*GameOfLife.cellSize, GameOfLife.cellSize, GameOfLife.cellSize);
				}
			}
		} 
		g.setColor(oldColor);
	}
	private void drawEmptyGrid(Graphics g){
		int x,y;
		g.setColor(cellColor);
		for (x=0;x<GameOfLife.gridWidth;x++){
			for (y=0;y<GameOfLife.gridHeight;y++){
				g.setColor(cellColor);
				g.fillRect(x*GameOfLife.cellSize, y*GameOfLife.cellSize, GameOfLife.cellSize, GameOfLife.cellSize);
				g.setColor(lineColor);
				g.drawRect(x*GameOfLife.cellSize, y*GameOfLife.cellSize, GameOfLife.cellSize, GameOfLife.cellSize);
			}
		}
	}
	
	public void run() {
		do{
			repaint();
			try{
				Thread.sleep(33);
				
			}catch (InterruptedException exc){}
		} while(true);
	}

}
