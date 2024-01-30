package com.example.eatfl.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatfl.Gridview.GridAdapterMyOrder;
import com.example.eatfl.Model.My_plan_model;
import com.example.eatfl.Model.Plan_item;
import com.example.eatfl.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link public_plan_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class public_plan_detail extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public public_plan_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment public_plan_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static public_plan_detail newInstance(String param1, String param2) {
        public_plan_detail fragment = new public_plan_detail();
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
    boolean isLike = false;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_public_plan_detail, menu);

        // Get the MenuItem for the item
        MenuItem item = menu.findItem(R.id.like_plan)
                ,item2 = menu.findItem(R.id.share);

        db.collection("plans").document(doc_id).collection("like").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                item.setIcon(R.drawable.ph_heart_fill);
                isLike = true;
            }
            else {
                item.setIcon(R.drawable.ph_heart_thin);
                isLike = false;
            }
        });
        item.setOnMenuItemClickListener(menuItem -> {
            //check drawable
            if(isLike){
                likePlan(db.collection("plans").document(doc_id) , -1);
                item.setIcon(R.drawable.ph_heart_thin);
                isLike = !isLike;
                return true;
            }
            else {
                likePlan(db.collection("plans").document(doc_id), 1);
                item.setIcon(R.drawable.ph_heart_fill);
                isLike = !isLike;
                return true;
            }
        });

        item2.setOnMenuItemClickListener(menuItem -> {
            sharePlan(name, money_all, cal_to_day, pro_to_day, day, money_save, money_spent);
            return true;
        });
        // Get the Drawable for the item
        Drawable icon = item.getIcon();

        // If the icon exists, apply the white tint
        if (icon != null) {
            DrawableCompat.setTint(icon, ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    protected void likePlan(DocumentReference docRef , Integer like){
        //update like increment
        docRef.update("like", FieldValue.increment(like));
        //set user like
        if(like == -1)
            docRef.collection("like").document(mAuth.getCurrentUser().getUid()).delete();
        else
        {
            Map<String, Object> data = new HashMap<>();
            data.put("like", true);
            docRef.collection("like").document(mAuth.getCurrentUser().getUid()).set(data);
        }

    }
    private void sharePlan(String name, String money_all, String cal_to_day, String pro_to_day, String day, String money_save, String money_spent) {
        String shareBody = "Plan Name: " + name + "\nTotal Money: " + money_all + "\nCalculate to Day: " + cal_to_day + "\nProject to Day: " + pro_to_day + "\nDay: " + day + "\nMoney Saved: " + money_save + "\nMoney Spent: " + money_spent;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_plan_detail, container, false);
    }
    String doc_id, name, money_all, cal_to_day, pro_to_day, day, money_save, money_spent;

    Button shareBtn;
    List<Plan_item> plan_items;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        shareBtn = view.findViewById(R.id.copyPlanBTN);
        shareBtn.setOnClickListener(v -> {
            copyPlan();
        });

        super.onViewCreated(view, savedInstanceState);
    }
    protected void copyPlan() {
        db.collection("plans").document(doc_id).get().addOnSuccessListener(documentSnapshot -> {
            My_plan_model plan = documentSnapshot.toObject(My_plan_model.class);
            plan.setOwner(mAuth.getCurrentUser().getUid());
            db.collection("plans").add(plan).addOnSuccessListener(documentReference -> {
                //add plan item
                db.collection("plans").document(doc_id).collection("cart").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Plan_item> plan_items = queryDocumentSnapshots.toObjects(Plan_item.class);
                    plan_items.forEach(plan_item -> {
                        db.collection("plans").document(documentReference.getId()).collection("cart").add(plan_item);
                    });
                });
                Toast.makeText(getContext(), "คัดลอกแผนสำเร็จ", Toast.LENGTH_SHORT).show();
                goToMyPlan();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show();

            });
        });
    }

    private void goToMyPlan() {
        NavHostFragment.findNavController(public_plan_detail.this)
                .navigate(R.id.action_global_my_plan);
    }
}