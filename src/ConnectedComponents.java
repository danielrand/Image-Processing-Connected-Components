import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConnectedComponents {

	int numRows, numCols, minVal, maxVal, newMin, newMax, newLabel, numNb;
	int [] [] zeroFramedAry;
	int [] nonZeroNeighbor;
	int [] EQAry;
	Property [] CCProperty;

	public ConnectedComponents (Scanner inFile){
		numRows = inFile.nextInt();
		numCols = inFile.nextInt();
		minVal = inFile.nextInt();
		maxVal = inFile.nextInt();
		zeroFramedAry = new int[numRows+2][numCols+2];
		frameAry(inFile);
	}

	private void frameAry (Scanner inFile) {
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				zeroFramedAry[i+1][j+1] = inFile.nextInt();
		/*for (int i = 0; i < numRows+2; i++) {
			for (int j = 0; j < numCols+2; j++) {
				System.out.print(zeroFramedAry[i][j] + " ");
			} System.out.println();
		}*/
	}

	public void pass1 () {
		newLabel = 1;
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++) {
				if (zeroFramedAry[i][j] > 0) {
					numNb = loadNonZero (1, i, 0, min, min)
				}
			}
		}
	}

	public void pass2 () {

	}

	public void manageEQAry () {

	}

	public void pass3 () {

	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("ERROR: Illegal arguments");
			System.exit(1);
		}
		try {
			Scanner inFile = new Scanner(new FileReader(args[0]));
			PrintWriter outFile1 = new PrintWriter(new BufferedWriter(new FileWriter(args[1])), true);
			PrintWriter outFile2 = new PrintWriter(new BufferedWriter(new FileWriter(args[2])), true);
			PrintWriter outFile3 = new PrintWriter(new BufferedWriter(new FileWriter(args[3])), true);
			ConnectedComponents CC = new ConnectedComponents(inFile);
			CC.pass1();
			CC.pass2();
			CC.manageEQAry();
			CC.pass3();

		} catch (FileNotFoundException e) {
			System.out.println("One or more input files not found.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
class Property {
	int label, numPixels, minRow, minCol, maxRow, maxCol;

	public Property () {
		label = numPixels = minRow = minCol = maxRow = maxCol = 0;
	}
}
