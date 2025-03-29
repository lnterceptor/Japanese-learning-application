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

public class ProfileFriendsListAdapter extends ArrayAdapter<FriendObject> {
        protected Context context;
        public ProfileFriendsListAdapter(@NonNull Context context, List<FriendObject> objectList) {
            super(context, 0, objectList);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.profile_friend_list_object, parent, false);
            }
            TextView name = view.findViewById(R.id.FriendsName);

            String object = getItem(position).nick;
            name.setText(object);


            setOnClickViewListener(view, position);

            return view;
        }
        protected void setOnClickViewListener(View view, int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileFriend.class);
                    intent.putExtra("userID", getItem(position).auth);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }
}
