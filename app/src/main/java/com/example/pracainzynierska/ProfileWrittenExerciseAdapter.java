package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ProfileWrittenExerciseAdapter extends ArrayAdapter<RecognitionComplexObject> {

    protected Context context;
    public ProfileWrittenExerciseAdapter(@NonNull Context context, List<RecognitionComplexObject> objectList) {
        super(context, 0, objectList);
        this.context = context;
    }

    protected View getView(ViewGroup parent){
        return LayoutInflater.from(getContext()).inflate(R.layout.profile_written_exercise_object, parent, false);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = getView(parent);
            //view = LayoutInflater.from(getContext()).inflate(R.layout.profile_written_exercise_object, parent, false);
        }
        TextView sentenceInPolish = view.findViewById(R.id.FriendsName);
        TextView correctSentence = view.findViewById(R.id.answer_1);
        TextView incorrectSentence1 = view.findViewById(R.id.answer_2);
        TextView incorrectSentence2 = view.findViewById(R.id.answer_3);
        TextView incorrectSentence3 = view.findViewById(R.id.answer_4);

        sentenceInPolish.setText(getItem(position).getPolishSentence());
        correctSentence.setText(getItem(position).getCorrectSentence());
        incorrectSentence1.setText(getItem(position).getIncorrectSentence1());
        incorrectSentence2.setText(getItem(position).getIncorrectSentence2());
        incorrectSentence3.setText(getItem(position).getIncorrectSentence3());


        setOnClickViewListener(view, position);

        return view;
    }
    protected void setOnClickViewListener(View view, int position){
    }
}
