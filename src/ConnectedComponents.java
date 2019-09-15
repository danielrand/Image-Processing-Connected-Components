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
