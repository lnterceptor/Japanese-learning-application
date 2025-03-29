package com.example.pracainzynierska;

import static com.example.pracainzynierska.FirebaseAuthObject.mAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuthObject firebaseAuthObject = new FirebaseAuthObject();
    Button check;
    EditText emailEditText, passwordEditText;
    TextView goToRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        setEditText();
        setButton();
    }

    void setEditText(){
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        goToRegister = findViewById(R.id.goToRegister);
    }
    void setButton(){
        check = findViewById(R.id.check);
        check.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                checkIfCorrect();
            }
        });

        goToRegister.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v){
                goToRegister();
            }
        });
    }
    void goToRegister(){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }
    void checkIfCorrect(){
            loginUser();
    }

    void clearTextBoxes(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
        emailEditText.setText("");
        passwordEditText.setText("");
    }

    void loginUser() {
        firebaseAuthObject.signInUser(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    clearTextBoxes("Wprowadzony email lub hasło są niepoprawne");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }
    }
}
