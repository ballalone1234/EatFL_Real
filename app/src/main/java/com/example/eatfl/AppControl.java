package com.example.eatfl;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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
    public void getDistance(OnDistanceReceivedListener listener , String placeId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String origin = getCurrentLocation();
                    String destination = placeId;
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
                    Log.i("ระยะทาง", outputString);
                    String distance = jsonResponse.getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0)
                            .getJSONObject("distance")
                            .getString("text");

                    Log.w("ระยะทาง", distance);

                    getPlaceName(new OnPlaceNameReceivedListener() {
                        @Override
                        public void onPlaceNameReceived(String placeName) {
                            listener.onDistanceReceived(distance, placeName);}
                    }, placeId);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void getPlaceName(OnPlaceNameReceivedListener listener , String des) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=" + des + "&key=" + apiKey;
                   Log.w("urlString",urlString);
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    String line, outputString = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        outputString += line;
                    }
                    JSONObject jsonResponse = new JSONObject(outputString);
                    Log.i("ตำแหน่ง", outputString);
                    String place_id = jsonResponse.getJSONArray("results")
                            .getJSONObject(0)
                            .getString("place_id");
                    getPlaceDetails(new OnPlaceDetailsReceivedListener() {
                        @Override
                        public void onPlaceDetailsReceived(String placeName) {
                            listener.onPlaceNameReceived(placeName);}
                    }, place_id);
//                    listener.onPlaceNameReceived(place_id);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public interface OnPlaceNameReceivedListener {
        void onPlaceNameReceived(String placeName);
    }
    public interface OnPlaceDetailsReceivedListener {
        void onPlaceDetailsReceived(String placeName);
    }

    public void getPlaceDetails(OnPlaceDetailsReceivedListener listener, String placeId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" + apiKey;
                    URL url = new URL(urlString);
                    Log.w("urlString2",urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    String line, outputString = "";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        outputString += line;
                    }
                    JSONObject jsonResponse = new JSONObject(outputString);
                    String placeName = jsonResponse.getJSONObject("result").getString("name");
                    listener.onPlaceDetailsReceived(placeName);
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

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


//    protected void addPlanToFireStore(String planName,String part , Double amount , String planPrice, String location) {
//        String userId = mAuth.getCurrentUser().getUid();
//        //add to collection plan in collection users in document user id
//        db.collection("plans").document(planName)
//                .set(new Plan(planName, part, amount, planPrice,location, userId, Timestamp.now()))
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(getContext(), "เพิ่มแผนสำเร็จ", Toast.LENGTH_SHORT).show();
//                    Log.i("addPlanToFireStore", "เพิ่มแผนสำเร็จ");
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getContext(), "เพิ่มแผนไม่สำเร็จ", Toast.LENGTH_SHORT).show();
//                    Log.i("addPlanToFireStore", "เพิ่มแผนไม่สำเร็จ");
//                });}


    protected void addItemToPlanToCart(Plan_item plan_item) {
        String userId = mAuth.getCurrentUser().getUid();
        //add to collection plan in collection users in document user id
        db.collection("users").document(userId).collection("cart").document()
                .set(plan_item)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "เพิ่มในแผนสำเร็จ", Toast.LENGTH_SHORT).show();
                    Log.i("addPlanToFireStore", "เพิ่มในแผนสำเร็จ");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "เพิ่มในแผนไม่สำเร็จ", Toast.LENGTH_SHORT).show();
                    Log.i("addPlanToFireStore", "เพิ่มในแผนไม่สำเร็จ");
                });}

    protected void getDataRecipeFormApi(String ingradient){
        try {
            String urlString = "https://api.edamam.com/api/nutrition-data?app_id=ff0a1a66&app_key=b0e097ef32571b07bf395fecdef4f62a&nutrition-type=cooking&ingr=" + ingradient ;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }
            JSONObject jsonResponse = new JSONObject(outputString);
            int calories = jsonResponse.getInt("calories");
            double protein = jsonResponse.getJSONObject("totalNutrients").getJSONObject("PROCNT").getDouble("quantity");
            System.out.println("Calories: " + calories);
            System.out.println("Protein: " + protein);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
