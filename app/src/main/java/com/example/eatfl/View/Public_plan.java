package com.example.eatfl.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.eatfl.Gridview.GridAdapterMyplan;
import com.example.eatfl.Gridview.GridAdapterPublicPlan;
import com.example.eatfl.Model.My_plan_model;
import com.example.eatfl.Model.Public_plan_model;
import com.example.eatfl.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Public_plan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Public_plan extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Public_plan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment public_plan.
     */
    // TODO: Rename and change types and number of parameters
    public static Public_plan newInstance(String param1, String param2) {
        Public_plan fragment = new Public_plan();
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

    List<Public_plan_model> plans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        plans = new ArrayList<>();
        assert getArguments() != null;
        Bundle bundle = getArguments();
        assert bundle != null;
        String filter = bundle.getString("filter");
        String userId = mAuth.getCurrentUser().getUid();
        show_actionbar("แผนสาธารณะ");
        Log.i("filter", filter);
        assert filter != null;
        if(filter.equals("like")) {
            Log.i("filter1", filter);
            db.collection("plans").whereEqualTo("permission", "public").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        //check like collection is doc_id same user_id
                        queryDocumentSnapshots.forEach(queryDocumentSnapshot -> {
                            db.collection("plans").document(queryDocumentSnapshot.getId()).collection("like").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    //add plan to list
                                    Public_plan_model plan = queryDocumentSnapshot.toObject(Public_plan_model.class);
                                    plan.setDoc_id(queryDocumentSnapshot.getId());
                                    plans.add(plan);
                                    GridAdapterPublicPlan adapter = new GridAdapterPublicPlan(getContext(), plans);
                                    GridView grid = getView().findViewById(R.id.gridViewPublicPlan);
                                    grid.setAdapter(adapter);
                                }
                            });
                        });
                    }
            );
        }else {
            Log.i("filter2", filter);
            db.collection("plans").whereEqualTo("permission", "public").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        plans = queryDocumentSnapshots.toObjects(Public_plan_model.class);
                        //add doc_id to plan
                        plans.forEach(plan -> {
                            plan.setDoc_id(queryDocumentSnapshots.getDocuments().get(plans.indexOf(plan)).getId());
                            Log.d("plan", plan.getDoc_id());
                        });
                        GridAdapterPublicPlan adapter = new GridAdapterPublicPlan(getContext(), plans);
                        GridView grid = getView().findViewById(R.id.gridViewPublicPlan);
                        grid.setAdapter(adapter);
                    }
            );
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_plan, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridView gridView = view.findViewById(R.id.gridViewPublicPlan);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Public_plan_model item = plans.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("doc_id", item.getDoc_id());
            bundle.putString("name", item.getName());
            bundle.putString("money_all", item.getMoney_all().toString());
            bundle.putString("cal_to_day", item.getCal_to_day().toString());
            bundle.putString("pro_to_day", item.getPro_to_day().toString());
            bundle.putString("pro_to_day", item.getPro_to_day().toString());
            bundle.putString("day", item.getDuration().toString());
            bundle.putString("money_save", item.getMoney_save().toString());
            bundle.putString("money_spent", item.getMoney_spent().toString());
            NavHostFragment.findNavController(Public_plan.this)
                    .navigate(R.id.action_public_plan_to_public_plan_detail, bundle);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}