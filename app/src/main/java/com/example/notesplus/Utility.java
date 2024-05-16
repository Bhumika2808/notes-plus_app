package com.example.notesplus;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static CollectionReference getCollectionReferenceForNotes() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(currUser.getUid()).collection("my_notes");
    }
    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp.toDate());
    }
}
