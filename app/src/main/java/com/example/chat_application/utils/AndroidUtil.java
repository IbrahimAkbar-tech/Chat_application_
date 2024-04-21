package com.example.chat_application.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.chat_application.model.UserModel;
import com.google.firebase.firestore.auth.User;

public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("userName", model.getUserName());
        intent.putExtra("phone", model.getPhone());
        intent.putExtra("userId", model.getUserId());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUserName(intent.getStringExtra("userName"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

}
