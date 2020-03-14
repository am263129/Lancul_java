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


public class ExperienceFragment extends Fragment {

    View view;
    RecyclerView list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_experience, container, false);
        list = view.findViewById(R.id.list_experience);

        ArrayList<Res_Exp> resExps = new ArrayList<>();

        resExps.add(new Res_Exp("إطلاق قائد المنطقة وجبة لذيذة","Riyadh","https://saudievents.sa/Content/Events-Images/c0d82e82-7f81-49f1-b5b7-8b56ef2b780f/1.png",(float)4.5));
        resExps.add(new Res_Exp("Crown Prince Carmel Festival | مطعم لينا","Taif","https://saudiseasons.sa/Style%20Library/seasons3/img/seasons/taif/mainSlider/en/hijin-festival.jpg",(float)5));
        resExps.add(new Res_Exp("Bujairi Experience | إطلاق قائد المنطقة","Al-Diriyah","https://saudiseasons.sa/ar/Seasons/PublishingImages/AddEvents/20191124094656",(float)4));


        Res_ExpAdapter adapter  = new Res_ExpAdapter(getContext(), resExps);
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));
        list.setAdapter(adapter);
        return view;
    }

}
