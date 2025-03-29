package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class Drawing extends AppCompatActivity{
    private DatabaseReference mDatabase;
    private DrawKanjiCanvas surfaceView;
    ConstraintLayout finishScreen;
    TextToSpeech textToSpeech;
    Button endButton, returnButton;
    Button correct, incorrect;
    Button undoButton, checkButton, restartButton;
    TextView kanjiOverall, numOfFinishedKanji;
    TextView japaneseWord, polishTranslation;
    ArrayList<ChooseKanjiObject> kanjiObjects;
    ArrayList<String> kanjiForRepetition = new ArrayList<>();
    Random random;
    String currentAnswer;
    ArrayList<ProfileKanjiObject> kanji = new ArrayList<>();
    ProfileKanjiObject singleRespondedKanji;
    ConstraintLayout endSessionScreen;
    @Override
    public void onBackPressed(){
        if(finishScreen.getVisibility() == View.VISIBLE) {
            setIsGUIEnabled(true, View.INVISIBLE);
            return;
        }
        setIsGUIEnabled(false, View.VISIBLE);
        kanjiOverall.setText("Łączna liczba pytań: " + String.valueOf(kanjiForRepetition.size() + kanji.size() + 1));
        numOfFinishedKanji.setText("Aktualne pytanie: " + String.valueOf(kanji.size() + 1));
    }

    protected void setEndScreenGUI(){
        endSessionScreen = findViewById(R.id.endSession);
        numOfFinishedKanji = findViewById(R.id.numberOfFinishedKanji);
        kanjiOverall = findViewById(R.id.numberOfOverallKanji);
        endButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
    }
    protected void endSession(){
        Intent intent = new Intent(getApplicationContext(),KanjiExerciseSummary.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AllKanji", kanji);
        intent.putExtra("Menu", WritingMenu.class);
        startActivity(intent);
        finish();
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            random = new Random();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.drawing);
            surfaceView = findViewById(R.id.kanjiImage);

            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if(i!=TextToSpeech.ERROR){
                        textToSpeech.setLanguage(Locale.JAPANESE);
                        setTextToSpeech();
                    }
                }
            });

            getAdditionalValues();
            setAnswer();
            getGUI();
            setGUI();
            setEndScreenGUI();
            setTranslations();
        }

    protected void setTextToSpeech(){
        String textToSpeak = "";
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnswer)){
                textToSpeak = kanji.getMostPopularKunReading();
                if(textToSpeak.length() > 0){
                    textToSpeak += ", ";
                }
                textToSpeak += kanji.getMostPopularOnReading();
            }
        }

        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
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
        protected void getAdditionalValues(){
            kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
            kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");
            System.out.println(kanjiObjects);
            System.out.println(kanjiForRepetition);
        }

        protected void getGUI(){
            correct = findViewById(R.id.correct);
            incorrect = findViewById(R.id.incorrect);
            endButton = findViewById(R.id.endSessionButton);
            returnButton = findViewById(R.id.returnButton);
            finishScreen = findViewById(R.id.endSession);
            undoButton = findViewById(R.id.undo);
            checkButton = findViewById(R.id.check);
            restartButton = findViewById(R.id.restart);
            japaneseWord = findViewById(R.id.japaneseVersion);
            polishTranslation = findViewById(R.id.translation);
        }
        protected void setGUI(){
            correct.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCorrect(true);
                }
            });
            incorrect.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isCorrect(false);
                }
            });

            undoButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    surfaceView.undoLastStroke();
                }
            });

            checkButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check();
                }
            });
            restartButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    surfaceView.restart();
                }
            });
            endButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endSession();
                }
            });

            returnButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setIsGUIEnabled(true, View.INVISIBLE);
                }
            });

            japaneseWord.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTextToSpeech();
                }
            });
        }

        private void drawKanji(String kanji){
            surfaceView.drawKanji(kanji);
        }
        protected void check(){
            drawKanji(currentAnswer);
            setGUIAfterAnswer();
            surfaceView.drawingEnabled(false);
        }

    protected void setGUIAfterAnswer(){
        correct.setVisibility(View.VISIBLE);
        incorrect.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        undoButton.setVisibility(View.INVISIBLE);
    }
    protected void beforeNextQuestion(){
        correct.setVisibility(View.INVISIBLE);
        incorrect.setVisibility(View.INVISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.VISIBLE);
        undoButton.setVisibility(View.VISIBLE);
    }
    protected void setIsGUIEnabled(boolean enabled, int visibility){
        finishScreen.setVisibility(visibility);
        checkButton.setEnabled(enabled);
        restartButton.setEnabled(enabled);
        undoButton.setEnabled(enabled);
        surfaceView.drawingEnabled(enabled);
        japaneseWord.setEnabled(enabled);
        polishTranslation.setEnabled(enabled);
    }

    protected void isCorrect(boolean isCorrect){
        if(isCorrect){
            singleRespondedKanji.setIncorrect(0);
            singleRespondedKanji.setCorrect(1);
        }
        else{
            singleRespondedKanji.setIncorrect(1);
            singleRespondedKanji.setCorrect(0);
        }
        kanji.add(singleRespondedKanji);
        surfaceView.drawingEnabled(true);
        beforeNextQuestion();
        nextQuestion();
        return;
    }


    protected void nextQuestion(){
            setAnswer();
            setTranslations();
            surfaceView.restart();
            setTextToSpeech();
    }

    void setTranslations() {
        ArrayList<String> translations = createCorrectAnswer();
        polishTranslation.setText(translations.get(0));

        String japanese = translations.get(1);
        if (translations.size() > 2) {
            japanese += '/' + translations.get(2);
        }
        japaneseWord.setText(japanese);
    }

    protected ArrayList<String> createCorrectAnswer(){
        ArrayList<String> romajiReadings = new ArrayList<String>();
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnswer)){
                romajiReadings.add(kanji.getMeanings());
                singleRespondedKanji = new ProfileKanjiObject(0,0,kanji.getMostPopularKunReading(),kanji.getMostPopularOnReading(),kanji.getMeanings(),kanji.getLevel(),kanji.getKanji());
                if(kanji.getMostPopularKunReading().length() > 0) {
                    romajiReadings.add(kanji.getMostPopularKunReading());
                }
                if(kanji.getMostPopularOnReading().length() > 0) {
                    romajiReadings.add(kanji.getMostPopularOnReading());
                }

            }
        }
        return romajiReadings;
    }

}
