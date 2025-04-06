package com.app.connectcare;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.util.Log;


public class NGOListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NGOAdapter adapter;
    private List<NGO> ngoList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NGOAdapter(ngoList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchNGOs();
    }
    private void addSampleNGOs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> ngo = new HashMap<>();
        ngo.put("name", "Helping Hands Foundation");
        ngo.put("category", "Education");
        ngo.put("location", "Mumbai, India");
        ngo.put("contact", "+91 98765 43210");
        ngo.put("latitude", 19.0760);
        ngo.put("longitude", 72.8777);

        db.collection("ngos").add(ngo)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "NGO added: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding NGO", e));
    }


    private void fetchNGOs() {
        db.collection("ngos").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        NGO ngo = doc.toObject(NGO.class);
                        ngoList.add(ngo);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(NGOListActivity.this, "Error fetching NGOs", Toast.LENGTH_LONG).show());
    }
}
