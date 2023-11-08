package com.example.pracainzynierska;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenuViewHolder extends RecyclerView.ViewHolder {

    TextView titleOfOption;
    Class menuToChangeInto;

    public MainMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        titleOfOption = itemView.findViewById(R.id.nameOfMainMenuItem);

        LinearLayout checkOtherMenu = itemView.findViewById(R.id.pressToGoToOtherMenus);
        assignValue(checkOtherMenu);
    }

    void assignValue(LinearLayout button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), menuToChangeInto);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                view.getContext().startActivity(intent);
            }
        });
    }


}
