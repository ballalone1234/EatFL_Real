package com.example.eatfl;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        // Retrieve the document
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    // Document was found, you can retrieve the data with the getData() method
                    Map<String, Object> data = document.getData();
                    // Now you can use the data
                    // For example, to get the username:

                    String username = (String) data.get("username");
                    Number BMR = (Number) data.get("BMR");




                    //get age

                    // Set the username to the TextView
                    tv.setText(String.format("%.2f", BMR));
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
        LinearLayout linearLayout = view.findViewById(R.id.bt1);
        LinearLayout btnMyplan = view.findViewById(R.id.my_plan_btn);
        linearLayout.setOnClickListener(v -> {
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.action_home2_to_save_money);
        });
        btnMyplan.setOnClickListener(v -> {
            NavHostFragment.findNavController(Home.this)
                    .navigate(R.id.action_home2_to_my_plan);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}