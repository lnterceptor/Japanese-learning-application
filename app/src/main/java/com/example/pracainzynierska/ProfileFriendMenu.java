package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileFriendMenu extends AppCompatActivity {
    Button checkFriendButton, addFriendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friend_menu);
        setData();
    }

    protected void setButtonText(){
        return;
    }
    protected void setData(){

        checkFriendButton = findViewById(R.id.checkFriend);
        addFriendButton = findViewById(R.id.addFriendButton);
        setButtonText();
        checkFriendButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = setIntentForFirstButton();
                startActivity(intent);
            }
        });

        addFriendButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = setIntentForSecondButton();
                startActivity(intent);
            }
        });
    }
    protected Intent setIntentForFirstButton(){
        return new Intent(getApplicationContext(), ProfileFriendsList.class);
    }
    protected Intent setIntentForSecondButton(){
        return new Intent(getApplicationContext(), ProfileAddFriend.class);
    }

}
