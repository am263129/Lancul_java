package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Res_Exp;
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
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Guider guider = guiders.get(position);
        holder.setDetails(guider);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getInstance(), ChatActivity.class);
                intent.putExtra("guider_index",position);
                MainActivity.getInstance().startActivity(intent);
                Toast.makeText(MainActivity.getInstance(),guider.getName().toString(),Toast.LENGTH_SHORT).show();
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
