package com.example.pracainzynierska;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ListeningMenu extends WritingMenu {

    LinearLayout recognitionFromHearingMenu, writingFromHearingMenu, passiveListeningMenu, sentenceRecognition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listening_menu);

        recognitionFromHearingMenu = findViewById(R.id.recognition_from_hearing_button_menu);
        assignValue(recognitionFromHearingMenu, ChooseJLPTLVL.class, ListeningRecognition.class);

        writingFromHearingMenu = findViewById(R.id.writing_from_hearing_button_menu);
        assignValue(writingFromHearingMenu, ChooseJLPTLVL.class, ListeningSpeaking.class);

        passiveListeningMenu = findViewById(R.id.passive_listening_button_menu);
        assignValue(passiveListeningMenu, ChooseJLPTLVL.class, ListeningPassive.class);

        sentenceRecognition = findViewById(R.id.sentence_recognition_button_menu);
        sentenceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListeningSentenceRecognitionMenu.class);
                view.getContext().startActivity(intent);
            }
        });
    }

}