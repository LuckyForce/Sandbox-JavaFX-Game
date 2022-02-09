package application;

import java.nio.file.Paths;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Game {
	int counterStandingOn;
	int counterRegeneration;
	Player player;
	double volumesound;
	int world;
	int worldHeight;
	int worldLength;
	int score;
	boolean w;
	boolean s;
	boolean a;
	boolean d;
	boolean up;
	boolean down;
	boolean left;
	boolean right;
	boolean shift;
	boolean number1Pushed;
	boolean number2Pushed;
	boolean number3Pushed;
	boolean number4Pushed;
	boolean number5Pushed;
	boolean number6Pushed;
	boolean number7Pushed;
	boolean number8Pushed;
	boolean number9Pushed;
	boolean number0Pushed;
	int idleCounter;
	int activeItem;
	VBox[][] inventarVBoxes;
	ImageView playerImageView;
	Image[] playerSprite;
	ImageView[][] activeTiles;
	Image[] textures;
	VBox[] healthbar;
	Image[] heartTextures;
	final int tileHeight = 24;
	final int tileWidth = 42;
	final int xplayer = (tileWidth * 25) - 47;
	final int yplayer = (tileHeight * 25) - 50;
	MediaPlayer lava;
	MediaPlayer water;
	final Media lavaDamage = new Media(Paths.get("sounds/lavaDamage.mp3").toUri().toString()); 

	public Game(double volumesound, int world, int worldHeight, int worldLength, int score, double health, int y, int x,
			VBox[][] inventarVBoxes, ImageView playerImageView, Image[] playerSprite, ImageView[][] activeTiles,
			Image[] textures, VBox[] healthbar, Image[] heartTextures) {
		player = new Player(y, x, worldHeight, worldLength, health);
		this.volumesound = volumesound;
		setW(false);
		setS(false);
		setA(false);
		setD(false);
		setUp(false);
		setDown(false);
		setLeft(false);
		setRight(false);
		setShift(false);
		this.activeItem = 0;
		setInventarVBoxes(inventarVBoxes);
		setIdleCounter(0);
		setPlayerImageView(playerImageView);
		setPlayerSprite(playerSprite);
		setActiveTiles(activeTiles);
		setTextures(textures);
		setHealthbar(healthbar);
		setHeartTextures(heartTextures);
		lava = new MediaPlayer(new Media(Paths.get("sounds/lava.mp3").toUri().toString()));
		lava.setAutoPlay(true);
		lava.setCycleCount(Integer.MAX_VALUE);
		water = new MediaPlayer(new Media(Paths.get("sounds/water.mp3").toUri().toString()));
		water.setAutoPlay(true);
		water.setCycleCount(Integer.MAX_VALUE);
	}

	public void save() {

	}

	public void pause() {
		setW(false);
		setUp(false);
		setS(false);
		setDown(false);
		setA(false);
		setLeft(false);
		setD(false);
		setRight(false);
	}
	
	public void exit() {
		lava.pause();
		water.pause();
	}

	public void update() {
		updatePlayer();
		bloeckeInNaehe();
		if (counterStandingOn >= 50) {
			standingOn();
			counterStandingOn = 0;
		}
		counterStandingOn++;
		if (counterRegeneration >= 200) {
			if (player.getHealth() < 20) {
				if (player.getLastHealth() == player.getHealth()) {
					player.setHealth(player.getHealth() + 1);
				} else {
					player.setLastHealth(player.getHealth());
				}
			}
			if (player.getHealth() > 20) {
				player.setHealth(20);
			}
			counterRegeneration = 0;
		}
		counterRegeneration++;
		updateHealth();
	}

	public void updateHealth() {
		int heartCounter = 0;
		for (int i = 0; i < 10; i++) {
			if (heartCounter < player.getHealth() - 1) {
				healthbar[i].getChildren().set(0, new ImageView(heartTextures[0]));
				heartCounter += 2;
			} else if (heartCounter + 1 == player.getHealth()) {
				healthbar[i].getChildren().set(0, new ImageView(heartTextures[1]));
				heartCounter += 2;
			} else {
				healthbar[i].getChildren().set(0, new ImageView(heartTextures[2]));
				heartCounter += 2;
			}
		}
	}

	public void standingOn() { // BROKEN
		boolean lavaInNaehe = false;
		boolean thorns = false;
		int radiusx = 40;
		int radiusy = 40;
		for (int i = 0; i < activeTiles.length; i++) {
			for (int j = 0; j < activeTiles[i].length; j++) {
				if (activeTiles[i][j].getLayoutY() + 50 < yplayer + 95 + radiusy
						&& activeTiles[i][j].getLayoutX() + 50 < xplayer + 50 + radiusx
						&& activeTiles[i][j].getLayoutY() + 50 > yplayer + 95 - radiusy
						&& activeTiles[i][j].getLayoutX() + 50 > xplayer + 50 - radiusx) {
					if (activeTiles[i][j].getImage() == textures[0]) {
						lavaInNaehe = true;
						// System.out.println((activeTiles[i][j].getLayoutY()-player.getYmargin())+"
						// "+(activeTiles[i][j].getLayoutX()-player.getXmargin())+" "+(yplayer+45)+"
						// "+(xplayer+50));
					}
					if (activeTiles[i][j].getImage() == textures[5]||activeTiles[i][j].getImage() == textures[9]) {
						thorns = true;
					}
				}
			}
		}
		if (lavaInNaehe) {
			if (player.getHealth() > 0) {
				new MediaPlayer(lavaDamage).play();
				player.health--;
			}
		}
		if (thorns) {
			if (player.getHealth() > 0) {
				new MediaPlayer(lavaDamage).play();
				player.health-=0.5;
			}
		}
	}

	public void bloeckeInNaehe() {
		boolean lavaInNaehe = false;
		boolean waterInNaehe = false;
		int radius = 400;
		double lavaNaeheX = radius;
		double lavaNaeheY = radius;
		double waterNaeheX = radius;
		double waterNaeheY = radius;
		for (int i = 0; i < activeTiles.length; i++) {
			for (int j = 0; j < activeTiles[i].length; j++) {
				if (activeTiles[i][j].getLayoutY() < yplayer + radius
						&& activeTiles[i][j].getLayoutX() < xplayer + radius
						&& activeTiles[i][j].getLayoutY() > yplayer - radius
						&& activeTiles[i][j].getLayoutX() > xplayer - radius) {
					
					// Abstand unten
					if (activeTiles[i][j].getLayoutY() < yplayer + radius && activeTiles[i][j].getLayoutY() > yplayer) {
						if (activeTiles[i][j].getImage() == textures[0]
								&& activeTiles[i][j].getLayoutY() - yplayer < lavaNaeheY) {
							lavaNaeheY = activeTiles[i][j].getLayoutY() - yplayer;
							lavaInNaehe = true;
						}
						if (activeTiles[i][j].getImage() == textures[1]
								&& activeTiles[i][j].getLayoutY() - yplayer < waterNaeheY) {
							waterNaeheY = activeTiles[i][j].getLayoutY() - yplayer;
							waterInNaehe = true;
						}
					}
					// Abstand rechts
					if (activeTiles[i][j].getLayoutX() < xplayer + radius && activeTiles[i][j].getLayoutX() > xplayer) {
						if (activeTiles[i][j].getImage() == textures[0]
								&& activeTiles[i][j].getLayoutX() - xplayer < lavaNaeheX) {
							lavaNaeheX = activeTiles[i][j].getLayoutX() - xplayer;
							lavaInNaehe = true;
						}
						if (activeTiles[i][j].getImage() == textures[1]
								&& activeTiles[i][j].getLayoutX() - xplayer < waterNaeheX) {
							waterNaeheX = activeTiles[i][j].getLayoutX() - xplayer;
							waterInNaehe = true;
						}
					}
					// Abstand oben
					if (activeTiles[i][j].getLayoutY() > yplayer - radius && activeTiles[i][j].getLayoutY() < yplayer) {
						if (activeTiles[i][j].getImage() == textures[0]
								&& yplayer - activeTiles[i][j].getLayoutY() < lavaNaeheY) {
							lavaNaeheY = yplayer - activeTiles[i][j].getLayoutY();
							lavaInNaehe = true;
						}
						if (activeTiles[i][j].getImage() == textures[1]
								&& yplayer - activeTiles[i][j].getLayoutY() < waterNaeheY) {
							waterNaeheY = yplayer - activeTiles[i][j].getLayoutY();
							waterInNaehe = true;
						}
					}
					// Abstand links
					if (activeTiles[i][j].getLayoutX() > xplayer - radius && activeTiles[i][j].getLayoutX() < xplayer) {
						if (activeTiles[i][j].getImage() == textures[0]
								&& xplayer - activeTiles[i][j].getLayoutX() < lavaNaeheX) {
							lavaNaeheX = xplayer - activeTiles[i][j].getLayoutX();
							lavaInNaehe = true;
						}
						if (activeTiles[i][j].getImage() == textures[1]
								&& xplayer - activeTiles[i][j].getLayoutX() < waterNaeheX) {
							waterNaeheX = xplayer - activeTiles[i][j].getLayoutX();
							waterInNaehe = true;
						}

					}
				}
			}
		}
		double lavaNaehe = Math.sqrt(Math.pow(lavaNaeheX, 2) + Math.pow(lavaNaeheY, 2));
		double lavaVolume = ((radius - lavaNaehe) / radius) * volumesound;
		double waterNaehe = Math.sqrt(Math.pow(waterNaeheX, 2) + Math.pow(waterNaeheY, 2));
		double waterVolume = ((radius - waterNaehe) / radius) * volumesound;
		if (lavaInNaehe && lavaNaehe > 0) {
			lava.setVolume(lavaVolume);
			lava.play();
		} else {
			lava.pause();
		}
		if (waterInNaehe && waterNaehe > 0) {
			water.setVolume(waterVolume);
			water.play();
		} else {
			water.pause();
		}
	}

	public void updatePlayer() {
		score++;
		if ((isW() || isUp()) && !(isS() || isDown()) && (isA() || isLeft()) && !(isD() || isRight())) {
			up(0.71);
			left(0.71);
			// Später vielleicht schräg gehen
			playerImageView.setImage(playerSprite[2]);
		} else if ((isW() || isUp()) && !(isS() || isDown()) && !(isA() || isLeft()) && (isD() || isRight())) {
			up(0.71);
			right(0.71);
			playerImageView.setImage(playerSprite[3]);
		} else if (!(isW() || isUp()) && (isS() || isDown()) && (isA() || isLeft()) && !(isD() || isRight())) {
			down(0.71);
			left(0.71);
			playerImageView.setImage(playerSprite[2]);
		} else if (!(isW() || isUp()) && (isS() || isDown()) && !(isA() || isLeft()) && (isD() || isRight())) {
			down(0.71);
			right(0.71);
			playerImageView.setImage(playerSprite[3]);
		} else if (!(isW() || isUp()) && (isS() || isDown()) && (isA() || isLeft()) && (isD() || isRight())) {
			down(1);
			playerImageView.setImage(playerSprite[0]);
		} else if ((isW() || isUp()) && !(isS() || isDown()) && (isA() || isLeft()) && (isD() || isRight())) {
			up(1);
			playerImageView.setImage(playerSprite[1]);
		} else if ((isW() || isUp()) && (isS() || isDown()) && (isA() || isLeft()) && (isD() || isRight())) {
			playerImageView.setImage(playerSprite[0]);
		} else if ((isA() || isLeft()) && (isD() || isRight())) {
			playerImageView.setImage(playerSprite[0]);
		} else if ((isW() || isUp()) && (isS() || isDown())) {
			playerImageView.setImage(playerSprite[0]);
		} else if (isW() || isUp()) {
			up(1);
			playerImageView.setImage(playerSprite[1]);
		} else if (isA() || isLeft()) {
			left(1);
			playerImageView.setImage(playerSprite[2]);
		} else if (isS() || isDown()) {
			down(1);
			playerImageView.setImage(playerSprite[0]);
		} else if (isD() || isRight()) {
			right(1);
			playerImageView.setImage(playerSprite[3]);
		}
		if (!(isW() || isUp()) && !(isS() || isDown()) && !(isA() || isLeft()) && !(isD() || isRight())) {
			idleCounter++;
			if (idleCounter > 50) {
				playerImageView.setImage(playerSprite[0]);
			}
		} else {
			idleCounter = 0;
		}
	}

	public double getVolumesound() {
		return volumesound;
	}

	public void setVolumesound(double volumesound) {
		this.volumesound = volumesound;
		bloeckeInNaehe();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getWorld() {
		return world;
	}

	public void setWorld(int world) {
		this.world = world;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public void setWorldHeight(int worldHeight) {
		this.worldHeight = worldHeight;
	}

	public int getWorldLength() {
		return worldLength;
	}

	public void setWorldLength(int worldLength) {
		this.worldLength = worldLength;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void up(double multiplicator) {
		System.out.println("Forwards");
		player.up(multiplicator);
		if (isShift()) {
			player.up(multiplicator);
		}

	}

	public void left(double multiplicator) {
		System.out.println("left");
		player.left(multiplicator);
		if (isShift()) {
			player.left(multiplicator);
		}
	}

	public void right(double multiplicator) {
		System.out.println("right");
		player.right(multiplicator);
		if (isShift()) {
			player.right(multiplicator);
		}
	}

	public void down(double multiplicator) {
		System.out.println("backwards");
		player.down(multiplicator);
		if (isShift()) {
			player.down(multiplicator);
		}
	}

	public boolean isW() {
		return w;
	}

	public void setW(boolean w) {
		this.w = w;
	}

	public boolean isS() {
		return s;
	}

	public void setS(boolean s) {
		this.s = s;
	}

	public boolean isA() {
		return a;
	}

	public void setA(boolean a) {
		this.a = a;
	}

	public boolean isD() {
		return d;
	}

	public void setD(boolean d) {
		this.d = d;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public int getActiveItem() {
		return activeItem;
	}

	public void setActiveItem(int newActiveItem) {
		getInventarVBoxes()[0][getActiveItem()].setId("hotbarItem");
		getInventarVBoxes()[0][newActiveItem].setId("activeItem");
		this.activeItem = newActiveItem;
	}

	public void changeActiveItem(ScrollEvent event) {
		if (event.getDeltaY() > 0 || event.getDeltaX() > 0) {
			if (getActiveItem() == 0) {
				getInventarVBoxes()[0][0].setId("hotbarItem");
				getInventarVBoxes()[0][9].setId("activeItem");
				this.activeItem = 9;
			} else {
				getInventarVBoxes()[0][getActiveItem()].setId("hotbarItem");
				getInventarVBoxes()[0][getActiveItem() - 1].setId("activeItem");
				this.activeItem = activeItem - 1;
			}
		} else {
			if (getActiveItem() == 9) {
				getInventarVBoxes()[0][9].setId("hotbarItem");
				getInventarVBoxes()[0][0].setId("activeItem");
				this.activeItem = 0;
			} else {
				getInventarVBoxes()[0][getActiveItem()].setId("hotbarItem");
				getInventarVBoxes()[0][getActiveItem() + 1].setId("activeItem");
				this.activeItem = activeItem + 1;
			}
		}
	}

	public VBox[][] getInventarVBoxes() {
		return inventarVBoxes;
	}

	public void setInventarVBoxes(VBox[][] inventarVBoxes) {
		this.inventarVBoxes = inventarVBoxes;
	}

	public boolean isNumber1Pushed() {
		return number1Pushed;
	}

	public void setNumber1Pushed(boolean number1Pushed) {
		this.number1Pushed = number1Pushed;
	}

	public boolean isNumber2Pushed() {
		return number2Pushed;
	}

	public void setNumber2Pushed(boolean number2Pushed) {
		this.number2Pushed = number2Pushed;
	}

	public boolean isNumber3Pushed() {
		return number3Pushed;
	}

	public void setNumber3Pushed(boolean number3Pushed) {
		this.number3Pushed = number3Pushed;
	}

	public boolean isNumber4Pushed() {
		return number4Pushed;
	}

	public void setNumber4Pushed(boolean number4Pushed) {
		this.number4Pushed = number4Pushed;
	}

	public boolean isNumber5Pushed() {
		return number5Pushed;
	}

	public void setNumber5Pushed(boolean number5Pushed) {
		this.number5Pushed = number5Pushed;
	}

	public boolean isNumber6Pushed() {
		return number6Pushed;
	}

	public void setNumber6Pushed(boolean number6Pushed) {
		this.number6Pushed = number6Pushed;
	}

	public boolean isNumber7Pushed() {
		return number7Pushed;
	}

	public void setNumber7Pushed(boolean number7Pushed) {
		this.number7Pushed = number7Pushed;
	}

	public boolean isNumber8Pushed() {
		return number8Pushed;
	}

	public void setNumber8Pushed(boolean number8Pushed) {
		this.number8Pushed = number8Pushed;
	}

	public boolean isNumber9Pushed() {
		return number9Pushed;
	}

	public void setNumber9Pushed(boolean number9Pushed) {
		this.number9Pushed = number9Pushed;
	}

	public boolean isNumber0Pushed() {
		return number0Pushed;
	}

	public void setNumber0Pushed(boolean number0Pushed) {
		this.number0Pushed = number0Pushed;
	}

	public int getIdleCounter() {
		return idleCounter;
	}

	public void setIdleCounter(int idleCounter) {
		this.idleCounter = idleCounter;
	}

	public ImageView getPlayerImageView() {
		return playerImageView;
	}

	public void setPlayerImageView(ImageView playerImageView) {
		this.playerImageView = playerImageView;
	}

	public Image[] getPlayerSprite() {
		return playerSprite;
	}

	public void setPlayerSprite(Image[] playerSprite) {
		this.playerSprite = playerSprite;
	}

	public ImageView[][] getActiveTiles() {
		return activeTiles;
	}

	public void setActiveTiles(ImageView[][] activeTiles) {
		this.activeTiles = activeTiles;
	}

	public Image[] getTextures() {
		return textures;
	}

	public void setTextures(Image[] textures) {
		this.textures = textures;
	}

	public VBox[] getHealthbar() {
		return healthbar;
	}

	public void setHealthbar(VBox[] healthbar) {
		this.healthbar = healthbar;
	}

	public Image[] getHeartTextures() {
		return heartTextures;
	}

	public void setHeartTextures(Image[] heartTextures) {
		this.heartTextures = heartTextures;
	}
}
