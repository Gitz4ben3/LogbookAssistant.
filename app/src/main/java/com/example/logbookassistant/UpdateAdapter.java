package com.example.logbookassistant;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.UpdateViewHolder>{
    private final Context context;
    private final List<Update> updateList;
    @NonNull
    @Override
    public UpdateAdapter.UpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.update_items, parent, false);
        return new UpdateAdapter.UpdateViewHolder(view);
    }
    public UpdateAdapter(Context context, List<Update> updateList) {
        this.context = context;
        this.updateList = updateList;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UpdateAdapter.UpdateViewHolder holder, int position) {
        // Don't bind anything if the list is empty
        if (updateList == null || updateList.isEmpty()) {
            return;
        }
        Update update = updateList.get(position);
        holder.description.setText(update.getDescription());
        holder.postedTime.setText("Posted on: " + update.getReportedTime());
        holder.customerInfo.setText("Posted by: " + update.getUserInfo());
        holder.place.setText(update.getPlace());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(update.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String sup = snapshot.child("supervisor").getValue(String.class);
                    holder.supervisor.setText("Supervised by: " + sup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Report2.class);
                intent.putExtra("issueId", update.getMyId());
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Reported Issues").child(update.getMyId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Issue or problem removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateList.size();
    }

    public class UpdateViewHolder extends RecyclerView.ViewHolder {
        TextView description, postedTime, customerInfo, place, supervisor;
        Button update, delete;
        public UpdateViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            postedTime = itemView.findViewById(R.id.postedTime);
            customerInfo = itemView.findViewById(R.id.customerInfo);
            place = itemView.findViewById(R.id.place);
            supervisor = itemView.findViewById(R.id.supervisor);
            update = itemView.findViewById(R.id.update);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
