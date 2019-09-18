import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectedComponents {

	int numRows, numCols, minVal, maxVal, newMin, newMax, newLabel, numNb;
	int minLabel, maxLabel;
	int [] [] zeroFramedAry;
	ArrayList<Integer> nonZeroNeighbor;
	int [] EQAry;
	Property [] CCProperty;

	public ConnectedComponents (Scanner inFile){
		numRows = inFile.nextInt();
		numCols = inFile.nextInt();
		minVal = inFile.nextInt();
		maxVal = inFile.nextInt();
		EQAry = new int [numRows*numCols];
		for (int i = 0; i < EQAry.length; i++)
			EQAry[i] = i;
		zeroFramedAry = new int[numRows+2][numCols+2];
		frameAry(inFile);
	}

	private void frameAry (Scanner inFile) {
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				zeroFramedAry[i+1][j+1] = inFile.nextInt();
	}

	public void prettyPrint (PrintWriter outFile, int passNum) {
		outFile.println("----------------------------------------------------------------------------------------");
		outFile.println("PASS " + passNum + ":\n");
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++) {
				if (zeroFramedAry[i][j] > 0)
					outFile.print(zeroFramedAry[i][j] + ((zeroFramedAry[i][j] < 10) ? "  " : " "));
				else outFile.print("   ");
			} outFile.println("\n");
		}
		printEQAry(outFile);
		outFile.println("----------------------------------------------------------------------------------------");
	}

	public void pass1 () {
		newLabel = 1;
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++) {
				if (zeroFramedAry[i][j] > 0) {
					minLabel = 999;
					numNb = loadNonZero (1, i, j);
					if (numNb == 0) {
						zeroFramedAry[i][j] = newLabel;
						newLabel++;
					}
					else if (numNb == 1)
						zeroFramedAry [i][j] = minLabel;
					else if (numNb > 1) {
						zeroFramedAry[i][j] = minLabel;
						updateEQ();
					}
				}
			}
		}
	}

	public void printEQAry (PrintWriter outFile) {
		outFile.print("\n\nEQARY:\nIndex: ");
		for (int i = 0; i < newLabel; i++)
			outFile.print(i +  " ");
		outFile.print("\nLabel: ");
		for (int i = 0; i < newLabel; i++)
			outFile.print(EQAry[i] +  ((i > 9 && EQAry[i] <= 9) ? "  " : " "));
		outFile.println();
	}

	private void processPixel (int row, int col) {
		int currentPixel = zeroFramedAry[row][col];
		if (currentPixel > 0 && !nonZeroNeighbor.contains(currentPixel)) {
			nonZeroNeighbor.add(currentPixel);
			if (currentPixel < minLabel)
				minLabel = currentPixel;
		}
	}

	public int loadNonZero (int whichPass, int i, int j) {
		nonZeroNeighbor = new ArrayList<>();
		if (whichPass == 1) {
			for (int row = i - 1; row <= i; row++) {
				for (int col = j - 1; col <= j + 1; col++) {
					if (row == i && col == j) break;
					processPixel(row,col);
				}
			}
		}
		else {
			for (int row = i + 1; row >= i; row--) {
				for (int col = j + 1; col >= j - 1; col--) {
					processPixel(row,col);
					if (row == i && col == j) break;
				}
			}
		}
		int numDiffLabels = nonZeroNeighbor.size();
		if (numDiffLabels == 0)
			minLabel = 0;
		return numDiffLabels;
	}

	private void updateEQ () {
		for (int i = 0; i < nonZeroNeighbor.size(); i++)
			EQAry[nonZeroNeighbor.get(i)] = minLabel;
	}

	public void pass2 () {
		for (int i = numRows; i > 0; i--) {
			for (int j = numCols; j > 0; j--) {
				if (zeroFramedAry[i][j] > 0) {
					minLabel = 999;
					numNb = loadNonZero(2, i, j);
					if (numNb > 1) {
						zeroFramedAry[i][j] = minLabel;
						updateEQ();
					}
				}
			}
		}
	}

	public void manageEQAry () {
		int label = 1;
		for (int i = 1; i < newLabel; i++) {
			if (i == EQAry[i]) {
				EQAry[i] = label;
				label++;
			}
			else EQAry[i] = EQAry[EQAry[i]];
		}
		maxLabel = --label;
	}

	public void pass3 () {
		newMin = 999;
		newMax = 0;
		CCProperty = new Property[maxLabel+1];
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++) {
				int currentPixel = zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];
				if (currentPixel < newMin)
					newMin = currentPixel;
				else if (currentPixel > newMax)
					newMax = currentPixel;
				if (currentPixel > 0) {
					if (CCProperty[currentPixel] == null)
						CCProperty[currentPixel] = new Property(currentPixel, 1, i, j, i, j);
					else {
						Property currentProperty = CCProperty[currentPixel];
						currentProperty.numPixels++;
						if (i < currentProperty.minRow) currentProperty.minRow = i;
						else if (i > currentProperty.maxRow) currentProperty.maxRow = i;
						if (j < currentProperty.minCol) currentProperty.minCol = j;
						else if (j > currentProperty.maxCol) currentProperty.maxCol = j;
					}
				}
			}
		}
		for (int i = 1; i < CCProperty.length; i++) {
			Property current = CCProperty[i];
			current.minRow -= 1;
			current.minCol -= 1;
			current.maxRow -= 1;
			current.maxCol -= 1;
		}
	}

	public void printFinalState (PrintWriter outFile) {
		outFile.println(numRows + " " + numCols + " " + newMin + " " + newMax);
		for (int i = 1; i <= numRows; i++) {
			for (int j = 1; j <= numCols; j++)
				outFile.print(zeroFramedAry[i][j] + " ");
			outFile.println();
		}
	}

	public void printCCProperty (PrintWriter outFile) {
        for (int i = 1; i < CCProperty.length; i++) {
            Property current = CCProperty[i];
            outFile.println("Property " + i + ":\n\tLabel: "
                    + current.label + "\n\tNumber of Pixels: " + current.numPixels
                    + "\n\tMin Row: " + current.minRow + "\n\tMin Col:"
                    + current.minCol + "\n\tMax Row: " + current.maxRow
                    + "\n\tMax Col: " + current.maxCol);
        }
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
			CC.prettyPrint(outFile1,1);
			CC.pass2();
			CC.prettyPrint(outFile1,2);
			CC.manageEQAry();
			CC.printEQAry(outFile1);
			CC.pass3();
			CC.prettyPrint(outFile1,3);
			CC.printFinalState(outFile2);
			CC.printCCProperty(outFile3);
		} catch (FileNotFoundException e) {
			System.out.println("One or more input files not found.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

class Property {

	int label, numPixels, minRow, minCol, maxRow, maxCol;

    public Property(int label, int numPixels, int minRow, int minCol, int maxRow, int maxCol) {
        this.label = label;
        this.numPixels = numPixels;
        this.minRow = minRow;
        this.minCol = minCol;
        this.maxRow = maxRow;
        this.maxCol = maxCol;
    }

}