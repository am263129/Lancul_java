package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
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

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.ChatActivity;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Client> clients;
    boolean like = false;

    public ClientAdapter(Context context, ArrayList<Client> guiders) {
        this.context = context;
        this.clients = guiders;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Client client = clients.get(position);
        holder.setDetails(client);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuiderActivity.getInstance(), ChatActivity.class);
                GuiderActivity.getInstance().startActivity(intent);
                Toast.makeText(GuiderActivity.getInstance(),client.getName().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView name, last_message;
        private TextView num_message;
        private CardView new_message;
        private ImageView photo;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.client_name);
            last_message = itemView.findViewById(R.id.last_message);
            num_message = itemView.findViewById(R.id.num_new_message);
            new_message = itemView.findViewById(R.id.new_message);
            photo = itemView.findViewById(R.id.client_photo);
        }

        void setDetails(Client client) {

            name.setText(client.getName());
            Glide.with(GuiderActivity.getInstance()).load(client.getPhoto()).into(photo);
//            last_message.setText(client.getLastMessage());
//            if(Integer.parseInt(client.getNumNewMessage()) > 0){
//                new_message.setVisibility(View.VISIBLE);
//                num_message.setText(client.getNumNewMessage());
//            }
        }
    }
}
