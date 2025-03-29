package com.example.pracainzynierska;
import android.net.wifi.hotspot2.pps.Credential;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class FirebaseAuthObject {
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

    public Task<AuthResult> createUser(String email, String password, FirebaseCallback callback) {
        Task<AuthResult> userCredential = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback::onCallback);
        return userCredential;
    }

    public void addUserToDatabase(Task<AuthResult> user, String nick){
        String uID = user.getResult().getUser().getUid();
        DatabaseReference currentUserDb = mDatabase.child(uID);
        currentUserDb.child("Nick").setValue(nick);
        currentUserDb.child("Friends").setValue("");
        currentUserDb.child("Grammar").setValue("");
        currentUserDb.child("Kanji").setValue("");
    }


    public Task<AuthResult> signInUser(String email, String password) {
        Task<AuthResult> userCredential = mAuth.signInWithEmailAndPassword(email, password);
        return userCredential;
    }

    public void signOutUser() {
        mAuth.signOut();
    }
}
