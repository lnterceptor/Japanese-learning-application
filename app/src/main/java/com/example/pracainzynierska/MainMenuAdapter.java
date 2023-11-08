package com.example.pracainzynierska;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuViewHolder> {

    Context context;
    List<MainMenuOption> options;
    int menus = 0;
    public MainMenuAdapter(Context context, List<MainMenuOption> options) {
        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public MainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainMenuViewHolder(LayoutInflater.from(context).inflate(R.layout.main_menu_view_option, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuViewHolder holder,int position) {
        holder.titleOfOption.setText(options.get(position).optionName);
        if(menus == 0) {
            holder.menuToChangeInto = WritingMenu.class;
        }
        else if (menus == 1) {
            holder.menuToChangeInto = ListeningMenu.class;
        }
        else if(menus == 2){
            holder.menuToChangeInto = GrammarMenu.class;
        }
        else if(menus == 3){
            holder.menuToChangeInto = OtherExercisesMenu.class;
        }
        else{
            holder.menuToChangeInto = AddExerciseMenu.class;
        }
        menus += 1;
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

}
