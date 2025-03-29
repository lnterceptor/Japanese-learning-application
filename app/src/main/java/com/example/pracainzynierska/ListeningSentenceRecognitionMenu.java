package com.example.pracainzynierska;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ListeningSentenceRecognitionMenu extends RecognitionComplexMenu{
    protected void setTextOnButtons(){
        nameOfActivity.setText("Rozpoznawanie zdań");
        randomQuestions.setText("Losowe pytania");
        questionsFromFriend.setText("Pytania stworzone przez znajomych");
    }

    protected void setGUI(){
        setTextOnButtons();

        randomQuestions.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ListeningSentenceRecognition.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        questionsFromFriend.setTextSize(20);
        questionsFromFriend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ListeningSentenceRecognitionFriendChoice.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
