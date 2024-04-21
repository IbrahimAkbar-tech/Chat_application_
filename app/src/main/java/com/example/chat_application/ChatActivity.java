package com.example.chat_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chat_application.R;
import com.example.chat_application.model.ChatRoomModel;
import com.example.chat_application.model.UserModel;
import com.example.chat_application.utils.AndroidUtil;
import com.example.chat_application.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    String chatRoomId;
    ChatRoomModel chatRoomModel;

    EditText messageInput;
    ImageButton sendMessageBtn, backBtn;
    TextView otherUserName;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // get userModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatRoomId = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.message_chat_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        backBtn = findViewById(R.id.back_btn);
        otherUserName = findViewById(R.id.other_user_name);
        recyclerView = findViewById(R.id.recycler_view_message);

        backBtn.setOnClickListener((v) ->{
            onBackPressed();
        });

        otherUserName.setText(otherUser.getUserName());
        getOrCreateChatRoomModel();
    }

    void getOrCreateChatRoomModel(){
        FirebaseUtil.getChatRoomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if(chatRoomModel == null){
                    // first time chat
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatRoomReference(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }
}