package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		if(docFile == null) 
			throw new FileNotFoundException("file not found");
		
		Scanner scan = new Scanner(new File(docFile));
		
		HashMap<String,Occurrence> map = new HashMap<String,Occurrence>();
		
		while(scan.hasNext()) {
			String word = scan.next();
			if(getKeyword(word)!=null) {
				
				if(map.containsKey(getKeyword(word))){
					map.get(getKeyword(word)).frequency++;
					
				}
				else
					map.put(getKeyword(word), new Occurrence(docFile,1));
			}
		}
		scan.close();
		return map;
	}

	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
	
		Set<String> keySet = kws.keySet();
		Iterator<String> keys = keySet.iterator();
		
		while(keys.hasNext()) {
			String word = keys.next();
			
			if(keywordsIndex.containsKey(word)) {
				keywordsIndex.get(word).add(kws.get(word));
 				insertLastOccurrence(keywordsIndex.get(word));
 				
			}
			else if(!keywordsIndex.containsKey(word)){
				keywordsIndex.put(word,new ArrayList<Occurrence>());
				keywordsIndex.get(word).add(kws.get(word));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		String result = "";
		for(int i = 0; i<word.length(); i++) {
			
			if(i!=word.length()-1 && !Character.isAlphabetic(word.charAt(i)) && Character.isAlphabetic(word.charAt(i+1))) {
				return null;
				
			}else if(!Character.isAlphabetic(word.charAt(i))) {
				if(word.charAt(i)=='.' || word.charAt(i)==',' || word.charAt(i)=='?' || word.charAt(i)==':' ||
					word.charAt(i)==';' || word.charAt(i)=='!') {
					continue;
				}
				else {
					return null;
				}
			}else {
					result += word.toLowerCase().charAt(i);
			}
		}
		
		//change noise to noiseWords when done implementing everything
		if(noiseWords.contains(result) || result.equals("") || noiseWords.contains(result.concat(" ")))
			return null;
		return result;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> midIndex = new ArrayList<Integer>();
		
		int hi = occs.size()-2, lo = 0, mid = 0, key=occs.get(occs.size()-1).frequency;
		
		while(lo<=hi) {
			mid = (hi+lo)/2;
			midIndex.add(mid);
			if(occs.get(mid).frequency==key) {
				break;
			}else if(occs.get(mid).frequency< key) {
				hi = mid-1;
			}else {
				lo = mid+1;
			}
		}
		Occurrence o = occs.remove(occs.size()-1);
		occs.add(lo,o);
		return midIndex;
	}
	
	
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> search = new ArrayList<String>();
		kw1 = kw1.toLowerCase(); 
		kw2 = kw2.toLowerCase();
		
		if(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			return null;
			
		}else if(!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			for(int i=0; i<5; i++) {
				if(i==keywordsIndex.get(kw2).size()) break;
				search.add(keywordsIndex.get(kw2).get(i).document);
			}
		}else if(keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)) {
			for(int i=0; i<5; i++) {
				if(i==keywordsIndex.get(kw1).size()) break;
				search.add(keywordsIndex.get(kw1).get(i).document);
			}
		
		}else if(keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			int kw1I = 0, kw2I = 0;
			System.out.println(keywordsIndex.get(kw1));
			System.out.println(keywordsIndex.get(kw2));
			while(search.size()<5 && (kw2I<keywordsIndex.get(kw2).size() || kw1I<keywordsIndex.get(kw1).size())) {
				
				if(kw2I==keywordsIndex.get(kw2).size()) {
					if(!search.contains(keywordsIndex.get(kw1).get(kw1I).document)) 
						search.add(keywordsIndex.get(kw1).get(kw1I).document);
					kw1I++;
				}
				else if(kw1I==keywordsIndex.get(kw1).size()) { 
					if(!search.contains(keywordsIndex.get(kw2).get(kw2I).document)) 
						search.add(keywordsIndex.get(kw2).get(kw2I).document);
					kw2I++;
				}
				else if(keywordsIndex.get(kw1).get(kw1I).frequency >= keywordsIndex.get(kw2).get(kw2I).frequency) {
					if(!search.contains(keywordsIndex.get(kw1).get(kw1I).document)) 
						search.add(keywordsIndex.get(kw1).get(kw1I).document);
					kw1I++;
				}
				else {
					if(!search.contains(keywordsIndex.get(kw2).get(kw2I).document)) 
						search.add(keywordsIndex.get(kw2).get(kw2I).document);
					kw2I++;
				}
			}
		}
		return search;
	}
	
}