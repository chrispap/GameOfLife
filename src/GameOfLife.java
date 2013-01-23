import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GameOfLife {
		public static final int cellSize = 6;
		public static final int gridWidth = 100;
		public static final int gridHeight = 60;
		
		public static int speed = 4;
		
		public static Thread 	gameLoop;
		public static boolean 	running;
		public static Cell[][] 	grid;
		public static Cell[][] 	nextGrid;
		public static GridPanel	gridPanel;
		public static Panel 	infoPanel;
		public static TextField speedPanel;
		public static Button 	goButton;
		public static Button 	nextButton;
		public static Button 	clearButton;
		public static Button 	slowerButton;
		public static Button 	fasterButton;
	
	public static void main(String[] args) {
		initGrid();
		nextGrid = new Cell[gridWidth][gridHeight];
		
		Frame win = new Frame("Game of life");;
		
		Panel buttonPanel = new Panel(new GridLayout(0,1));
		infoPanel = new Panel(){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				super.paint(g);
				g.setColor(running?Color.RED:Color.GRAY);
				g.fillOval(10,5,22,22);
			}
		};
		win.add((gridPanel = new GridPanel()));
		win.add(infoPanel,BorderLayout.NORTH);
		win.add(buttonPanel,BorderLayout.WEST);
		
		infoPanel.add((speedPanel = new TextField()));
		Label helpLabel1 = new Label("Click -> Bring a cell to life:::::::::::::::::::Alt Click -> Kill it");
		helpLabel1.setBounds(250, 4, 350, 28);
		infoPanel.add(helpLabel1);
		
		infoPanel.setLayout(null);
		infoPanel.setSize(300, 30);
		speedPanel.setEditable(false);
		speedPanel.setBounds(45,5,180,20);
		speedPanel.setBackground(Color.BLACK);
		speedPanel.setForeground(Color.GREEN);
		updateSpeedPanel();
		
		buttonPanel.add((goButton = new Button("Play / Pause")));
		goButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				toggleRunningState();
			}
		});
		
		buttonPanel.add((nextButton = new Button("Generate Next")));
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				generateNext();
			}
		});
		
		buttonPanel.add((clearButton = new Button("RESET")));
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				initGrid();
			}
		});
		
		buttonPanel.add((fasterButton = new Button("FASTER !!!")));
		fasterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				speedUp();
			}
		});
		
		buttonPanel.add((slowerButton = new Button("SLOW DOWN")));
		slowerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				slowDown();
			}
		});
		
		win.pack();
		win.setVisible(true);
		goButton.requestFocus();
		win.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		
		Thread refreshLoop = new Thread(gridPanel,"panel refresh loop");
		refreshLoop.setPriority(Thread.MIN_PRIORITY);
		refreshLoop.start();
		
		gameLoop = new Thread("game loop"){
			public void run(){
				do{
					while (!running){
						try {
							synchronized(this){
								this.wait();
							}
						} catch (InterruptedException e) {}
					}
					GameOfLife.generateNext();
					
					try {
						sleep(1000/speed-50);
					} catch (InterruptedException e) {}
					
				} while(true);
			}
		};
	}
	
	private static void toggleRunningState() {
		if (gameLoop.isAlive()) {
			running = running?false:true;
			synchronized(gameLoop){
				gameLoop.notifyAll();
			}
		}
		else {
			running=true;
			gameLoop.start();
		}
		infoPanel.repaint();
	}
	private static void updateSpeedPanel() {
		Float rate = (1000/(1000/(float)speed-50));
		String str = rate.toString();
		str = str.substring(0,Math.min(4, str.length()));
		speedPanel.setText("SPEED:  "+str+" states per sec");
	}
	private static void slowDown(){
		speed = speed-2>0?speed-2: 1;
		updateSpeedPanel();
	}
	private static void speedUp() {
		speed = speed+2<=10?speed+2: 14;
		updateSpeedPanel();
	}
	public  static synchronized void initGrid(){
		grid = new Cell[gridWidth][gridHeight];
		int x,y;
		for (x=0;x<gridWidth;x++){
			for (y=0;y<gridHeight;y++){
				//COPY-PASTE SYNTHIKES 
				
				//ема асвето PATTERN
				//(x%(y+1))==3 || (x%(y+1))==11
				
				//лиа ояифомтиа цяаллг 10 йекиым стгм лесг 
				//y==gridHeight/2 && x>gridWidth/2-10 && x<=gridWidth/2+11
				
				//ема тетяацымо 20x20 сто йемтяо
				//y>gridHeight/2-10 && y<=gridHeight/2+10 && x>gridWidth/2-10 && x<=gridWidth/2+10
				
				//туваиа
				//Math.random()>0.66
				
				if (/*>>*/ y>gridHeight/2-10 && y<=gridHeight/2+10 && x>gridWidth/2-10 && x<=gridWidth/2+10 /*<<*/) {
					grid[x][y] = new Cell(x,y,grid,true);
				}
				else grid[x][y] = new Cell(x,y,grid,false);
			}
		}
	}
	private static synchronized void generateNext() {
		
		
		int x,y;
		for (x=0;x<gridWidth;x++){
			for (y=0;y<gridHeight;y++){
				nextGrid[x][y] = grid[x][y].nextState(nextGrid);
			}
		}
		Cell[][] tempGrid;
		//Swap grids
		tempGrid = grid;
		grid = nextGrid;
		nextGrid = tempGrid;
		//gridPanel.repaint();//To analamvanei to refresh loop tou gridPanel ayto stin trexousa ylopoiisi
	}

}

