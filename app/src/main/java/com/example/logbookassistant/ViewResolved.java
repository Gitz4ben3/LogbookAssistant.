package com.example.logbookassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ViewResolved extends AppCompatActivity {
    private ResolvedAdapter adapter;
    private List<Resolved> resolvedList;
    Button generateLogBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resolved);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resolvedList = new ArrayList<>();
        adapter = new ResolvedAdapter(this, resolvedList);
        recyclerView.setAdapter(adapter);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        // Retrieve resolved issues from Firebase
        mRef.child("Resolved Issues").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resolvedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("description").getValue(String.class);
                    String reportedTime = snapshot.child("reportedTime").getValue(String.class);
                    String solvedTime = snapshot.child("solvedTime").getValue(String.class);
                    String userId = snapshot.child("userId").getValue(String.class);
                    String userInfo = snapshot.child("userInfo").getValue(String.class);
                    Resolved resolved = new Resolved(userId, description, reportedTime, solvedTime, userInfo);
                    resolvedList.add(resolved);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("ViewResolved", "Error fetching resolved issues: " + databaseError.getMessage());
            }
        });

        generateLogBook = findViewById(R.id.exportLog);
        generateLogBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    private void generatePDF() {
        try {
            // Create a new PDF document
            Document document = new Document();
            // Get the internal storage directory for the app
            Context context = getApplicationContext();
            FileOutputStream fos = context.openFileOutput("resolved_issues.pdf", Context.MODE_PRIVATE);
            // Initialize PDF writer with the file output stream
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Reported Time                        SolvedTime                 Description"));
            document.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------"));
            // Write resolved issues data into the PDF
            for (Resolved resolved : resolvedList) {
                document.add(new Paragraph(resolved.getReportedTime()+"             "+resolved.getSolvedTime()+"     "+resolved.getDescription()));
                document.add(new Paragraph("-------------------------------------------------------------------------------------------------------------------------------"));
            }

            // Close the document
            document.close();

            // Open the generated PDF using an intent
            File file = new File(context.getFilesDir(), "resolved_issues.pdf");
            Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            // Notify user that PDF generation is successful
            Toast.makeText(getApplicationContext(), "Wait for PDF to open.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error generating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
