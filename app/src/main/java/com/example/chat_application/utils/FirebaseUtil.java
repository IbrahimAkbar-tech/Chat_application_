package com.example.chat_application.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static Boolean isLoggedIn(){
        if(currentUserId() != null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatRoomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("chats");
    }

    public static CollectionReference allChatRoomReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherFromChatRoom(List<String> userId){
        if(userId.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userId.get(1));
        } else {
            return allUserCollectionReference().document(userId.get(0));
        }
    }

    public static String getChatRoomId(String userId1, String userId2){
        if(userId1.hashCode() < userId2.hashCode()){
            return userId1+"_"+userId2;
        } else {
            return userId2+"_"+userId1;
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
}
