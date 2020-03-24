package com.arabian.lancul.UI.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Adapter.GuiderAdapter;
import com.arabian.lancul.UI.Adapter.InviteAdapter;
import com.arabian.lancul.UI.Object.Invite;
import com.arabian.lancul.UI.Util.Global;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class InviteFragment extends Fragment {

    private static final String TAG = "InviteFragment";
    View view;
    RecyclerView invite_list;

    ArrayList<Invite> my_invites;

    private FirebaseFirestore mDb;
    private ListenerRegistration mInviteEventListener;
    private InviteAdapter mInviteAdapter;
    private Set<String> mInviteSenders = new HashSet<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_livechat, container, false);
        invite_list = view.findViewById(R.id.list_guider);
        my_invites = new ArrayList<>();
        init_inviteList();
        get_invite();
        return view;
    }
    private void init_inviteList(){
        mInviteAdapter = new InviteAdapter(GuiderActivity.getInstance(),my_invites);
        invite_list.setAdapter(mInviteAdapter);
        invite_list.setLayoutManager(new LinearLayoutManager(GuiderActivity.getInstance()));
    }

    private void get_invite(){
        mDb = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = mDb
                .collection("guiders")
                .document(Global.my_email)
                .collection("invite");

        mInviteEventListener = messagesRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Listen failed.", e);
                            return;
                        }
                        my_invites.clear();
                        if(queryDocumentSnapshots != null){
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Invite invite = doc.toObject(Invite.class);
                                my_invites.add(invite);

                            }
                            mInviteAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

}
