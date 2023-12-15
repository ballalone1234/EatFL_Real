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
    List<String> name;
    List<String> description;
    List<String> price;
    List<String> image;
    LayoutInflater inflater;

    public GridAdapter(Context context, List<String> name, List<String> description, List<String> price, List<String> image) {
        this.context = context;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
    @Override
    public int getCount() {
        return name.size();
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

        ImageView imageView=convertView.findViewById(R.id.item_image);
        Glide.with(context).load(image.get(position)).into(imageView);
        TextView textView=convertView.findViewById(R.id.item_name);
        textView.setText(name.get(position));
        TextView textView1=convertView.findViewById(R.id.item_des);
        textView1.setText(description.get(position));
        TextView textView2=convertView.findViewById(R.id.item_price);
        textView2.setText(price.get(position));

        return convertView;
    }
}
