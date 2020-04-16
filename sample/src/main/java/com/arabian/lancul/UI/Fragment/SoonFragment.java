package com.arabian.lancul.UI.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
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


public class SoonFragment extends Fragment {

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
        view =  inflater.inflate(R.layout.fragment_soon, container, false);

        return view;
    }

}
