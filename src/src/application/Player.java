package application;

public class Player {
	int y;
	int x;
	int ymargin;
	int xmargin;
	double health;
	double lastHealth;
	int worldHeight;
	int worldLength;
	final int speed=5;
	final int tileHeight = 24;
	final int tileWidth = 42;
	
	public Player(int y, int x, int worldHeight, int worldLength, double health) {
		setY(y);
		setX(x);
		setWorldHeight(worldHeight);
		setWorldLength(worldLength);
		setHealth(health);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getYmargin() {
		return ymargin;
	}

	public void setYmargin(int ymargin) {
		this.ymargin = ymargin;
	}

	public int getXmargin() {
		return xmargin;
	}

	public void setXmargin(int xmargin) {
		this.xmargin = xmargin;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}
	
	public double getLastHealth() {
		return lastHealth;
	}

	public void setLastHealth(double lastHealth) {
		this.lastHealth = lastHealth;
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

	public void up(double multiplicator) {
		if(y>0) {
			ymargin+=(speed*multiplicator);
			if(ymargin>=100) {
				y--;
				ymargin=0;
			}
		}
	}
	
	public void left(double multiplicator) {
		if(x>0) {
			xmargin+=(speed*multiplicator);
			if(xmargin>=100) {
				x--;
				xmargin=0;
			}
		}
	}

	public void right(double multiplicator) {
		if(x<worldLength-tileWidth)
		{
			xmargin-=(speed*multiplicator);
			if(xmargin<=0) {
				x++;
				xmargin=100;
			}
		}
	}

	public void down(double multiplicator) {
		if(y<worldHeight-tileHeight) {
			ymargin-=(speed*multiplicator);
			if(ymargin<=0) {
				y++;
				ymargin=100;
			}
		}
	}
}
