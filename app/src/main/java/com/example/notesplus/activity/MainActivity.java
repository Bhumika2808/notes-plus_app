package com.example.notesplus.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.example.notesplus.Utility;
import com.example.notesplus.adapter.NoteAdapter;
import com.example.notesplus.databinding.ActivityMainBinding;
import com.example.notesplus.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding; //binding object helps in accessing views
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "inside Main Activity");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));
        binding.menuBtn.setOnClickListener((v) -> showMenu());
        setupRecyclerView();

    }

    //first we will query the db with the timestamp
    //then create option instance using FirestoreRecyclerOptions(convert query into options)
    //then options passed in noteAdapter
    //and in notes adapter we setting data to text views of note item
    void setupRecyclerView() {
        Log.i(TAG, "inside setUpRecyclerView");
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        binding.notesRv.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        Log.i(TAG, "set Note Adapter");
        binding.notesRv.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, binding.menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle() == "Logout") {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}