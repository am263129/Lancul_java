package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.ChatActivity;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Activity.InviteActivity;
import com.arabian.lancul.UI.Activity.InviteGuiderActivity;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GuiderAdapter extends RecyclerView.Adapter<GuiderAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Guider> guiders;
    boolean like = false;

    public GuiderAdapter(Context context, ArrayList<Guider> guiders) {
        this.context = context;
        this.guiders = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guider, parent, false);
        Log.e("check",guiders.get(i).getName());
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Guider guider = guiders.get(position);
        holder.setDetails(guider);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean already_linked = false;
                for (int i = 0; i< Global.my_user_data.getLinked_guiders().size(); i++){
                    if(Global.my_user_data.getLinked_guiders().get(i).equals(guider.getEmail())){
                        already_linked = true;
                    }
                }
                if (!already_linked) {
                    Intent intent = new Intent(MainActivity.getInstance(), InviteGuiderActivity.class);
                    Global.chat_guider_name = guider.getName();
                    intent.putExtra("Index", String.valueOf(position));
                    MainActivity.getInstance().startActivity(intent);
                }
                else{
                    Intent intent =  new Intent(MainActivity.getInstance(), ChatActivity.class);
                    intent.putExtra("partner_index", position);
                    MainActivity.getInstance().startActivity(intent);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return guiders.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView name, bio, text_languages;
        private TextView ratingBar;
        private ImageView guider_photo, verified;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            bio = itemView.findViewById(R.id.bio);
            ratingBar = itemView.findViewById(R.id.rating);
            guider_photo = itemView.findViewById(R.id.photo);
            verified = itemView.findViewById(R.id.veriried);
            text_languages = itemView.findViewById(R.id.languages);
            mView = itemView;

        }

        void setDetails(Guider guider) {
            name.setText(guider.getName());
            bio.setText(guider.getBio());
            ratingBar.setText(String.valueOf(guider.getRate().toString()));
            Glide.with(MainActivity.getInstance()).load(guider.getImageURL()).into(guider_photo);
            List<String> languages = guider.getLanguages();
            String language = "[ ";
            for (int i = 0; i < languages.size(); i++) {
                language = language + languages.get(i) + ", ";
            }
            language = language + "]";
            text_languages.setText(language);
            if (!guider.verified) {
                verified.setVisibility(View.GONE);
            }

        }
    }
}
