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

public class GridAdapter extends BaseAdapter {
    Context context;
    List<Item> items;
    LayoutInflater inflater;

    public GridAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
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
            convertView=inflater.inflate(R.layout.itew_grid,null);
        }

        Item item = items.get(position);

        ImageView imageView=convertView.findViewById(R.id.item_image);
        Glide.with(context).load(item.getImage()).into(imageView);
        TextView textView=convertView.findViewById(R.id.item_name);
        TextView textView1=convertView.findViewById(R.id.item_des);
        TextView textView2=convertView.findViewById(R.id.item_price);
        textView.setText(item.getName());
        textView1.setText(item.getDescription());
        textView2.setText(item.getPrice().toString());
        return convertView;
    }
}
