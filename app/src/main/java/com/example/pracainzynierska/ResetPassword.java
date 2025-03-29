package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

public class ResetPassword extends AppCompatActivity {
    Button check;
    EditText newPasswordEditText, repeatNewPasswordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        setEditText();
        setButton();
    }
    void setButton(){
        check = findViewById(R.id.check);
        check.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                checkIfCorrect();
            }
        });
    }
    void setNewPassword(String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Hasło zostało zmienione", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Nie udało się zaktualizować hasła", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    void checkIfCorrect(){
        if(!checkPassword()) {
            if (!newPasswordEditText.toString().equals(repeatNewPasswordEditText.toString())) {
                Toast.makeText(getApplicationContext(), "Nowe hasło i powtórzone nowe hasło się nie zgadzają", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Nowe hasło jest zbyt krótkie", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            setNewPassword(newPasswordEditText.toString());
        }
    }
    boolean checkPassword(){
        if(!newPasswordEditText.toString().equals(repeatNewPasswordEditText.toString())) return false;
        else return newPasswordEditText.toString().length() >= 12;
    }


    void setEditText(){
        newPasswordEditText = findViewById(R.id.newPassword);
        repeatNewPasswordEditText = findViewById(R.id.repeatNewPassword);
    }

}
