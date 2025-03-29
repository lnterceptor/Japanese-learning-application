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

public class GrammarOptionAdapter extends ArrayAdapter<GrammarObject> {
    private Context context;
    List<GrammarObject> grammarObjects;
    public GrammarOptionAdapter(@NonNull Context context, List<GrammarObject> objectList) {
        super(context, 0, objectList);
        this.context = context;
        this.grammarObjects = objectList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.grammar_menu_option, parent, false);
        }
        TextView title = view.findViewById(R.id.grammarOptionTitle);
        TextView description = view.findViewById(R.id.grammarOptionDescription);

        GrammarObject object = getItem(position);

        title.setText(object.getTitle());
        description.setText(object.getDefinition());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GrammarChoosenMenu.class);
                GrammarObject grammarObject = grammarObjects.get(position);
                intent.putExtra("grammarObject", grammarObject);
                context.startActivity(intent);
            }
        });

        return view;
    }
}

