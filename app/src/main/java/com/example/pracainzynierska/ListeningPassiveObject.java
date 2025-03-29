package com.example.pracainzynierska;

import java.io.Serializable;
import java.util.ArrayList;

public class ListeningPassiveObject implements Serializable {
    private String polishMeaning;

    private String kanji;
    private int amount;
    private String readings;

    private String onReading;
    private String kunReading;
    private String level;
    private ArrayList<String> wordsContainingThisKanji;

    public ListeningPassiveObject(String polishMeaning, String kanji,String level, int amount,String kunReading, String onReading, String readings, ArrayList<String> wordsContainingThisKanji) {
        this.polishMeaning = polishMeaning;
        this.kanji = kanji;
        this.level = level;
        this.amount = amount;
        this.readings = readings;
        this.wordsContainingThisKanji = wordsContainingThisKanji;
        this.kunReading = kunReading;
        this.onReading = onReading;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPolishMeaning() {
        return polishMeaning;
    }

    public String getOnReading() {
        return onReading;
    }

    public String getLevel() {
        return level;
    }

    public String getKunReading() {
        return kunReading;
    }
    public ArrayList<String> getWordsContainingThisKanji() {
        return wordsContainingThisKanji;
    }

    public String getKanji() {
        return kanji;
    }

    public int getAmount() {
        return amount;
    }

    public String getReadings() {
        return readings;
    }

}
