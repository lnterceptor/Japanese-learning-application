package com.example.pracainzynierska;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FirebaseCallback {
    void onCallback(Task<AuthResult> task);
}