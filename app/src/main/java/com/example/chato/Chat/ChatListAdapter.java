package com.example.chato.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chato.ChatActivity;
import com.example.chato.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<com.example.chato.Chat.ChatListAdapter.ChatListViewHolder> {



    ArrayList<Pogovor> Pogovori;
    public ChatListAdapter(ArrayList<Pogovor> Pogovori){
        this.Pogovori = Pogovori;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        ChatListAdapter.ChatListViewHolder xyz = new ChatListAdapter.ChatListViewHolder(layoutView);
        return xyz;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ChatListViewHolder holder, final int position) {
        holder.title.setText(Pogovori.get(position).getChatID());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatID", Pogovori.get(holder.getAdapterPosition()).getChatID());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Pogovori.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public LinearLayout mLayout;
        public ChatListViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            mLayout = view.findViewById(R.id.layout);
        }
    }
}
