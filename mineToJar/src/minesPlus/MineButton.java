package minesPlus;

import javafx.scene.control.Button;

public class MineButton extends Button{
	private int i;
	private int j;
	public MineButton(String txt, int i, int j) {
		super(txt);
		this.i=i;
		this.j=j;
		this.setPrefSize(35, 35);
	}
	public int iGet() {
		return i;
	}
	public int jGet() {
		return j;
	}
}
