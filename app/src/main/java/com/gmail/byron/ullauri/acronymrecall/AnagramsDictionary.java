package com.gmail.byron.ullauri.acronymrecall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by ullauri on 10/9/17.
 */

public final class AnagramsDictionary implements AcronymWordFinder {
//    private static final boolean BLOCK_SINGLE_LETTERS = true;
//    private static final boolean BLOCK_SWAPPED_WORDS = true;
    private static final String DELIMITER = ";";
    private Map<String, ArrayList<String>> anagrams;


    public AnagramsDictionary(InputStreamReader inputStreamReader) throws IOException {
        anagrams = new HashMap<>();

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String word;
        while ((word = bufferedReader.readLine()) != null) {
            word = word.trim();

            String sortedWord = sortWord(word);

            if (!anagrams.containsKey(sortedWord))
                anagrams.put(sortedWord, new ArrayList<String>());

            anagrams.get(sortedWord).add(word);
        }
    }

    private String sortWord(String word) {
        char[] sortedLetters = word.toCharArray();
        Arrays.sort(sortedLetters);
        return new String(sortedLetters);
    }

    public ArrayList<String> getWords(String acronym) {
        ArrayList<String> choices = new ArrayList<>();
        acronym = sortWord(acronym);

        if (anagrams.containsKey(acronym)) {
            for (String word : anagrams.get(acronym)) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
                choices.add(word);
            }
        }

        HashSet<String> acronym_splits = new HashSet<>();

        for (int i = 0; i < acronym.length(); i++) {
            String split_acronym = acronym.substring(0, i) + DELIMITER + acronym.substring(i);
            acronym_splits.add(split_acronym);

            for (int k = 0; k < acronym.length() - (i - 1); k++) {
                String chunk1 = acronym.substring(k, k + i);
                String chunk2 = acronym.replace(chunk1, "");

//                if (BLOCK_SINGLE_LETTERS)
//                    if (chunk1.length() <= 1 || chunk2.length() <= 1 && !chunk1.equals("i"))
//                        continue;

                split_acronym = chunk1 + DELIMITER + chunk2;

//                if (BLOCK_SWAPPED_WORDS) {
//                    String swap = chunk2 + DELIMITER + chunk1;
//                    if (!acronym_splits.contains(swap))
//                        acronym_splits.add(split_acronym);
//                }
//                else {
//                    acronym_splits.add(split_acronym);
//                }

                acronym_splits.add(split_acronym);
            }
        }

        for (String possibleWord : acronym_splits) {
            String[] chunks = possibleWord.split(DELIMITER);
            if (anagrams.containsKey(chunks[0]) && anagrams.containsKey(chunks[1])) {
                ArrayList<String> chunk1Anagrams = anagrams.get(chunks[0]);
                ArrayList<String> chunk2Anagrams = anagrams.get(chunks[1]);

                for (String anagram1 : chunk1Anagrams) {
                    for (String anagram2 : chunk2Anagrams) {
                        anagram1 = anagram1.substring(0, 1).toUpperCase() + anagram1.substring(1);
                        anagram2 = anagram2.substring(0, 1).toUpperCase() + anagram2.substring(1);

                        choices.add(anagram1 + " " + anagram2);
                    }
                }
            }
        }

        return choices;
    }

}
