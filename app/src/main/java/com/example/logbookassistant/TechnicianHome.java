package com.example.logbookassistant;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
public class TechnicianHome extends AppCompatActivity {
    CardView newProblems, resolvedProblems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);

        newProblems = findViewById(R.id.viewProblems);
        resolvedProblems = findViewById(R.id.resolvedProblems);
        newProblems.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ViewIssues.class)));
        resolvedProblems.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ViewResolved.class)));
    }
}