package com.example.chat_application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chat_application.model.UserModel;
import com.example.chat_application.utils.AndroidUtil;
import com.example.chat_application.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;


public class ProfileFragment extends Fragment {

    ImageView profile_pic;
    EditText usernameInput, phoneInput;
    Button updateProfileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;

    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imgPickLauncher;
    Uri selectedImgUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgPickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data != null && data.getData() != null){
                    selectedImgUri = data.getData();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_pic = view.findViewById(R.id.profile_pic_image_view);
        usernameInput = view.findViewById(R.id.profile_username);
        phoneInput = view.findViewById(R.id.profile_phone);
        updateProfileBtn = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.logout_btn);

        getUserData();

        updateProfileBtn.setOnClickListener(v -> {
            updateBtnClick();;
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }

    void updateBtnClick(){
       String newUserName = usernameInput.getText().toString();

        if(newUserName.isEmpty() || newUserName.length() < 3){
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }
        currentUserModel.setUserName(newUserName);
        setInProgress(true);
        updateToFirestore();
    }

    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                setInProgress(false);
                AndroidUtil.showToast(getContext(), "Updated Successfully");
            } else {
                AndroidUtil.showToast(getContext(), "Something went wrong, please try again");
            }
        });
    }

    void getUserData(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
        currentUserModel = task.getResult().toObject(UserModel.class);
        usernameInput.setText(currentUserModel.getUserName());
        phoneInput.setText(currentUserModel.getPhone());

        });
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }
}