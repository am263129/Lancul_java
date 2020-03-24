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
import com.arabian.lancul.UI.Adapter.Res_ExpAdapter;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Util.Global;

import java.util.ArrayList;

public class RetaurantFragment extends Fragment {

    View view;
    RecyclerView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_restaurant, container, false);
        list = view.findViewById(R.id.list_restaurant);
        Res_ExpAdapter adapter  = new Res_ExpAdapter(getContext(), Global.array_restaurant);
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));

        list.setAdapter(adapter);
        return view;
    }

}
