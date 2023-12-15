package com.example.eatfl;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    FirebaseAuth mAuth;
    FirebaseUser userCurrent;
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
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
        ((MainActivity) getActivity()).getSupportActionBar().hide();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);

    }
    public static long convertDateStringToTimestamp(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if parsing fails
    }
    public void register(String email, String password ,  View view) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Register", "createUserWithEmail:success");
                        Bundle bundle = new Bundle();
                        userCurrent = mAuth.getCurrentUser();

                        EditText dob = view.findViewById(R.id.editTextDOB);
                        EditText userName = view.findViewById(R.id.editTextUsername);
                        EditText weight = view.findViewById(R.id.editWeight);
                        EditText height = view.findViewById(R.id.editHeight);
                        Integer weightStr = Integer.parseInt(weight.getText().toString());
                        Integer heightStr = Integer.parseInt(height.getText().toString());
                        String usernameStr = userName.getText().toString();
                        String dobStr = dob.getText().toString();
                        Spinner spinner = view.findViewById(R.id.spinnerExercise);
                        int exercise =spinner.getSelectedItemPosition();
                        String exerciseStr = spinner.getSelectedItem().toString();
                        Integer age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(dob.getText().toString().split("/")[2]);

                        double A =
                                sex.equals("Male") ? (10*weightStr) + (6.25*heightStr) + (5*age) + 5
                                        : (10*weightStr) + (6.25*heightStr)+ (5*age) - 161;
                        double BMR = exercise == 0 ? A * 1.2
                                : exercise == 1 ? A * 1.375
                                : exercise == 2 ? A * 1.55
                                : exercise == 3 ? A * 1.725
                                : A * 1.9;
                        // แปลงวันที่ในรูปแบบ String ให้เป็น Timestamp

                        long timestamp = convertDateStringToTimestamp(dobStr, "dd/MM/yyyy");
                        Timestamp timestampFirebase = new Timestamp(new Date(timestamp));
                        saveUserData(usernameStr , password , userCurrent.getEmail(),timestampFirebase ,sex , weightStr , heightStr ,BMR , exerciseStr , age);
                        bundle.putString("my_key", "my_value");
                        NavHostFragment.findNavController(Register.this)
                                .navigate(R.id.action_register_to_home2, bundle);


                    } else {

                        Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                    // ...
                });
    }
    private static final String TAG = "REG";
    public void saveUserData(String userId, String password, String email, Timestamp dob, String gender ,
                             Integer weightStr ,Integer heightStr , double BMR , String exerciseStr , Integer age) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // สร้าง Map เพื่อเก็บข้อมูลที่ต้องการบันทึก
        Map<String, Object> user = new HashMap<>();
        user.put("username", userId);
        user.put("password", password);
        user.put("email", email);
        user.put("weight", weightStr);
        user.put("height", heightStr);
        user.put("dob", dob);
        user.put("gender", gender);
        user.put("BMR", BMR);
        user.put("exercise", exerciseStr);
        user.put("age", age);


        // กำหนด Collection Reference และ Document Reference สำหรับข้อมูล
        CollectionReference usersCollectionRef = db.collection("users");

        // กำหนดชื่อ Document ที่ต้องการเพิ่มข้อมูลลงไป เป็นชื่อเดียวกับ User ID
        DocumentReference userDocRef = usersCollectionRef.document(userCurrent.getUid());

        userDocRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    // ทำตามที่คุณต้องการเมื่อบันทึกข้อมูลสำเร็จ
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    // ทำตามที่คุณต้องการในกรณีเกิดข้อผิดพลาด
                });
    }


    String sex = "Male";
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Spinner spinner =	view.findViewById(R.id.spinnerExercise);
//	Create	an	ArrayAdapter using	 the	string	array	and	a	default	spinner	 layout
        ArrayAdapter<CharSequence> adapter =	ArrayAdapter.createFromResource(requireContext(),
                R.array.type_array,	android.R.layout.simple_spinner_item);
//	Specify	 the	layout	to	use	when	the	list	of	choices	 appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//	Apply	 the	adapter	to	the	spinner
        spinner.setAdapter(adapter);
        ImageView imageViewDatePicker = view.findViewById(R.id.imageViewDatePicker);
        final EditText editTextDOB = view.findViewById(R.id.editTextDOB);
        mAuth = FirebaseAuth.getInstance();

        editTextDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, year1, month1, dayOfMonth) -> {
                        // ทำตามที่คุณต้องการเมื่อผู้ใช้เลือกวันที่
                        // ในที่นี้, เราจะตั้งค่าวันที่ที่เลือกใน EditText
                        editTextDOB.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1));
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);


// ตรวจสอบ RadioButton ที่ถูกเลือก

        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            // ตรวจสอบ RadioButton ที่ถูกเลือก
            if (checkedId == R.id.radioButtonMale) {
                // ทำอะไรก็ตามที่คุณต้องการเมื่อเลือก RadioButton ชาย
                sex = "Male";
            } else if (checkedId == R.id.radioButtonFemale) {
                // ทำอะไรก็ตามที่คุณต้องการเมื่อเลือก RadioButton หญิง
                sex = "Female";
            }


        });
        Button buttonReg = view.findViewById(R.id.buttonRegister);
        EditText email = view.findViewById(R.id.editTextEmail2);
        EditText password =  view.findViewById(R.id.editTextPassword2);

        buttonReg.setOnClickListener(v -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
        Log.i("Register", "onClick: " + emailStr + " " + passwordStr);
            register(emailStr , passwordStr, view);



        });
    }


}