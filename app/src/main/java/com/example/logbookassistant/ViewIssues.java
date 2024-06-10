package com.example.logbookassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewIssues extends AppCompatActivity {
    private ViewIssuesAdapter adapter;
    private List<Issues> issuesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_issues);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        issuesList = new ArrayList<>();
        adapter = new ViewIssuesAdapter(this, issuesList);
        recyclerView.setAdapter(adapter);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        // Retrieve issues from Firebase
        mRef.child("Reported Issues").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                issuesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Map Firebase fields to Issues object
                    String description = snapshot.child("description").getValue(String.class);
                    String reportedTime = snapshot.child("reportedTime").getValue(String.class);
                    String solvedTime = snapshot.child("solvedTime").getValue(String.class);
                    String userId = snapshot.child("userId").getValue(String.class);
                    String userInfo = snapshot.child("userInfo").getValue(String.class);
                    String myId = snapshot.getKey();
                    String myPlace = snapshot.child("myPlace").getValue(String.class);
                    Issues issue = new Issues(userId, description, reportedTime, solvedTime, userInfo, myId, myPlace);
                    issuesList.add(issue);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("ViewIssues", "Error fetching issues: " + databaseError.getMessage());
            }
        });
    }
}
