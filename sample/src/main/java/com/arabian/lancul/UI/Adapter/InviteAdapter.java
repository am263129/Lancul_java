package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.ChatActivity;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Invite;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Invite> invites;
    boolean like = false;

    public InviteAdapter(Context context, ArrayList<Invite> invites) {
        this.context = context;
        this.invites = invites;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Invite invite = invites.get(position);
        holder.setDetails(invite);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return invites.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView label_invite_sender;
        private TextView label_invite_date;
        private ImageView invite_icon;
        View mView;

        PlanetHolder(View itemView) {
            super(itemView);

            label_invite_sender = itemView.findViewById(R.id.label_invite_sender);
            label_invite_date = itemView.findViewById(R.id.label_invite_date);
            invite_icon = itemView.findViewById(R.id.invite_status);
            mView = itemView;
        }

        void setDetails(Invite invite) {
            label_invite_date.setText(invite.getInvite_date().toString());
            label_invite_sender.setText(invite.getInvite_sender().toString());

            if(invite.getInvite_status().equals("Accepted")){
                invite_icon.setBackgroundResource(R.drawable.ico_opended_invitation);
            }
            else if(invite.getInvite_status().equals("New")){
                invite_icon.setBackgroundResource(R.drawable.ico_new_invitatiaon);
            }
            else{
                invite_icon.setBackgroundResource(R.drawable.ico_checked_invatition);
            }
        }
    }
}
