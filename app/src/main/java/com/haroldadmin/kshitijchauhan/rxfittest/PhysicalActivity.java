package com.haroldadmin.kshitijchauhan.rxfittest;

import android.graphics.drawable.Drawable;

public class PhysicalActivity {

    private String startTime;
    private String endTime;
    private String name;
    private Drawable icon;

    public PhysicalActivity(String startTime, String endTime, String name, Drawable icon) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.icon = icon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "PhysicalActivity{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                '}';
    }
}
