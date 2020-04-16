package com.arabian.lancul.UI.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Object.Chat;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.arabian.lancul.UI.Util.Global.my_email;

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

        private ImageView partner_photo;
        private TextView msg_partner, msg_mine;
        private LinearLayout guider_chat, my_chat;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            partner_photo = itemView.findViewById(R.id.partner_photo);
            msg_partner = itemView.findViewById(R.id.message_partner);
            msg_mine = itemView.findViewById(R.id.message_mine);
            guider_chat = itemView.findViewById(R.id.item_guider);
            my_chat = itemView.findViewById(R.id.item_mychat);
            mView = itemView;

        }

        void setDetails(Chat chat) {

            if (chat.getChat_sender().equals(my_email)){
                my_chat.setVisibility(View.VISIBLE);
                guider_chat.setVisibility(View.GONE);
                msg_mine.setText(chat.getChat_content());
            }
            else
            {
                if(!Global.partner_photo.equals(""))
                {
                    Glide.with(context).load(Global.partner_photo).into(partner_photo);
                }
                else{
                    partner_photo.setImageResource(R.drawable.man_dummy);
                }
                my_chat.setVisibility(View.GONE);
                guider_chat.setVisibility(View.VISIBLE);
                msg_partner.setText(chat.getChat_content());
            }

        }
    }
}
