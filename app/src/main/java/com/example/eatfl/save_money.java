package com.example.eatfl;

import android.os.Bundle;

import java.util.HashMap;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        bottom_navbar_hide();


        List<Item> itemsList3 = new ArrayList<>();
        Spinner spinner =	view.findViewById(R.id.spinner_type);
//	Create	an	ArrayAdapter using	 the	string	array	and	a	default	spinner	 layout
        ArrayAdapter<CharSequence> adapter =	ArrayAdapter.createFromResource(requireContext(),
                R.array.type_time_array,	android.R.layout.simple_spinner_item);
//	Specify	 the	layout	to	use	when	the	list	of	choices	 appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//	Apply	 the	adapter	to	the	spinner
        spinner.setAdapter(adapter);
        //show action bar
        show_actionbar("จัดการงบ");

        // Create a HashMap to store items with their names as keys and prices as values
        HashMap<String, Item> itemsMap = new HashMap<>();

        
        db.collection("items").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    String name = document.getString("item_name");
                    String description = document.getString("item_des");
                    Double price = document.getDouble("item_price");
                    String image = document.getString("item_url");
                    String part = document.getString("item_part");
                    String location = document.getString("item_location");

                    // If the item already exists in the map and its price is higher, skip this iteration
                    if (itemsMap.containsKey(name) && itemsMap.get(name).getPrice() <= price) {
                        continue;
                    }

                    // Create a new Item object and put it in the map
                    Item item = new Item(name, description, price, image, part ,location);
                    itemsMap.put(name, item);
                }

                // Convert the values of the HashMap to a list
                List<Item> itemsList = new ArrayList<>(itemsMap.values());
                itemsList3.addAll(itemsList);
                // Now you can use your GridAdapter with the filtered list
                GridView gridView = view.findViewById(R.id.grid_view);
                GridAdapter gridAdapter = new GridAdapter(getContext(), itemsList);
                gridView.setAdapter(gridAdapter);
            }
        });
        //end get data in firestore
        //when click item in gridview send data to fragment
      GridView gridView = view.findViewById(R.id.grid_view);
gridView.setOnItemClickListener((parent, view1, position, id) -> {
    Item item = itemsList3.get(position);
    Bundle bundle = new Bundle();
    bundle.putString("name", item.getName());
    bundle.putString("description", item.getDescription());
    bundle.putDouble("price", item.getPrice());
    bundle.putString("image", item.getImage());
    bundle.putString("part", item.getPart());
    bundle.putString("location", item.getLocation());

    NavHostFragment.findNavController(save_money.this)
            .navigate(R.id.action_save_money_to_detail_item, bundle);
});
        //end when click item in gridview send data to fragment
        super.onViewCreated(view, savedInstanceState);
    }



}