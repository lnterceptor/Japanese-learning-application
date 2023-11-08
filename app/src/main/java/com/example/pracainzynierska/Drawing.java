package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Drawing extends AppCompatActivity{
    private DatabaseReference mDatabase;
    private DrawKanjiCanvas surfaceView;
    private Button undo;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.drawing);

            surfaceView = findViewById(R.id.kanjiImage);
            //surfaceView.drawKanji("A");

            undo = findViewById(R.id.undo);
            undo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    surfaceView.undoLastStroke();
                }
            });
        }

        private void drawKanji(String kanji){
            surfaceView.drawKanji(kanji);
        }

}
