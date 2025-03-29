package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileAddFriend extends AppCompatActivity {
    Button addFriendButton;
    EditText nameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add_friend);
        setData();
    }
    void setData(){
        addFriendButton = findViewById(R.id.addFriendButton);
        nameText = findViewById(R.id.addFriendField);
        addFriendButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                addFriend();
            }
        });
    }

    void addFriend(){
        MainMenu.user.addFriend(nameText.getText().toString(), getApplicationContext());

    }

}
