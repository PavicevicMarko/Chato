package com.example.chato;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID = getIntent().getExtras().getString("chatID");

        Button mSend= findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        initializeRecyclerView();
    }

    private void sendMessage(){
        EditText mMessage = findViewById(R.id.message);

        if(!mMessage.getText().toString().isEmpty()){
            DatabaseReference msgDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).push();
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("text", mMessage.getText().toString());
            msgMap.put("creator", FirebaseAuth.getInstance().getUid());

            msgDB.updateChildren(msgMap); //gor da v tabelo novo sporočilo
         }
        mMessage.setText(null);
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