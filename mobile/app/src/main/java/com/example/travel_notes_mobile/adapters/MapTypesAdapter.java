package com.example.travel_notes_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel_notes_mobile.R;
import com.example.travel_notes_mobile.model.MapElement;
import com.example.travel_notes_mobile.model.OnMapTypeChangeListener;

import java.util.List;

public class MapTypesAdapter extends BaseAdapter {
    private Context context;
    private List<MapElement> mapElements;

    public MapTypesAdapter(Context context, List<MapElement> mapElements) {
        this.context = context;
        this.mapElements = mapElements;
    }

    @Override
    public int getCount() {
        return mapElements.size();
    }

    @Override
    public Object getItem(int i) {
        return mapElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.map_type_item, viewGroup, false);
        }

        TextView tv1 = convertView.findViewById(R.id.tv1);
        ImageView iv1 = convertView.findViewById(R.id.iv1);


        MapElement mapElement = mapElements.get(i);
        tv1.setText(mapElement.getText());
        iv1.setImageResource(mapElement.getResourceId());

        iv1.setOnClickListener(v->{
            if(context instanceof OnMapTypeChangeListener){
                ((OnMapTypeChangeListener) context).onMapTypeChanged(mapElement.getType());
            }
        });


        return convertView;
    }
}