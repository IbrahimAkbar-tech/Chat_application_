package com.example.chat_application.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_application.ChatActivity;
import com.example.chat_application.R;
import com.example.chat_application.model.ChatRoomModel;
import com.example.chat_application.model.UserModel;
import com.example.chat_application.utils.AndroidUtil;
import com.example.chat_application.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherFromChatRoom(model.getUserId()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());

                UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                holder.usernameText.setText(otherUserModel.getUserName());
                if(lastMessageSentByMe)
                    holder.lastMessageText.setText("You: " + model.getLastMessage());
                else
                    holder.lastMessageText.setText(model.getLastMessage());
                holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                holder.itemView.setOnClickListener(v -> {
                    // Navigate to chat
                    Intent intent = new Intent(context, ChatActivity.class);
                    AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            }
        });

    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatRoomModelViewHolder(view);
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText, lastMessageText, lastMessageTime;
        ImageView profilePic;

        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }


}
