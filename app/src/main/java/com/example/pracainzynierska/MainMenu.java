package com.example.pracainzynierska;

import static com.example.pracainzynierska.FirebaseAuthObject.mAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {
    static FirebaseCurrentUserObject user = new FirebaseCurrentUserObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);




        List<MainMenuOption> options = new ArrayList<>();
        options.add(new MainMenuOption("Poznawanie"));
        options.add(new MainMenuOption("Powtarzanie"));
        options.add(new MainMenuOption("Gramatyka"));
        options.add(new MainMenuOption("Stwórz ćwiczenie"));
        options.add(new MainMenuOption("Profil"));


        RecyclerView recyclerView = findViewById(R.id.MainMenuRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainMenuAdapter(getApplicationContext(), options));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainMenu.user.refreshData();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }
}