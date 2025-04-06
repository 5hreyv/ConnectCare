package com.app.connectcare;

public class Event {
    private String name;
    private String description;
    private String date;
    private String location;

    // Empty constructor required for Firestore
    public Event() {}

    public Event(String name, String description, String date, String location) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
}
