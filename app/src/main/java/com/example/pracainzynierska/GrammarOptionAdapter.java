package com.example.pracainzynierska;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class GrammarOptionAdapter extends ArrayAdapter<GrammarOptionObject> {

    public GrammarOptionAdapter(@NonNull Context context, List<GrammarOptionObject> objectList) {
        super(context, 0, objectList);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.grammar_menu_option, parent, false);
        }
        TextView title = view.findViewById(R.id.grammarOptionTitle);
        TextView description = view.findViewById(R.id.grammarOptionDescription);

        GrammarOptionObject object = getItem(position);

        title.setText(object.getTitle());
        description.setText(object.getDescription());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement OnItemClickListener, probably i just need some kind of key and place all the grammar in json with proper keys
            }
        });

        return view;
    }
}

