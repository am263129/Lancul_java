package com.arabian.lancul.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Adapter.ClientAdapter;
import com.arabian.lancul.UI.Adapter.GuiderAdapter;
import com.arabian.lancul.UI.Util.Global;


public class chatFragment_guider extends Fragment {

    View view;
    RecyclerView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.chatfragment_guider, container, false);
        list = view.findViewById(R.id.list_users);
        ClientAdapter adapter  = new ClientAdapter(getContext(), Global.array_client);
        list.setLayoutManager(new LinearLayoutManager(GuiderActivity.getInstance()));
        list.setAdapter(adapter);
        return view;
    }

}
