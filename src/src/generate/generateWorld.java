package generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class generateWorld {
	int world[][];

	public generateWorld() {
		world = new int[100][100];
		generate();
		File file = new File("worldparts/part4.txt");
		File saveTarget = new File(file.toURI());
		try (FileWriter fw = new FileWriter(saveTarget); BufferedWriter bw = new BufferedWriter(fw);) {
			for (int i = 0; i < world.length; i++) {
				for (int j = 0; j < world[i].length; j++) {
					bw.write(world[i][j] + "");
					if (j != world[i].length - 1) {
						bw.write(":");
					}
				}
				if (i != world.length - 1) {
					bw.write(",");
				}
			}
			bw.flush();
		} catch (IOException exc) {
			System.out.println("Didnt save properly");
		}
	}

	private void generate() {
		System.out.println("TEST");
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[i].length; j++) {
				world[i][j] = 2;
			}
		}
		// Streets
		for (int i = 0; i < world.length; i++) {
			world[i][43] = 3;
			world[i][44] = 3;
			world[i][45] = 3;
			world[i][46] = 3;
		}
		for (int i = 0; i < world[0].length; i++) {
			world[43][i] = 3;
			world[44][i] = 3;
			world[45][i] = 3;
			world[46][i] = 3;
		}
		// TEICH
		int teich[][] = new int[10][10];
		for (int i = 0; i < teich.length; i++) {
			for (int j = 0; j < teich[i].length; j++) {
				teich[i][j] = 2;
			}
		}
		teich[1][2] = 5;
		teich[1][3] = 4;
		teich[1][4] = 4;
		teich[1][5] = 4;
		teich[1][6] = 4;
		teich[1][7] = 4;
		teich[1][8] = 4;

		teich[2][1] = 5;
		teich[2][2] = 4;
		teich[2][3] = 4;
		teich[2][4] = 4;
		teich[2][5] = 4;
		teich[2][6] = 4;
		teich[2][7] = 4;
		teich[2][8] = 4;

		teich[3][1] = 4;
		teich[3][2] = 4;
		teich[3][3] = 4;
		teich[3][4] = 1;
		teich[3][5] = 1;
		teich[3][6] = 1;
		teich[3][7] = 1;
		teich[3][8] = 4;

		teich[4][1] = 4;
		teich[4][2] = 1;
		teich[4][3] = 1;
		teich[4][4] = 1;
		teich[4][5] = 1;
		teich[4][6] = 1;
		teich[4][7] = 1;
		teich[4][8] = 4;

		teich[5][1] = 4;
		teich[5][2] = 1;
		teich[5][3] = 1;
		teich[5][4] = 1;
		teich[5][5] = 1;
		teich[5][6] = 1;
		teich[5][7] = 4;
		teich[5][8] = 4;

		teich[6][1] = 4;
		teich[6][2] = 4;
		teich[6][3] = 1;
		teich[6][4] = 1;
		teich[6][5] = 1;
		teich[6][6] = 4;
		teich[6][7] = 4;
		teich[6][8] = 4;

		teich[7][1] = 5;
		teich[7][2] = 4;
		teich[7][3] = 4;
		teich[7][4] = 4;
		teich[7][5] = 4;
		teich[7][6] = 4;
		teich[7][7] = 4;
		teich[7][8] = 5;

		// LAVA SEE
		int lava[][] = new int[10][10];
		for (int i = 0; i < lava.length; i++) {
			for (int j = 0; j < lava[i].length; j++) {
				lava[i][j] = 2;
			}
		}
		lava[1][3] = 9;
		lava[1][4] = 8;
		lava[1][5] = 8;
		lava[1][6] = 8;

		lava[2][2] = 8;
		lava[2][3] = 8;
		lava[2][4] = 0;
		lava[2][5] = 0;
		lava[2][6] = 8;
		lava[2][7] = 8;

		lava[3][1] = 8;
		lava[3][2] = 8;
		lava[3][3] = 0;
		lava[3][4] = 0;
		lava[3][5] = 0;
		lava[3][6] = 0;
		lava[3][7] = 8;
		lava[3][8] = 9;

		lava[4][0] = 8;
		lava[4][1] = 8;
		lava[4][2] = 0;
		lava[4][3] = 0;
		lava[4][4] = 0;
		lava[4][5] = 0;
		lava[4][6] = 0;
		lava[4][7] = 8;
		lava[4][8] = 8;

		lava[5][0] = 8;
		lava[5][1] = 8;
		lava[5][2] = 0;
		lava[5][3] = 0;
		lava[5][4] = 0;
		lava[5][5] = 0;
		lava[5][6] = 0;
		lava[5][7] = 0;
		lava[5][8] = 8;
		lava[5][9] = 9;

		lava[6][1] = 8;
		lava[6][2] = 8;
		lava[6][3] = 0;
		lava[6][4] = 0;
		lava[6][5] = 0;
		lava[6][6] = 0;
		lava[6][7] = 0;
		lava[6][8] = 8;

		lava[7][1] = 9;
		lava[7][2] = 8;
		lava[7][3] = 8;
		lava[7][4] = 8;
		lava[7][5] = 0;
		lava[7][6] = 0;
		lava[7][7] = 0;
		lava[7][8] = 8;

		lava[8][2] = 9;
		lava[8][3] = 8;
		lava[8][4] = 8;
		lava[8][5] = 8;
		lava[8][6] = 8;
		lava[8][7] = 8;
		lava[8][8] = 8;

		lava[9][3] = 9;
		lava[9][8] = 9;

		Random random = new Random();

		// FRIEDHOF
		int graveyard[][] = new int[10][10];
		for (int i = 0; i < graveyard.length; i++) {
			for (int j = 0; j < graveyard[i].length; j++) {
				if (i == 3 || i == 6 || j == 3 || j == 6) {
					int randomnumber = random.nextInt(2);
					graveyard[i][j] = 6 + randomnumber;
				}
				if (i == 4 || i == 5 || j == 4 || j == 5) {
					graveyard[i][j] = 3;
				}
				if (i != 3 && i != 6 && j != 3 && j != 6 && i != 4 && i != 5 && j != 4 && j != 5) {
					graveyard[i][j] = 2;
				}
			}
		}

		int index = 0;
		while (index <= 60) {
			int randomnumbery = random.nextInt(10);
			int randomnumberx = random.nextInt(10);
			int randompart = random.nextInt(3);
			if (randomnumbery != 4 && randomnumberx != 4) {
				if (randompart == 0) {
					for (int i = 0; i < teich.length; i++) {
						for (int j = 0; j < teich[i].length; j++) {
							world[i + randomnumbery * 10][j + randomnumberx * 10] = teich[i][j];
						}
					}
				} else if (randompart == 1) {
					for (int i = 0; i < graveyard.length; i++) {
						for (int j = 0; j < graveyard[i].length; j++) {
							world[i + randomnumbery * 10][j + randomnumberx * 10] = graveyard[i][j];
						}
					}
				} else {
					for (int i = 0; i < lava.length; i++) {
						for (int j = 0; j < lava[i].length; j++) {
							world[i + randomnumbery * 10][j + randomnumberx * 10] = lava[i][j];
						}
					}
				}
				index++;
			}
		}
	}

	public static void main(String[] args) {
		generateWorld world = new generateWorld();

	}

	public int[][] getWorld() {
		return world;
	}

	public void setWorld(int[][] world) {
		this.world = world;
	}
}
