package com.example.pracainzynierska;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
    private Button imageView;//todo: button, zeby po jego wcisnieciu text to speech

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);

        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");
        currentAnsKanji = kanjiForRepetition.get(0);
        setContentView(R.layout.recognition);

        createAnswers();

        setButtons();
        setImageView();
        setAnswers();
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

    protected void setImageView(){
        imageView = findViewById(R.id.RecognitionKanjiImage);
        imageView.setText(currentAnsKanji);
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
        if(button.getText().equals(ansCorrect)){
            button.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));

        }
        else{
            button.setBackgroundColor(ContextCompat.getColor(this,R.color.IncorrectAnswer));
            for (Button buttonTemp: answerButtons) {
                if(buttonTemp.getText().equals(ansCorrect)){
                    buttonTemp.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));
                    break;
                }
            }
        }
    }
    protected void createAnswers(){
        createCorrectAnswer();
        createIncorrectAnswers();
    }

    protected void createCorrectAnswer(){
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnsKanji)){
                ansCorrect = kanji.getReadings_kun();
            }
        }
    }

    protected void createIncorrectAnswers(){
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            boolean shouldBePlaced;
            do{
                shouldBePlaced = true;
                String incorrectMeaning = kanjiObjects.get(random.nextInt(kanjiObjects.size())).getReadings_kun();
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