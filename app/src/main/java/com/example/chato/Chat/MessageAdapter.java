package com.example.chato.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chato.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<com.example.chato.Chat.MessageAdapter.MessageViewHolder> {


    ArrayList<Message> messages;
    public MessageAdapter(ArrayList<Message> Messagei){
        this.messages = Messagei;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        MessageAdapter.MessageViewHolder xyz = new MessageViewHolder(layoutView);
        return xyz;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, final int position) {
        holder.nMessage.setText(messages.get(position).getText());
        holder.nSender.setText(messages.get(position).getSenderID());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView nMessage,
                nSender;
        LinearLayout mLayout;
        public MessageViewHolder(View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);
            nSender = view.findViewById(R.id.sender);
            nMessage = view.findViewById(R.id.message);
        }
    }
}
