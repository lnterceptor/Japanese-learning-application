package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    FirebaseAuthObject firebaseAuthObject = new FirebaseAuthObject();
    Button logoutButton, kanjiButton, grammarButton, friendButton, checkExercisesButton, resetPassword;
    TextView login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        setData();
    }

    protected void setData(){
        setButtons();
        setTextField();
    }


    protected void setTextField(){
        login = findViewById(R.id.Login);
        login.setText("Cześć, "+MainMenu.user.nick+"!");
    }
    protected void setButtons(){
        checkExercisesButton = findViewById(R.id.CreatedExercises);
        logoutButton = findViewById(R.id.Logout);
        kanjiButton = findViewById(R.id.Kanji);
        grammarButton = findViewById(R.id.Grammar);
        friendButton = findViewById(R.id.Friends);
        resetPassword = findViewById(R.id.changePassword);

        resetPassword.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToNewPassword();
            }
        });
        logoutButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                logout();
            }
        });
        kanjiButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToKanjiScreen();
            }
        });
        checkExercisesButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToExercisesScreen();
            }
        });
        grammarButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToGrammarScreen();
            }
        });
        friendButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                goToFriendsScreen();
            }
        });
    }

    protected void goToExercisesScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckExercisesMenu.class);
        intent.putExtra("user", MainMenu.user.nick);
        startActivity(intent);
    }
    protected void goToKanjiScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckKanji.class);
        intent.putExtra("Kanji", MainMenu.user.userKanji);
        startActivity(intent);
    }
    protected void goToGrammarScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileCheckGrammar.class);
        intent.putExtra("Grammar", MainMenu.user.userGrammar);
        startActivity(intent);
    }
    protected void goToFriendsScreen(){
        Intent intent = new Intent(getApplicationContext(), ProfileFriendMenu.class);
        startActivity(intent);
    }

    protected void goToNewPassword(){
        Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
        startActivity(intent);
    }
    private void logout(){
        firebaseAuthObject.signOutUser();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

}
