package com.app.connectcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EmergencyAidActivity extends AppCompatActivity {
    private EditText etTitle, etDescription, etLocation;
    private Button btnRequestAid;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_aid);

        // Make sure the IDs match the XML layout
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        btnRequestAid = findViewById(R.id.btnRequestAid);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnRequestAid.setOnClickListener(v -> postEmergencyAid());
    }

    private void postEmergencyAid() {
        String ngoId = auth.getCurrentUser().getUid();
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> aidRequest = new HashMap<>();
        aidRequest.put("ngo_id", ngoId);
        aidRequest.put("title", title);
        aidRequest.put("description", description);
        aidRequest.put("location", location);
        aidRequest.put("urgency", "High");

        db.collection("emergency_aid").add(aidRequest)
                .addOnSuccessListener(docRef -> Toast.makeText(this, "Emergency Aid Requested!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Request Failed!", Toast.LENGTH_SHORT).show());
    }
}
