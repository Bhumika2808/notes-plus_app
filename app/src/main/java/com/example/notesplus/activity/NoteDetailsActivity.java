package com.example.notesplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notesplus.R;
import com.example.notesplus.Utility;
import com.example.notesplus.databinding.ActivityNoteDetailsBinding;
import com.example.notesplus.model.Note;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NoteDetailsActivity";
    boolean isEditMode = false;
    String docId = null;
    private ActivityNoteDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, "inside note details activity");

        //receive data from intent passed from note adapter
        String title = getIntent().getStringExtra("noteTitle");
        String content = getIntent().getStringExtra("noteContent");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;

        }
        if (isEditMode) {
            binding.newNoteLabel.setText(R.string.edit_your_note);
            binding.noteTitleEt.setText(title);
            binding.contentEt.setText(content);
            binding.deleteNoteBtn.setVisibility(View.VISIBLE);
        }

        binding.deleteNoteBtn.setOnClickListener((v) -> deleteNoteFromFirebase());
        binding.saveNoteBtn.setOnClickListener((v) -> saveNote());
    }

    private void saveNote() {
        String noteTitle = binding.noteTitleEt.getText().toString();
        String noteContent = binding.contentEt.getText().toString();

        if (noteTitle.isEmpty()) {
            binding.noteTitleEt.setError("Title is required");
            return;
        }
        if (noteContent.isEmpty()) {
            binding.contentEt.setError("Content is empty");
            return;
        }

        Note note = new Note();
        note.setNoteTitle(noteTitle);
        note.setNoteContent(noteContent);
        note.setTimeStamp(Timestamp.now());
        saveNoteToFirebase(note);
    }

    private void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //note is added in curr user id document collection
                Utility.showToast(NoteDetailsActivity.this, "Note Added Successfully");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Retry,error while adding note");
            }
        });
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //note is deleted
                Utility.showToast(NoteDetailsActivity.this, "Note Deleted Successfully");
                finish();
            } else {
                Utility.showToast(NoteDetailsActivity.this, "Retry,error while deleting note");
            }
        });
    }
}