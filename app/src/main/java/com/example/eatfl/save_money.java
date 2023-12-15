package com.example.eatfl;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link save_money#newInstance} factory method to
 * create an instance of this fragment.
 */
public class save_money extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public save_money() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment save_money.
     */
    // TODO: Rename and change types and number of parameters
    public static save_money newInstance(String param1, String param2) {
        save_money fragment = new save_money();
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
        return inflater.inflate(R.layout.fragment_save_money, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //show action bar
        show_actionbar("จัดการงบ");
        String[] name = new String[0];
        String[] description = new String[0];
        String[] price ;
        String[] image ;

        //get data in firestore
        db.collection("items").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> nameList = new ArrayList<>();
                List<String> descriptionList = new ArrayList<>();
                List<String> priceList = new ArrayList<>();
                List<String> imageList = new ArrayList<>();
                for(QueryDocumentSnapshot document : task.getResult()){
                    nameList.add(document.getString("item_name"));
                    descriptionList.add(document.getString("item_des"));
                    priceList.add(document.getString("item_price"));
                    imageList.add(document.getString("item_url"));
                }

                GridView gridView = view.findViewById(R.id.grid_view);
                GridAdapter gridAdapter = new GridAdapter(getContext(),nameList,descriptionList,priceList,imageList);
                gridView.setAdapter(gridAdapter);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}