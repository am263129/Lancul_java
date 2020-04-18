package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.ChatActivity;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Feedback;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Feedback> feedbacks;
    boolean like = false;

    public FeedbackAdapter(Context context, ArrayList<Feedback> feedbacks) {
        this.context = context;
        this.feedbacks = feedbacks;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Feedback feedback = feedbacks.get(position);
        holder.setDetails(feedback);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     }
        });

    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView name, feedback_content;
        private TextView leave_date;
        private ImageView photo;
        private RatingBar rate;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.client_name);
            feedback_content = itemView.findViewById(R.id.feedback_content);
            leave_date = itemView.findViewById(R.id.date);
            photo = itemView.findViewById(R.id.client_photo);
            rate = itemView.findViewById(R.id.rating);
        }

        void setDetails(Feedback feedback) {

            Client client = Global.get_client_from_email(feedback.getClient());
            if(client!=null) {
                name.setText(client.getName());
                if(client.getPhoto().equals("")){
                    photo.setImageResource(R.drawable.man_dummy);
                }
                else
                {
                    Glide.with(GuiderActivity.getInstance()).load(client.getPhoto()).into(photo);
                }
            }
            else
                name.setText(feedback.getClient());
            feedback_content.setText(feedback.getFeedback());
            leave_date.setText(feedback.getLeave_feed_date());
            rate.setRating(feedback.getRate());

        }
    }
}
