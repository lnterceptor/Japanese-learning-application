package com.example.pracainzynierska;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WritingMenu extends AppCompatActivity {

    LinearLayout drawingMenu, recognitionMenu, readingMenu, translationMenu, complexRecognitionMenu;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_menu);

        drawingMenu = findViewById(R.id.drawing_button_menu);
        assignValue(drawingMenu, ChooseJLPTLVL.class, Drawing.class);

        recognitionMenu = findViewById(R.id.recognition_button_menu);
        assignValue(recognitionMenu, ChooseJLPTLVL.class, Recognition.class);

        readingMenu = findViewById(R.id.reading_button_menu);
        assignValue(readingMenu, ChooseJLPTLVL.class, Reading.class);

        translationMenu = findViewById(R.id.translation_button_menu);
        assignValue(translationMenu, ChooseJLPTLVL.class, Translation.class);

        complexRecognitionMenu = findViewById(R.id.recognition_complex_button_menu);
        assignValue(complexRecognitionMenu, ChooseJLPTLVL.class, ComplexRecognition.class);

    }

    void assignValue(LinearLayout button, Class activityClass, Class nextActivity){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), activityClass);
                intent.putExtra("nextActivity", nextActivity);
                view.getContext().startActivity(intent);
            }
        });
    }
}