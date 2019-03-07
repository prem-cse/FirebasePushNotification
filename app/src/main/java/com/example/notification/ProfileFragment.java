package com.example.notification;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button logOut;
    private FirebaseAuth proAuth;
    private CircleImageView circleImageView;
    private TextView name;
    private FirebaseFirestore firestore;
    private String userId;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logOut = view.findViewById(R.id.logout);
        proAuth = FirebaseAuth.getInstance();
        circleImageView = view.findViewById(R.id.dp);
        name = view.findViewById(R.id.username);
        firestore = FirebaseFirestore.getInstance();
        userId = proAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Name = documentSnapshot.getString("name");
                String Image = documentSnapshot.getString("image");
                name.setText(Name);
                RequestOptions placeholders = new RequestOptions();
                placeholders.placeholder(R.drawable.default_dp);
                Glide.with(container.getContext()).setDefaultRequestOptions(placeholders).load(Image).into(circleImageView);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proAuth.signOut();
                startActivity(new Intent(container.getContext(),LoginActivity.class));

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
