package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    FirebaseAuthObject firebaseAuthObject = new FirebaseAuthObject();
    Button check;
    EditText emailEditText, passwordEditText, repeadPasswordEditText, nickEditText;
    TextView goToLogin;
    boolean isNickTaken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        setEditText();
        setButton();
    }
    void setButton(){
        goToLogin = findViewById(R.id.goToLogin);
        check = findViewById(R.id.check);
        check.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                checkIfCorrect();
            }
        });
        goToLogin.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                goToLogin();
            }
        });
    }
    void goToLogin(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    void checkIfCorrect(){
        checkNick(nickEditText.getText().toString(), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isTaken) {
                if (isTaken) {
                    printStringNotCorrect("Wprowadzony nick jest już wykorzystywany");
                    isNickTaken = false;
                }
                else{
                    String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(emailEditText.getText().toString());
                        if(nickEditText.getText().toString().trim().length() == 0 || passwordEditText.getText().toString().trim().length() == 0 ||
                                repeadPasswordEditText.getText().toString().trim().length() == 0 || emailEditText.getText().toString().trim().length() == 0  ){
                            Toast.makeText(getApplicationContext(),"Pozostawiono część pól puste",Toast.LENGTH_SHORT).show();
                        }
                        else if(!matcher.matches()){
                            printStringNotCorrect("Wprowadzony email nie jest poprawny");
                        }
                        else if (passwordEditText.getText().toString().length() < 12) {
                            printStringNotCorrect("Wprowadzone hasło musi mieć powyżej 12 znaków");
                        } else if (!passwordEditText.getText().toString().equals(repeadPasswordEditText.getText().toString())) {
                            printStringNotCorrect("wprowadzone hasła są różne");
                        } else {
                            addUser();
                        }
                }
            }
        });

    }
    void checkNick(String nick, Consumer<Boolean> callback){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" );
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String newNick = userSnapshot.child("Nick").getValue(String.class);
                    if (Objects.equals(nick, newNick)) {
                        isNickTaken = true;
                        break;
                    }
                }
                callback.accept(isNickTaken);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void printStringNotCorrect(String data){
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
        emailEditText.setText("");
        passwordEditText.setText("");
        repeadPasswordEditText.setText("");
        nickEditText.setText("");
    }

    void addUser(){

        Task<AuthResult> user = firebaseAuthObject.createUser(emailEditText.getText().toString(),passwordEditText.getText().toString(),
                new FirebaseCallback(){
            @Override
            public void onCallback(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuthObject.addUserToDatabase(task,nickEditText.getText().toString());

                    Intent a = new Intent(getApplicationContext(),MainMenu.class);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                } else {
                    Exception exception = task.getException();
                    Toast.makeText(getApplicationContext(),"Wprowadzony mail jest już wykorzystywany", Toast.LENGTH_SHORT).show();
                }
        }
        });
    }

    void setEditText(){
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        repeadPasswordEditText = findViewById(R.id.passwordRepeat);
        nickEditText = findViewById(R.id.nick);
    }

}
