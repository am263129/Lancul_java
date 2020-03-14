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

import java.util.ArrayList;

public class RetaurantFragment extends Fragment {

    View view;
    RecyclerView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_restaurant, container, false);
        list = view.findViewById(R.id.list_restaurant);

        ArrayList<Res_Exp> resExps = new ArrayList<>();

        resExps.add(new Res_Exp("Homeros | وجبة لذيذة","Dubai","https://firebasestorage.googleapis.com/v0/b/lancul-v1.appspot.com/o/DJ7Vr2PW0AEyM42.jpg?alt=media&token=c755f0fb-515c-47e9-951c-2d45c1d79c85",(float)4.5));
        resExps.add(new Res_Exp("Lina Res_Exp | مطعم لينا","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_2.jpg?alt=media&token=53a61099-fb6a-409a-9039-936e1d982549",(float)5));
        resExps.add(new Res_Exp("Launch of Region Commander | إطلاق قائد المنطقة","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_3.jpg?alt=media&token=42f827bc-3008-4167-86c1-33842b723e5a",(float)4));
        resExps.add(new Res_Exp("Huge Tiny |  ضخمة صغيرة","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_4.jpg?alt=media&token=bad33dbf-3f56-4696-ab00-e6f6e8396c6b",(float)4.5));

        Res_ExpAdapter adapter  = new Res_ExpAdapter(getContext(), resExps);
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));
        list.setAdapter(adapter);
        return view;
    }

}
