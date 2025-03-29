package com.example.pracainzynierska;

import java.io.Serializable;

public class GrammarTranslationPairObject implements Serializable {
    public String getJapaneseMeaning() {
        return japaneseMeaning;
    }

    public String getPolishMeaning() {
        return polishMeaning;
    }

    String japaneseMeaning;
    String polishMeaning;
    GrammarTranslationPairObject(String japaneseMeaning, String polishMeaning){
        this.japaneseMeaning = japaneseMeaning;
        this.polishMeaning = polishMeaning;
    }
}
