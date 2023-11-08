package com.example.pracainzynierska;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ListeningMenu extends WritingMenu {

    LinearLayout recognitionFromHearingMenu, writingFromHearingMenu, passiveListeningMenu, sentenceRecognition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listening_menu);

        recognitionFromHearingMenu = findViewById(R.id.recognition_from_hearing_button_menu);
        assignValue(recognitionFromHearingMenu, ChooseJLPTLVL.class, ListeningRecognition.class);

        writingFromHearingMenu = findViewById(R.id.writing_from_hearing_button_menu);
        assignValue(writingFromHearingMenu, ChooseJLPTLVL.class, ListeningWriting.class);

        passiveListeningMenu = findViewById(R.id.passive_listening_button_menu);
        assignValue(passiveListeningMenu, ChooseJLPTLVL.class, ListeningPassive.class);

        sentenceRecognition = findViewById(R.id.sentence_recognition_button_menu);
        assignValue(sentenceRecognition, ChooseJLPTLVL.class, ListeningSentenceRecognition.class);
    }

}