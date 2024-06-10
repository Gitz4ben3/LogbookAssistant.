package com.example.logbookassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Create_Account extends AppCompatActivity {
    FirebaseAuth auth;
    private EditText name, surname, phone, email, pswd, c_pswd, persal, supervisor;
    private String firstname, lastname, contact, em, pass, c_pass, pn, sn;
    DatabaseReference dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        persal  = findViewById(R.id.persal);
        supervisor = findViewById(R.id.supervisor);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.Phone);
        email = findViewById(R.id.email);
        pswd = findViewById(R.id.password);
        c_pswd = findViewById(R.id.c_password);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = name.getText().toString().trim();
                lastname = surname.getText().toString().trim();
                contact = phone.getText().toString().trim();
                em = email.getText().toString().trim();
                pass = pswd.getText().toString().trim();
                c_pass = c_pswd.getText().toString().trim();
                sn = supervisor.getText().toString().trim();
                pn = persal.getText().toString().trim();
                if (firstname.isEmpty()){
                    name.requestFocus();
                    name.setError("Please input your first name");
                } else if (sn.isEmpty()) {
                    supervisor.requestFocus();
                    supervisor.setError("Please input your supervisor's name");
                } else if (pn.isEmpty()) {
                    persal.requestFocus();
                    persal.setError("Please input your persal");
                } else if (lastname.isEmpty()){
                    surname.requestFocus();
                    surname.setError("Please input your last name");
                }
                else if (contact.isEmpty()){
                    phone.requestFocus();
                    phone.setError("Please input your phone number");
                }
                else if (em.isEmpty()){
                    email.requestFocus();
                    email.setError("Please input your email address");
                }
                else if (pass.isEmpty()){
                    pswd.requestFocus();
                    pswd.setError("Please input your password");
                }
                else if (c_pass.isEmpty()){
                    c_pswd.requestFocus();
                    c_pswd.setError("Please confirm your password");
                }
                else if (!c_pass.equals(pass)){
                    c_pswd.requestFocus();
                    c_pswd.setError("Passwords do not match");
                }
                else{
                    auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dataref = FirebaseDatabase.getInstance().getReference();
                            String myKey = auth.getUid();
                            //(String name, String surname, String email, String phone, String officeNumber, String department)
                            Info info = new Info(firstname, lastname, em, contact, pn, sn);
                            dataref.child("Users").child(myKey).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }
}