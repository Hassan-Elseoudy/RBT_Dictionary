package pkjRBT;

import java.util.Scanner;

public class Driver {

	public static final int INPUT_SIZE = 10; // the number of numbers we want to try to insert

	public static void main(String[] args) throws Exception {
		RedBlackTree<String> t = new RedBlackTree<String>(); // Create a new RBT
		Scanner s = new Scanner(System.in);
		// This will reference one line at a time
		// int deletions = 0; // number of successful deletions
		Dictionary.loadADictionary(t);
		t.printPreOrder();
		System.out.println("Max depth -> " + t.getMaxDepth(t) + " Black height -> " + t.getBlackHeight());
		Dictionary.printDictionarySize(t);
		int choice = 0;
		while (choice != 3) {
			System.out.println(
					"\n------------------- \nEnter your choice ? \n0. Look up a word \n1. remove a word \n2. insert a word \n3. End!\n-------------------");
			choice = s.nextInt();
			if (choice == 0) {
				String tst = s.next();
				System.out.println(Dictionary.lookUpAWord(t, tst));
			} else if (choice == 1) {
				String tst = s.next();
				Dictionary.removeAWord(t, tst);
			} else if (choice == 2) {
				String tst = s.next();
				Dictionary.insertAWord(t, tst);
			}
			t.printPreOrder();
			System.out.println("Max depth -> " + t.getMaxDepth(t) + " Black height -> " + t.getBlackHeight());
			Dictionary.printDictionarySize(t);
		}
		s.close();
	}
}