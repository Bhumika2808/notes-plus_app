package com.example.notesplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.example.notesplus.Utility;
import com.example.notesplus.databinding.ActivityCreateAccountBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createAcctBtn.setOnClickListener(v -> createAccount());
        binding.loginText.setOnClickListener(v -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));


    }

    void createAccount() {
        String email = binding.emailEt.getText().toString();
        String pwd = binding.pwdEt.getText().toString();
        String confirmPwd = binding.confirmPwdEt.getText().toString();

        boolean isValidCredentials = checkValidity(email, pwd, confirmPwd);

        if (isValidCredentials) {
            createAcctInFirebase(email, pwd);
        }
    }

    boolean checkValidity(String email, String pwd, String confirmPwd) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.setError("Email is Invalid");
            return false;
        }
        if (pwd.length() < 6) {
            binding.pwdEt.setError("Password length should be greater than 5");
            return false;
        }
        if (!pwd.equals(confirmPwd)) {
            binding.confirmPwdEt.setError("Password not matched");
            return false;
        }
        return true;
    }

    void createAcctInFirebase(String email, String pwd) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(CreateAccountActivity.this,
                task -> {
                    changeInProgress(false);
                    if (task.isSuccessful()) {
                        //means acc created successfully
                        Utility.showToast(CreateAccountActivity.this, "Successfully account created, open email to verify");
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    } else { //in failure
                        Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                    }
                }
        );

    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.createAcctBtn.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.createAcctBtn.setVisibility(View.VISIBLE);
        }
    }
}