package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Main extends Application {

	Stage primaryStage;
	Scene menu;
	Pane root;
	BorderPane center;
	VBox centerVBox;
	BorderPane top;
	Label titel;
	int currentMenu;
	MediaView mediaView;
	MediaPlayer mediaPlayer1;
	MediaPlayer mediaPlayer2;
	MediaPlayer mediaPlayer3;
	double volumemusic;
	double volumesound;
	double width;
	double height;
	int lastmenu;
	boolean inMenuInGame;
	boolean inInventar;
	final int worldsize = 7;
	int worldData[][][];
	final int tileHeight = 24;
	final int tileWidth = 42;
	final int xplayer = (tileWidth * 25) - 47;
	final int yplayer = (tileHeight * 25) - 50;

	@Override
	public void init() throws Exception { // Optional Initialisierung VOR Zugriff auf GUI (Dateien auslesen, etc.)!!
		super.init();
		System.out.println("init() - initialization (BEFORE access to GUI) called");
		// load games
		worldData = new int[3][worldsize * 100][worldsize * 100];
		getWorldData();
		// load settings
		getSettings();
		System.out.println("Settings: Volumemusic:" + volumemusic + ", Soundeffects: " + volumesound);
		// sound
		mediaView = new MediaView();
		mediaPlayer1 = new MediaPlayer(new Media(Paths.get("music/music1.mp3").toUri().toString()));
		mediaPlayer2 = new MediaPlayer(new Media(Paths.get("music/music2.mp3").toUri().toString()));
		mediaPlayer3 = new MediaPlayer(new Media(Paths.get("music/music3.mp3").toUri().toString()));
		mediaView.setMediaPlayer(mediaPlayer1);
		mediaView.getMediaPlayer().play();
		mediaView.getMediaPlayer().setVolume(volumemusic);

		mediaPlayer1.setOnEndOfMedia(() -> {
			mediaView.setMediaPlayer(mediaPlayer2);
			mediaView.getMediaPlayer().play();
			mediaView.getMediaPlayer().setVolume(volumemusic);
		});

		mediaPlayer2.setOnEndOfMedia(() -> {
			mediaView.setMediaPlayer(mediaPlayer3);
			mediaView.getMediaPlayer().play();
			mediaView.getMediaPlayer().setVolume(volumemusic);
		});

		mediaPlayer3.setOnEndOfMedia(() -> {
			mediaView.setMediaPlayer(mediaPlayer1);
			mediaView.getMediaPlayer().play();
			mediaView.getMediaPlayer().setVolume(volumemusic);
		});
	}

	@Override
	public void stop() throws Exception {
		// Aufraeumen - MIT Zugriff auf GUI
		System.out.println("stop() called");
		super.stop();
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			height = Screen.getPrimary().getBounds().getHeight();
			width = Screen.getPrimary().getBounds().getWidth();
			primaryStage.setHeight(height);
			primaryStage.setWidth(width);
			primaryStage.setMinHeight(720);
			primaryStage.setMinWidth(1280);
			System.out.println(height + " " + width);
			primaryStage.setTitle("Sandbox");
			primaryStage.getIcons().add(new Image("file:player/front.png"));
			this.primaryStage = primaryStage;
			root = new Pane();
			root.setId("menupicture");
			center = new BorderPane();
			top = new BorderPane();
			centerVBox = new VBox();
			centerVBox.setAlignment(Pos.TOP_CENTER);
			centerVBox.setPrefHeight(height);
			centerVBox.setPrefWidth(width);
			center.setCenter(centerVBox);
			root.getChildren().addAll(center, top);
			menu = new Scene(root, height, width);
			menu.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
			// Titel
			titel = new Label("Sandbox");
			titel.setId("titellabel");
			titel.setFont(new Font("French Script MT", height / 4));
			titel.setMaxWidth(Double.MAX_VALUE);
			titel.setAlignment(Pos.CENTER);
			VBox.setMargin(titel, new Insets(height / 100, 0, height / 20, 0));
			centerVBox.getChildren().add(titel);
			currentMenu = 0;
			menu();
			primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
				width = (double) newVal;
				titel.setFont(
						new Font("French Script MT", (height / 4) * Math.sqrt(Math.sqrt(Math.sqrt(height / 1080)))));
				VBox.setMargin(titel, new Insets((height / 100) * Math.pow(height / 1080, 100), 0,
						(height / 20) * Math.pow(height / 1080, 100), 0));
				updateWidthScreen((double) newVal, (double) oldVal);
				updateScreen();
			});
			primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
				height = (double) newVal;
				titel.setFont(
						new Font("French Script MT", (height / 4) * Math.sqrt(Math.sqrt(Math.sqrt(height / 1080)))));
				VBox.setMargin(titel, new Insets((height / 100) * Math.pow(height / 1080, 100), 0,
						(height / 20) * Math.pow(height / 1080, 100), 0));
				updateHeightScreen((double) newVal, (double) oldVal);
				updateScreen();
			});
			primaryStage.setScene(menu);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateHeightScreen(Double newVal, Double oldVal) {
		double multiplier = newVal / oldVal;
		root.setPrefHeight(oldVal * multiplier);
		center.setPrefHeight(oldVal * multiplier);
		centerVBox.setPrefHeight(oldVal * multiplier);
		titel.setPrefHeight(titel.getHeight() * multiplier);
		System.out.println(center.getHeight() + " " + top.getHeight() + " " + centerVBox.getHeight() + " " + height);
	}

	public void updateWidthScreen(Double newVal, Double oldVal) {
		double multiplier = newVal / oldVal;
		root.setPrefWidth(oldVal * multiplier);
		center.setPrefWidth(oldVal * multiplier);
		top.setPrefWidth(oldVal * multiplier);
		centerVBox.setPrefWidth(oldVal * multiplier);
		titel.setPrefWidth(titel.getWidth() * multiplier);
	}

	public void updateScreen() {
		if (currentMenu != 4 && currentMenu != 3 && currentMenu != 5) {
			changeMenu();
		}
		switch (currentMenu) {
		case 0:
			menu();
			break;
		case 1:
			playmenu();
			break;
		case 2:
			settings();
			break;
		case 6:
			createWorldMenu(0);
			break;
		case 7:
			createWorldMenu(1);
			break;
		case 8:
			createWorldMenu(2);
			break;
		}
	}

	public void changeMenu() {
		if (primaryStage.getScene() != menu) {
			primaryStage.setScene(menu);
		}
		centerVBox.getChildren().remove(0, centerVBox.getChildren().size());
		centerVBox.getChildren().add(titel);
		if (top.getChildren().size() != 0) {
			top.getChildren().remove(0);
		}
		root.setOnKeyPressed(null);
	}

	public void menu() {
		// Main Menu
		// Play
		Button play = new Button();
		play.setId("menubutton");
		play.setPrefSize(width / 3, height / 20);
		play.setText("Play");
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lastmenu = 1;
				currentMenu = 1;
				changeMenu();
				playmenu();
			}
		});
		// Settings
		Button settings = new Button();
		settings.setId("menubutton");
		settings.setPrefSize(width / 3, height / 20);
		settings.setText("Settings");
		settings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lastmenu = 2;
				currentMenu = 2;
				changeMenu();
				settings();
			}
		});
		// Controls
		Button controls = new Button();
		controls.setId("menubutton");
		controls.setPrefSize(width / 3, height / 20);
		controls.setText("Controls");
		controls.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lastmenu = 3;
				currentMenu = 3;
				changeMenu();
				controls();
			}
		});
		// Credits
		Button credits = new Button();
		credits.setId("menubutton");
		credits.setPrefSize(width / 3, height / 20);
		credits.setText("Credits");
		credits.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lastmenu = 4;
				currentMenu = 4;
				changeMenu();
				credits();
			}
		});
		centerVBox.getChildren().addAll(play, settings, controls, credits);
		VBox.setMargin(play, new Insets(height / 50, 0, height / 100, 0));
		VBox.setMargin(settings, new Insets(height / 50, 0, height / 100, 0));
		VBox.setMargin(controls, new Insets(height / 50, 0, height / 100, 0));
		VBox.setMargin(credits, new Insets(height / 50, 0, height / 100, 0));

		switch (lastmenu) {
		case 1:
			play.requestFocus();
			System.out.println("play requested focus");
			break;
		case 2:
			settings.requestFocus();
			System.out.println("settings requested focus");
			break;
		case 3:
			controls.requestFocus();
			System.out.println("controls requested focus");
			break;
		case 4:
			credits.requestFocus();
			System.out.println("credits requested focus");
			break;
		default:
			System.out.println("no one requested focus " + lastmenu);
		}
	}

	public void playmenu() {
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					currentMenu = 0;
					changeMenu();
					menu();
				}
			}
		});

		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("esc");
		escbutton.setGraphic(esc);
		escbutton.setFocusTraversable(false);
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentMenu = 0;
				changeMenu();
				menu();
			}
		});
		top.setTop(escbutton);
		// worlds
		for (int i = 0; i < 3; i++) {
			centerVBox.getChildren().add(worlds(i));
		}
	}

	public HBox worlds(int world) {
		String worldname;
		String difficulty;
		int score;
		double health;
		int y;
		int x;
		String[] worldinfo = getWorldInfosShort(world);
		if (worldinfo != null) {
			worldname = worldinfo[0];
			difficulty = worldinfo[1];
			score = Integer.parseInt(worldinfo[2]);
			health = Double.parseDouble(worldinfo[3]);
			y = Integer.parseInt(worldinfo[4]);
			x = Integer.parseInt(worldinfo[5]);
		} else {
			worldname = "New World";
			difficulty = "-";
			score = 0;
			health = 20.0;
			y = worldsize * 100 / 2;
			x = worldsize * 100 / 2;
		}

		HBox hbox = new HBox();
		hbox.setId("settings");
		hbox.setMaxWidth(1920 / 3);
		VBox.setMargin(hbox, new Insets(height / 50, 0, 0, 0));

		Label name = new Label(worldname);
		name.setId("playmenulabel1");
		Label diff = new Label("Difficulty: " + difficulty);
		diff.setId("playmenulabel2");
		diff.setTextAlignment(TextAlignment.LEFT);
		VBox.setMargin(diff, new Insets(10, 20, 10, 10));
		Label scr = new Label("Score: " + score);
		scr.setId("playmenulabel2");
		scr.setTextAlignment(TextAlignment.RIGHT);
		VBox.setMargin(scr, new Insets(10, 10, 10, 10));

		VBox vbox1 = new VBox();
		vbox1.getChildren().add(name);
		GridPane gridPane = new GridPane();
		gridPane.setMinWidth(300);
		ColumnConstraints col1 = new ColumnConstraints(200);
		ColumnConstraints col2 = new ColumnConstraints();
		gridPane.getColumnConstraints().addAll(col1, col2);
		gridPane.add(diff, 0, 0);
		gridPane.add(scr, 1, 0);
		vbox1.getChildren().add(gridPane);

		VBox vbox2 = new VBox();
		vbox2.setAlignment(Pos.CENTER_RIGHT);

		if (worldname.equals("New World")) {
			Button newWorld = new Button();
			newWorld.setId("playmenubutton");
			newWorld.setPrefSize(1920 / 10, 1080 / 12);
			newWorld.setText("New World");
			newWorld.setAlignment(Pos.CENTER);
			newWorld.setWrapText(true);
			VBox.setMargin(newWorld, new Insets(10, 10, 10, 10));
			newWorld.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					currentMenu = 6 + world;
					changeMenu();
					createWorldMenu(world);
				}
			});
			newWorld.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						// focused
						hbox.setStyle(
								"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
					} else {
						// not focused
						hbox.setStyle("");
					}
				}
			});
			hbox.setOnMouseClicked((e) -> {
				newWorld.requestFocus();
			});
			vbox2.getChildren().add(newWorld);
		} else {
			Button play = new Button();
			play.setId("playmenubutton1");
			play.setPrefSize(1920 / 10, 1080 / 23);
			play.setText("Play");
			play.setAlignment(Pos.CENTER);
			play.setWrapText(true);
			VBox.setMargin(play, new Insets(5, 5, 0, 5));
			play.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent e) {
					currentMenu = 5;
					try {
						play(world, score, health, y, x);
					} catch (gameexception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			play.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						// focused
						hbox.setStyle(
								"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
					} else {
						// not focused
						hbox.setStyle("");
					}
				}
			});
			vbox2.getChildren().add(play);
			// Delete Button
			Button delete = new Button();
			delete.setId("playmenubutton1");
			delete.setPrefSize(1920 / 10, 1080 / 23);
			delete.setText("Delete");
			delete.setAlignment(Pos.CENTER);
			delete.setWrapText(true);
			VBox.setMargin(delete, new Insets(5, 5, 5, 5));
			delete.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						// focused
						hbox.setStyle(
								"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
					} else {
						// not focused
						hbox.setStyle("");
					}
				}
			});
			// U sure Button
			Button sure = new Button();
			sure.setId("playmenubutton1");
			sure.setPrefSize(1920 / 10, 1080 / 23);
			sure.setText("Sure?");
			sure.setAlignment(Pos.CENTER);
			sure.setWrapText(true);
			VBox.setMargin(sure, new Insets(5, 5, 0, 5));
			sure.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						// focused
						hbox.setStyle(
								"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
					} else {
						// not focused
						hbox.setStyle("");
					}
				}
			});
			// Keep Button
			Button keep = new Button();
			keep.setId("playmenubutton1");
			keep.setPrefSize(1920 / 10, 1080 / 23);
			keep.setText("Keep?");
			keep.setAlignment(Pos.CENTER);
			keep.setWrapText(true);
			VBox.setMargin(keep, new Insets(5, 5, 5, 5));
			keep.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						// focused
						hbox.setStyle(
								"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
					} else {
						// not focused
						hbox.setStyle("");
					}
				}
			});
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					vbox2.getChildren().remove(0);
					vbox2.getChildren().remove(0);
					vbox2.getChildren().add(sure);
					vbox2.getChildren().add(keep);
					keep.requestFocus();
				}
			});
			sure.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					deleteWorld(world);
					changeMenu();
					playmenu();
				}
			});
			keep.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					vbox2.getChildren().remove(0);
					vbox2.getChildren().remove(0);
					vbox2.getChildren().add(play);
					vbox2.getChildren().add(delete);
					delete.requestFocus();
				}
			});
			hbox.setOnMouseClicked((e) -> {
				play.requestFocus();
			});
			vbox2.getChildren().add(delete);
		}

		hbox.getChildren().add(vbox1);
		hbox.getChildren().add(vbox2);
		HBox.setHgrow(vbox2, Priority.ALWAYS);

		return hbox;

	}

	private int[][] getInventar(int world) {
		File settings = new File("worlds/world" + world + "/inventar.txt");
		File readSource = new File(settings.toURI());
		try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
			String line;
			int inventar[][] = new int[4][10];
			System.out.println("Datei " + readSource + ":");
			while ((line = br.readLine()) != null) {
				String str[] = line.split(",");
				for (int i = 0; i < str.length; i++) {
					String arr[] = str[i].split(":");
					for (int j = 0; j < arr.length; j++) {
						inventar[i][j] = Integer.parseInt(arr[j]);
					}
				}
			}
			inventar[0][0] = 1;
			System.out.println("inventar loaded");
			return inventar;
		} catch (IOException exc) {
			System.out.println("Worlds didn't load properly " + exc);
			int inventar[][] = new int[4][10];
			for (int i = 0; i < inventar.length; i++) {
				for (int j = 0; j < inventar[i].length; j++) {
					inventar[i][j] = 0;
				}
			}
			inventar[0][0] = 1;
			return inventar;
		}
	}

	public String[] getWorldInfosShort(int world) {
		File settings = new File("worlds/world" + world + "/worldinfo.txt");
		File readSource = new File(settings.toURI());
		try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String str[] = line.split(",");
				return str;
			}
		} catch (IOException exc) {
			System.out.println("Worldinfo didn't load properly " + exc);
		}
		return null;
	}

	public void deleteWorld(int world) {
		File settings = new File("worlds/world" + world + "/worldinfo.txt");
		File saveTarget = new File(settings.toURI());
		try (FileWriter fw = new FileWriter(saveTarget); PrintWriter pw = new PrintWriter(fw);) {
			String temp = "New World,-,0,20.0,0,0";
			pw.write(temp);
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
		File inventar = new File("worlds/world" + world + "/inventar.txt");
		File inventarTarget = new File(inventar.toURI());
		try (FileWriter fw = new FileWriter(inventarTarget); PrintWriter pw = new PrintWriter(fw);) {
			String temp = "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0";
			pw.write(temp);
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
		File worlddata = new File("worlds/world" + world + "/worlddata.txt");
		File worlddataTarget = new File(worlddata.toURI());
		try (FileWriter fw = new FileWriter(worlddataTarget); PrintWriter pw = new PrintWriter(fw);) {
			String temp = "";
			pw.write(temp);
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
		File abilities = new File("worlds/world" + world + "/abilities.txt");
		File abilitiesTarget = new File(abilities.toURI());
		try (FileWriter fw = new FileWriter(abilitiesTarget); PrintWriter pw = new PrintWriter(fw);) {
			String temp = "";
			pw.write(temp);
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
	}

	public void createWorldMenu(int world) {
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					currentMenu = 1;
					changeMenu();
					playmenu();
				}
			}
		});
		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("esc");
		escbutton.setGraphic(esc);
		escbutton.setFocusTraversable(false);
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentMenu = 1;
				changeMenu();
				playmenu();
			}
		});
		top.setTop(escbutton);
		// create infos sammeln
		// name
		GridPane name = new GridPane();
		name.setId("createWorld");
		name.setAlignment(Pos.CENTER_LEFT);
		name.setPrefSize(width / 3, height / 14);
		name.getColumnConstraints().addAll(new ColumnConstraints(150), new ColumnConstraints());
		// nodes in name
		TextField textField = new TextField();
		textField.setPrefSize(463, 60);
		textField.setId("createWorld1");
		Label nameLabel = new Label("Name:");
		nameLabel.setId("credits");
		// adding nodes
		name.add(nameLabel, 0, 0);
		name.add(textField, 1, 0);
		VBox.setMargin(name, new Insets(height / 50, width / 3, 0, width / 3));
		// Überprüft Zeichen
		textField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if ((event.getCharacter().matches("[A-Za-z]")
						|| (event.getCharacter().matches(" ") && textField.getText().length() > 1))
						&& (textField.getText().length() < 6)) {

				} else {
					event.consume();
				}
			}
		});
		// Wenn textField focused ist man es an name erkennt
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					name.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					name.setStyle("");
				}
			}
		});
		// Wenn gridpane clicked das textfield focused ist
		name.setOnMouseClicked((e) -> {
			textField.requestFocus();
		});
		// difficulty
		GridPane diff = new GridPane();
		diff.setId("createWorld");
		diff.setAlignment(Pos.CENTER_LEFT);
		diff.setPrefSize(width / 3, height / 14);
		diff.getColumnConstraints().addAll(new ColumnConstraints(150), new ColumnConstraints());
		// nodes in name
		Button difficulty = new Button();
		difficulty.setId("createWorld2");
		difficulty.setPrefSize(463, 60);
		difficulty.setText("Normal");
		difficulty.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (difficulty.getText() == "Easy") {
					difficulty.setText("Normal");
				} else if (difficulty.getText() == "Normal") {
					difficulty.setText("Hard");
				} else {
					difficulty.setText("Easy");
				}
			}
		});
		Label diffLabel = new Label("Difficulty:");
		diffLabel.setId("credits");
		// adding nodes
		diff.add(diffLabel, 0, 0);
		diff.add(difficulty, 1, 0);
		VBox.setMargin(diff, new Insets(height / 50, width / 3, 0, width / 3));
		// Wenn textField focused ist man es an name erkennt
		difficulty.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					diff.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					diff.setStyle("");
				}
			}
		});
		// difficulty action
		difficulty.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (difficulty.getText() == "Easy") {
					difficulty.setText("Normal");
				} else if (difficulty.getText() == "Normal") {
					difficulty.setText("Hard");
				} else {
					difficulty.setText("Easy");
				}
			}
		});
		// difficulty mit Pfeiltasten ändern
		difficulty.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.LEFT) {
					if (difficulty.getText() == "Easy") {
						difficulty.setText("Hard");
					} else if (difficulty.getText() == "Hard") {
						difficulty.setText("Normal");
					} else {
						difficulty.setText("Easy");
					}
				} else if (key == KeyCode.RIGHT) {
					if (difficulty.getText() == "Easy") {
						difficulty.setText("Normal");
					} else if (difficulty.getText() == "Normal") {
						difficulty.setText("Hard");
					} else {
						difficulty.setText("Easy");
					}
				}
			}
		});
		// Wenn gridpane clicked das textfield focused ist
		diff.setOnMouseClicked((e) -> {
			difficulty.requestFocus();
		});

		// Damit ich aus Textfield mit Arrow Key rauskomme
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.DOWN) {
					difficulty.requestFocus();
				}
			}
		});
		// Create
		Button createWorld = new Button();
		createWorld.setId("menubutton");
		createWorld.setPrefSize(width / 3, height / 20);
		createWorld.setText("Create World");
		createWorld.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				currentMenu=5;
				if (textField.getText() != null && !textField.getText().equals("New World")
						&& textField.getText().matches("[A-Za-z]") && textField.getText().length() < 7) {
				} else {
					textField.setText("World " + (world + 1));
				}
				createWorld.setDisable(true);
				createWorld();
				try {
					play(world, 0, 20.0, worldsize * 100 / 2, worldsize * 100 / 2);
				} catch (gameexception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			public void createWorld() {
				File settings = new File("worlds/world" + world + "/worldinfo.txt");
				File saveTarget = new File(settings.toURI());
				try (FileWriter fw = new FileWriter(saveTarget); PrintWriter pw = new PrintWriter(fw);) {
					String temp = textField.getText() + "," + difficulty.getText() + ",0,20.0," + (worldsize * 100 / 2)
							+ "," + (worldsize * 100 / 2);
					pw.write(temp);
				} catch (IOException exc) {
					System.out.println("Didnt save properly");
				}
				File player = new File("worlds/world" + world + "/player.txt");
				File saveTarget1 = new File(player.toURI());
				try (FileWriter fw = new FileWriter(saveTarget1); PrintWriter pw = new PrintWriter(fw);) {
					String temp = "0,0,20";
					pw.write(temp);
				} catch (IOException exc) {
					System.out.println("Didnt save properly");
				}
				File inventar = new File("worlds/world" + world + "/inventar.txt");
				File saveTarget2 = new File(inventar.toURI());
				try (FileWriter fw = new FileWriter(saveTarget2); PrintWriter pw = new PrintWriter(fw);) {
					String temp = "1:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0,0:0:0:0:0:0:0:0:0:0";
					pw.write(temp);
				} catch (IOException exc) {
					System.out.println("Didnt save properly");
				}
				createWorldData(world);
			}
		});
		VBox.setMargin(createWorld, new Insets(height / 50, width / 3, 0, width / 3));

		centerVBox.getChildren().add(name);
		centerVBox.getChildren().add(diff);
		centerVBox.getChildren().add(createWorld);
	}

	public void createWorldData(int world) {
		int worlds = 5;
		int worldPresetWorldParts[][][] = new int[worlds + 1][100][100];
		for (int i = 0; i < worlds; i++) {
			File settings = new File("worldparts/part" + i + ".txt");
			File readSource = new File(settings.toURI());
			try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
				String line;
				System.out.println("Datei " + readSource + ":");
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					String str[] = line.split(",");
					for (int j = 0; j < str.length; j++) {
						String arr[] = str[j].split(":");
						for (int k = 0; k < arr.length; k++) {
							worldPresetWorldParts[i][j][k] = Integer.parseInt(arr[k]);
						}
					}
				}
			} catch (IOException exc) {
				System.out.println("World didn't set up properly " + exc);
			}
		}
		System.out.println("Start");
		for (int o = 0; o < worldPresetWorldParts[1].length; o++) {
			for (int p = 0; p < worldPresetWorldParts[1][o].length; p++) {
				System.out.println(worldPresetWorldParts[1][o][p]);
			}
		}
		System.out.println("TEST");
		// Welt zusammen fügen
		int worldscounter = 0;
		int h = 0;
		int randomWorldsRow[] = new int[worldsize];
		for (int i = 0; i < worldsize * 100; i++) {
			if (h == 0 && worldscounter != 0 && worldscounter != worldsize - 1) {
				for (int k = 1; k < worldsize - 1; k++) {
					Random random = new Random();
					randomWorldsRow[k] = random.nextInt(worlds - 1) + 1;

					System.out.println(randomWorldsRow[k]);
				}
				randomWorldsRow[0] = 0;
				randomWorldsRow[worldsize - 1] = 0;
				worldscounter++;
			} else if (h == 0) {
				for (int k = 0; k < worldsize; k++) {
					randomWorldsRow[k] = 0;
				}
				worldscounter++;
			}
			int j = 0;
			for (int k = 0; k < worldsize; k++) {
				for (int l = 0; l < 100; l++) {
					worldData[world][i][j] = worldPresetWorldParts[randomWorldsRow[k]][h][l];
					j++;
				}
			}
			h++;
			if (h == 100) {
				h = 0;
			}
		}
		saveWorldData(world);
	}

	public void saveWorldData(int world) {
		int worldDataCopy[][][] = worldData;
		// Saving
		Thread thread = new Thread(new Runnable() {
			public void run() {
				// save your data here
				File inventar = new File("worlds/world" + world + "/worlddata.txt");
				File saveTarget2 = new File(inventar.toURI());
				try (FileWriter fw = new FileWriter(saveTarget2); BufferedWriter bw = new BufferedWriter(fw);) {
					for (int i = 0; i < worldsize * 100; i++) {
						for (int j = 0; j < worldsize * 100; j++) {
							bw.write(worldDataCopy[world][i][j] + "");
							if (j != worldsize * 100 - 1) {
								bw.write(":");
							}
						}
						if (i != worldsize * 100 - 1) {
							bw.write(",");
						}
					}
					bw.flush();
				} catch (IOException exc) {
					System.out.println("Didnt save properly");
				}
				System.out.println("finished creating " + world);
			}
		});
		thread.start();
	}

	public void getWorldData() {
		for (int i = 0; i < 3; i++) {
			File settings = new File("worlds/world" + i + "/worlddata.txt");
			File readSource = new File(settings.toURI());
			try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
				String line;
				System.out.println("Datei " + readSource + ":");
				while ((line = br.readLine()) != null) {
					String str[] = line.split(",");
					for (int j = 0; j < worldsize * 100; j++) {
						String arr[] = str[j].split(":");
						for (int k = 0; k < worldsize * 100; k++) {
							worldData[i][j][k] = Integer.parseInt(arr[k]);
						}
					}
				}
			} catch (IOException exc) {
				System.out.println("Worlds didn't load properly " + exc);
			}
			System.out.println(i + " finished loading");
		}
	}

	public void settings() {
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					changeMenu();
					menu();
				}
			}
		});
		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("esc");
		escbutton.setGraphic(esc);
		escbutton.setFocusTraversable(false);
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeMenu();
				menu();
			}
		});
		top.setTop(escbutton);
		// settings
		// music
		VBox music = new VBox();
		music.setId("settings");
		Label musiclabel = new Label("Music");
		musiclabel.setMaxWidth(Double.MAX_VALUE);
		musiclabel.setAlignment(Pos.CENTER);
		musiclabel.setId("titellabel");
		Slider musicslider = new Slider(0.0, 1.0, volumemusic);
		musicslider.setMaxWidth(Double.MAX_VALUE);
		musicslider.setSnapToTicks(true);
		musicslider.setMajorTickUnit(0.01);
		musicslider.setBlockIncrement(0.01);
		musicslider.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					music.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					music.setStyle("");
				}
			}
		});
		music.getChildren().add(musiclabel);
		music.getChildren().add(musicslider);
		// sound
		VBox sound = new VBox();
		sound.setId("settings");
		Label soundlabel = new Label("Sound");
		soundlabel.setMaxWidth(Double.MAX_VALUE);
		soundlabel.setAlignment(Pos.CENTER);
		soundlabel.setId("titellabel");
		Slider soundslider = new Slider(0.0, 1.0, volumesound);
		soundslider.setMaxWidth(Double.MAX_VALUE);
		soundslider.setSnapToTicks(true);
		soundslider.setMajorTickUnit(0.01);
		soundslider.setBlockIncrement(0.01);
		soundslider.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					sound.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					sound.setStyle("");
				}
			}
		});
		sound.getChildren().add(soundlabel);
		sound.getChildren().add(soundslider);
		// Apply
		Button apply = new Button();
		apply.setId("menubutton");
		apply.setPrefSize(width / 3, height / 20);
		apply.setText("Apply Changes");
		apply.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				volumemusic = musicslider.getValue();
				volumesound = soundslider.getValue();
				mediaView.getMediaPlayer().setVolume(volumemusic);
				setSettings();
				changeMenu();
				menu();
			}
		});

		VBox.setMargin(music, new Insets(height / 50, width / 3, height / 100, width / 3));
		VBox.setMargin(sound, new Insets(height / 50, width / 3, height / 100, width / 3));
		VBox.setMargin(apply, new Insets(height / 50, width / 3, height / 100, width / 3));
		VBox.setMargin(musicslider, new Insets(0, width / 1000, height / 500, width / 1000));
		VBox.setMargin(soundslider, new Insets(0, width / 1000, height / 500, width / 1000));

		centerVBox.getChildren().add(music);
		centerVBox.getChildren().add(sound);
		centerVBox.getChildren().add(apply);
	}

	private void getSettings() {
		File settings = new File("settings/settings.txt");
		File readSource = new File(settings.toURI());
		try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
			String line;
			System.out.println("Datei " + readSource + ":");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String str[] = line.split(",");
				for (int i = 0; i < str.length; i++) {
					String arr[] = str[i].split(":");
					if (arr[0].equals("volumemusic")) {
						volumemusic = Double.parseDouble(arr[1]);
					} else if (arr[0].equals("volumesound")) {
						volumesound = Double.parseDouble(arr[1]);
					}
				}
			}
		} catch (IOException exc) {
			System.out.println("Settings didn't load properly " + exc);
			volumemusic = 0.2;
			volumesound = 0.2;
		}
	}

	public void setSettings() {
		File settings = new File("settings/settings.txt");
		File saveTarget = new File(settings.toURI());
		try (FileWriter fw = new FileWriter(saveTarget); PrintWriter pw = new PrintWriter(fw);) {
			String temp = "volumemusic:" + volumemusic + ",volumesound:" + volumesound;
			pw.write(temp);
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
	}

	public void controls() {
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					changeMenu();
					menu();
				}
			}
		});
		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("esc");
		escbutton.setGraphic(esc);
		escbutton.setFocusTraversable(false);
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeMenu();
				menu();
			}
		});
		top.setTop(escbutton);
		// controls

		final ObservableList<Control> data = getControls();
		TableView<Control> table = new TableView<Control>(data);
		table.setId("Controltable");
		table.setMaxWidth(600);
		table.setMinWidth(600);
		table.setEditable(false);

		TableColumn<Control, String> keyCol = new TableColumn<>("Key");
		keyCol.setCellValueFactory(new PropertyValueFactory<Control, String>("key"));
		keyCol.setSortable(false);
		TableColumn<Control, String> descriptionCol = new TableColumn<>("Description");
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Control, String>("description"));
		descriptionCol.setSortable(false);
		table.getColumns().addAll(keyCol, descriptionCol);
		keyCol.setMinWidth(100);
		descriptionCol.setMinWidth(500);

		centerVBox.getChildren().add(table);
	}

	public ObservableList<Control> getControls() {
		File controls = new File("controls/controls.txt");
		File readSource = new File(controls.toURI());
		try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
			String line;
			System.out.println("Datei " + readSource + ":");
			while ((line = br.readLine()) != null) {
				ObservableList<Control> data = FXCollections.observableArrayList();
				String str[] = line.split(",");
				for (int i = 0; i < str.length; i++) {
					String arr[] = str[i].split("#");
					data.add(new Control(arr[0], arr[1]));
				}
				return data;
			}
		} catch (IOException exc) {
			System.out.println("Settings didn't load properly " + exc);
		}
		return null;
	}

	public void credits() {
		List<Label> creds = getCredits();
		AnimationTimer creditsAnimation = new AnimationTimer() {
			long lastTick = 0;
			int i = 0;
			int speed = 5;
			float temp = 0;

			public void handle(long now) {
				if (lastTick == 0) {
					lastTick = now;
					centerVBox.getChildren().add(0, creds.get(i));
					i++;
					return;
				}

				if (now - lastTick > 1 / speed) {
					lastTick = now;

					if ((temp < 32.5 || (i == creds.size() - 14 && temp < 65))) {
						VBox.setMargin(centerVBox.getChildren().get(0), new Insets(temp, 0, 0, 0));
						temp += 0.5;
					} else {
						VBox.setMargin(centerVBox.getChildren().get(0), new Insets(0, 0, 0, 0));
						centerVBox.getChildren().add(0, creds.get(i));
						temp = 0;
						i++;
					}
				}

				if (i == creds.size()) {
					stop();
				}
			}

		};
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (key == KeyCode.ESCAPE) {
					creditsAnimation.stop();
					changeMenu();
					menu();
				}
			}
		});
		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("esc");
		escbutton.setGraphic(esc);
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				creditsAnimation.stop();
				changeMenu();
				menu();
			}
		});
		top.setTop(escbutton);
		creditsAnimation.start();
	}

	public List<Label> getCredits() {
		File settings = new File("credits/credits.txt");
		File readSource = new File(settings.toURI());
		try (FileReader fr = new FileReader(readSource); BufferedReader br = new BufferedReader(fr);) {
			String line;
			System.out.println("Datei " + readSource + ":");
			while ((line = br.readLine()) != null) {
				String str[] = line.split("%");
				List<Label> creds = new ArrayList<Label>();
				for (int i = 0; i < str.length; i++) {
					Label l = new Label(str[i]);
					l.setId("credits");
					l.setFont(new Font("Maiandra GD", 1080 / 40));
					l.setMaxWidth(Double.MAX_VALUE);
					l.setAlignment(Pos.CENTER);
					creds.add(l);
				}
				Label m = new Label("To Return To The Menu Press ESC");
				m.setId("credits");
				m.setFont(new Font("Maiandra GD", 1080 / 20));
				m.setMaxWidth(Double.MAX_VALUE);
				m.setAlignment(Pos.CENTER);
				creds.add(m);
				for (int i = 0; i < 13; i++) {
					Label l = new Label("");
					l.setId("credits");
					l.setFont(new Font("Maiandra GD", 1080 / 40));
					l.setMaxWidth(Double.MAX_VALUE);
					l.setAlignment(Pos.CENTER);
					creds.add(l);
				}
				return creds;
			}
		} catch (IOException exc) {
			System.out.println("Settings didn't load properly " + exc);
		}
		return null;
	}

	public void play(int world, int score, double health, int y, int x) throws gameexception {
		try {
		int inventar[][] = getInventar(world);

		StackPane rootGame = new StackPane();
		rootGame.setId("backgroundscreen");
		Scene play = new Scene(rootGame, height, width);
		play.getStylesheets().add(getClass().getResource("game.css").toExternalForm());

		// Oberfläche des Spieles

		VBox tilesBox = new VBox();
		tilesBox.setMinSize(1960, 1080);
		tilesBox.setMaxSize(1960, 1080);
		tilesBox.setAlignment(Pos.CENTER);
		TilePane tiles = new TilePane();
		tiles.setMinSize(tileWidth * 50, tileHeight * 50);
		// Images
		Image lava = new Image("file:textures/lava.png");
		Image water = new Image("file:textures/water.png");
		Image grass = new Image("file:textures/grass.png");
		Image stone = new Image("file:textures/stone.png");
		Image sand = new Image("file:textures/sand.png");
		Image dorns = new Image("file:textures/thorns.png");
		Image gravestone0 = new Image("file:textures/gravestone_0.png");
		Image gravestone1 = new Image("file:textures/gravestone_1.png");
		Image blackstone = new Image("file:textures/blackstone.png");
		Image blackstone_thorns = new Image("file:textures/blackstone_thorns.png");
		Image textures[] = { lava, water, grass, stone, sand, dorns, gravestone0, gravestone1, blackstone,
				blackstone_thorns };
		// Aktive Images Hinzufügen
		ImageView activeTiles[][] = new ImageView[tileHeight][tileWidth];
		for (int i = 0; i < tileHeight; i++) {
			for (int j = 0; j < tileWidth; j++) {
				ImageView tile = new ImageView(textures[worldData[world][i + y][j + x]]);
				activeTiles[i][j] = tile;
				tiles.getChildren().add(activeTiles[i][j]);
			}
		}
		tilesBox.getChildren().add(tiles);

		// Entities
		VBox mobsVBox = new VBox();
		mobsVBox.setPickOnBounds(false);
		mobsVBox.setMinSize(width, height);
		mobsVBox.setMaxSize(width, height);
		mobsVBox.setAlignment(Pos.CENTER);
		Pane mobs = new Pane();
		mobs.setMinSize(tileWidth * 50, tileHeight * 50);
		// Player Images
		Image playerfront = new Image("file:player/front.png");
		Image playerback = new Image("file:player/back.png");
		Image playerleft = new Image("file:player/left.png");
		Image playerright = new Image("file:player/right.png");
		Image playerSprite[] = { playerfront, playerback, playerleft, playerright };
		ImageView player = new ImageView(playerSprite[0]);
		mobs.setId("hotbar");
		player.setFitHeight(100);
		player.setFitWidth(100);
		player.setLayoutX(xplayer);
		player.setLayoutY(yplayer);
		// Circle c = new Circle(5);
		// c.setLayoutX(xplayer+50);
		// c.setLayoutY(yplayer+95);
		// c.setFill(Color.RED);
		mobs.getChildren().add(player);
		// mobs.getChildren().add(c);
		mobsVBox.getChildren().add(mobs);

		// main: slots, infos, score
		BorderPane inventarBorderPane = new BorderPane();
		VBox mainbottom = new VBox();
		mainbottom.setAlignment(Pos.CENTER);
		mainbottom.setMinHeight(1920 / 20);

		// Label anpassen

		// Inventar
		BorderPane main = new BorderPane();
		main.setPickOnBounds(false);
		// Score im Inventar
		Label scoreLabel = new Label("Score: " + score + " ");
		scoreLabel.setId("score");
		scoreLabel.setMinSize(100, 300);
		scoreLabel.setMaxWidth(Double.MAX_VALUE);
		scoreLabel.setAlignment(Pos.TOP_CENTER);
		main.setTop(scoreLabel);

		// Leben/activeItem
		Label selectedItem = new Label("Here is gonna be some awesome text someday.");
		selectedItem.setId("activeItemLabel");
		selectedItem.setMinSize((1920 / 3) - 4, 1080 / 20);
		selectedItem.setMaxWidth(Double.MAX_VALUE);
		selectedItem.setAlignment(Pos.CENTER);
		VBox.setMargin(selectedItem, new Insets(0, 1920 / 3, 0, 1920 / 3));
		mainbottom.getChildren().add(selectedItem);

		HBox lives = new HBox();
		lives.setMaxSize((1920 / 3) - 4, 1080 / 20);
		Image fullheart = new Image("file:player/heart.png");
		Image halfheart = new Image("file:player/heart_half.png");
		Image emptyheart = new Image("file:player/heart_empty.png");
		Image heartTextures[] = { fullheart, halfheart, emptyheart };

		// Herzen
		VBox[] healthbar = new VBox[10];
		int heartCounter = 0;
		for (int i = 0; i < 10; i++) {
			healthbar[i] = new VBox();
			healthbar[i].setMinWidth(64);
			healthbar[i].setMinHeight(52);
			healthbar[i].setAlignment(Pos.CENTER);
			if (heartCounter < health - 1) {
				healthbar[i].getChildren().add(new ImageView(heartTextures[0]));
				heartCounter += 2;
			} else if (heartCounter + 1 == health) {
				healthbar[i].getChildren().add(new ImageView(heartTextures[1]));
				heartCounter += 2;
			} else {
				healthbar[i].getChildren().add(new ImageView(heartTextures[2]));
				heartCounter += 2;
			}
			lives.getChildren().add(healthbar[i]);
		}
		mainbottom.getChildren().add(lives);

		HBox maincenter = new HBox();
		maincenter.setPrefSize(1960, 1080);
		maincenter.setAlignment(Pos.BOTTOM_CENTER);

		// Inventar Muelleimer
		VBox muelleimer = new VBox();
		muelleimer.setMinWidth(63);
		muelleimer.setMaxHeight(63);
		muelleimer.setId("hotbar");
		// Muelleimer Bild
		ImageView muelleimerPng = new ImageView("file:pictures/muelleimer.png");
		muelleimerPng.setFitHeight(50);
		muelleimerPng.setFitWidth(50);
		muelleimer.setAlignment(Pos.CENTER);
		muelleimer.getChildren().add(muelleimerPng);
		maincenter.getChildren().add(muelleimer);
		HBox.setMargin(muelleimer, new Insets(450, 0, 0, 10));

		// Inventar Blöcke
		// Inventar Images erstellen
		Image itemHand = new Image("file:items/hand.png");
		Image items[] = { null, itemHand, sand, dorns };
		VBox inventarVBoxes[][] = new VBox[4][10];
		for (int i = 0; i < inventar.length; i++) {
			for (int j = 0; j < inventar[i].length; j++) {
				inventarVBoxes[i][j] = new VBox(new ImageView(items[inventar[i][j]]));
				inventarVBoxes[i][j].setMinWidth(63);
				inventarVBoxes[i][j].setMinHeight(52);
				inventarVBoxes[i][j].setId("hotbarItem");
				inventarVBoxes[i][j].setAlignment(Pos.CENTER);
			}
		}
		inventarVBoxes[0][0].setId("activeItem");

		// Hotbar
		HBox hotbar = new HBox();
		hotbar.setId("hotbar");
		hotbar.setMaxSize((1920 / 3) - 4, 1080 / 20);
		VBox.setMargin(hotbar, new Insets(0, 0, 1080 / 40, 0));
		mainbottom.getChildren().add(hotbar);
		inventarBorderPane.setBottom(mainbottom);

		// Inventar
		VBox inventarVBox = new VBox();
		inventarVBox.setId("hotbar");
		inventarVBox.setMinSize((1920 / 3) - 4, 162);
		inventarVBox.setMaxSize((1920 / 3) - 4, 162);
		maincenter.getChildren().add(0, inventarVBox);

		// Inventar erstellung
		for (int i = 0; i < 4; i++) {
			HBox row = new HBox();
			for (int j = 0; j < 10; j++) {
				if (i == 0) {
					hotbar.getChildren().add(inventarVBoxes[i][j]);
				} else {
					row.getChildren().add(inventarVBoxes[i][j]);
				}
			}
			if (i != 0) {
				inventarVBox.getChildren().add(row);
			}
		}
		HBox.setMargin(inventarVBox, new Insets(350, 0, 0, 68.8));

		// menu
		BorderPane menu = new BorderPane();
		menu.setPickOnBounds(false);

		// Menu Window mit Label
		VBox menuVBox = new VBox();
		Label menuLabel = new Label("Menu");
		menuLabel.setId("menuLabel");
		menuLabel.setMinSize(1920 / 3, 1080 / 20);
		menuLabel.setMaxWidth(Double.MAX_VALUE);
		menuLabel.setAlignment(Pos.CENTER);
		menuVBox.getChildren().add(menuLabel);

		// Menu Window
		VBox menuWindow = new VBox();
		menuWindow.setId("menuWindow");
		menuWindow.setMinSize(1920 / 3, 530);
		menuWindow.setMaxSize(1920 / 3, 530);
		VBox.setMargin(menuWindow, new Insets(0, 1920 / 3, 0, 1920 / 3));

		// Menu Window Button
		// Resume
		Button resume = new Button();
		resume.setId("menuWindowButton");
		resume.setPrefSize(600, 1080 / 20);
		resume.setText("Resume");

		// Save
		Button save = new Button();
		save.setId("menuWindowButton");
		save.setPrefSize(600, 1080 / 20);
		save.setText("Save");

		// Save & Quit
		Button savequit = new Button();
		savequit.setId("menuWindowButton");
		savequit.setPrefSize(600, 1080 / 20);
		savequit.setText("Save & Quit");

		// Music
		VBox music = new VBox();
		music.setId("settings");
		Label musiclabel = new Label("Music");
		musiclabel.setMaxWidth(Double.MAX_VALUE);
		musiclabel.setAlignment(Pos.CENTER);
		musiclabel.setId("blacktext");
		Slider musicslider = new Slider(0.0, 1.0, volumemusic);
		musicslider.setMaxWidth(Double.MAX_VALUE);
		musicslider.setSnapToTicks(true);
		musicslider.setMajorTickUnit(0.01);
		musicslider.setBlockIncrement(0.01);
		// Musicslider Event
		musicslider.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					music.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					music.setStyle("");
				}
			}
		});
		music.getChildren().add(musiclabel);
		music.getChildren().add(musicslider);

		// Sound
		VBox sound = new VBox();
		sound.setId("settings");
		Label soundlabel = new Label("Sound");
		soundlabel.setMaxWidth(Double.MAX_VALUE);
		soundlabel.setAlignment(Pos.CENTER);
		soundlabel.setId("blacktext");
		Slider soundslider = new Slider(0.0, 1.0, volumesound);
		soundslider.setMaxWidth(Double.MAX_VALUE);
		soundslider.setSnapToTicks(true);
		soundslider.setMajorTickUnit(0.01);
		soundslider.setBlockIncrement(0.01);
		// Soundslider Event
		soundslider.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					// focused
					sound.setStyle(
							"-fx-border-color: black; -fx-border-width: 5.0; -fx-border-style: solid; -fx-border-radius: 10.0; -fx-border-insets: 1.0 1.0 1.0 1.0, 0.0;");
				} else {
					// not focused
					sound.setStyle("");
				}
			}
		});
		sound.getChildren().add(soundlabel);
		sound.getChildren().add(soundslider);

		VBox.setMargin(resume, new Insets(1080 / 50, 20, 1080 / 50, 20));
		VBox.setMargin(save, new Insets(0, 20, 1080 / 50, 20));
		VBox.setMargin(savequit, new Insets(0, 20, 1080 / 50, 20));
		VBox.setMargin(music, new Insets(0, 20, 1080 / 50, 20));
		VBox.setMargin(sound, new Insets(0, 20, 1080 / 50, 20));
		menuWindow.getChildren().addAll(resume, save, savequit, music, sound);
		menuVBox.getChildren().add(menuWindow);

		// Menu Button
		VBox menuButtonVBox = new VBox();
		Button menuButton = new Button();
		menuButton.setId("menuButton");
		menuButton.setPrefSize(1920 / 15, 1080 / 20);
		menuButton.setText("Menu");
		menuButton.setFocusTraversable(false);
		VBox.setMargin(menuButton, new Insets(10, 0, 0, 10));
		menuButtonVBox.getChildren().add(menuButton);

		// ESC Button
		VBox escButtonVBox = new VBox();
		ImageView esc = new ImageView("file:pictures/esc.png");
		esc.setFitHeight(100);
		esc.setFitWidth(100);
		Button escbutton = new Button();
		escbutton.setId("escButton");
		escbutton.setGraphic(esc);
		escbutton.setFocusTraversable(false);
		VBox.setMargin(escbutton, new Insets(0, 0, 0, 10));
		escButtonVBox.getChildren().add(escbutton);
		menu.setTop(menuButtonVBox);

		// AnimationTimer
		Game game = new Game(volumesound, world, worldData[0].length, worldData[0][0].length, score, health, y, x,
				inventarVBoxes, player, playerSprite, activeTiles, textures, healthbar, heartTextures);
		AnimationTimer animation = new AnimationTimer() {
			long lastTick = 0;

			public void handle(long now) {

				if (now - lastTick > 1000000 * 10) {
					lastTick = now;

					if (!primaryStage.isFocused()) {
						menu.setTop(escButtonVBox);
						menu.setCenter(menuVBox);
						super.stop();
						pause(game);
					}
					game.update();
					if (game.player.getHealth() <= 0) {
						super.stop();
						game.exit();
						dead(world);
					}
					scoreLabel.setText("Score: " + game.getScore() + " ");
					int ycord = game.player.getY();
					int xcord = game.player.getX();
					int ymargin = game.player.getYmargin();
					int xmargin = game.player.getXmargin();
					for (int i = 0; i < tileHeight; i++) {
						for (int j = 0; j < tileWidth; j++) {
							activeTiles[i][j].setImage(textures[worldData[world][i + ycord][j + xcord]]);
						}
					}
					VBox.setMargin(tiles, new Insets(ymargin, 0, 0, xmargin));
				}
			}
		};
		// Events
		// Menu Button Event
		inMenuInGame = false;
		inInventar = false;

		// Resumebutton Event
		resume.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				menu.setTop(menuButtonVBox);
				menu.setCenter(null);
				animation.start();
				inMenuInGame = false;
			}
		});

		// Savebutton Event
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				saveWorldData(world);
				game.save();
				menu.setTop(menuButtonVBox);
				menu.setCenter(null);
				animation.start();
				inMenuInGame = false;
			}
		});

		// Save&Quitbutton Event
		savequit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				saveWorldData(world);
				game.save();
				game.exit();
				changeMenu();
				menu();
			}
		});

		// Muiscslider Event
		musicslider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				volumemusic = (double) newValue;
				mediaView.getMediaPlayer().setVolume(volumemusic);
				setSettings();
			}
		});

		// Soundslider Event
		soundslider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				volumesound = (double) newValue;
				game.setVolumesound(volumesound);
				setSettings();
			}
		});

		// Menubutton Event
		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				menu.setTop(escButtonVBox);
				menu.setCenter(menuVBox);
				animation.stop();
				pause(game);
			}
		});

		// ESC Picture Event
		escbutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				menu.setTop(menuButtonVBox);
				menu.setCenter(null);
				inventarBorderPane.setCenter(null);
				animation.start();
				inMenuInGame = false;
				inInventar = false;
			}
		});

		// Mousescroll Event
		play.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (!inMenuInGame && !game.isNumber0Pushed() && !game.isNumber1Pushed() && !game.isNumber2Pushed()
						&& !game.isNumber3Pushed() && !game.isNumber4Pushed() && !game.isNumber5Pushed()
						&& !game.isNumber6Pushed() && !game.isNumber7Pushed() && !game.isNumber8Pushed()
						&& !game.isNumber9Pushed()) {
					game.changeActiveItem(event);
				}
			}
		});

		tilesBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				System.out.println(event.getSceneX() + " " + event.getSceneY());
			}
		});

		// Keys
		play.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				switch (key) {
				case ESCAPE:
					if (!inMenuInGame) {
						menu.setTop(escButtonVBox);
						menu.setCenter(menuVBox);
						animation.stop();
						pause(game);
					} else {
						menu.setTop(menuButtonVBox);
						menu.setCenter(null);
						inventarBorderPane.setCenter(null);
						animation.start();
						inMenuInGame = false;
						inInventar = false;
					}
					break;
				case E:
					if (!inInventar && !inMenuInGame) {
						inventarBorderPane.setCenter(maincenter);
						menu.setTop(escButtonVBox);
						inMenuInGame = true;
						inInventar = true;
					} else if (inInventar) {
						menu.setTop(menuButtonVBox);
						inventarBorderPane.setCenter(null);
						inMenuInGame = false;
						inInventar = false;
					}
					break;
				}
				if (!inMenuInGame || inInventar) {
					switch (key) {
					case W:
						game.setW(true);
						break;
					case UP:
						game.setUp(true);
						break;
					case S:
						game.setS(true);
						break;
					case DOWN:
						game.setDown(true);
						break;
					case A:
						game.setA(true);
						break;
					case LEFT:
						game.setLeft(true);
						break;
					case D:
						game.setD(true);
						break;
					case RIGHT:
						game.setRight(true);
						break;
					case SHIFT:
						game.setShift(true);
						break;
					case DIGIT1:
						game.setActiveItem(0);
						game.setNumber1Pushed(true);
						break;
					case DIGIT2:
						game.setActiveItem(1);
						game.setNumber2Pushed(true);
						break;
					case DIGIT3:
						game.setActiveItem(2);
						game.setNumber3Pushed(true);
						break;
					case DIGIT4:
						game.setActiveItem(3);
						game.setNumber4Pushed(true);
						break;
					case DIGIT5:
						game.setActiveItem(4);
						game.setNumber5Pushed(true);
						break;
					case DIGIT6:
						game.setActiveItem(5);
						game.setNumber6Pushed(true);
						break;
					case DIGIT7:
						game.setActiveItem(6);
						game.setNumber7Pushed(true);
						break;
					case DIGIT8:
						game.setActiveItem(7);
						game.setNumber8Pushed(true);
						break;
					case DIGIT9:
						game.setActiveItem(8);
						game.setNumber9Pushed(true);
						break;
					case DIGIT0:
						game.setActiveItem(9);
						game.setNumber0Pushed(true);
						break;
					}
				}
			}
		});

		play.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				KeyCode key = t.getCode();
				if (!inMenuInGame || inInventar) {
					switch (key) {
					case W:
						game.setW(false);
						break;
					case UP:
						game.setUp(false);
						break;
					case S:
						game.setS(false);
						break;
					case DOWN:
						game.setDown(false);
						break;
					case A:
						game.setA(false);
						break;
					case LEFT:
						game.setLeft(false);
						break;
					case D:
						game.setD(false);
						break;
					case RIGHT:
						game.setRight(false);
						break;
					case SHIFT:
						game.setShift(false);
						break;
					case DIGIT1:
						game.setActiveItem(0);
						game.setNumber1Pushed(false);
						break;
					case DIGIT2:
						game.setActiveItem(1);
						game.setNumber2Pushed(false);
						break;
					case DIGIT3:
						game.setActiveItem(2);
						game.setNumber3Pushed(false);
						break;
					case DIGIT4:
						game.setActiveItem(3);
						game.setNumber4Pushed(false);
						break;
					case DIGIT5:
						game.setActiveItem(4);
						game.setNumber5Pushed(false);
						break;
					case DIGIT6:
						game.setActiveItem(5);
						game.setNumber6Pushed(false);
						break;
					case DIGIT7:
						game.setActiveItem(6);
						game.setNumber7Pushed(false);
						break;
					case DIGIT8:
						game.setActiveItem(7);
						game.setNumber8Pushed(false);
						break;
					case DIGIT9:
						game.setActiveItem(8);
						game.setNumber9Pushed(false);
						break;
					case DIGIT0:
						game.setActiveItem(9);
						game.setNumber0Pushed(false);
						break;
					}
				}
			}
		});

		// Item select
		for (int i = 0; i < inventarVBoxes.length; i++) {
			for (int j = 0; j < inventarVBoxes[i].length; j++) {
				if (i != 0 || j != 0) {

					inventarVBoxes[i][j].getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent t) {
							t.getPickResult().getIntersectedNode().getParent().setId("selectedItemVBox");
						}
					});

					inventarVBoxes[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent t) {
							t.getPickResult().getIntersectedNode().setId("selectedItemVBox");
						}
					});
				}
			}
		}

		rootGame.getChildren().addAll(tilesBox, mobsVBox, main, inventarBorderPane, menu);
		primaryStage.setScene(play);
		animation.start();
		//throw new Exception();

		}catch(Exception e) {
			throw new gameexception("Looks like somthing went wrong please restart your game!!!");
		}
	}

	public void pause(Game game) {
		System.out.println("Pausing Game");
		inMenuInGame = true;
		game.pause();
	}

	public void dead(int world) {
		System.out.println("Looks like you're dead");
		Label label = new Label("Game Over");
		label.setId("gameover");
		Button button = new Button("Back to menu.");
		button.setId("menubutton");
		button.setPrefSize(width / 3, height / 20);
		button.setOnAction(e -> {
			changeMenu();
			menu();
		});
		deleteWorld(world);
		changeMenu();
		centerVBox.getChildren().addAll(label, button);
		primaryStage.setScene(menu);
		MediaPlayer mediaPlayer = new MediaPlayer(new Media(Paths.get("sounds/gameover.mp3").toUri().toString()));
		mediaPlayer.play();
	}

	public static void main(String[] args) {
		launch(args);
	}
}