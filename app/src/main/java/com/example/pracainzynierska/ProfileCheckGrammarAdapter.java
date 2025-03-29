package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ProfileCheckGrammarAdapter extends ArrayAdapter<ProfileGrammarObject> {
    public ProfileCheckGrammarAdapter(@NonNull Context context, List<ProfileGrammarObject> objectList) {
        super(context, 0, objectList);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.profile_grammar_object, parent, false);
        }
        TextView progress = view.findViewById(R.id.progressPercentage);
        TextView title = view.findViewById(R.id.TitleOfKanji);

        ProgressBar percentDone = view.findViewById(R.id.percentDone);
        ProfileGrammarObject object = getItem(position);
        int progressToSet = (int) (((float) object.getAmount() / object.getOverallAmount()) * 100);
        progress.setText(progressToSet + "%");
        title.setText(object.getTitle());
        if(object.getTitle().length() > 3){
            title.setTextSize(25);
        }
        if(object.getTitle().length() > 5){
            title.setTextSize(17);
            title.setMaxLines(2);
        }
        if(object.getTitle().length() > 8){
            title.setTextSize(15);
            title.setMaxLines(2);
        }
        percentDone.setProgress(object.getAmount());
        percentDone.setMax(object.getOverallAmount());

        return view;
    }
}
