package com.example.pracainzynierska;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class RecognitionComplexFriendsListAdapter extends ProfileFriendsListAdapter{

    public RecognitionComplexFriendsListAdapter(@NonNull Context context, List<FriendObject> objectList) {
        super(context, objectList);
    }

    @Override
    protected void setOnClickViewListener(View view, int position){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = setIntent();
                intent.putExtra("friendsName", getItem(position).getNick());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    protected Intent setIntent(){
        return new Intent(context, RecognitionComplex.class);
    }
}
