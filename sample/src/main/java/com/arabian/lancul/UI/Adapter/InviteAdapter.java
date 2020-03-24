package com.arabian.lancul.UI.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Activity.LoginActivity;
import com.arabian.lancul.UI.Object.Invite;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.PlanetHolder> implements Filterable {

    private static final String TAG = "Invite Adapter";
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
    public void onBindViewHolder(@NonNull final PlanetHolder holder, final int position) {
        final Invite invite = invites.get(position);
        holder.setDetails(invite);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_accept_dialog(holder.mView.findViewById(R.id.invite_status),position);
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
            label_invite_sender.setText(invite.getInvite_sender_name().toString());
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

    private void show_accept_dialog(final View icon, final Integer position) {
        Invite invite = invites.get(position);
        final Dialog dialog= new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_accept_invite, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        GuiderActivity.getInstance().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.setContentView(dialog_view);
        Rect displayRectangle = new Rect();
        Window full_window = GuiderActivity.getInstance().getWindow();
        full_window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog_view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));

        ImageView sender_photo = dialog_view.findViewById(R.id.sender_photo);
        TextView sender_name = dialog_view.findViewById(R.id.sender_name);
        TextView invite_content = dialog_view.findViewById(R.id.invite_content);

        sender_name.setText(invite.getInvite_sender_name());
        invite_content.setText(invite.getInvite_content());

        for(int i = 0; i < Global.array_client.size(); i++){
            if(Global.array_client.get(i).getName().equals(invites.get(position).getInvite_sender_name())
            && !Global.array_client.get(i).getPhoto().equals(""))
            {
                Glide.with(GuiderActivity.getInstance()).load(Global.array_client.get(i).getPhoto()).into(sender_photo);
            }
        }

        Button accept = dialog_view.findViewById(R.id.btn_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_accepted(icon, position);
                dialog.dismiss();
            }
        });
        Button cancel = dialog_view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_checked(icon, position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void make_checked(View icon,Integer position){
        icon.setBackgroundResource(R.drawable.ico_checked_invatition);
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> status = new HashMap<>();
        status.put("invite_status","Checked");
        db.collection("guiders").document(Global.my_email).collection("invite").document(invites.get(position).getInvite_sender_email()).update(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });
    }
    private void make_accepted(View icon,Integer position){
        icon.setBackgroundResource(R.drawable.ico_opended_invitation);
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> status = new HashMap<>();
        status.put("invite_status","Accepted");
        db.collection("guiders").document(Global.my_email).collection("invite").document(invites.get(position).getInvite_sender_email()).update(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });
    }
}
