package com.example.logbookassistant;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewIssuesAdapter extends RecyclerView.Adapter<ViewIssuesAdapter.ViewIssuesViewHolder>{
    FirebaseAuth auth;
    DatabaseReference ref, rRef;
    private Context context;
    private List<Issues> IssuesList;

    public ViewIssuesAdapter(Context context, List<Issues> IssuesList) {
        this.context = context;
        this.IssuesList = IssuesList;
    }

    @NonNull
    @Override
    public ViewIssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.issues_items, parent, false);
        return new ViewIssuesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewIssuesAdapter.ViewIssuesViewHolder holder, int position) {
        Issues issue = IssuesList.get(position);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        ref2.child("Users").child(issue.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String sup = snapshot.child("supervisor").getValue(String.class);
                    holder.supervisor.setText("Supervised by: " + sup);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Set the title, description, posted time, and customer info
        holder.Title.setText("New issue detected");
        holder.description.setText(issue.getDescription());
        holder.postedTime.setText("Posted on: "+issue.getReportedTime());
        holder.customerInfo.setText("Reported by: "+issue.getUserInfo());
        holder.place.setText(issue.getMyPlace());
        // Handle click event
        holder.fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference("Reported Issues");
                rRef = FirebaseDatabase.getInstance().getReference();

                String solvedTime=getCurrentTimeStamp();

                String desc = issue.getDescription();
                String reported = issue.getReportedTime();
                String resId = issue.getUserId();
                String user = issue.getUserInfo();
                String issueId = issue.getMyId();

                //public Resolved(String userId, String description, String reportedTime, String solvedTime, String userInfo)
                Resolved resolved = new Resolved(resId, desc, reported, solvedTime, user);
                rRef.child("Resolved Issues").child(issueId).setValue(resolved).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ref.child(issueId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()){
                                        Toast.makeText(context, "Issues solved successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent("custom-event-name");

                                        // Add data to the Intent
                                        intent.putExtra("issueId", issueId);
                                        intent.putExtra("userId", resId); // Pass the user ID to MainActivity

                                        // Broadcast the Intent
                                        //Create user Feedback
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return IssuesList != null ? IssuesList.size() : 0;
    }
    public class ViewIssuesViewHolder extends RecyclerView.ViewHolder {
        TextView Title,description, postedTime, customerInfo, place, supervisor;
        LinearLayout myIssue;
        Button fixed;
        public ViewIssuesViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.Title);
            description = itemView.findViewById(R.id.description);
            postedTime = itemView.findViewById(R.id.postedTime);
            customerInfo = itemView.findViewById(R.id.customerInfo);
            myIssue = itemView.findViewById(R.id.myIssue);
            fixed = itemView.findViewById(R.id.fixed);
            place = itemView.findViewById(R.id.place);
            supervisor = itemView.findViewById(R.id.supervisor);
        }
    }
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    public void promptForFeedback(Context context) {

    }
}
