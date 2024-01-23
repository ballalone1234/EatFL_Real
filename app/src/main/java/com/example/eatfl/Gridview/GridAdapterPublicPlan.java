package com.example.eatfl.Gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eatfl.Model.My_plan_model;
import com.example.eatfl.Model.Public_plan_model;
import com.example.eatfl.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GridAdapterPublicPlan extends BaseAdapter {
    Context context;
    List<Public_plan_model> plans;
    LayoutInflater inflater;
    String owner_name = "s";
    public GridAdapterPublicPlan(Context context, List<Public_plan_model> places) {
        this.context = context;
        this.plans = places;
    }


    @Override
    public int getCount() {
        return plans.size();
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
            convertView=inflater.inflate(R.layout.plan_public_grid,null);
        }

        Public_plan_model plan = plans.get(position);

        TextView name =convertView.findViewById(R.id.my_plan_name);
        TextView money =convertView.findViewById(R.id.my_plan_money);
        TextView cal =convertView.findViewById(R.id.my_plan_cal);
        TextView pro =convertView.findViewById(R.id.my_plan_pro);
        TextView owner =convertView.findViewById(R.id.owner_plan);
        TextView time_ago =convertView.findViewById(R.id.time_ago);
        TextView like =convertView.findViewById(R.id.like);


        name.setText(plan.getName());
        money.setText(plan.getMoney_all().toString());
        cal.setText(plan.getCal_to_day().toString());
        pro.setText(plan.getPro_to_day().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(plan.getOwner()).get().addOnSuccessListener(documentSnapshot -> {
            owner_name = documentSnapshot.getString("username");
            owner.setText(owner_name);
        });
        time_ago.setText(plan.getFormattedDate());
        like.setText(plan.getLike().toString());


        return convertView;
    }
}
