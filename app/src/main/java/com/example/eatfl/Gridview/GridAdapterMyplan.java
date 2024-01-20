package com.example.eatfl.Gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eatfl.Model.My_plan_model;
import com.example.eatfl.R;

import java.util.List;

public class GridAdapterMyplan extends BaseAdapter {
    Context context;
    List<My_plan_model> plans;
    LayoutInflater inflater;

    public GridAdapterMyplan(Context context, List<My_plan_model> places) {
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
            convertView=inflater.inflate(R.layout.my_plan_grid,null);
        }

        My_plan_model plan = plans.get(position);

        TextView name =convertView.findViewById(R.id.my_plan_name);
        TextView money =convertView.findViewById(R.id.my_plan_money);
        TextView cal =convertView.findViewById(R.id.my_plan_cal);
        TextView pro =convertView.findViewById(R.id.my_plan_pro);

        name.setText(plan.getName());
        money.setText(plan.getMoney_all().toString());
        cal.setText(plan.getCal_to_day().toString());
        pro.setText(plan.getPro_to_day().toString());


        return convertView;
    }
}
