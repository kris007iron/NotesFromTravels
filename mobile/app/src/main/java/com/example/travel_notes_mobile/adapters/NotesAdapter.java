package com.example.travel_notes_mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.model.Note;
import com.example.travel_notes_mobile.view.SingleNoteActivity;
import com.example.travel_notes_mobile.viewmodel.NotesViewModel;

import java.util.List;

public class NotesAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;
    private Activity activity;

    public NotesAdapter(Context context, List<Note> notes, Activity activity) {
        this.context = context;
        this.notes = notes;
        this.activity = activity;

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
        TextView location = convertView.findViewById(R.id.location);


        title.setText(note.getTitle());
        String location_string = "Coordinates: " + note.getLocation().getLatitude() + ", " + note.getLocation().getLongitude();
        location.setText(location_string);
        ImageView delete = convertView.findViewById(R.id.delete_icon);
        delete.setOnClickListener(v->{
//            notes.remove(note);
            NotesViewModel notesViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotesViewModel.class);
            notesViewModel.deleteNote(note);
            notifyDataSetChanged();

            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();

        });

        convertView.setOnClickListener(v->{

            Intent intent = new Intent(context, SingleNoteActivity.class);
            intent.putExtra("note", note);
            Log.d("ELOOO", "Starting SingleNoteActivity");
            activity.startActivityForResult(intent, 1);
        });

        return convertView;
    }
}
