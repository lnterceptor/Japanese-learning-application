package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListeningSentenceRecognition extends RecognitionComplex {

    @Override
    @NonNull
    protected DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference("exercises/listening_exercises");
    }

    @Override
    protected void setContentView(){
        setContentView(R.layout.sentence_recognition);
    }

    @Override
    protected void setSentence() {
        sentence = kanjiQuestions.get(currentExercise).getPolishSentence();
        if(textToSpeech != null) {
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        button.setText("");
        button.setBackgroundResource(R.drawable.baseline_volume_up_24);
        userProfileText.setText("Ćwiczenie użytkownika: " + kanjiQuestions.get(currentExercise).getUser());
    }
    @Override
    protected void getGUI(){
        endSessionScreen2 = findViewById(R.id.endSession2);
        endSessionScreen = findViewById(R.id.endSession);

        layout = findViewById(R.id.recognition_background);
        userProfileText = findViewById(R.id.userProfileText);


        button = findViewById(R.id.polishText);
        buttons.add(findViewById(R.id.translationInput1));
        buttons.add(findViewById(R.id.translationInput2));
        buttons.add(findViewById(R.id.translationInput3));
        buttons.add(findViewById(R.id.translationInput4));
    }

    @Override
    protected void setTextToSpeech(){
        String textToSpeak = sentence;
        textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    @Override
    protected void endSession(){
        Intent intent = new Intent(getApplicationContext(),ListeningMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void utterKanji(){
        textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void utterFirstWord() {
        textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
