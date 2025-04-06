package com.app.connectcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NgoProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST_PROFILE = 1;
    private static final int PICK_IMAGE_REQUEST_NGO_PHOTOS = 2;

    private ImageView profileImageView, ngoPhotoContainer;
    private TextView tvNgoName, tvNgoEmail, tvNgoDetails, tvRequests, tvEvents;
    private Button btnUploadPhoto, btnUploadNgoPhotos;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize UI components
        profileImageView = findViewById(R.id.profileImage);
        ngoPhotoContainer = findViewById(R.id.ngoPhotoContainer);
        tvNgoName = findViewById(R.id.tvNgoName);
        tvNgoEmail = findViewById(R.id.tvNgoEmail);
        tvNgoDetails = findViewById(R.id.tvNgoDetails);
        tvRequests = findViewById(R.id.tvRequests);
        tvEvents = findViewById(R.id.tvEvents);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadNgoPhotos = findViewById(R.id.btnUploadNgoPhotos);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Fetch NGO details
        fetchNgoDetails();
        loadNGOHistory();

        // Set up photo upload buttons
        btnUploadPhoto.setOnClickListener(view -> openGallery(PICK_IMAGE_REQUEST_PROFILE));
        btnUploadNgoPhotos.setOnClickListener(view -> openGallery(PICK_IMAGE_REQUEST_NGO_PHOTOS));

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                if (requestCode == PICK_IMAGE_REQUEST_PROFILE) {
                    profileImageView.setImageURI(imageUri);
                    uploadImageToFirebase(imageUri, "profile_pictures");
                } else if (requestCode == PICK_IMAGE_REQUEST_NGO_PHOTOS) {
                    ngoPhotoContainer.setImageURI(imageUri);
                    uploadImageToFirebase(imageUri, "ngo_images");
                }
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri, String folder) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show();
            return;
        }

        StorageReference storageRef = storage.getReference(folder)
                .child(mAuth.getCurrentUser().getUid() + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            db.collection("NGOs").document(mAuth.getCurrentUser().getUid())
                                    .update(folder.equals("profile_pictures") ? "profileImageUrl" : "ngoImageUrl", downloadUrl)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Image uploaded!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image!", Toast.LENGTH_SHORT).show());
    }

    private void fetchNgoDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("NGOs").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            tvNgoName.setText(documentSnapshot.getString("name"));
                            tvNgoDetails.setText(documentSnapshot.getString("details"));
                            tvNgoEmail.setText(user.getEmail());

                            // Load profile image if available
                            String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                Glide.with(this)
                                        .load(profileImageUrl)
                                        .placeholder(R.drawable.ic_ngo)
                                        .into(profileImageView);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to load NGO details", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadNGOHistory() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("NGOHistory").whereEqualTo("ngoId", userId).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        StringBuilder historyText = new StringBuilder();
                        queryDocumentSnapshots.forEach(document ->
                                historyText.append(document.getString("event"))
                                        .append(" on ")
                                        .append(document.getString("date"))
                                        .append("\n")
                        );
                        tvEvents.setText(historyText.toString());
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to load NGO history", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    return true;
                } else if (id == R.id.nav_dashboard) {
                    startActivity(new Intent(NgoProfileActivity.this, NGODashboardActivity.class));
                    return true;
                } else if (id == R.id.nav_ngo_add_emergency_request) {
                    startActivity(new Intent(NgoProfileActivity.this, AddEmergencyRequestActivity.class));
                    return true;
                } else if (id == R.id.nav_ngo_notifications) {
                    startActivity(new Intent(NgoProfileActivity.this, NotificationsActivity.class));
                    return true;
                } else if (id == R.id.nav_ngo_settings) {
                    startActivity(new Intent(NgoProfileActivity.this, SettingsActivity.class));
                    return true;
                }
                return true;
            }
        });
    }
}
