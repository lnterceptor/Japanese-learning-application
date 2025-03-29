package com.example.pracainzynierska;

import java.io.Serializable;

public class ProfileGrammarObject implements Serializable {
    String title;
    int amount, overallAmount;

    public ProfileGrammarObject(String title, int overallAmount, int amount) {
        this.title = title;
        this.overallAmount = overallAmount;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public int getOverallAmount() {
        return overallAmount;
    }
    public int getAmount() {
        return amount;
    }
}
