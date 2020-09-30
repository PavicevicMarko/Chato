package com.example.chato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chato.Chat.MediaAdapter;
import com.example.chato.Chat.Message;
import com.example.chato.Chat.MessageAdapter;
import com.example.chato.User.Uporabnik;
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
    private RecyclerView nmessageList, mMedia;
    private RecyclerView.Adapter<MessageAdapter.MessageViewHolder> nmessageListAdapter;
    private MediaAdapter mMediaAdapter;
    private RecyclerView.LayoutManager nChatListLayoutManager, mMediaLayoutManager;

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
        addMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        initializeMessage();
        initializeMedia();

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
                        creatorID = snapshot.child("creator").getValue().toString();   //vrne pošitelja iz baze
                    }


                    Message message = new Message(snapshot.getKey(), creatorID, text);  //sporočilo
                    messageList.add(message);
                    nChatListLayoutManager.scrollToPosition(messageList.size()-1);      // scrolla na zadnji element v chatu
                    nmessageListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

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




    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();

    private void initializeMedia() {

        mMedia = findViewById(R.id.mediaList);
        mMedia.setNestedScrollingEnabled(false);
        mMedia.setHasFixedSize(false);
        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mMedia.setLayoutManager(mMediaLayoutManager);
        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);
        mMedia.setAdapter(mMediaAdapter);
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //več naenkrat
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Izberi sliko"), PICK_IMAGE_INTENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK){
        if(requestCode == PICK_IMAGE_INTENT){
            if(data.getClipData() == null){ //če je samo 1 slika
            mediaUriList.add(data.getData().toString());
            }
            else
            {
                for(int i = 0; i < data.getClipData().getItemCount();i++){  //več slik
                    mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                }
            }
            mMediaAdapter.notifyDataSetChanged();
        }
    }
    }

    private void initializeMessage() {

        nmessageList = findViewById(R.id.messageView);
        nmessageList.setNestedScrollingEnabled(false);
        nmessageList.setHasFixedSize(false);
        nChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        nmessageList.setLayoutManager(nChatListLayoutManager);
        nmessageListAdapter = new MessageAdapter(messageList);
        nmessageList.setAdapter(nmessageListAdapter);
    }
}