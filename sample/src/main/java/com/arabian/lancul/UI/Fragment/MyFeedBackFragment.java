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
import com.arabian.lancul.UI.Adapter.FeedbackAdapter;
import com.arabian.lancul.UI.Adapter.InviteAdapter;
import com.arabian.lancul.UI.Adapter.Res_ExpAdapter;
import com.arabian.lancul.UI.Object.Feedback;
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


public class MyFeedBackFragment extends Fragment {

    private static final String TAG = "InviteFragment";
    View view;
    RecyclerView feedback_list;

    ArrayList<Feedback> my_feedbacks;

    private FirebaseFirestore mDb;
    private ListenerRegistration mInviteEventListener;
    private Set<String> mInviteSenders = new HashSet<>();

    private FeedbackAdapter feedbackAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_myfeedback, container, false);

        feedback_list = view.findViewById(R.id.list_feedback);
        my_feedbacks = new ArrayList<>();
        init_feedbacks();
        get_feedbacks();

        return view;
    }

    private void init_feedbacks() {
        feedbackAdapter = new FeedbackAdapter(getContext(), my_feedbacks);
        feedback_list.setLayoutManager(new LinearLayoutManager(GuiderActivity.getInstance()));
        feedback_list.setAdapter(feedbackAdapter);
    }

    private void get_feedbacks(){
        mDb = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = mDb
                .collection("guiders")
                .document(Global.my_email)
                .collection("feedback");

        mInviteEventListener = messagesRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Listen failed.", e);
                            return;
                        }
                        my_feedbacks.clear();
                        if(queryDocumentSnapshots != null){
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Feedback feedback = doc.toObject(Feedback.class);
                                my_feedbacks.add(feedback);

                            }
                            feedbackAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

}
