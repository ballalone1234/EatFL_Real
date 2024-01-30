package com.example.eatfl.View;

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
import android.widget.GridView;

import com.example.eatfl.Gridview.GridAdapterMyplan;
import com.example.eatfl.Model.My_plan_model;
import com.example.eatfl.R;

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
        setHasOptionsMenu(true);
    }


    List<My_plan_model> plans;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        show_actionbar("แผนของฉัน");
        // Inflate the layout for this fragment
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("plans").whereEqualTo("owner" , userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            plans = queryDocumentSnapshots.toObjects(My_plan_model.class);
               //add doc_id to plan

                    plans.forEach(plan -> {
                        plan.setDoc_id(queryDocumentSnapshots.getDocuments().get(plans.indexOf(plan)).getId());
                        Log.d("plan", plan.getDoc_id());
                        Log.d("plan", plan.getCal_to_day().toString());
                    });
            GridAdapterMyplan adapter = new GridAdapterMyplan(getContext(),plans);
            GridView grid = getView().findViewById(R.id.gridViewMyPlan);
            grid.setAdapter(adapter);
        }
        );
        return inflater.inflate(R.layout.fragment_my_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridView gridView = view.findViewById(R.id.gridViewMyPlan);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            My_plan_model item = plans.get(position);
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
            NavHostFragment.findNavController(my_plan.this)
                    .navigate(R.id.action_my_plan_to_show_detail_my_plan, bundle);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}