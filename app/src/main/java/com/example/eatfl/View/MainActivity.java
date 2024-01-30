package com.example.eatfl.View;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatfl.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                // Handle the navigation action here
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Home()).commit();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Navigate to Profile Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new profile()).commit();
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                // Handle the navigation action here

                return true;
            }
            return false;
        });
    }
}