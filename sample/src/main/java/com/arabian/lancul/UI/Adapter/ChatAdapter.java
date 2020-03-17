package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.ChatActivity;
import com.arabian.lancul.UI.Object.Chat;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.PlanetHolder> implements Filterable {

    private Context context;
    private ArrayList<Chat> chat_history;

    public ChatAdapter(Context context, ArrayList<Chat> chat_history) {
        this.context = context;
        this.chat_history = chat_history;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_private_chat, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Chat chat = chat_history.get(position);
        holder.setDetails(chat);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getInstance(), ChatActivity.class);
                intent.putExtra("guider_index",position);
                MainActivity.getInstance().startActivity(intent);
                Toast.makeText(MainActivity.getInstance(),chat.getChat_sender().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return chat_history.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public void notifyData(ArrayList<Chat> chat_history) {
        Log.d("notifyData ", chat_history.size() + "");
        this.chat_history = chat_history;
        notifyDataSetChanged();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private ImageView guider_photo;
        private TextView msg_guider, msg_mine;
        private LinearLayout guider_chat, my_chat;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            guider_photo = itemView.findViewById(R.id.guider_photo);
            msg_guider = itemView.findViewById(R.id.message_guider);
            msg_mine = itemView.findViewById(R.id.message_mine);
            guider_chat = itemView.findViewById(R.id.item_guider);
            my_chat = itemView.findViewById(R.id.item_mychat);
            mView = itemView;

        }

        void setDetails(Chat chat) {
            if (chat.getChat_sender().equals(Global.my_name)){
                my_chat.setVisibility(View.VISIBLE);
                guider_chat.setVisibility(View.GONE);
                msg_mine.setText(chat.getChat_content());
            }
            else
            {
                my_chat.setVisibility(View.GONE);
                guider_chat.setVisibility(View.VISIBLE);
                msg_guider.setText(chat.getChat_content());
            }

        }
    }
}
