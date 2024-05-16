package com.example.notesplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesplus.R;
import com.example.notesplus.Utility;
import com.example.notesplus.activity.NoteDetailsActivity;
import com.example.notesplus.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//to use firebase data as a rv we need this note adapter to be inherited from Firestore recycler adapter(firebase UI component)

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {

        super(options);
        this.context=context;
        Log.i("Note Adapter const","inside node adaper"+this.context);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        Log.i("Bhumika adapter","+note fields"+note.getNoteTitle() + note.getNoteContent() + note.getTimeStamp());
        holder.titleTv.setText(note.getNoteTitle());
        holder.contentTv.setText(note.getNoteContent());
        holder.timestampTv.setText(Utility.timestampToString(note.getTimeStamp()));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, NoteDetailsActivity.class);
            intent.putExtra("noteTitle", note.getNoteTitle());
            intent.putExtra("noteContent", note.getNoteContent());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override //here we pass the view we created i.e note_item
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Bhumika adapter","inside on create view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{ //that  hold views for rv note item
        TextView titleTv, contentTv, timestampTv;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.note_title_tv);
            contentTv = itemView.findViewById(R.id.note_content_tv);
            timestampTv = itemView.findViewById(R.id.note_timestamp_tv);
        }
    }
}
