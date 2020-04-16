package com.arabian.lancul.UI.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Adapter.GuiderAdapter;
import com.arabian.lancul.UI.Adapter.Res_ExpAdapter;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Util.Global;

import java.util.ArrayList;
import java.util.List;


public class LivechatFragment extends Fragment {

    View view;
    static RecyclerView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_livechat, container, false);
        list = view.findViewById(R.id.list_guider);
        GuiderAdapter adapter  = new GuiderAdapter(getContext(), Global.array_guider);
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));
        list.setAdapter(adapter);


        return view;
    }

    public static void filter_guiders(Activity context, List<String> filter_list){
        ArrayList<Guider> filtered_guiders =  new ArrayList<>();
        if(filter_list.size() == 0){
            filtered_guiders = Global.array_guider;
        }
        else {
            for (int i = 0; i < Global.array_guider.size(); i++) {
                for (int j = 0; j < filter_list.size(); j++) {
                    if (Global.array_guider.get(i).getLanguages().contains(filter_list.get(j)) && !filtered_guiders.contains(Global.array_guider.get(i))) {
                        filtered_guiders.add(Global.array_guider.get(i));
                    }
                }
            }
        }
        GuiderAdapter adapter  = new GuiderAdapter(context,filtered_guiders);
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));
        list.setAdapter(adapter);

    }

}
