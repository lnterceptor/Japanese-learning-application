package com.example.pracainzynierska;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class ListeningSpeaking extends Translation {

    SpeechRecognizer speechRecognizer;
    Button speakingButton;
    TextView correct, response;
    Intent speechIntent;
    protected DatabaseReference mDatabase;
    boolean isListening = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        random = new Random();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listening_speaking);

        kanjiObjects = (ArrayList<ChooseKanjiObject>) getIntent().getSerializableExtra("allKanji");
        kanjiForRepetition = getIntent().getStringArrayListExtra("kanjiArray");

        findElements();
        setElements();
        setAnswer();
        setEndScreenGUI();

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        setSpeakRecognizer();

    }

    @Override
    protected void setLayoutVisible(int invisible, boolean enabled) {
        endSessionScreen.setVisibility(invisible);
        checkAnswer.setEnabled(enabled);
        layout.setEnabled(enabled);
        image.setEnabled(enabled);
        speakingButton.setEnabled(enabled);
    }
    private void setSpeakRecognizer(){
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP");
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }
            @Override
            public void onResults(Bundle bundle) {
                speakingButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonColor));
                ArrayList<String> arrayList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                response.setText(arrayList.get(0));
                isListening = false;
            }

            @Override
            public void onBeginningOfSpeech() {
                speakingButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonColorBrighter));
                isListening = true;
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void checkPermission(){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(ListeningSpeaking.this, new String[] {
                    Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


    protected void findElements(){
        checkAnswer = findViewById(R.id.buttonAnswer);
        image = findViewById(R.id.recognitionKanjiButton);
        layout = findViewById(R.id.speakingCanvas);

        speakingButton = findViewById(R.id.buttonSpeak);
        correct = findViewById(R.id.answer);
        response = findViewById(R.id.response);
    }

    protected void setElements(){
        setSpeakingButton();
        setCheckButton(checkAnswer);
        setBackground(layout);
        setImage();

        setSpeakingButton();
    }
    protected void setSpeakingButton(){
        speakingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isListening) {
                    speechRecognizer.startListening(speechIntent);
                    isListening = !isListening;
                }
                else{
                    speechRecognizer.stopListening();
                    isListening = false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        response.setText(text.get(0));
    }

    @Override
    protected void checkAnswer(){
        speakingButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonColor));
        ArrayList<String> romajiReadings = createCorrectAnswer();
        correct.setText(currentAnswer);
        if(romajiReadings.get(0).equals(((String)answerInput.getText().toString()).toLowerCase().trim())){
            correct.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));
            correct.setAlpha(0.8f);
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));

        }
        else{
            if(romajiReadings.size() > 1){
                System.out.println(romajiReadings.get(1));
                if(romajiReadings.get(1).equals(((String)answerInput.getText().toString()).toLowerCase().trim())) {
                    correct.setBackgroundColor(ContextCompat.getColor(this,R.color.IncorrectAnswer));
                    correct.setAlpha(0.8f);
                    checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.CorrectAnswer));
                }
            }
            checkAnswer.setBackgroundColor(ContextCompat.getColor(this,R.color.IncorrectAnswer));
        }
        answered = true;
    }

    @Override
    protected ArrayList<String> createCorrectAnswer(){
        ArrayList<String> romajiReadings = new ArrayList<String>();
        for (ChooseKanjiObject kanji:kanjiObjects) {
            if(kanji.getKanji().equals(currentAnswer)){
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

    @Override
    protected void setMenu(Intent intent) {
        intent.putExtra("Menu", ListeningMenu.class);
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        speechRecognizer.destroy();
    }

}
