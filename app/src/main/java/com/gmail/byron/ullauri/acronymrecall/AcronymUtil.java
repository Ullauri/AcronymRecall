package com.gmail.byron.ullauri.acronymrecall;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ullauri on 10/8/17.
 */

public enum AcronymUtil {
    INSTANCE;


    private ArrayList<AcronymWordFinder> acronymWordFinders;


    public void init(InputStreamReader inputStreamReader) {
        acronymWordFinders = new ArrayList<>();

        try {
            acronymWordFinders.add(new AnagramsDictionary(inputStreamReader));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAcronym(ArrayList<String> keyWords) {
        String acronym = "";

        for (String word : keyWords)
            acronym += word.charAt(0);

        return acronym;
    }

    public ArrayList<String> getAcronymWords(ArrayList<String> keyWords) {
        ArrayList<String> choices = new ArrayList<>();
        String acronym = getAcronym(keyWords);

        for (AcronymWordFinder acronymWordFinder : acronymWordFinders) {
            choices.addAll(acronymWordFinder.getWords(acronym));
        }
        
        return choices;
    }

}
