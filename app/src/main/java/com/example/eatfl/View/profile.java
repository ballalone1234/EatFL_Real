package com.example.eatfl.View;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eatfl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends AppControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    TextView menu_logout , menu_like;
    ImageView profileImage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get current user
        TextView user = view.findViewById(R.id.profile_name);
        //get user name from firebase firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        user.setText(documentSnapshot.getString("username"));
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error reading document", e));

        TextView email = view.findViewById(R.id.profile_email);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        menu_logout = view.findViewById(R.id.menu_logout);
        menu_like = view.findViewById(R.id.menu_my_likes);
        menu_logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            //go to login
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Login()).commit();

        });
        menu_like.setOnClickListener(v -> {
            //add bundle to send filter
            Bundle bundle = new Bundle();
            bundle.putString("filter","like");

            NavHostFragment.findNavController(this).navigate(R.id.action_profile_to_public_plan,bundle);
        });
        ImageView cameraButton = view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(v -> {
            // Handle camera button click
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        profileImage = view.findViewById(R.id.profile_image);
        //get image from firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the image using Glide
            Glide.with(this)
                    .load(uri)
                    .into(profileImage);
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                saveImageToFirebaseStorage(imageUri); // Save the image to Firebase Storage
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToFirebaseStorage(Uri imageUri) {
        // Get Firebase Storage instance
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");

        // Upload file
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful upload
                Log.d("FirebaseStorage", "Upload successful!");

                // Get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Get Firebase Firestore instance
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Create a new user with a first and last name
                        Map<String, Object> user = new HashMap<>();
                        user.put("imageUrl", uri.toString());

                        // Add a new document with a generated ID
                        db.collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Firestore", "Error writing document", e);
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful upload
                Log.d("FirebaseStorage", "Upload failed!");
            }
        });
    }
}