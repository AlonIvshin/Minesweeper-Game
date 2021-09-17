package minesPlus;

import java.util.Set;
import java.util.TreeSet;

public class Mines {
	private Spot[][] board;
	private int height;
	private int width;
	private int serialCount=0;
	private boolean showAll = false;
	
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		Set<Integer> minesSet = new TreeSet<Integer>(); //sets of the mines location
		for (int n=0;n<numMines;n++) {
			Integer randomNum = (int)(Math.random() * (height*width) + 1); //generate random location
			if (minesSet.contains(randomNum)) //if already exist in the set
				n--; //try again
			else
				minesSet.add(randomNum); //insert random location to the set
		}
		board = new Spot[height][width];
		for (int i=0; i<height ;i++) { //for each column
			for (int j=0;j<width;j++) { //for each row
				board[i][j]=new Spot(serialCount++);
			}
		}
		for (int i=0; i<height ;i++) { //for each column
			for (int j=0;j<width;j++) { //for each row
				if (minesSet.contains(board[i][j].serial())) //if location is in set of random mine locations
					addMine(i,j); //add mine to that location
			}
		}

		//this.numMines = numMines;
		
	}
	
	public boolean addMine(int i, int j) {
		if (!(i >= height || j >= width || i<0 ||j<0)&&(board[i][j].getMine()==false)) {
			board[i][j].setMine();
	//		numMines++;
			for (int dirX = -1; dirX < 2; dirX++) { //all 8 possible directions
				for (int dirY = -1; dirY < 2; dirY++) {
					if (!(dirX == 0 && dirY == 0)) //dx ,dy =0 is not a step, avoiding infinity loop.
						if (j-dirX >= 0 && i-dirY >= 0 && i-dirY < height && j-dirX < width) //if the place is in the board
							if(board[i-dirY][j-dirX].getMine()==false) //if not a mine
								board[i-dirY][j-dirX].addValue(); //add one to the area
				}
			}
			return true;
		}
		return false; //if i,j out of bound or mine already exist return false
	}
	
	public boolean open(int i, int j) {
		if (board[i][j].getMine()) {
	//		board[i][j].open();
			return false; //if mine return false
		}
		else {
			board[i][j].open(); //mark as open
			if (board[i][j].getValue()==0) { //if has no mines around it
				for (int dirX = -1; dirX < 2; dirX++) { //all 8 possible directions
					for (int dirY = -1; dirY < 2; dirY++) {
						if (!(dirX == 0 && dirY == 0)) //dx ,dy =0 is not a step, avoiding infinity loop.
							if (j-dirX >= 0 && i-dirY >= 0 && i-dirY < height && j-dirX < width) //if the spot is in the board
								if(board[i-dirY][j-dirX].isOpen()==false) //if not a mine and not open
									open(i-dirY,j-dirX); //open recursively
					}
				}
			}
			return true;
		}
	}
	
	public void toggleFlag(int x, int y) {
		board[x][y].toggleFlag();
	}
	
	public boolean isDone() {
		for (int i=0; i<height ;i++) { //for each column
			for (int j=0;j<width;j++) { //for each row
				if (board[i][j].getMine()==false && board[i][j].isOpen()==false) { //if the spot is not mine and not open
					return false; //return false
				}
			}
		}
		return true;
	}
	
	public String get(int i, int j) { 
		if (board[i][j].isOpen()||showAll==true) { //if open
			if (board[i][j].getMine()) { //if mine
				return "X";
			}
			else { //if not mine
				int value = board[i][j].getValue();
				if (value==0) {
					return " ";
				}
				else
					return ""+value;
			}
		}
		else { //if not open
			if(board[i][j].getFlag()) { //if flagged
				return "F";
			}
			else //if not flagged
				return ".";
		}
	}
	
	public void setShowAll(boolean showAll) {
		this.showAll=showAll;
	}
	
	public boolean getShowAll() {
		return showAll;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i=0; i<height ;i++) { //for each column
			for (int j=0 ; j<width;j++) { //for each row
				str.append(get(i,j));
			}
			str.append("\n");
		}
		return str.toString();
	}

	public static void main(String[] args) {
		Mines m = new Mines(40, 10, 5);
		m.addMine(0, 1);
		m.addMine(2, 0);
		System.out.println(m);
		m.open(2, 0);
		System.out.println(m);
		m.toggleFlag(0, 1);
		System.out.println(m);
	}
}
