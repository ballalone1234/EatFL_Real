package com.example.eatfl;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link place_view#newInstance} factory method to
 * create an instance of this fragment.
 */

public class place_view extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public place_view() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment place_view.
     */
    // TODO: Rename and change types and number of parameters
    public static place_view newInstance(String param1, String param2) {
        place_view fragment = new place_view();
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
        return inflater.inflate(R.layout.fragment_place_view, container, false);
    }

    GridView gridView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //get data from firestore
        HashMap<String, Place> itemsMap = new HashMap<>();
        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        String part = bundle.getString("part");
        String origin = "";
        try {
            db.collection("items")
                    .whereEqualTo("item_name" , name)
                    .whereEqualTo("item_part" , part)
                    .get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Double price = document.getDouble("item_price");
                                String location = document.getString("item_location");
                                Log.i("location",location);

                                getDistance(new OnDistanceReceivedListener() {
                                    @Override
                                    public void onDistanceReceived(String distance, String destination_addresses) {

                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String destination_addresses2 = destination_addresses;
                                                // limit String length
                                                if (destination_addresses2.length() > 20) {
                                                    destination_addresses2 = destination_addresses.substring(0, 20) + "...";
                                                }
                                                Place place = new Place(destination_addresses2,  distance, price);
                                                itemsMap.put(price.toString(), place);
                                                Log.i("itemsMap",itemsMap.toString());
                                                List<Place> itemsList = new ArrayList<>(itemsMap.values());
                                                // Now you can use your GridAdapter with the filtered list
                                                GridView gridView = view.findViewById(R.id.grid_view_place);
                                                GridAdapterPlace gridAdapterPlace  = new GridAdapterPlace(getContext(), itemsList);
                                                gridView.setAdapter(gridAdapterPlace);
                                            }

                                        });


                                    }
                                }, location);
                            }

                        }
                    });
        }catch (Exception e) {
            Log.i("error", e.toString());
        };

        //end get data in firestore
        //when click item in gridview send data to fragment
        GridView gridView = view.findViewById(R.id.grid_view_place);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            TextView price = view1.findViewById(R.id.am_ex);
            TextView distance = view1.findViewById(R.id.item_order_price);
            TextView namep = view1.findViewById(R.id.item_order_name);
            Bundle bundle2 = new Bundle();
            bundle2.putString("name", namep.getText().toString());
            bundle2.putString("distance", distance.getText().toString());
            bundle2.putString("price", price.getText().toString());
            //when click item in gridview send data to previous fragment
            NavController navController = NavHostFragment.findNavController(place_view.this);
            NavBackStackEntry previousBackStackEntry = navController.getPreviousBackStackEntry();

            if (previousBackStackEntry != null) {
                SavedStateHandle savedStateHandle = previousBackStackEntry.getSavedStateHandle();

                if (savedStateHandle != null) {
                    savedStateHandle.set("back", bundle2);
                    navController.popBackStack();
                } else {
                    Log.e("NavigationError", "SavedStateHandle is null");
                }
            } else {
                Log.e("NavigationError", "No previous back stack entry found");
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}