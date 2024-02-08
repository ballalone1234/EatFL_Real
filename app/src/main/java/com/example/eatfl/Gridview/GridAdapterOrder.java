package com.example.eatfl.Gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eatfl.Model.Plan_item;
import com.example.eatfl.R;
import com.example.eatfl.View.AppControl;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GridAdapterOrder extends BaseAdapter {
    Context context;
    List<Plan_item> items;
    LayoutInflater inflater;

    public GridAdapterOrder(Context context, List<Plan_item> items) {
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
            convertView=inflater.inflate(R.layout.order_item_grid,null);
        }

        Plan_item item = items.get(position);

        ImageView imageView=convertView.findViewById(R.id.item_order_image_view);
        Glide.with(context).load(item.getImage()).into(imageView);
        TextView name =convertView.findViewById(R.id.item_order_name);
        TextView price_text=convertView.findViewById(R.id.item_order_price);
        TextView amount_ex=convertView.findViewById(R.id.am_ex);
        TextView amount=convertView.findViewById(R.id.am_e);
        Double price = item.getPrice();
        if(item.getEt().equals("g")){
            price = (price/1000)*item.getAmount();
        }
        else {
            price = price*item.getAmount();
        }
        Button delete =convertView.findViewById(R.id.deleteBTN);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(AppControl.mAuth.getCurrentUser().getUid()).collection("cart").document(item.getDocId()).delete();
                items.remove(position);
                notifyDataSetChanged();
            }
        });
        name.setText(item.getPart());
        price_text.setText(String.format("%.2f", price) );
        amount_ex.setText(item.getEt());
        amount.setText(item.getAmount().toString());
        return convertView;
    }
}
