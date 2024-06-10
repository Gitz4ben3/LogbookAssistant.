package com.example.logbookassistant;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText persal, password;
    TextView createAccount, forgotPassword;
    String persalNo, pswd;
    FirebaseAuth auth;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        persal = findViewById(R.id.persal);
        password = findViewById(R.id.password);

        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Create_Account.class)));
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> {

            persalNo = persal.getText().toString().trim();
            pswd = password.getText().toString().trim();
            if (persalNo.isEmpty()){
                persal.requestFocus();
                persal.setError("Please input your persal");
            }
            else if(pswd.isEmpty()){
                password.requestFocus();
                password.setError("Please input your password");
            }
            else{
                String newEmail, newPassword;
                newEmail = persal.getText().toString().trim();
                newPassword = password.getText().toString().trim();
                if (newEmail.equalsIgnoreCase("555555") && newPassword.equals("654321")){
                    startActivity(new Intent(getApplicationContext(), TechnicianHome.class));
                }
                else{
                    auth = FirebaseAuth.getInstance();
                    Info info = new Info();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                boolean flag = false; // Initialize flag here
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    String pNum = dataSnapshot.child("persal").getValue(String.class);

                                    if(pNum.equals(persalNo)){
                                        String email = dataSnapshot.child("email").getValue(String.class);
                                        info.setEmail(email);
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) { // Check flag here
                                    String m = info.getEmail();
                                    Toast.makeText(getApplicationContext(), "Persal found", Toast.LENGTH_SHORT).show();
                                    auth.signInWithEmailAndPassword(m, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Persal not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPassword.class)));
    }
}