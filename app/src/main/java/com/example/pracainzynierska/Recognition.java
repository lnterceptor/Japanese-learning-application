package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.Objects;
import java.util.Random;

public class Recognition extends AppCompatActivity {
    //todo: jesli sie nie myle, to poki co kanji dla ktorych chcemy znalezc odpowiedz sa w kolejności od 1 kliknięcia, bo array jest tak przekazany, wiec go przetasuj
    //todo: database dla zapisania progresu na koncie użytkownika
    protected DatabaseReference mDatabase;
    protected ArrayList<Button> answerButtons = new ArrayList<Button>();
    String ansCorrect;
    ArrayList<String> incorrectAnswers = new ArrayList<String>();
    String currentAnsKanji;
    ArrayList<ChooseKanjiObject> kanjiObjects;
    ArrayList<String> kanjiForRepetition;
    ArrayList<ProfileKanjiObject> respondedKanji = new ArrayList<>();
    ProfileKanjiObject singleRespondedKanji;
    protected Button imageView;
    ConstraintLayout layout;
    protected boolean responded = true;
    protected int textSize = 90;
    Random random = new Random();
    TextToSpeech textToSpeech;
    ConstraintLayout endSessionScreen;
    Button returnButton, endSessionButton;
    TextView numberOfFinishedKanji, numberOfOverallKanji;
    boolean shouldSpeak = false;
    @Override
    public void onBackPressed(){
        if(endSessionScreen.getVisibility() == View.VISIBLE) {
            endSessionScreen.setVisibility(View.INVISIBLE);
            for(Button button: answerButtons){
                button.setEnabled(true);
            }
            layout.setEnabled(true);
            imageView.setEnabled(true);
            return;
        }
        endSessionScreen.setVisibility(View.VISIBLE);
        numberOfFinishedKanji.setText("Aktualne pytanie: " + String.valueOf(1 + respondedKanji.size()));
        numberOfOverallKanji.setText("Łączna liczba pytań: " +String.valueOf(respondedKanji.size() + kanjiForRepetition.size()));
        for(Button button: answerButtons){
            button.setEnabled(false);
        }
        layout.setEnabled(false);
        imageView.setEnabled(false);
    }

    void returnBack(){
        endSessionScreen.setVisibility(View.INVISIBLE);
        for(Button button: answerButtons){
            button.setEnabled(true);
        }
        layout.setEnabled(true);
        imageView.setEnabled(true);
    }
    protected void endSession(){
        Intent intent = new Intent(getApplicationContext(),KanjiExerciseSummary.class);
        intent.putExtra("Menu", WritingMenu.class);
        intent.putExtra("AllKanji", respondedKanji);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.JAPANESE);
                    shouldSpeak();
                }
            }
        });

        getGUIElements();
        setOtherButtons();
        setButtons();
        setCanvas();
        nextQuestion();
        setTextViews();
    }
    protected void getGUIElements(){
        setContentView(R.layout.recognition);
        imageView = findViewById(R.id.RecognitionKanjiImage);
        endSessionScreen = findViewById(R.id.endSession);
        returnButton = findViewById(R.id.returnButton);
        endSessionButton = findViewById(R.id.endSessionButton);
    }
    protected void shouldSpeak(){
        if(shouldSpeak){
            setTextToSpeech();
        }
    }

    void setTextViews(){
        numberOfFinishedKanji = findViewById(R.id.numberOfFinishedKanji);
        numberOfOverallKanji = findViewById(R.id.numberOfOverallKanji);
    }
    void setOtherButtons(){
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack();
            }
        });

        endSessionButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });
    }

    protected void setCanvas(){
        layout = findViewById(R.id.recognition_background);

        layout.setOnClickListener(new ConstraintLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

    }

    protected void nextQuestion(){
        if(responded){
            createAnswers();
            resetButtons();
            setAnswers();
            setImageView();

            responded = false;
        }
    }

    protected void createAnswers(){
        setNextKanji();
        createCorrectAnswer();
        createIncorrectAnswers();
    }

    protected void setNextKanji(){
        if(kanjiForRepetition.size() == 0){
            endSession();
            return;
        }
        int questionNumber = random.nextInt(kanjiForRepetition.size());
        currentAnsKanji = kanjiForRepetition.get(questionNumber);
        kanjiForRepetition.remove(currentAnsKanji);
    }
    protected void setAnswers(){
        Random random = new Random();
        ArrayList<String> answers = new ArrayList<>(incorrectAnswers);
        answers.add(ansCorrect);


        for (Button tempButton: answerButtons) {
            if(answers.size() > 1) {
                int number = random.nextInt(answers.size());
                tempButton.setText(answers.get(number));
                answers.remove(number);
            }
            else{
                tempButton.setText(answers.get(0));
            }
        }
    }

    protected void resetButtons() {
        for (Button button : answerButtons) {
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonColor));
        }
    }
    protected void setButtons(){
        Button button = findViewById(R.id.buttonResponce1);
        Button button2 = findViewById(R.id.buttonResponce2);
        Button button3 = findViewById(R.id.buttonResponce3);
        Button button4 = findViewById(R.id.buttonResponce4);
        Button button5 = findViewById(R.id.buttonResponce5);
        Button button6 = findViewById(R.id.buttonResponce6);

        answerButtons.add(button);
        answerButtons.add(button2);
        answerButtons.add(button3);
        answerButtons.add(button4);
        answerButtons.add(button5);
        answerButtons.add(button6);
        for (Button tempButton: answerButtons) {
            setAnswerButtons(tempButton);
        }
    }

    protected void setTextToSpeech(){
        String textToSpeak = "";
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnsKanji)){
                textToSpeak = kanji.getMostPopularKunReading();
                if(textToSpeak.length() > 0){
                    textToSpeak += ", ";
                }
                textToSpeak += kanji.getMostPopularOnReading();
            }
        }

        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    protected void setImageView(){
        imageView = findViewById(R.id.RecognitionKanjiImage);
        imageView.setTextSize(textSize);
        imageView.setText(currentAnsKanji);
        imageView.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                setTextToSpeech();
            }
        });
    }

    protected void setAnswerButtons(Button button){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(button);
            }
        });
    }

    protected void checkAnswer(Button button){
        if(!responded) {
            if (button.getText().equals(ansCorrect)) {
                singleRespondedKanji.setCorrect(1);
                singleRespondedKanji.setIncorrect(0);
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswer));

            } else {
                singleRespondedKanji.setCorrect(0);
                singleRespondedKanji.setIncorrect(1);
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.IncorrectAnswer));
                for (Button buttonTemp : answerButtons) {
                    if (buttonTemp.getText().equals(ansCorrect)) {
                        buttonTemp.setBackgroundColor(ContextCompat.getColor(this, R.color.CorrectAnswer));
                        break;
                    }
                }
            }
            respondedKanji.add(singleRespondedKanji);
            responded = true;
        }
        else{
            nextQuestion();
        }
    }

    protected void createCorrectAnswer(){
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnsKanji)){
                singleRespondedKanji = new ProfileKanjiObject(0,0,kanji.getMostPopularKunReading(),kanji.getMostPopularOnReading(),kanji.getMeanings(),kanji.getLevel(),kanji.getKanji());

                ansCorrect = kanji.getMostPopularKunReading();
                if(ansCorrect.length() > 0){
                    ansCorrect += ", ";
                }
                ansCorrect += kanji.getMostPopularOnReading();
            }
        }
    }

    protected void createIncorrectAnswers(){

        incorrectAnswers.clear();
        for (int i = 0; i < 5; i++) {
            boolean shouldBePlaced;
            do{
                shouldBePlaced = true;
                int randMeaning =  random.nextInt(kanjiObjects.size());
                String incorrectMeaning = kanjiObjects.get(randMeaning).getMostPopularKunReading();
                if(incorrectMeaning.length() > 0){
                    incorrectMeaning += ", ";
                }
                incorrectMeaning += kanjiObjects.get(randMeaning).getMostPopularOnReading();


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