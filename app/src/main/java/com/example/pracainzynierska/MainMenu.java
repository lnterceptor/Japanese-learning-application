package com.example.pracainzynierska;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        List<MainMenuOption> options = new ArrayList<>();

        //options.add(new MainMenuOption("Profil"));
        options.add(new MainMenuOption("Pisanie"));
        options.add(new MainMenuOption("Słuchanie"));
        options.add(new MainMenuOption("Gramatyka"));
        options.add(new MainMenuOption("Inne"));
        options.add(new MainMenuOption("Stworz cwiczenie"));


        RecyclerView recyclerView = findViewById(R.id.MainMenuRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainMenuAdapter(getApplicationContext(), options));
    }
}