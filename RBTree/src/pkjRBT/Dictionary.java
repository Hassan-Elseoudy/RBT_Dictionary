package pkjRBT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
	public static String fileName = "dictionary.txt"; // The name of the file to open.
	public static int words = 0;

	private static boolean chckIfAvailable(RedBlackTree<String> t, String str) {
		boolean flag = true; // We predict that word isn't @ dictionary
		if (t.search(str) == false)
			flag = false;
		return flag;
	}

	public static void loadADictionary(RedBlackTree<String> t) {
		String line = null;
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);
			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if (!chckIfAvailable(t, line)) {
					t.add(line);
					words++;
				} else
					System.out.println("Duplicated!");
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public static void printDictionarySize(RedBlackTree<String> t) {
		System.out.println("Dictionary size is : " + words);
	}

	public static Boolean lookUpAWord(RedBlackTree<String> t, String str) {
		boolean flag = true; // We predict that word isn't @ dictionary
		if (t.search(str) == null)
			flag = false;
		return flag;
	}

	public static void removeAWord(RedBlackTree<String> t, String str) throws Exception {
		if (chckIfAvailable(t, str)) {
			t.remove(str);
			words--;
		} else
			System.out.println("Dictionary doesn't contain required word!");
	}

	public static void insertAWord(RedBlackTree<String> t, String str) {
		if (!chckIfAvailable(t, str)) {
			t.add(str);
			words++;
		} else
			System.out.println("Duplicated!");
	}
}
