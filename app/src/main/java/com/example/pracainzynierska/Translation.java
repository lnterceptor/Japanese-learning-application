package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moji4j.MojiConverter;


import java.util.ArrayList;
import java.util.Random;


public class Translation extends AppCompatActivity {
    MojiConverter converter = new MojiConverter();

    Button checkAnswer, image;
    TextInputEditText answerInput;
    ArrayList<String> kanjiForRepetition;
    ArrayList<ChooseKanjiObject> kanjiObjects;
    String currentAnswer;
    boolean answered = false;
    ConstraintLayout layout;
    Random random;
    TextView ifIncorrect;
    ConstraintLayout endSessionScreen;
    TextView numOfFinishedKanji, kanjiOverall;
    Button returnButton, endSessionButton;
    ArrayList<ProfileKanjiObject> kanji = new ArrayList<>();
    ProfileKanjiObject singleRespondedKanji;

    protected void setEndScreenGUI(){
        endSessionScreen = findViewById(R.id.endSession);
        numOfFinishedKanji = findViewById(R.id.numberOfFinishedKanji);
        kanjiOverall = findViewById(R.id.numberOfOverallKanji);
        returnButton = findViewById(R.id.returnButton);
        endSessionButton = findViewById(R.id.endSessionButton);

        endSessionButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSessionScreen.setVisibility(View.INVISIBLE);
                checkAnswer.setEnabled(true);
                layout.setEnabled(true);
            }
        });

    }

    @Override
    public void onBackPressed(){
        if(endSessionScreen.getVisibility() == View.VISIBLE) {
            setLayoutVisible(View.INVISIBLE, true);
            return;
        }
        setLayoutVisible(View.VISIBLE, false);
        kanjiOverall.setText("Łączna liczba pytań: " + String.valueOf(kanjiForRepetition.size() + kanji.size() + 1));
        numOfFinishedKanji.setText("Aktualne pytanie: " + String.valueOf(kanji.size() + 1));
    }

    protected void setLayoutVisible(int invisible, boolean enabled) {
        endSessionScreen.setVisibility(invisible);
        checkAnswer.setEnabled(enabled);
        layout.setEnabled(enabled);
        image.setEnabled(enabled);
    }

    void endSession(){
        Intent intent = new Intent(getApplicationContext(),KanjiExerciseSummary.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AllKanji", kanji);
        setMenu(intent);
        startActivity(intent);
        finish();
    }

    protected void setMenu(Intent intent) {
        intent.putExtra("Menu", WritingMenu.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        random = new Random();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);


        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");

        setAnswer();
        findViews();
        setCheckButton(checkAnswer);
        setBackground(layout);
        setImage();
        setEndScreenGUI();
    }

    protected void setAnswer() {
        if(kanjiForRepetition.size() > 0) {
            int nextKanjiIndex = random.nextInt(kanjiForRepetition.size());
            String nextKanji = kanjiForRepetition.get(nextKanjiIndex);
            kanjiForRepetition.remove(nextKanjiIndex);
            currentAnswer = nextKanji;
        }
        else{
            endSession();
        }
    }

    protected void findViews() {
        ifIncorrect = findViewById(R.id.ifIncorrect);
        checkAnswer = findViewById(R.id.checkAnswer);
        image = findViewById(R.id.TranslationKanjiImage);
        answerInput = findViewById(R.id.translationInput);
        layout = findViewById(R.id.translationBackground);
    }

    protected void setBackground(ConstraintLayout layout){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answered) {
                    nextQuestion();
                }
            }
        });
    }

    protected void setCheckButton(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!answered) {
                    checkAnswer();
                }
                else{
                    if(ifIncorrect.getVisibility() == (View.VISIBLE)) {
                        ifIncorrect.setVisibility(View.INVISIBLE);
                    }
                    nextQuestion();
                }
            }
        });
    }

    protected void nextQuestion(){
        if(answered){
            setAnswer();
            setImage();
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonColor));
            answered = false;
            answerInput.setText("");
        }
    }

    protected void setImage(){
        image.setText(currentAnswer);
    }

    protected ArrayList<String> createCorrectAnswer(){
        ArrayList<String> romajiReadings = new ArrayList<String>();
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnswer)){
                singleRespondedKanji = new ProfileKanjiObject(0,0,kanji.getMostPopularKunReading(),kanji.getMostPopularOnReading(),kanji.getMeanings(),kanji.getLevel(),kanji.getKanji());
                if(kanji.getMostPopularKunReading().length() > 0) {
                    romajiReadings.add(converter.convertKanaToRomaji(kanji.getMostPopularKunReading()));
                }
                if(kanji.getMostPopularOnReading().length() > 0) {
                    romajiReadings.add(converter.convertKanaToRomaji(kanji.getMostPopularOnReading()));
                }
            }
        }
        return romajiReadings;
    }

    protected void checkAnswer(){
        ArrayList<String> romajiReadings = createCorrectAnswer();

        if(romajiReadings.get(0).split("\\.")[0].equals(((String)answerInput.getText().toString()).toLowerCase().trim())
                || (romajiReadings.get(0).equals(((String)answerInput.getText().toString()).toLowerCase().trim()))){
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));
            singleRespondedKanji.setIncorrect(0);
            singleRespondedKanji.setCorrect(1);
        }
        else {
            if (romajiReadings.size() > 1) {
                System.out.println(romajiReadings.get(1));
                if (romajiReadings.get(1).split("\\.")[0].equals(((String) answerInput.getText().toString()).toLowerCase().trim())
                        || romajiReadings.get(1).equals(((String) answerInput.getText().toString()).toLowerCase().trim())) {
                    checkAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswer));
                    singleRespondedKanji.setIncorrect(0);
                    singleRespondedKanji.setCorrect(1);
                }
                else {
                    checkAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.IncorrectAnswer));
                    ifIncorrect.setVisibility(View.VISIBLE);
                    ifIncorrect.setText("kun readings: " + romajiReadings.get(0).split("\\.")[0]+"\n"+"on readings: " +romajiReadings.get(1).split("\\.")[0]);
                    singleRespondedKanji.setIncorrect(1);
                    singleRespondedKanji.setCorrect(0);
                }
            }
            else {
                checkAnswer.setBackgroundColor(ContextCompat.getColor(this, R.color.IncorrectAnswer));
                ifIncorrect.setVisibility(View.VISIBLE);
                ifIncorrect.setText(romajiReadings.get(0).split("\\.")[0]);
                singleRespondedKanji.setIncorrect(1);
                singleRespondedKanji.setCorrect(0);
            }
        }
        kanji.add(singleRespondedKanji);
        answered = true;
    }


}