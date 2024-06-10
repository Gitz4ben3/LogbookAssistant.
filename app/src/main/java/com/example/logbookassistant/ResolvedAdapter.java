package com.example.logbookassistant;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ResolvedAdapter extends RecyclerView.Adapter<ResolvedAdapter.ResolvedViewHolder> {
    private final Context context;
    private final List<Resolved> resolvedList;

    public ResolvedAdapter(Context context, List<Resolved> resolvedList) {
        this.context = context;
        this.resolvedList = resolvedList;
    }

    @NonNull
    @Override
    public ResolvedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resolved_items, parent, false);
        return new ResolvedViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResolvedViewHolder holder, int position) {
        Resolved resolved = resolvedList.get(position);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        ref2.child("Users").child(resolved.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        holder.description.setText(resolved.getDescription());
        holder.postedTime.setText("Posted on: " + resolved.getReportedTime());
        holder.customerInfo.setText("Posted by: " + resolved.getUserInfo());
        holder.resolvedTime.setText("Resolved on: " + resolved.getSolvedTime());
    }

    @Override
    public int getItemCount() {
        return resolvedList.size();
    }

    public static class ResolvedViewHolder extends RecyclerView.ViewHolder {
        TextView description, postedTime, customerInfo, resolvedTime, supervisor;

        public ResolvedViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            postedTime = itemView.findViewById(R.id.postedTime);
            resolvedTime = itemView.findViewById(R.id.resolvedTime);
            customerInfo = itemView.findViewById(R.id.customerInfo);
            supervisor = itemView.findViewById(R.id.supervisor);
        }
    }
}
