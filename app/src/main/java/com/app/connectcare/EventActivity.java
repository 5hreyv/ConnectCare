package com.app.connectcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {
    private EditText etEventName, etEventDate, etEventLocation, etEventDescription;
    private Button btnCreateEvent;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        etEventLocation = findViewById(R.id.etEventLocation);
        etEventDescription = findViewById(R.id.etEventDescription);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        recyclerView = findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        btnCreateEvent.setOnClickListener(v -> createEvent());

        fetchEvents();
    }

    private void createEvent() {
        String eventName = etEventName.getText().toString().trim();
        String eventDate = etEventDate.getText().toString().trim();
        String eventLocation = etEventLocation.getText().toString().trim();
        String eventDescription = etEventDescription.getText().toString().trim();
        String ngoId = auth.getCurrentUser().getUid();  // Assuming NGO is logged in

        if (eventName.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty() || eventDescription.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("ngo_id", ngoId);
        event.put("name", eventName);
        event.put("description", eventDescription);
        event.put("date", eventDate);
        event.put("location", eventLocation);
        event.put("registrations", new ArrayList<String>());

        db.collection("events").add(event)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Event Created!", Toast.LENGTH_SHORT).show();
                    fetchEvents();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show());
    }

    private void fetchEvents() {
        db.collection("events").get()
                .addOnSuccessListener(querySnapshot -> {
                    eventList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Event event = doc.toObject(Event.class);
                        eventList.add(event);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show());
    }
}
