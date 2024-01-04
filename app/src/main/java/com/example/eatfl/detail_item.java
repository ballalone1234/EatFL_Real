package com.example.eatfl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail_item#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail_item extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button saveBtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public detail_item() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detail_item.
     */
    // TODO: Rename and change types and number of parameters
    public static detail_item newInstance(String param1, String param2) {
        detail_item fragment = new detail_item();
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
        return inflater.inflate(R.layout.fragment_detail_item, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        show_actionbar("รายละเอียด");
        Bundle bundle = getArguments();
        String name = bundle.getString("name");
        Double price = bundle.getDouble("price");
        String detail = bundle.getString("description");
        String image = bundle.getString("image");
        String part = bundle.getString("part");
        String location = bundle.getString("location");
        ImageView imageView = view.findViewById(R.id.item_image);
        TextView textView = view.findViewById(R.id.title);
        TextView textView1 = view.findViewById(R.id.subtitle);
        TextView textView3 = view.findViewById(R.id.price);
        TextView textView2 = view.findViewById(R.id.detail);
        TextView textView4 = view.findViewById(R.id.namelo);
        TextView textView5 = view.findViewById(R.id.item_distance);

        textView.setText(name);
        textView1.setText(part);
        textView3.setText(price.toString());
        textView2.setText(detail);
        Bundle receivedBundle = NavHostFragment.findNavController(this).getCurrentBackStackEntry().getSavedStateHandle().get("back");
        if (receivedBundle != null)
        {
            String name1 = receivedBundle.getString("name");
            String distance = receivedBundle.getString("distance");
            String price1 = receivedBundle.getString("price");

            // Now you can use these values in your fragment
            // For example, let's log them
            assert price1 != null;
            float f = Float.valueOf(price1.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
            Log.i("ReceivedData", "Name: " + name1 + ", Distance: " + distance + ", Price: " + f);
                textView4.setText(name1);
                textView5.setText(distance);
                textView3.setText(String.valueOf(f));



        }else {
            getDistance(new OnDistanceReceivedListener() {
                @Override
                public void onDistanceReceived(String distance, String destination_addresses) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String destination_addresses2 = destination_addresses;
                            // limit String length
                            if (destination_addresses2.length() > 20) {
                                destination_addresses2 = destination_addresses.substring(0, 20) + "...";
                            }
                            textView4.setText(destination_addresses2);
                            textView5.setText(distance);
                        }
                    });
                }
            }, location);

        }
        saveBtn = view.findViewById(R.id.saveB);
        saveBtn.setOnClickListener(v -> {

        });
        textView4.setOnClickListener(v -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("name", name);
            bundle1.putString("part", part);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_detail_item_to_place_view, bundle);
        });
        Glide.with(getContext()).load(image).into(imageView);

        // you can use findViewById() here
        super.onViewCreated(view, savedInstanceState);
        // you can use findViewById() here
    }




}