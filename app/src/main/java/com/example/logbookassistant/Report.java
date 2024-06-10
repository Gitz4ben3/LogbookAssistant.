package com.example.logbookassistant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report extends AppCompatActivity {
    FirebaseAuth auth;
    Spinner issueSpinner, departmentSpinner;
    DatabaseReference ref;
    String userId, reportDate, fixDate;
    Button submitIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        issueSpinner = findViewById(R.id.issueSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.issueSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issueSpinner.setAdapter(adapter);

        departmentSpinner = findViewById(R.id.departmentSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.departmentSpinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter2);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        userId = auth.getUid();
        submitIssue = findViewById(R.id.submitIssue);
        submitIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String name = snapshot.child("name").getValue(String.class);
                            String surname = snapshot.child("surname").getValue(String.class);
                            String userInfo = name + " " + surname;
                            reportDate = getCurrentTimeStamp();
                            //Log.d("UserInfo", "User info: " + userInfo);
                            if (reportDate != null) {
                                String issueId = ref.child("Reported Issues").push().getKey();
                                String myPlace = "Department: " + departmentSpinner.getSelectedItem();
                                String desc = (String) issueSpinner.getSelectedItem();
                                fixDate = null;
                                Issues newIssue = new Issues(userId, desc, reportDate, fixDate, userInfo, issueId, myPlace);
                                ref.child("Reported Issues").child(issueId).setValue(newIssue).addOnCompleteListener(task -> {
                                    Toast.makeText(getApplicationContext(), "Issue reported, please wait for feedback from the Technician", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(getApplicationContext(), "There's an issue with capturing today's date", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Log.d("UserInfo", "User data not found in database.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static String getCurrentTimeStamp () {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date()); // Find today's date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}