package com.example.eatfl.View;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatfl.Gridview.GridAdapterMyOrder;
import com.example.eatfl.Model.Plan_item;
import com.example.eatfl.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link show_detail_my_plan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class show_detail_my_plan extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public show_detail_my_plan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment show_detail_my_plan.
     */
    // TODO: Rename and change types and number of parameters
    public static show_detail_my_plan newInstance(String param1, String param2) {
        show_detail_my_plan fragment = new show_detail_my_plan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_detail_my_plan, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_my_plan, menu);

        // Get the MenuItem for the item
        MenuItem item = menu.findItem(R.id.bin)
                ,item2 = menu.findItem(R.id.share);
        item.setOnMenuItemClickListener(menuItem -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this plan?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User clicked the "Yes" button, delete the plan
                        db.collection("plans").document(doc_id).delete();
                        Toast.makeText(getContext(), "ลบแผนเรียบร้อย", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(show_detail_my_plan.this)
                                .navigate(R.id.action_global_my_plan);
                    })
                    .setNegativeButton("No", null) // User clicked the "No" button, do nothing
                    .show();
            return true;}
        );

        item2.setOnMenuItemClickListener(menuItem -> {
            //check permission
            db.collection("plans").document(doc_id).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.get("permission").equals("public")){
                    //update permission
                    db.collection("plans").document(doc_id).update("permission" , "private");
                    Toast.makeText(getContext(), "แผนของคุณถูกเปลี่ยนเป็นส่วนตัวแล้ว", Toast.LENGTH_SHORT).show();
                }
                else {
                    //update permission
                    db.collection("plans").document(doc_id).update("permission" , "public");
                    Toast.makeText(getContext(), "แผนของคุณถูกแชร์แล้ว", Toast.LENGTH_SHORT).show();
                }
            });
            return true;}
        );


        // Get the Drawable for the item
        Drawable icon = item.getIcon();

        // If the icon exists, apply the white tint
        if (icon != null) {
            DrawableCompat.setTint(icon, ContextCompat.getColor(getContext(), R.color.white));
        }
    }


    String doc_id, name, money_all, cal_to_day, pro_to_day, day, money_save, money_spent;

    Button usePlanBTN;
    List<Plan_item> plan_items;
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String userId = mAuth.getCurrentUser().getUid();
        Bundle bundle = getArguments();
        if (bundle != null) {
             doc_id = bundle.getString("doc_id");
            name = bundle.getString("name");
            money_all = bundle.getString("money_all");
            cal_to_day = bundle.getString("cal_to_day");
            pro_to_day = bundle.getString("pro_to_day");
            day = bundle.getString("day");
            money_save = bundle.getString("money_save");
            money_spent = bundle.getString("money_spent");
            TextView money_all_view = view.findViewById(R.id.money_all_value);
            TextView day_view = view.findViewById(R.id.duration_value);
            EditText name_view = view.findViewById(R.id.editTextText);
            TextView cal_to_day_view = view.findViewById(R.id.cal_to_day_value);
            TextView pro_to_day_view = view.findViewById(R.id.Pro_to_day_value);
            TextView money_spent_view = view.findViewById(R.id.money_spent_value);
            TextView money_save_view = view.findViewById(R.id.money_save_value);
            day_view.setText(day);
            money_all_view.setText(money_all + " บาท");
            name_view.setText(name);
            cal_to_day_view.setText(cal_to_day);
            pro_to_day_view.setText(pro_to_day);
            money_spent_view.setText(money_spent+ " บาท");
            money_save_view.setText(money_save+ " บาท");


        }
         db.collection("plans").document(doc_id).collection("cart").get().addOnSuccessListener(queryDocumentSnapshots -> {
            plan_items = queryDocumentSnapshots.toObjects(Plan_item.class);;
            GridView gridView = view.findViewById(R.id.gridViewOrder);
            GridAdapterMyOrder adapter = new GridAdapterMyOrder(getContext(), plan_items);
            gridView.setAdapter(adapter);

        });
        usePlanBTN = view.findViewById(R.id.usePlanBTN);
        usePlanBTN.setOnClickListener(v -> {
          db.collection("users").document(userId).update("current_plan" , db.document("plans/"+doc_id));
            Toast.makeText(getContext(), "ใช้แผนนี้เป็นแผนปัจจุบัน", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(show_detail_my_plan.this)
                    .navigate(R.id.action_global_home2);
        });


        super.onViewCreated(view, savedInstanceState);
    }

}
