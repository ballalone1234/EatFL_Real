package com.example.eatfl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridAdapterPlace extends BaseAdapter {
    Context context;
    List<Place> places;
    LayoutInflater inflater;

    public GridAdapterPlace(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }


    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView=inflater.inflate(R.layout.place_grid,null);
        }

        Place place = places.get(position);


        TextView textView=convertView.findViewById(R.id.place_name);
        TextView textView1=convertView.findViewById(R.id.place_distance);
        TextView textView2=convertView.findViewById(R.id.place_price);

        textView.setText(place.getName());
        textView1.setText(place.getDistace());
        textView2.setText("ราคา กก. ล่ะ "+place.getPrice().toString() + " บาท");
        return convertView;
    }
}
