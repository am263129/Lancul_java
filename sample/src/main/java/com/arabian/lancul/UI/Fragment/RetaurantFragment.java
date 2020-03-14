package com.arabian.lancul.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.fragment.app.Fragment;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Adapter.RestaurantAdapter;
import com.arabian.lancul.UI.Object.Restaurant;

import java.util.ArrayList;

public class RetaurantFragment extends Fragment {

    View view;
    ListView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_restaurant, container, false);
        list = view.findViewById(R.id.list_restaurant);

        ArrayList<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("Homeros | وجبة لذيذة","Dubai","https://firebasestorage.googleapis.com/v0/b/lancul-v1.appspot.com/o/DJ7Vr2PW0AEyM42.jpg?alt=media&token=c755f0fb-515c-47e9-951c-2d45c1d79c85",(float)4.5));
        restaurants.add(new Restaurant("Lina Restaurant | مطعم لينا","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_2.jpg?alt=media&token=53a61099-fb6a-409a-9039-936e1d982549",(float)5));
        restaurants.add(new Restaurant("Launch of Region Commander | إطلاق قائد المنطقة","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_3.jpg?alt=media&token=42f827bc-3008-4167-86c1-33842b723e5a",(float)4));
        restaurants.add(new Restaurant("Huge Tiny |  ضخمة صغيرة","Dubai","https://firebasestorage.googleapis.com/v0/b/elsa-project-12a47.appspot.com/o/restaurant_4.jpg?alt=media&token=bad33dbf-3f56-4696-ab00-e6f6e8396c6b",(float)4.5));

        RestaurantAdapter adapter  = new RestaurantAdapter(getContext(),R.layout.item_restaurant, restaurants);

        list.setAdapter(adapter);
        return view;
    }

}
