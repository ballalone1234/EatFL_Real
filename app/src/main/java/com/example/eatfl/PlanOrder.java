package com.example.eatfl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanOrder extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanOrder newInstance(String param1, String param2) {
        PlanOrder fragment = new PlanOrder();
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
        return inflater.inflate(R.layout.fragment_plan_order, container, false);
    }
    ArrayList<String> recipe = new ArrayList<>();
    Double money_spent = 0.0;

    String money_all,day,type;

    Button saveBtn;
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String userId = mAuth.getCurrentUser().getUid();
        Bundle bundle = getArguments();
        if(bundle != null) {
            money_all = bundle.getString("money_all");
            day = bundle.getString("save_day");
            type = bundle.getString("type");
            TextView money_all_view = view.findViewById(R.id.money_all_value);
            TextView day_view = view.findViewById(R.id.duration_value);
            day_view.setText(day + " " + type);
            money_all_view.setText(money_all);
        }
        Gson gson = new Gson();
        db.collection("users").document(userId).collection("cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Plan_item> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Plan_item item = document.toObject(Plan_item.class);
                            Log.i("item", item.getRecipe());
                            recipe.add(item.getRecipe());
                            if(item.getEt().equals("g")){
                                money_spent += (item.getPrice()/1000)*item.getAmount();
                            }
                            else {
                                money_spent += item.getPrice()*item.getAmount();
                            }
                            items.add(item);
                        }
                        Log.i("recipe", recipe.toString());
                        TextView money_spent_view = view.findViewById(R.id.money_spent_value);
                        money_spent_view.setText(money_spent.toString());
                        TextView money_save_view = view.findViewById(R.id.money_save_value);
                        money_save_view.setText(String.valueOf(Double.parseDouble(money_all) - money_spent));
                        GridView gridView = view.findViewById(R.id.gridViewOrder);
                        GridAdapterOrder adapter = new GridAdapterOrder(getContext(), items);
                        gridView.setAdapter(adapter);
                        InlineModel inlineModel = new InlineModel("test", recipe);
                        String json = gson.toJson(inlineModel);
                        Log.i("json", json);
                        String urlString = "https://api.edamam.com/api/nutrition-details?app_id=ff0a1a66&app_key=b0e097ef32571b07bf395fecdef4f62a";
                        new SendPostRequestAsyncTask(this).execute(urlString, json);



                    } else {
                        Log.d("Error 47", "Error getting documents: ", task.getException());
                    }
                });


        saveBtn = view.findViewById(R.id.savePlanBTN);


        super.onViewCreated(view, savedInstanceState);
    }
    public void displayData(String data) throws JSONException {
        TextView calorie = getView().findViewById(R.id.cal_to_day_value);
        TextView protein = getView().findViewById(R.id.Pro_to_day_value);
        EditText name = getView().findViewById(R.id.editTextText);
        JSONObject jsonObject = new JSONObject(data);
        String calories = jsonObject.getString("calories");
        Double calories_to_day,protein_to_day;
        JSONObject protein_j = jsonObject.getJSONObject("totalNutrients").getJSONObject("PROCNT");
        String proteinValue = protein_j.getString("quantity");
        if(type.equals("วัน")){
            calories_to_day = Double.parseDouble(calories)/Double.parseDouble(day);
            protein_to_day = Double.parseDouble(proteinValue)/Double.parseDouble(day);
        }
        else if(type.equals("สัปดาห์")) {
            calories_to_day = Double.parseDouble(calories)/(Double.parseDouble(day)*7);
            protein_to_day = Double.parseDouble(proteinValue)/(Double.parseDouble(day)*7);
        }
        else {
            calories_to_day = Double.parseDouble(calories)/(Double.parseDouble(day)*30);
            protein_to_day = Double.parseDouble(proteinValue)/(Double.parseDouble(day)*30);
        }

        calorie.setText(calories_to_day.toString());
        protein.setText(protein_to_day.toString());
        saveBtn.setOnClickListener(v -> {
          addPlan(new PlanTopic(name.getText().toString(),
                  Timestamp.now(),
                  day,
                  Double.parseDouble(money_all),
                  money_spent,
                  Double.parseDouble(money_all) - money_spent,
                  mAuth.getCurrentUser().getUid(),
                  "private",
                  calories_to_day,
                  protein_to_day));
        });

    }
}