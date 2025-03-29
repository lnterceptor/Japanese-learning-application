package com.example.pracainzynierska;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Reading extends Recognition {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createCorrectAnswer() {
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnsKanji)){
                singleRespondedKanji = new ProfileKanjiObject(0,0,kanji.getMostPopularKunReading(),kanji.getMostPopularOnReading(),kanji.getMeanings(),kanji.getLevel(),kanji.getKanji());
                ansCorrect = kanji.getMeanings();
            }
        }
    }
    @Override
    protected void createIncorrectAnswers(){
        incorrectAnswers.clear();
        for (int i = 0; i < 5; i++) {
            boolean shouldBePlaced;
            do{
                shouldBePlaced = true;
                String incorrectMeaning = kanjiObjects.get(random.nextInt(kanjiObjects.size())).getMeanings();
                if(!Objects.equals(incorrectMeaning, ansCorrect)) {
                    if (incorrectAnswers.size() > 0) {
                        for (String element : incorrectAnswers) {
                            if (Objects.equals(incorrectMeaning, element)) {
                                shouldBePlaced = false;
                                break;
                            }
                        }
                        if(shouldBePlaced) {
                            incorrectAnswers.add(incorrectMeaning);
                        }
                    } else {
                        incorrectAnswers.add(incorrectMeaning);
                    }
                }
            }while(!shouldBePlaced);
        }
    }
}