package com.example.notesplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.notesplus.Utility;
import com.example.notesplus.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginAcctBtn.setOnClickListener((v) -> loginUser());
        binding.createAcctText.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }
    private void loginUser() {

        String email = binding.emailEt.getText().toString();
        String pwd = binding.pwdEt.getText().toString();
        if (checkValidity(email, pwd)) {
            loginAccountInFirebase(email, pwd);
        }
    }
    private void loginAccountInFirebase(String email, String pwd) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful()) { //login is successful means email and pwd is correct
                // then check if current user is email verified or not because only after email get verifed we can make the user login
                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                    //go to notes main activity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Utility.showToast(LoginActivity.this, "Email not verified, please verify your email");
                }
            } else { //login is failed might be due to  wrong email/pwd
                Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
            }
        });
    }
    boolean checkValidity(String email, String pwd) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.setError("Email is Invalid");
            return false;
        }
        if (pwd.length() < 6) {
            binding.pwdEt.setError("Password length should be greater than 5");
            return false;
        }
        return true;
    }
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginAcctBtn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginAcctBtn.setVisibility(View.VISIBLE);
        }
    }
}