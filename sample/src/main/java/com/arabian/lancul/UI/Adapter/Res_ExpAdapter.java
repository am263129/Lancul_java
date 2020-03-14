package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Res_ExpAdapter extends RecyclerView.Adapter<Res_ExpAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Res_Exp> resExps;
    boolean like = false;

    public Res_ExpAdapter(Context context, ArrayList<Res_Exp> resExps) {
        this.context = context;
        this.resExps = resExps;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        Res_Exp resExp = resExps.get(position);
        holder.setDetails(resExp);
    }

    @Override
    public int getItemCount() {
        return resExps.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView name, location;
        private RatingBar ratingBar;
        private ImageView restaurant_photo, heart;
        private CardView card;


        PlanetHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.label_restaurant);
            location = itemView.findViewById(R.id.location);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            restaurant_photo = itemView.findViewById(R.id.item_photo);
            heart = itemView.findViewById(R.id.btn_heart);
            card = itemView.findViewById(R.id.card_view);
        }

        void setDetails(Res_Exp resExp) {
            name.setText(resExp.getName());
            location.setText(resExp.getLocation());
            ratingBar.setRating(resExp.getRating());

            Glide.with(MainActivity.getInstance()).load(resExp.getPhoto()).into(restaurant_photo);

            card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));
            card.setMaxCardElevation((float) 0.0);
            card.setRadius((float) 50);

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like = !like;
                    if (like)
                        heart.setBackgroundResource(R.drawable.full_heart);
                    else
                        heart.setBackgroundResource(R.drawable.empty_heart);
                }
            });
            }
    }



//    ArrayList<Res_Exp> resExps = new ArrayList<Res_Exp>();
//    Context mcontext;
//    ImageView heart;
//    boolean like = false;
//    public Res_ExpAdapter(Context context, int textViewResourceId, ArrayList<Res_Exp> objects) {
//        super(context, textViewResourceId, objects);
//        resExps = objects;
//        mcontext = context;
//    }
//    @Override
//    public int getCount() {
//        return super.getCount();
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        v = inflater.inflate(R.layout.item_restaurant, null);
//
//        ImageView restaurant_photo;
//        Res_Exp restaurant = resExps.get(position);
//        TextView name = v.findViewById(R.id.label_restaurant);
//        TextView location = v.findViewById(R.id.location);
//        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
//        restaurant_photo = v.findViewById(R.id.item_photo);
//        heart = v.findViewById(R.id.btn_heart);
//        name.setText(restaurant.getName());
//        location.setText(restaurant.getLocation());
//        ratingBar.setRating(restaurant.getRating());
//
//        Glide.with(MainActivity.getInstance()).load(restaurant.getPhoto()).apply(new RequestOptions().override(200, 400)).into(restaurant_photo);
//        CardView card = v.findViewById(R.id.card_view);
//        card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));
//        card.setMaxCardElevation((float) 0.0);
//        card.setRadius((float) 50);
//
//        heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                like = !like;
//                if (like)
//                    heart.setBackgroundResource(R.drawable.full_heart);
//                else
//                    heart.setBackgroundResource(R.drawable.empty_heart);
//            }
//        });
//        return v;
//
//    }
}