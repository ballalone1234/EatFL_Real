package com.example.eatfl.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatfl.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters

    FirebaseAuth mAuth;
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // สร้าง method Register สำหรับสมัครสมาชิก


    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("login", "createUserWithEmail:success");
                        Bundle bundle = new Bundle();
                        bundle.putString("my_key", "my_value");
                        NavHostFragment.findNavController(Login.this)
                                .navigate(R.id.action_login_to_home2, bundle);
                        //FirebaseUser user = mAuth.getCurrentUser();

                        //updateUI(user);
                    } else {

                        Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }


                    // ...
                });
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


        // hide action bar
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onResume() {
        hide_actionbar();
        super.onResume();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bottom_navbar_hide();
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.buttonLogin);
        Button button2 = view.findViewById(R.id.buttonReg);
        //get Text from edittext
        TextView email = view.findViewById(R.id.editTextUsername);
        TextView password = view.findViewById(R.id.editTextPassword);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(keepLogin()){
            Bundle bundle = new Bundle();
            NavHostFragment.findNavController(Login.this)
                    .navigate(R.id.action_login_to_home2, bundle);
        }

        // onclick listener for the textView
        button.setOnClickListener(v -> {
            this.login(email.getText().toString(), password.getText().toString());

        });

        button2.setOnClickListener(v -> {
            //ให้ไปที่ยังหน้า Register

            NavHostFragment.findNavController(Login.this)
                    .navigate(R.id.action_login_to_register);

        });


    }
    public boolean keepLogin() {
        if (mAuth.getCurrentUser() != null) {
            return true;
        } else {
          return false;
        }
    }

}