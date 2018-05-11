package pkjRBT;

import java.util.Scanner;
import java.util.Random;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import pkjRBT.RedBlackTree;

public class Driver {

	public static final int INPUT_SIZE = 10; // the number of numbers we want to try to insert
	
	public static void main(String[] args) {
		RedBlackTree<Integer> t = new RedBlackTree<Integer>(); // Create a new RBT
		// This will reference one line at a time
		int deletions = 0; // number of successful deletions


		if (t.isValid()) {
			System.out.println("Yes!");
			int elements = 0; //additions - deletions;
			int blackHeight = t.getBlackHeight();
			double log2 = (Math.log(((double) elements) + 1.0) / Math.log(2.0));
			DecimalFormat df = new DecimalFormat("#.##"); // for formatting the log2 variable
			df.setRoundingMode(RoundingMode.CEILING); // for formatting the log2 variable
			String s = "The tree has " + elements + " elements and a black height of " + blackHeight + ".\n";
			s += "The black-height of a proper tree should be <= 1 + log base 2 of (n + 1).\n";
			s += "Log base 2 of (n + 1) is " + df.format(log2) + ".\n";
			s += "Also, the height of a red-black tree <= twice the black-height of the tree.\n";
			s += "As we can observe, this means that the height is bounded by O(log n).\n";
			s += "The total height is no more than twice the black-height, or " + (2 * blackHeight) + " in our case.";
			System.out.println(s);
		} else {
			System.out.println("No.");
		}
		t.printPreOrder(); // printing tree?
		t.getBlackHeight(); // getBlackHeight.
		t.search(0); // search for an element.
		t.add(1); // add an element in tree.
		t.remove(1); // remove an element from tree.
		t.isValid(); // check if the tree is valid.
	}
}
