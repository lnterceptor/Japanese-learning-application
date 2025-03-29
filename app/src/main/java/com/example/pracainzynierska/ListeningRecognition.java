package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

public class ListeningRecognition extends Recognition{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shouldSpeak = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createCorrectAnswer(){

        for(ChooseKanjiObject kanji: kanjiObjects) {
            System.out.println(currentAnsKanji + " " + kanji.getKanji());
            if (Objects.equals(kanji.getKanji(), currentAnsKanji)) {
                System.out.println(currentAnsKanji + " " + kanji.getKanji());
                singleRespondedKanji = new ProfileKanjiObject(0, 0, kanji.getMostPopularKunReading(), kanji.getMostPopularOnReading(), kanji.getMeanings(), kanji.getLevel(), kanji.getKanji());
            }
        }
        ansCorrect = currentAnsKanji;
    }

    @Override
    protected void createIncorrectAnswers(){

        incorrectAnswers.clear();
        for (int i = 0; i < 5; i++) {
            boolean shouldBePlaced;
            do{
                shouldBePlaced = true;
                int randMeaning =  random.nextInt(kanjiObjects.size());
                String incorrectMeaning = kanjiObjects.get(randMeaning).getKanji();

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

    @Override
    protected void setImageView(){
        imageView = findViewById(R.id.RecognitionKanjiImage);
        imageView.setBackgroundResource(R.drawable.baseline_volume_up_24);
        imageView.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                setTextToSpeech();
            }
        });
    }
    @Override
    protected void endSession(){
        Intent intent = new Intent(getApplicationContext(),KanjiExerciseSummary.class);
        intent.putExtra("Menu", ListeningMenu.class);
        intent.putExtra("AllKanji", respondedKanji);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void nextQuestion(){
        if(responded){
            createAnswers();
            resetButtons();
            setAnswers();
            setImageView();

            responded = false;
            setTextToSpeech();
        }
    }
}
