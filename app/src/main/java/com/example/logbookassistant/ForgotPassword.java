package com.example.logbookassistant;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText email;
    private String em;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.email);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {
            em = email.getText().toString().trim();
            if(em.isEmpty()){
                email.requestFocus();
                email.setError("Please input email");
            }else{
                auth.sendPasswordResetEmail(em).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Password recovery email sent", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Retry again later", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }
}