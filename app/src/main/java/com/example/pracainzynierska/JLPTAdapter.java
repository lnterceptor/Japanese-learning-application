package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class JLPTAdapter extends ArrayAdapter<JLPTObject> {
    private Context context;
    Class nextActivity;
    public JLPTAdapter(@NonNull Context context, List<JLPTObject> objectList, Class nextActivity) {
        super(context, 0, objectList);
        this.context = context;
        this.nextActivity = nextActivity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.kanji_choose_list_object, parent, false);
        }
        TextView title = view.findViewById(R.id.TitleOfKanjiChoose);
        TextView description = view.findViewById(R.id.DescriptionOfKanjiChoose);
        //FloatingActionButton kanjiImage = view.findViewById(R.id.ImageKanjiChoose);

        JLPTObject object = getItem(position);

        //kanjiImage.setImageResource(object.getImageResourceId());
        title.setText(object.getTitle());
        description.setText(object.getDescription());



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChooseKanji.class);

                Integer title;
                try{
                    title = Integer.parseInt(object.getTitle().replaceAll("[^0-9]", ""));
                    intent.putExtra("lvl", "jlpt_" + title.toString());
                } catch (NumberFormatException e){
                    intent.putExtra("lvl", "other_kanji");
                }

                intent.putExtra("nextActivity", nextActivity);
                context.startActivity(intent);
                //todo: Implement OnItemClickListener, probably i just need some kind of key and place all the grammar in json with proper keys
            }
        });

        return view;
    }
}
