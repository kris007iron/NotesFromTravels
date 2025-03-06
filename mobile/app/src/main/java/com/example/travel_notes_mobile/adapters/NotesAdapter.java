package com.example.travel_notes_mobile.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.view.SingleNoteActivity;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;
import com.example.travel_notes_mobile.viewmodel.NotesViewModelFactory;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;

    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void updateNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notes.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.note_layout, parent, false);
        }
        Note note = notes.get(i);
        TextView title = convertView.findViewById(R.id.title);

        title.setText(note.getTitle());

        ImageView delete = convertView.findViewById(R.id.delete_icon);
        delete.setOnClickListener(v->{
            notes.remove(note);
            notifyDataSetChanged();

            NotesViewModelFactory factory = NotesViewModelFactory.getInstance((Application) context.getApplicationContext());
            NotesViewModel notesViewModel = new ViewModelProvider((ViewModelStoreOwner) context, factory).get(NotesViewModel.class);
            notesViewModel.deleteNote(note);

            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();

        });

        convertView.setOnClickListener(v->{

            Intent intent = new Intent(context, SingleNoteActivity.class);
            intent.putExtra("note", note);
            Log.d("ELOOO", "Starting SingleNoteActivity");
            context.startActivity(intent);
        });

        return convertView;
    }
}
