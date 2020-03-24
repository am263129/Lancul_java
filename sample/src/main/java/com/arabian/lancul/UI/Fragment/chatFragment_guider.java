package com.arabian.lancul.UI.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Adapter.ClientAdapter;
import com.arabian.lancul.UI.Adapter.GuiderAdapter;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class chatFragment_guider extends Fragment {

    private static final String TAG = "ChatFragment";
    View view;
    RecyclerView list;

    FirebaseFirestore db;
    ArrayList<Client> my_clients;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.chatfragment_guider, container, false);
        list = view.findViewById(R.id.list_users);
        init_clients();

        return view;
    }

    private void init_clients() {
        my_clients = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("guiders").document(Global.my_email).collection("invite")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                for(int i = 0; i < Global.array_client.size(); i ++){
                                    if(Global.array_client.get(i).getEmail().toString().equals(document.getId())){
                                        my_clients.add(Global.array_client.get(i));
                                    }
                                }
                            }
                            ClientAdapter adapter  = new ClientAdapter(getContext(),my_clients);
                            list.setLayoutManager(new LinearLayoutManager(GuiderActivity.getInstance()));
                            list.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
