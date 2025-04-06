package com.app.connectcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class NgoVolunteerListActivity extends AppCompatActivity {
    private ListView listViewVolunteers;
    private DatabaseReference dbRef;
    private List<String> volunteerList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_volunteer_list);

        listViewVolunteers = findViewById(R.id.listViewVolunteers);
        volunteerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, volunteerList);
        listViewVolunteers.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("volunteers");
        fetchVolunteers();
    }

    private void fetchVolunteers() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                volunteerList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String skills = childSnapshot.child("skills").getValue(String.class);
                    if (name != null && skills != null) {
                        volunteerList.add(name + " - " + skills);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NgoVolunteerListActivity.this, "Failed to load volunteers!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
