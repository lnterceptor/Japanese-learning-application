package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecognitionComplexMenu extends AppCompatActivity {
    Button randomQuestions, questionsFromFriend;
    TextView nameOfActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friend_menu);
        getGUI();
        setGUI();
    }
    private void getGUI(){
        randomQuestions = findViewById(R.id.addFriendButton);
        questionsFromFriend = findViewById(R.id.checkFriend);
        nameOfActivity = findViewById(R.id.friendsText);
    }

    protected void setTextOnButtons(){
        nameOfActivity.setText("Rozpoznawanie wyrazów");
        randomQuestions.setText("Losowe pytania");
        questionsFromFriend.setText("Pytania stworzone przez znajomych");
    }
    protected void setGUI(){
        setTextOnButtons();

        randomQuestions.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RecognitionComplex.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        questionsFromFriend.setTextSize(20);
        questionsFromFriend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RecognitionComplexFriendChoice.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

}
