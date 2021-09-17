package minesPlus;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MineControl implements Initializable {
	private Mines logic;
	private MineButton[][] buttonBoard;
	private GridPane grid;
	private Stage stage;
	private MediaPlayer backgroundPlayer;
	private MediaPlayer loopPlayer;
	private MediaPlayer player;
	private boolean isLooping = false;
	private boolean bagroundPlayed = false;
	private Media youWin = new Media(new File("src/minesPlus/youWin.mp3").toURI().toString()); 
	private Media winner = new Media(new File("src/minesPlus/winner.mp3").toURI().toString()); 
	private Media explosion = new Media(new File("src/minesPlus/explosion.mp3").toURI().toString());
	private Media loser = new Media(new File("src/minesPlus/loser.mp3").toURI().toString());
	private Media background = new Media(new File("src/minesPlus/Roi Amar - In The Eyes Of The Observer.mp3").toURI().toString());
    @FXML
    private HBox root;

    @FXML
    private VBox settingsPart;

    @FXML
    private GridPane minerPic;

    @FXML
    private Button reset;

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField mines;

    @FXML
    private StackPane stack;

    @FXML
    void resetClick(MouseEvent event) {
    	Integer heightVal, widthVal, minesVal;
    	try {
    		heightVal = Integer.parseInt(height.getText().toString());
		} catch (Exception e) {
			heightVal = null;
		}
    	try {
    		widthVal = Integer.parseInt(width.getText().toString());
		} catch (Exception e) {
			widthVal = null;
		}
    	try {
    		minesVal = Integer.parseInt(mines.getText().toString());
		} catch (Exception e) {
			minesVal = null;
		}
    	if (heightVal==null || widthVal==null || minesVal==null) {
    		popUp("Error","Some of the values are not defined","Please make sure you enter Width Height and Mines...");
    		return;
    	}
    	if (heightVal*widthVal<minesVal+1) { //too many mines  
    		popUp("Error","Too many Mines!!","Please enter less mines...");
    		return;
    	}
    	if (widthVal>60 || widthVal<3) {
    		popUp("Error","Width out of bound!!","Min width allowed is 3, Max width allowed is 60, Please try again...");
    		return;
    	}
    	if (heightVal>40 || heightVal<3) {
    		popUp("Error","Hight out of bound!!","Min height allowed is 3, Max height allowed is 40, Please try again...");
    		return;
    	}
    	play(background, true, true);
		stack.getChildren().remove(grid);
    	newGame(heightVal,widthVal,minesVal,getSize(heightVal,widthVal));
    	stack.getChildren().add(grid);
    	refreshStyle();
    	stage.setResizable(false);
		stage.sizeToScene();
    }
    
	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		newGame(10,10,10,35);
		stack.getChildren().add(grid);
		minerPic.getChildren().add(new ImageView(new Image(this.getClass().getResource("miner.png").toExternalForm())));
		play(background, true, true);
		refreshStyle();
	}
	
	public void newGame(int height, int width, int numMines, double buttonSize) {
		logic = new Mines(height,width,numMines);
		grid = new GridPane();
		buttonBoard = new MineButton[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				MineButton button = new MineButton(logic.get(i, j),i,j);
				buttonBoard[i][j] = button;
				button.setMinSize(buttonSize, buttonSize);
				button.setPrefSize(buttonSize, buttonSize);
				button.setOnMouseClicked(event -> {
					if (event.getButton() == MouseButton.PRIMARY) { //rightClick
						if (logic.open(button.iGet(), button.jGet())==false) {
							play(explosion, false, false);
							play(loser,false, false);
							endgamePopUP("LOSSER","you lost!\n\nLoser...");
							logic.setShowAll(true);
						}
					}
					else if (event.getButton() == MouseButton.SECONDARY) { //leftClick
						logic.toggleFlag(button.iGet(), button.jGet());
					}
				//	else if (event.getButton() == MouseButton.MIDDLE) { //rollClick
				//		System.out.println("middle");
				//	}
					if (logic.isDone()) {
						play(youWin, false, false);
						play(winner,true, false);
						endgamePopUP("WINNER","wow you are so good!!");
						logic.setShowAll(true);
					}
					refreshStyle();
				});
				grid.add(buttonBoard[i][j], j, i);
			}
		}
	}
	
	double getSize(int heightVal, int widthVal) { //sizing buttons to fit screen
		int switchKey=0;
		if (heightVal>27)
			switchKey+=1;
		if (widthVal>42)
			switchKey+=2;
		switch(switchKey) {
			case 1:
				return 35*(27.0/heightVal);
			case 2:
				return 35*(42.0/widthVal);
			case 3:
				double size,size2;
				size = 35*(27.0/heightVal);
				size2 = 35*(42.0/widthVal);
				return Math.min(size, size2);
			default:
				break;
		}
		return 35;
	}
	
	void popUp(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		ImageView imageView = new ImageView();
		imageView.setImage(new Image(this.getClass().getResource("oops.gif").toExternalForm()));
		alert.setGraphic(imageView);
		imageView.setFitHeight(100);
		imageView.setFitWidth(150);
		alert.getDialogPane().setMaxSize(350, 150);
		alert.setTitle(title);
		alert.setHeaderText("");
		alert.setContentText(header+"\n"+content);
		alert.show();
	}
	
	void endgamePopUP(String title, String content) {
		Alert alert = null;
		ImageView imageView = new ImageView();
		if (title=="WINNER") {
			alert = new Alert(Alert.AlertType.CONFIRMATION);
			imageView.setImage(new Image(this.getClass().getResource("wow.gif").toExternalForm()));
		}
		if (title=="LOSSER") {
			alert = new Alert(Alert.AlertType.WARNING);
			imageView.setImage(new Image(this.getClass().getResource("boom.gif").toExternalForm()));
		}
		imageView.setFitHeight(100);
		imageView.setFitWidth(150);
		alert.setGraphic(imageView);
		alert.getDialogPane().setMaxSize(150, 100);
		alert.setHeaderText("");
		alert.setTitle(title);
		alert.setContentText(content);
		alert.show();
	}
	
	void refreshStyle() {
		//play(background, true);
		ObservableList<Node> children = grid.getChildren(); //updating board
		for (Node child : children) {
			if (child instanceof MineButton) {
				buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setText("");
				//MineButton chButton = ((MineButton) child);
				String retString = logic.get(((MineButton) child).iGet(), ((MineButton) child).jGet()); //X=mine, F=flag, .=closed , " "-8 = open
				if (logic.getShowAll()) {
				
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setDisable(true);
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setOpacity(1);
				}
				if (retString==".") {
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setStyle("-fx-background-image: url('/minesPlus/rock.png');" + "-fx-background-size: 100%;" + "-fx-background-repeat: no-repeat;" + "-fx-background-color: transparent;" + "-fx-border-color: transparent;");
					continue;
				}
				else if (retString == " "){
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setDisable(true);
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setOpacity(0);
					continue;
				}
				else {
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setStyle("-fx-background-image: url('/minesPlus/" + retString + ".png');" + "-fx-background-size: 100%;" + "-fx-background-repeat: no-repeat;" + "-fx-background-color: transparent;" + "-fx-border-color: transparent;");
					continue;
				}
				
			}
		}
	}
	
	void play(Media sound, boolean loop, boolean background) {
		if (background) {
			if (isLooping) {
				loopPlayer.stop();
				isLooping = false;
			}
			if (!bagroundPlayed) {
				backgroundPlayer = new MediaPlayer(sound);
				backgroundPlayer.setVolume(0.1);
				backgroundPlayer.setOnEndOfMedia(new Runnable() {
				       public void run() {
				    	   backgroundPlayer.seek(Duration.ZERO);
				       }
				   });
				backgroundPlayer.play();
				bagroundPlayed = true;
			}
			else {
				backgroundPlayer.play();
			}
		}
		else if (loop) {
			backgroundPlayer.pause();
			loopPlayer = new MediaPlayer(sound);
			loopPlayer.setVolume(0.1);
			loopPlayer.setOnEndOfMedia(new Runnable() {
			       public void run() {
			    	   loopPlayer.seek(Duration.ZERO);
			       }
			   });
			loopPlayer.play();
			isLooping = true;
		}
		else {
			player = new MediaPlayer(sound);
			player.setVolume(0.4);
			player.stop();
			player.play();
		}
	}
	

}
