package com.example.eatfl;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppControl extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void bottom_navbar_show() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
    public void bottom_navbar_hide() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
    }
    void hide_actionbar(){
        ((MainActivity) getActivity()).getSupportActionBar().hide();
    }
    void show_actionbar(String title){
        ((MainActivity) getActivity()).getSupportActionBar().show();
        //change title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("จัดการงบ");
        //change back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //change background color
        ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.primary));

    }
}
