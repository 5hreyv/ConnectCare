package com.app.connectcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class VolunteerActivity extends AppCompatActivity {
    private EditText etName, etSkills, etAvailability;
    private Button btnVolunteer;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        etName = findViewById(R.id.etName);
        etSkills = findViewById(R.id.etSkills);
        etAvailability = findViewById(R.id.etAvailability);
        btnVolunteer = findViewById(R.id.btnVolunteer);

        dbRef = FirebaseDatabase.getInstance().getReference("volunteers");
        auth = FirebaseAuth.getInstance();

        btnVolunteer.setOnClickListener(v -> registerVolunteer());
    }

    private void registerVolunteer() {
        String userId = auth.getCurrentUser().getUid();
        String name = etName.getText().toString().trim();
        String skills = etSkills.getText().toString().trim();
        String availability = etAvailability.getText().toString().trim();

        if (name.isEmpty() || skills.isEmpty() || availability.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> volunteer = new HashMap<>();
        volunteer.put("name", name);
        volunteer.put("skills", skills);
        volunteer.put("availability", availability);

        dbRef.child(userId).setValue(volunteer)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Volunteer Registered Successfully!", Toast.LENGTH_SHORT).show();
                    notifyNGOs(userId, name);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show());
    }

    private void notifyNGOs(String userId, String name) {
        DatabaseReference ngoUpdatesRef = FirebaseDatabase.getInstance().getReference("NGOUpdates");
        Map<String, Object> update = new HashMap<>();
        update.put("message", "New volunteer registered: " + name);
        update.put("volunteer_id", userId);

        ngoUpdatesRef.push().setValue(update);
    }
}
