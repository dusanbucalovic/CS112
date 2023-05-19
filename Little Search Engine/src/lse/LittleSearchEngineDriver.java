package lse;

import java.io.*;
import java.util.*;

public class LittleSearchEngineDriver {

	public static void main(String[] args) throws FileNotFoundException  {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a docsFile: ");
		String docsFile = sc.nextLine();
		System.out.println("Enter a noise Words file: ");
		String noiseWords = sc.nextLine();
		LittleSearchEngine little = new LittleSearchEngine();
		little.makeIndex(docsFile, noiseWords);
		
		int choice = -1;
		do {
			System.out.println("----Little Search Engine--- \n 1. Perform a search \n 2. Quit");
			choice = sc.nextInt();
			if(choice == 1) {
				System.out.println("Top 5 Search: ");
				System.out.println("Enter 1st word: ");
				String word1 = sc.next();
				System.out.println("Enter 2nd word: ");
				String word2 = sc.next();
				ArrayList<String> top5 = little.top5search(word1, word2);
				if(top5 == null || top5.isEmpty()) {
					System.out.println("No documents are related to these words");
				}else {
				for(int i = 0; i < top5.size(); i++) {
					System.out.println(top5.get(i));
				}
				}
				System.out.println();
			}
		}while(choice != 2);
		sc.close();
	}
	
}