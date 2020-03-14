package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.arabian.lancul.UI.Object.Restaurant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Restaurant> restaurants;
    boolean like = false;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.setDetails(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
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

        void setDetails(Restaurant restaurant) {
            name.setText(restaurant.getName());
            location.setText(restaurant.getLocation());
            ratingBar.setRating(restaurant.getRating());

            Glide.with(MainActivity.getInstance()).load(restaurant.getPhoto()).apply(new RequestOptions().override(200, 400)).into(restaurant_photo);

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



//    ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
//    Context mcontext;
//    ImageView heart;
//    boolean like = false;
//    public RestaurantAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> objects) {
//        super(context, textViewResourceId, objects);
//        restaurants = objects;
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
//        Restaurant restaurant = restaurants.get(position);
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