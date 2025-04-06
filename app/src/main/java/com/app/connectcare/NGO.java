package com.app.connectcare;
public class NGO {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public NGO() {}

    // Constructor for NGOs with name & address
    public NGO(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // New constructor for NGOs with coordinates
    public NGO(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
