package minesPlus;

public class Spot {
	private int value=0;
	private boolean flag;
	private boolean mine;
	private boolean open;
	private int serialNum;
	
	public Spot(int serialNum) {
		value = 0;
		flag = false;
		mine = false;
		open = false;
		this.serialNum = serialNum;
	}
	
	public void setMine() {
		mine = true;
	}
	
	public void toggleFlag() {
		if (flag==false)
			flag=true;
		else
			flag=false;
	}
	
	public void open() {
		open = true;
	}
	
	public boolean getFlag() {
		return flag;
	}
	
	public void addValue() {
		value+=1;
	}
	
	public boolean getMine() {
		return mine;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public int getValue() {
		return value;
	}
	
	public int serial() {
		return serialNum;
	}
}
