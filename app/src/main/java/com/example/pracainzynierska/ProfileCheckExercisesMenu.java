package com.example.pracainzynierska;

import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileCheckExercisesMenu extends ProfileFriendMenu {

    @Override
    protected Intent setIntentForFirstButton(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckExercises.class);
        intent.putExtra("user", getNick());
        return intent;
    }
    @Override
    protected Intent setIntentForSecondButton(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckExercisesSentences.class);
        intent.putExtra("user", getNick());
        return intent;
    }
    protected String getNick(){
        return getIntent().getStringExtra("user");
    }
    @Override
    protected void setButtonText(){
        TextView textView = findViewById(R.id.friendsText);
        textView.setText("Stworzone ćwiczenia");
        checkFriendButton.setText("Stworzone wyrazy");
        addFriendButton.setText("Stworzone zdania");
    }

}
