package com.app.connectcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsActivity extends AppCompatActivity {
    private ListView notificationList;
    private String[] notifications = {
            "Emergency request approved",
            "New donor registered",
            "Fundraising goal reached",
            "New volunteer joined your cause"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationList = findViewById(R.id.notification_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notifications);
        notificationList.setAdapter(adapter);
    }
}

