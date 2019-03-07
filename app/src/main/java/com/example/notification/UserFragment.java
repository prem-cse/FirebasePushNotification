package com.example.notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Users> list;
    private RecyclerAdapter adapter;
    private FirebaseFirestore firestore;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new RecyclerAdapter(container.getContext(),list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // EVERY TIME THE FRAGMENT START IT AGAIN ADD USER TO LIST SO TO AVOID THEM WE HAVE NEW EMPTY LIST EVERYTIME
        list.clear();

        firestore.collection("Users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED) {

                        String userId = doc.getDocument().getId();
                        Users user = doc.getDocument().toObject(Users.class).withId(userId);
                        list.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
