package com.example.eatfl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.eatfl.Gridview.GridAdapterMyplan;
import com.example.eatfl.Model.My_plan_model;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link my_plan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class my_plan extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public my_plan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment my_plan.
     */
    // TODO: Rename and change types and number of parameters
    public static my_plan newInstance(String param1, String param2) {
        my_plan fragment = new my_plan();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("plans").whereEqualTo("owner" , userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<My_plan_model> plans = queryDocumentSnapshots.toObjects(My_plan_model.class);
                    plans.forEach(plan -> {
                        Log.d("plan", plan.getCal_to_day().toString());
                    });
            GridAdapterMyplan adapter = new GridAdapterMyplan(getContext(),plans);
            GridView grid = getView().findViewById(R.id.gridViewMyPlan);
            grid.setAdapter(adapter);
        }
        );
        return inflater.inflate(R.layout.fragment_my_plan, container, false);
    }


}