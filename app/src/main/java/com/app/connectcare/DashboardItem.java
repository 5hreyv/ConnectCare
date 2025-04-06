package com.app.connectcare;

public class DashboardItem {
    private String title;
    private int icon;

    public DashboardItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
