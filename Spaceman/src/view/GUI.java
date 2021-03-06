package view;

import java.net.URL;
import java.util.ArrayList;

import controller.InterfaceController;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.text.*;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.media.AudioClip;

import javafx.event.EventHandler;

public class GUI {

	private InterfaceController controller;

	private final double SCENE_HEIGHT = 768;
	private final double SCENE_WIDTH = 1024.0;
	private Scene scene;
	
	//Nodes in this scene
	private Group menu;
	private Group subFrame;
	private Group multiMenu;
//	private Group setting;
	
	private AudioClip cycle;
	private AudioClip click;
	
	private DropShadow shadow;
	private GaussianBlur blur;
	
	//Main Menu Options (Eg. Story, Classic etc)
	private int option = 0;
	private ArrayList<ImageView> optionList;
	
	//Multiplayer Menu Controls
	private int multiplayerSel=0;
	private ArrayList<ImageView> multiplayerControls;
	private ArrayList<ImageView> joinedIndicator;	

	public GUI (InterfaceController controller) {
		this.controller = controller;
		
		shadow = new DropShadow(50, Color.YELLOW);
		blur = new GaussianBlur();

		//Initialise Sound Clips for the GUI
		URL url = this.getClass().getResource("sound/laser1.wav");
		click = new AudioClip(url.toString());
		click.setVolume(0.4);

		url = this.getClass().getResource("sound/sound1.wav");
		cycle = new AudioClip(url.toString());
		
		AnchorPane root = new AnchorPane(); //Parent Node
		
		//Child node of parent containing main menu options
		menu = addComponents();
		root.getChildren().add(menu);
		
		//Child node of parent node containing the multiplayer menu options
		subFrame = addSubFrame();	//consider moving this to child of menu instead if setting is scrapped
		subFrame.setVisible(false);
		root.getChildren().add(subFrame);
		
		setKeyInput(root);

		BackgroundImage bg = new BackgroundImage(new Image(getClass().getResourceAsStream("bg/test.png")), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
		root.setBackground(new Background(bg));
		
		scene = new Scene(root,SCENE_WIDTH,SCENE_HEIGHT);	
	}

	private Group addComponents() {
		Group group = new Group();
		
		double minDistFromNodes = 50.0;
		optionList = new ArrayList<ImageView>();
		
		Image title = new Image(getClass().getResourceAsStream("bg/title.png"));
		ImageView titleView = new ImageView(title);
		titleView.setX((SCENE_WIDTH-title.getWidth())*0.5);
		titleView.setY(minDistFromNodes);
		titleView.setScaleX(0.75);
		titleView.setScaleY(0.75);
		group.getChildren().add(titleView);
		
		titleView.setFocusTraversable(true);	//not specific to title view i think

		Image btn0 = new Image(getClass().getResourceAsStream("bg/btn0.png"));
		ImageView btn0View = new ImageView(btn0);
		btn0View.setEffect(shadow); //Highlight initial option
		optionList.add(btn0View);

		Image btn1 = new Image(getClass().getResourceAsStream("bg/btn1.png"));
		ImageView btn1View = new ImageView(btn1);
		optionList.add(btn1View);

		Image btn2 = new Image(getClass().getResourceAsStream("bg/btn2.png"));
		ImageView btn2View = new ImageView(btn2);
		optionList.add(btn2View);

		Image btn3 = new Image(getClass().getResourceAsStream("bg/btn3.png"));
		ImageView btn3View = new ImageView(btn3);
		optionList.add(btn3View);

		Image btn4 = new Image(getClass().getResourceAsStream("bg/btn4.png"));
		ImageView btn4View = new ImageView(btn4);
		optionList.add(btn4View);

		Image btn5 = new Image(getClass().getResourceAsStream("bg/btn5.png"));
		ImageView btn5View = new ImageView(btn5);
		optionList.add(btn5View);
		
		
		//Setup all the buttons on the scene
		for (int i = 0; i < optionList.size(); i++) {
			optionList.get(i).setScaleX(0.4);
			optionList.get(i).setScaleY(0.4);
			//Centres the buttons on the x-axis
			optionList.get(i).setX((SCENE_WIDTH-optionList.get(i).getLayoutBounds().getWidth())*0.5);
			//Calculates the y-axis of each button
			optionList.get(i).setY((minDistFromNodes*(i+2)+title.getHeight()*titleView.getScaleY()+optionList.get(i).getLayoutBounds().getHeight()*i*0.4));
			group.getChildren().add(optionList.get(i));
		}
		return group;
	}
	
	private void setKeyInput(AnchorPane root) {
		root.setOnKeyPressed(new EventHandler <KeyEvent> () {
			@Override
			public void handle(KeyEvent event) {
				
				if (event.getCode() == KeyCode.UP ) {
					//Cycles options upwards based on which frame is active
					if (multiMenu.isVisible()) {
						if (multiplayerSel < 1) {
							cycle.play();
							multiplayerControls.get(multiplayerSel).setEffect(null);
							multiplayerSel++;
						}	
					} else {
						if (option> 0) {
							cycle.play();
							optionList.get(option).setEffect(null);
							option--;
						}
					}	
					
				} else if (event.getCode() == KeyCode.DOWN) {
					//Cycles options downwards based on which frame is active
					if (multiMenu.isVisible()) {
						if (multiplayerSel > 0) {
							cycle.play();
							multiplayerControls.get(multiplayerSel).setEffect(null);
							multiplayerSel--;
						}
						
					} else {
						if (option < 5) {
							cycle.play();
							optionList.get(option).setEffect(null);
							option++;
						}
						
					}

				} else if (event.getCode() == KeyCode.ENTER) {
					//Executes a command based on which frame and option is active
					click.play();
					//Opens up multiplayer menu
					if (!multiMenu.isVisible() & option == 2) {
						subFrame.setVisible(true);
						multiMenu.setVisible(true);
						
						menu.setEffect(blur);
						multiplayerSel = 0;
						multiplayerControls.get(0).setEffect(shadow);
						
					//Closes multiplayer menu
					} else if (multiMenu.isVisible() & multiplayerSel == 1) {
						subFrame.setVisible(false);
						multiMenu.setVisible(false);
						
						menu.setEffect(null);
						
					//Generates level when at least one ghost player has joined in mulitplayer screen
					} else if (multiMenu.isVisible() & multiplayerSel == 0){
						if (joinedIndicator.get(1).isVisible() || joinedIndicator.get(2).isVisible()) {
							if (joinedIndicator.get(1).isVisible()) {
								//enable ghost1 player
								controller.executeProcess(69);
							}
							if (joinedIndicator.get(2).isVisible()) {
								//enable ghost2 player
								controller.executeProcess(420);
							}
							controller.executeProcess(option);
							subFrame.setVisible(false);
							multiMenu.setVisible(false);
							menu.setEffect(null);
						}
						
					//When no specific case is selected, then calls controller as normal
					} else {
						controller.executeProcess(option); 
					}
					joinedIndicator.get(1).setVisible(false);
					joinedIndicator.get(2).setVisible(false);
				
				//Join/Leave as first ghost
				} else if (event.getCode() == KeyCode.W||event.getCode() == KeyCode.A||event.getCode() == KeyCode.S||event.getCode() == KeyCode.D) {
					cycle.play();
					if (joinedIndicator.get(1).isVisible()) {
						joinedIndicator.get(1).setVisible(false);
					} else {
						joinedIndicator.get(1).setVisible(true);
					}
				//Join/Leave as second ghost
				} else if (event.getCode() == KeyCode.I||event.getCode() == KeyCode.J||event.getCode() == KeyCode.K||event.getCode() == KeyCode.L) {
					cycle.play();
					if (joinedIndicator.get(2).isVisible()) {
						joinedIndicator.get(2).setVisible(false);
					} else {
						joinedIndicator.get(2).setVisible(true);
					}
				}
				
				//Highlights options
				optionList.get(option).setEffect(shadow);
				multiplayerControls.get(multiplayerSel).setEffect(shadow);
			}

		});
	}

	//Frame for multiplayer menu or player settings
	private Group addSubFrame() {
		Group group = new Group();
		
		Rectangle frame = new Rectangle(900,600);
		frame.setFill(Color.BLACK);
		frame.setStroke(Color.WHITE);
		frame.setStrokeWidth(2.0);
		frame.setArcHeight(15);
		frame.setArcWidth(15);
		frame.setX((SCENE_WIDTH-frame.getLayoutBounds().getWidth())*0.5);
		frame.setY((SCENE_HEIGHT-frame.getLayoutBounds().getHeight())*0.5);
		group.getChildren().add(frame);
		
		multiMenu = addMultiplayerOptions(frame);
		multiMenu.setVisible(false);
		group.getChildren().add(multiMenu);
		
		//might be scrapped
//		setting = addSettings(frame);
//		setting.setVisible(false);
//		group.getChildren().add(setting);
		
		return group;
	}
	
	//Setup multiplayer setup display
	private Group addMultiplayerOptions(Rectangle frame) {
		Group group = new Group();
		
		ArrayList<ImageView> multiplayerOptions = new ArrayList<ImageView>();
		multiplayerControls = new ArrayList<ImageView>();	//Stores play/close objects
		joinedIndicator = new ArrayList<ImageView>();		//Stores objects to indicate join/leave
		
		Text label = new Text("Press One of the Listed Keys to Join/Leave");
		label.setFont(Font.font(30));
		label.setFill(Color.WHITE);
		label.setX(frame.getX()+50);
		label.setY(frame.getY()+label.getLayoutBounds().getHeight()+10);
		group.getChildren().add(label);
		
		Image player1 = new Image(getClass().getResourceAsStream("bg/choose1.png"));
		ImageView player1Disp = new ImageView(player1);
		multiplayerOptions.add(player1Disp);
		
		Image player2 = new Image(getClass().getResourceAsStream("bg/choose2.png"));
		ImageView player2Disp = new ImageView(player2);
		multiplayerOptions.add(player2Disp);
		
		Image player3 = new Image(getClass().getResourceAsStream("bg/choose3.png"));
		ImageView player3Disp = new ImageView(player3);
		multiplayerOptions.add(player3Disp);
		
		Image tick = new Image(getClass().getResourceAsStream("misc/tick2.png"));
		
		//Setup for the images indicating whether a player has joined or not
		for (int i = 0; i < multiplayerOptions.size(); i++) {
			multiplayerOptions.get(i).setX(40*(i+1) + player1Disp.getLayoutBounds().getWidth()*i+frame.getX());
			multiplayerOptions.get(i).setY(100+label.getLayoutBounds().getHeight());
			group.getChildren().add(multiplayerOptions.get(i));
			
			ImageView joined = new ImageView(tick);
			joined.setX(multiplayerOptions.get(i).getX());
			joined.setY(multiplayerOptions.get(i).getY());
			joinedIndicator.add(joined);
			group.getChildren().add(joined);
		}
		joinedIndicator.get(1).setVisible(false);
		joinedIndicator.get(2).setVisible(false);
		
		Image play = new Image(getClass().getResourceAsStream("misc/playBtn.png"));
		ImageView playBtn = new ImageView(play);
		playBtn.setX((SCENE_WIDTH-playBtn.getLayoutBounds().getWidth())*0.5);
		playBtn.setY(frame.getY()+frame.getLayoutBounds().getHeight()-play.getHeight()-40);
		playBtn.setEffect(shadow);
		multiplayerControls.add(playBtn);
		group.getChildren().add(playBtn);
		
		Image close = new Image(getClass().getResourceAsStream("misc/close2.png"));
		ImageView closeBtn = new ImageView(close);
		closeBtn.setScaleX(0.3);
		closeBtn.setScaleY(0.3);
		closeBtn.setX((frame.getLayoutBounds().getWidth()-90));
		closeBtn.setY(frame.getY()-50);
		multiplayerControls.add(closeBtn);
		group.getChildren().add(closeBtn);
		
		return group;
	}
	
	public Scene returnScene() {
		return scene;
	}

}
