package com.example.eatfl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        String price = bundle.getString("price");
        String detail = bundle.getString("description");
        String image = bundle.getString("image");
        String part = bundle.getString("part");
        ImageView imageView = view.findViewById(R.id.item_image);
        TextView textView = view.findViewById(R.id.title);
        TextView textView1 = view.findViewById(R.id.subtitle);
        TextView textView3 = view.findViewById(R.id.price);
        TextView textView2 = view.findViewById(R.id.detail);
        textView.setText(name);
        textView1.setText(part);
        textView3.setText(price);
        textView2.setText(detail);
        Glide.with(getContext()).load(image).into(imageView);
        // you can use findViewById() here
        super.onViewCreated(view, savedInstanceState);
        // you can use findViewById() here
    }
}