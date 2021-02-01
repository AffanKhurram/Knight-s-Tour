import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Tour
{
	public static void main (String args[]) throws IOException
	{	
		
		JFrame frame = new JFrame("Knight's Toour");
		Knight knight = new Knight(frame);
		knight.getStart();
		knight.solveTour();
		knight.displayBoard();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(knight);
		frame.setSize(800, 800);
		frame.setVisible(true);
		
		knight.drawKnight();
		
	}
}

class Knight extends JComponent
{
	private int board[][];			// stores the sequence of knight moves
	private int startRow;			// row location where the knight starts
	private int startCol;			// col location where the knight starts
	private int rowPos;			// current row position of the knight
	private int colPos;			// current col position of the knight
	private int moves;			// number of location visited by the knight
	private DecimalFormat output;
	private boolean graphics;
	private ArrayList<Coordinate> coordinates;
	private JFrame f;
	private int knightx;
	private int knighty;
	private Image image;
	boolean hasEntered;
	private boolean[] visited;
	
	final private int ACCESS[][] = {{0,0,0,0,0,0,0,0,0,0,0,0},
 						 			{0,0,0,0,0,0,0,0,0,0,0,0},
     					 			{0,0,2,3,4,4,4,4,3,2,0,0},
     					 			{0,0,3,4,6,6,6,6,4,3,0,0},
     					 			{0,0,4,6,8,8,8,8,6,4,0,0},
     					 			{0,0,4,6,8,8,8,8,6,4,0,0},
     					 			{0,0,4,6,8,8,8,8,6,4,0,0},
     					 			{0,0,4,6,8,8,8,8,6,4,0,0},
     					 			{0,0,3,4,6,6,6,6,4,3,0,0},
     					 			{0,0,2,3,4,4,4,4,3,2,0,0},
     					 			{0,0,0,0,0,0,0,0,0,0,0,0},
     					 			{0,0,0,0,0,0,0,0,0,0,0,0}}; 	  					 			  
   	         	  					 			  
   	         
	public Knight(JFrame frame)
	// constructor used to initializes the data attributes
	{   
		board = new int[8][8];
		moves = 1;
		output = new DecimalFormat("00");
		coordinates = new ArrayList();
		f = frame;
		image = null;
  		try {
    		image = ImageIO.read(new File("knight.png"));
  		} catch (Exception e) {
  			
  		}
  		hasEntered = false;
	}
    
    public void paintComponent(Graphics g) {
    	if (hasEntered) {
    		drawBoard(g);
  			g.drawImage(image, knighty, knightx, 75, 75, null, null);
    	}
    	
    }
    
    public void drawKnight () {
    	for (int i=0; i<moves; i++) {
    		visited[i] = true;
    		if (i==0 || i == moves-1) {
    			knightx = coordinates.get(i).x*75+10;
    			knighty = coordinates.get(i).y*75+10;
    			hasEntered = true;
    		}
    		else {
    			int x = -(coordinates.get(i-1).x - coordinates.get(i).x) * 75;
    			int y = -(coordinates.get(i-1).y - coordinates.get(i).y) * 75;
    			for (int j=0; j<4; j++) {
    				knightx = (coordinates.get(i-1).x * 75 + 10) + x/(4-j);
    				knighty = (coordinates.get(i-1).y * 75 + 10) + y/(4-j);
    				try {
    					Thread.sleep(10);
    				} catch (InterruptedException e) {
    					
    				}
    				f.repaint();
    			}
    		}
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
    		f.repaint();
    	}
    }
    
    public void drawBoard(Graphics g) {
    	boolean white = true;
    	int val = 0;
    	for (int i=0; i<8; i++) {
    		for(int j=0; j<8; j++) {
    			if (val%2 == 1) {
    				g.setColor(Color.black);
    				if (startRow == i || startCol == j)
    					white = false;
    			}
    			else {
    				g.setColor(Color.white);
    				if (startRow == i || startCol == j)
    					white = true;
    			}
    			g.fillRect(i*75+10, j*75+10, 75, 75);
    			val++;
    		}
    		val++;
    	}
    	for (int i=0; i<visited.length; i++) {
    		white = !white;
    		if (!visited[i])
    			break;
    		g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
    		g.setColor(white? Color.white : Color.black);
    		g.drawString(String.valueOf(i+1), coordinates.get(i).y*75+30, coordinates.get(i).x *75 +75);
    	}
    }
    
	public void getStart() throws IOException
	// input method to get starting row and col from keyboard entry
	{    
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the row: ");
		startRow = in.nextInt()-1;
		System.out.print("Enter the col: ");
		startCol = in.nextInt()-1;
		rowPos = startRow;
		colPos = startCol;
		board[rowPos][colPos] = 1;
		coordinates.add(new Coordinate(rowPos, colPos));
		knightx = rowPos * 75 + 10;
		knighty = colPos * 75 + 10;
	}
   
	public void displayBoard()
	// displays the chessboard after the tour is concluded
	{
		for (int[] i : board) {
			for (int j : i) {
				System.out.print(output.format(j) + " ");
			}
			System.out.println();
		}
		System.out.println("The knight has toured " + moves + " positions");
	}  

	private boolean getMove()
	// computes the next available knight's move.  Alters RowPos and ColPos and
	// returns true if move is possible, otherwise returns false
	{
		boolean possible = false;
		Coordinate least = new Coordinate(2, 2);
		boolean og = true;
		
		int[][] path = {
			{-2,1},
			{-1,2},
			{1,2},
			{2,1},
			{2,-1},
			{1,-2},
			{-1,-2},
			{-2,-1}
		};
		
		for (int[] i : path) {
			
			if (ACCESS[2+rowPos+i[0]][2+colPos+i[1]] != 0 && board[rowPos+i[0]][colPos+i[1]] == 0) {
				if (ACCESS[2+rowPos+i[0]][2+colPos+i[1]] < ACCESS[2+least.x][2+least.y]) {
					least = new Coordinate(rowPos+i[0], colPos+i[1]);
					possible = true;
				}
				if (ACCESS[2+rowPos+i[0]][2+colPos+i[1]] == ACCESS[2+least.x][2+least.y]) {
					if (og && ACCESS[2+least.x][2+least.y] == 8) {
						og = false;
						least = new Coordinate(rowPos+i[0], colPos+i[1]);
					}
					else {
						least = checkBoard(least, new Coordinate(rowPos+i[0], colPos+i[1]));
					}
					
					possible = true;
				}
			}
		}
		
		rowPos = least.x;
		colPos = least.y;
		
		return possible;
		
	}
	
	private Coordinate checkBoard (Coordinate n1, Coordinate n2) {
		int[][] path = {
			{-2,1},
			{-1,2},
			{1,2},
			{2,1},
			{2,-1},
			{1,-2},
			{-1,-2},
			{-2,-1}
		};
		
		int s1=0;
		int s2=0;
		for (int[] i : path) {
			if (ACCESS[n1.x + i[0] + 2][n1.y + i[1] + 2] != 0 && board[n1.x + i[0]][n1.y + i[1]] == 0) {
				s1 += ACCESS[n1.x + i[0] + 2][n1.y + i[1] + 2];
			}
		}
		
		for (int[] i : path) {
			if (ACCESS[n2.x + i[0] + 2][n2.y + i[1] + 2] != 0 && board[n2.x + i[0]][n2.y + i[1]] == 0) {
				s2 += ACCESS[n2.x + i[0] + 2][n2.y + i[1] + 2];
			}
		}
			
		if (s1 < s2)
			return n1;
		else 
			return n2;
			
	}
	
	public void solveTour()
	// primary method that drives the knight's tour solution
	{
		while (getMove()) {
			coordinates.add(new Coordinate(rowPos, colPos));
			moves++;
			board[rowPos][colPos] = moves;
		}
		visited = new boolean[moves];
	}
	

}

class Coordinate {
	public int x,y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals (Coordinate c) {
		return c.x == x && c.y == y;
	}
}