package com.example.chato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chato.Chat.ChatListAdapter;
import com.example.chato.Chat.Pogovor;
import com.example.chato.User.SeznamUpoAdapter;
import com.example.chato.User.Uporabnik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView nChats;
    private RecyclerView.Adapter nChatAdapter;
    private RecyclerView.LayoutManager nChatListLayoutManager;

    ArrayList<Pogovor> chats = new ArrayList<Pogovor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button odjavi = findViewById(R.id.odjavi);
        Button najdiOsebo = findViewById(R.id.najdiOsebo);


        najdiOsebo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), NajdiOsebeActivity.class));
            }
        });


        odjavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent gatekeep = new Intent(getApplicationContext(), LoginActivity.class);
                gatekeep.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(gatekeep);
                finish();
                return;
            }
        });

        getPermissions();
        initializeRecyclerView();
        getUserChatList();
    }

    private void getUserChatList(){
        DatabaseReference userChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        userChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                for (DataSnapshot childSnapshot : snapshot.getChildren()){    //loop skozi vsaki ID v chat
                    Pogovor chat = new Pogovor(childSnapshot.getKey());
                    for(Pogovor iterator : chats){
                        boolean exists = false;
                        if(iterator.getChatID().equals(chat.getChatID())){
                            exists = true;
                        }
                        if (exists){
                            continue;
                        }

                    }
                    chats.add(chat);
                    nChatAdapter.notifyDataSetChanged();
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeRecyclerView() {

        nChats = findViewById(R.id.chatList);
        nChats.setNestedScrollingEnabled(false);
        nChats.setHasFixedSize(false);
        nChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        nChats.setLayoutManager(nChatListLayoutManager);
        nChatAdapter = new ChatListAdapter(chats);
        nChats.setAdapter(nChatAdapter);
    }

    private void getPermissions() {
        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);
    }
}