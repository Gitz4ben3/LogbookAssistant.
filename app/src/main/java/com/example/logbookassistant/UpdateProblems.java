package com.example.logbookassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateProblems extends AppCompatActivity {
    private List<Update> updateList;
    private UpdateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_problems);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateList = new ArrayList<>();
        adapter = new UpdateAdapter(this, updateList);
        recyclerView.setAdapter(adapter);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        // Retrieve resolved issues from Firebase
        mRef.child("Reported Issues").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.child("userId").getValue(String.class);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String logUser = user.getUid();
                    if (userId.equals(logUser)){
                        String description = snapshot.child("description").getValue(String.class);
                        String reportedTime = snapshot.child("reportedTime").getValue(String.class);
                        String solvedTime = snapshot.child("solvedTime").getValue(String.class);

                        String userInfo = snapshot.child("userInfo").getValue(String.class);
                        String place = snapshot.child("myPlace").getValue(String.class);
                        String myId = snapshot.child("myId").getValue(String.class);
                        Update update = new Update(userId, description, reportedTime, solvedTime, userInfo, place, myId);
                        updateList.add(update);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No issues to update from logged in user", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("UpdateProblems", "Error fetching reported issues: " + databaseError.getMessage());
            }
        });
    }
}