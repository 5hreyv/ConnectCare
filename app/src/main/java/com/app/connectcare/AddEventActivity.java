package com.app.connectcare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {
    private EditText etEventName, etEventDescription, etEventDate;
    private Button btnSubmitEvent;
    private ProgressBar progressBar;
    private DatabaseReference eventDatabaseRef;
    private String ngoId = "ngo_123"; // Replace with dynamic NGO ID if available

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etEventName = findViewById(R.id.etEventName);
        etEventDescription = findViewById(R.id.etEventDescription);
        etEventDate = findViewById(R.id.etEventDate);
        btnSubmitEvent = findViewById(R.id.btnSubmitEvent);
        progressBar = findViewById(R.id.progressBar);

        // Firebase reference
        eventDatabaseRef = FirebaseDatabase.getInstance().getReference("NGOEvents").child(ngoId);

        btnSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent();
            }
        });
    }

    private void submitEvent() {
        String eventName = etEventName.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();
        String eventDate = etEventDate.getText().toString().trim();

        if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventDescription) || TextUtils.isEmpty(eventDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String eventId = eventDatabaseRef.push().getKey(); // Generate unique event ID

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", eventId);
        eventData.put("name", eventName);
        eventData.put("description", eventDescription);
        eventData.put("date", eventDate);
        eventData.put("timestamp", System.currentTimeMillis());

        if (eventId != null) {
            eventDatabaseRef.child(eventId).setValue(eventData)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEventActivity.this, "Event added!", Toast.LENGTH_SHORT).show();
                            finish(); // Close activity after success
                        } else {
                            Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
