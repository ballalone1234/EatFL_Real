package com.example.eatfl.View;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eatfl.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Home extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth mAuth;
    FirebaseFirestore db ;
    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    }

   //รับค่าจากหน้า Login มาแสดงผล

    TextView planName, calToday, proToday , moneyToUse;
    ConstraintLayout myPlan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        ((MainActivity) getActivity()).getSupportActionBar().show();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //get data in firestore

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        Bundle bundle = getArguments();
        //String username = bundle.getString("my_key");
        DocumentReference docRef = db.collection("users").document(Objects.requireNonNull(mAuth.getUid()));
        TextView tv = view.findViewById(R.id.calshosw);
        TextView pv = view.findViewById(R.id.proshosw);
        // Retrieve the document
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    // Document was found, you can retrieve the data with the getData() method
                    Map<String, Object> data = document.getData();
                    // Now you can use the data
                    // For example, to get the username:
                    planName = view.findViewById(R.id.my_plan_name);
                    calToday = view.findViewById(R.id.my_plan_cal);
                    proToday = view.findViewById(R.id.my_plan_pro);
                    moneyToUse = view.findViewById(R.id.my_plan_money);
                    myPlan = view.findViewById(R.id.conCurrentPlan);
                    String username = (String) data.get("username");
                    Number BMR = (Number) data.get("BMR");
                    Number weight = (Number) data.get("weight");
                    Number height = (Number) data.get("height");
                    Double minprotien = weight.doubleValue() * 1.2;
                    Double maxprotien = weight.doubleValue() * 2;
                    String protienPerDay = String.format("%.2f", minprotien) + " - " + String.format("%.2f", maxprotien) + " g.";
                    //get age

                    DocumentReference planRef = data.get("current_plan") != null ? (DocumentReference) data.get("current_plan") : null;
                    planName.setText("คุณยังไม่มีแผนในปัจจุบัน");
                    // Set the username to the TextView
                    assert planRef != null;
                    planRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot document1 = task1.getResult();
                            if (document1.exists()) {
                                // Document was found, you can retrieve the data with the getData() method
                                Map<String, Object> data1 = document1.getData();
                                // Now you can use the data
                                // For example, to get the username:
                                String planname = (String) data1.get("name");
                                Double planCal = (Double) data1.get("cal_to_day");
                                Double planPro = (Double) data1.get("pro_to_day");
                                Double planMoney = (Double) data1.get("money_all");
                                planName.setText(planname);
                                calToday.setText(planCal.toString());
                                proToday.setText(planPro.toString());
                                moneyToUse.setText(planMoney.toString());
                                myPlan.setOnClickListener(
                                        v -> {
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("doc_id", planRef.getId());
                                            bundle1.putString("name", planname);
                                            bundle1.putString("money_all", planMoney.toString());
                                            bundle1.putString("cal_to_day", planCal.toString());
                                            bundle1.putString("pro_to_day", planPro.toString());
                                            bundle1.putString("day", data1.get("duration").toString());
                                            bundle1.putString("money_save", data1.get("money_save").toString());
                                            bundle1.putString("money_spent", data1.get("money_spent").toString());
                                            bundle1.putString("owner", data1.get("owner").toString());

                                            show_actionbar("แผนปัจจุบันของฉัน");
                                            NavHostFragment.findNavController(Home.this)
                                                    .navigate(R.id.action_home2_to_show_detail_my_plan, bundle1);
                                        }
                                );
                            } else {
                                // Document was not found
                                Log.d(TAG, "No such document");
                            }
                        }
                    });
                    tv.setText(String.format("%.2f kcal.", BMR));
                    pv.setText(protienPerDay);
                } else {
                    // Document was not found
                    Log.d(TAG, "No such document");
                }
            } else {
                // Task failed with an exception
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


        return view;
    }
    public void onResume() {
        hide_actionbar();
        super.onResume();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottom_navbar_show();
        //onclick item in bottom navbar
        getUsernameByDocumentId(mAuth.getCurrentUser().getUid());
        LinearLayout linearLayout = view.findViewById(R.id.bt1);
        LinearLayout btnMyplan = view.findViewById(R.id.my_plan_btn);
        LinearLayout feed = view.findViewById(R.id.feed_btn);
        linearLayout.setOnClickListener(v -> {
            bottom_navbar_hide();
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.action_home2_to_save_money);
        });
        btnMyplan.setOnClickListener(v -> {
            bottom_navbar_hide();
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.action_home2_to_my_plan);
        });
        feed.setOnClickListener(v -> {
            bottom_navbar_hide();
            Bundle bundle = new Bundle();
            bundle.putString("filter","all");

            NavHostFragment.findNavController(this).navigate(R.id.action_home2_to_public_plan,bundle);
        });



        super.onViewCreated(view, savedInstanceState);
    }
}