package com.example.eatfl.Gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eatfl.Model.Place;
import com.example.eatfl.R;

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


        TextView textView=convertView.findViewById(R.id.item_order_name);
        TextView textView1=convertView.findViewById(R.id.item_order_price);
        TextView textView2=convertView.findViewById(R.id.am_ex);

        textView.setText(place.getName());
        textView1.setText(place.getDistace());
        textView2.setText("ราคา กก. ล่ะ "+place.getPrice().toString() + " บาท");
        return convertView;
    }
}
