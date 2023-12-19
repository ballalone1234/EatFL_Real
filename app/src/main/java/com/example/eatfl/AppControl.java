package com.example.eatfl;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class AppControl extends Fragment {
    String current_location = "Hà Nội";
    String apiKey = "AIzaSyAg73jf1U-e9efFTXy4hiCA_Wef_b0B26I"; // replace with your API key
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Test save git
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        //change back button
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //change background color
        ((MainActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.primary));

    }

    public interface OnDistanceReceivedListener {
        void onDistanceReceived(String distance, String destination_addresses);
    }
    public void getDistance(OnDistanceReceivedListener listener , String des) {
    Executors.newSingleThreadExecutor().execute(new Runnable() {
        @Override
        public void run() {
            try {
                String origin = getCurrentLocation();
                String destination = des;
                String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin + "&destinations=" + destination + "&key=" + apiKey;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                String line, outputString = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    outputString += line;
                }
                JSONObject jsonResponse = new JSONObject(outputString);
                String destination_addresses = jsonResponse.getJSONArray("destination_addresses").getString(0);
                String distance = jsonResponse.getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0)
                        .getJSONObject("distance")
                        .getString("text");
                Log.w("ระยะทาง", distance);
                listener.onDistanceReceived(distance, destination_addresses);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    });
}
    public String getCurrentLocation() {
        current_location = "";
        try {
            URL url = new URL("https://www.googleapis.com/geolocation/v1/geolocate?key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }
            JSONObject jsonResponse = new JSONObject(outputString);
            Log.w("ตำแหน่งปัจจุบัน", outputString);
            JSONObject currentLocationJson = new JSONObject(jsonResponse.getString("location"));
            current_location = currentLocationJson.getString("lat") + "," + currentLocationJson.getString("lng");


            System.out.println(current_location); // this will print the raw JSON
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        //return currentLocation formatted as "latitude,longitude";
        return current_location;


    }
}
