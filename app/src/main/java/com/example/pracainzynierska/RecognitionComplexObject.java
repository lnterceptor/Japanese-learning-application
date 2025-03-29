package com.example.pracainzynierska;

import java.io.Serializable;

public class RecognitionComplexObject implements Serializable {
    private String polishSentence, correctSentence, incorrectSentence1, incorrectSentence2, incorrectSentence3, user;

    public RecognitionComplexObject(String polishSentence, String correctSentence, String incorrectSentence1, String incorrectSentence2, String incorrectSentence3, String user) {
        this.polishSentence = polishSentence;
        this.correctSentence = correctSentence;
        this.incorrectSentence1 = incorrectSentence1;
        this.incorrectSentence2 = incorrectSentence2;
        this.incorrectSentence3 = incorrectSentence3;
        this.user = user;
    }

    public String getPolishSentence() {
        return polishSentence;
    }

    public String getUser() {
        return user;
    }

    public String getCorrectSentence() {
        return correctSentence;
    }

    public String getIncorrectSentence1() {
        return incorrectSentence1;
    }

    public String getIncorrectSentence2() {
        return incorrectSentence2;
    }

    public String getIncorrectSentence3() {
        return incorrectSentence3;
    }
}
