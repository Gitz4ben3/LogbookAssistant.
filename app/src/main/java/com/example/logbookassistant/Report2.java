package com.example.logbookassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report2 extends AppCompatActivity {
    Spinner departmentSpinner, issueSpinner;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        issueSpinner = findViewById(R.id.issueSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.issueSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issueSpinner.setAdapter(adapter);

        departmentSpinner = findViewById(R.id.departmentSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.departmentSpinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(adapter2);


        Intent intent = getIntent();
        String data = intent.getStringExtra("issueId");
        departmentSpinner =findViewById(R.id.departmentSpinner);
        Issues issues = new Issues();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Reported Issues").child(data.trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String desc = snapshot.child("description").getValue(String.class);
                    if (desc != null) {
                        String place = snapshot.child("myPlace").getValue(String.class);

                        String uId = snapshot.child("userId").getValue(String.class);
                        String rp = snapshot.child("reportedTime").getValue(String.class);
                        String st = snapshot.child("solvedTime").getValue(String.class);
                        String ui = snapshot.child("userInfo").getValue(String.class);

                        issues.setUserId(uId);
                        issues.setReportedTime(rp);
                        issues.setSolvedTime(st);
                        issues.setUserInfo(ui);
                        issues.setMyId(data);
                        issues.setMyPlace(place);


                        int position = -1; // Initialize the position to -1 (not found)
                        for (int i = 0; i < adapter.getCount(); i++) {
                            if (adapter.getItem(i).equals(desc)) {
                                position = i;
                                break;
                            }
                        }
                        if (position != -1) {
                            issueSpinner.setSelection(position);
                        }
                        int position2 = -1; // Initialize the position to -1 (not found)
                        for (int i = 0; i < adapter2.getCount(); i++) {
                            if (adapter2.getItem(i).equals(place)) {
                                position2 = i;
                                break;
                            }
                        }
                        if (position2 != -1) {
                            departmentSpinner.setSelection(position2);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Description is null", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Log.d("Snapshot","Snapshot does not exist");
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        update = findViewById(R.id.updateN);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issues.setDescription(issueSpinner.getSelectedItem().toString());
                issues.setMyPlace(departmentSpinner.getSelectedItem().toString());


                ref.child("Reported Issues").child(data).setValue(issues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Issue update successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}