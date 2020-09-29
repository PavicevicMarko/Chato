package com.example.chato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chato.Chat.Message;
import com.example.chato.Chat.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView nmessageList;
    private RecyclerView.Adapter<MessageAdapter.MessageViewHolder> nmessageListAdapter;
    private RecyclerView.LayoutManager nChatListLayoutManager;

    ArrayList<Message> messageList = new ArrayList<>();
    String chatID;
    DatabaseReference chatDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");
        chatDB =FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        Button mSend= findViewById(R.id.send);
        Button addMedia= findViewById(R.id.addMedia);
//        addMedia.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGallery();
//            }
//        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        initializeRecyclerView();

        getChatMessages();
    }


    private void getChatMessages() {
        chatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())  {
                    String text = "",
                             creatorID  ="";
                    if(snapshot.child("text").getValue() != null){
                        text = snapshot.child("text").getValue().toString();    //vrne sporočilo iz baze
                    }
                    if(snapshot.child("creator").getValue() != null){
                        creatorID = snapshot.child("creator").getValue().toString();    //vrne sporočilo iz baze
                    }

                    Message message = new Message(snapshot.getKey(), creatorID, text);  //sporočilo
                    messageList.add(message);
                    nChatListLayoutManager.scrollToPosition(messageList.size()-1);      // scrolla na zadnji element v chatu
                    nmessageListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void sendMessage(){
        EditText mMessage = findViewById(R.id.writeMessage);

        if(!mMessage.getText().toString().isEmpty()){
            DatabaseReference msgDB = chatDB.push();
            Map msgMap = new HashMap<>();
            msgMap.put("text", mMessage.getText().toString());
            msgMap.put("creator", FirebaseAuth.getInstance().getUid());

            msgDB.updateChildren(msgMap); //gor da v tabelo novo sporočilo
         }
        mMessage.setText(null);
    }
    private void openGallery() {

    }

    private void initializeRecyclerView() {

        nmessageList = findViewById(R.id.messageView);
        nmessageList.setNestedScrollingEnabled(false);
        nmessageList.setHasFixedSize(false);
        nChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        nmessageList.setLayoutManager(nChatListLayoutManager);
        nmessageListAdapter = new MessageAdapter(messageList);
        nmessageList.setAdapter(nmessageListAdapter);
    }
}