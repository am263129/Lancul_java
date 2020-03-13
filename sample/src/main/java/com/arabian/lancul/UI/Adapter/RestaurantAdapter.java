package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> implements Filterable {


    ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    Context mcontext;
    ImageView heart;
    boolean like = false;
    public RestaurantAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> objects) {
        super(context, textViewResourceId, objects);
        restaurants = objects;
        mcontext = context;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_restaurant, null);

        ImageView restaurant_photo;
        Restaurant restaurant = restaurants.get(position);
        TextView name = v.findViewById(R.id.label_restaurant);
        TextView location = v.findViewById(R.id.location);
        RatingBar ratingBar = v.findViewById(R.id.ratingBar);
        restaurant_photo = v.findViewById(R.id.item_photo);
        heart = v.findViewById(R.id.btn_heart);
        name.setText(restaurant.getName());
        location.setText(restaurant.getLocation());
        ratingBar.setRating(restaurant.getRating());
        Picasso.with(MainActivity.getInstance()).load(restaurant.getPhoto()).placeholder(R.drawable.placeholder_restaurant).error(R.drawable.error_connection).resize(200,200).centerCrop().into(restaurant_photo);


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
        return v;

    }
}