package com.app.connectcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.AutoResolveHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonationActivity extends AppCompatActivity {

    private Spinner spinnerDonationType;
    private EditText etNgoId, etAmountOrItem, etDonationNote;
    private Button btnGooglePay;
    private PaymentsClient paymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_donate) {
                startActivity(new Intent(this, DonationActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_more) {
                startActivity(new Intent(this, MoreOptionsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_donate);
        initializeUI();
        setupSpinner();
        checkGooglePayAvailability();
    }

    private void initializeUI() {
        spinnerDonationType = findViewById(R.id.spinnerDonationType);
        etNgoId = findViewById(R.id.etNgoId);
        etAmountOrItem = findViewById(R.id.etAmountOrItem);
        etDonationNote = findViewById(R.id.etDonationNote);
        btnGooglePay = findViewById(R.id.btnGooglePay);

        paymentsClient = Wallet.getPaymentsClient(this, new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build());

        btnGooglePay.setOnClickListener(v -> startGooglePay());
    }

    private void setupSpinner() {
        List<String> donationTypes = new ArrayList<>();
        donationTypes.add("Select Donation Type");
        donationTypes.add("Money");
        donationTypes.add("Clothes");
        donationTypes.add("Food");
        donationTypes.add("Medical Supplies");
        donationTypes.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, donationTypes);
        spinnerDonationType.setAdapter(adapter);
    }

    private void checkGooglePayAvailability() {
        Optional<JSONObject> isReadyToPayJson = GooglePayHelper.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            Log.d("GooglePay", "Google Pay check failed - Invalid JSON request.");
            btnGooglePay.setVisibility(View.GONE); // Hide button
            return;
        }

        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        paymentsClient.isReadyToPay(request).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult()) {
                btnGooglePay.setVisibility(View.VISIBLE);  // Show button if Google Pay is available
                Log.d("GooglePay", "Google Pay is supported.");
            } else {
                Log.d("GooglePay", "Google Pay is NOT supported on this device/emulator.");
                btnGooglePay.setVisibility(View.GONE);  // Hide button if not supported

                // Show Toast only ONCE when Google Pay is unavailable
                if (!isFinishing()) {
                    runOnUiThread(() -> {
                        Toast.makeText(DonationActivity.this,
                                "Google Pay is not available on this device.",
                                Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }



    private void startGooglePay() {
        String amount = etAmountOrItem.getText().toString().trim();
        if (amount.isEmpty()) {
            Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Optional<JSONObject> paymentDataRequestJson = GooglePayHelper.getPaymentDataRequest(amount);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentData paymentData = PaymentData.getFromIntent(data);
                if (paymentData != null) {
                    String paymentInfo = paymentData.toJson();
                    Log.d("GooglePay", "Payment successful: " + paymentInfo);
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("GooglePay", "Payment failed.");
                Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
