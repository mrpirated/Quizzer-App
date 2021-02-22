package com.example.quizzer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,password,cnfpassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email= findViewById(R.id.register_email);
        password=findViewById(R.id.register_password);
        cnfpassword=findViewById(R.id.register_password_cnf);
        Button register = findViewById(R.id.register);
        TextView loginLink = findViewById(R.id.login_link);
        progressBar = findViewById(R.id.progress_bar);
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_email = email.getText().toString().trim();
                String text_password = password.getText().toString().trim();
                String text_cnfpassword = cnfpassword.getText().toString().trim();
                if(TextUtils.isEmpty(text_email)||TextUtils.isEmpty(text_password)||TextUtils.isEmpty(text_cnfpassword)){
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(text_email))
                        email.setError("Email is Required.");
                    if(TextUtils.isEmpty(text_password))
                        password.setError("Password is Required.");
                    if(TextUtils.isEmpty(text_cnfpassword))
                        cnfpassword.setError("Confirm Password is Required.");
                }
                else if(!text_password.equals(text_cnfpassword)){
                    Toast.makeText(RegisterActivity.this, "Password And Confirm Password Don't Match", Toast.LENGTH_SHORT).show();
                }
                else if(text_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Passwords Should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    password.setError("Passwords Should be at least 6 characters long.");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(text_password, text_email);
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser(String text_password, String text_email) {
        auth.createUserWithEmailAndPassword(text_email, text_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}