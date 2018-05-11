package pkjRBT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
	public static String fileName = "dictionary.txt"; // The name of the file to open.

	private boolean chckIfAvailable (RedBlackTree<Integer> t, String str) {
		boolean flag = false; //We predict that word is @ dictionary
		for(int i = 0; i < str.length(); i++ ) {
			if (!t.search((int)str.charAt(i))){
				flag = true;
			}
		}
		return flag;
	}
	public int loadADictionary(RedBlackTree<Integer> t) {
		String line = null;
		int additions = 0; // number of successful additions

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				if(chckIfAvailable(t,line))
					// insert
					
				additions++;
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
		return additions;
	}
}
