package com.example.primitivephysiolight;

public class Pattern {
    private String name;
    private int duration;
    private int powerOnTime;
    private int powerOffTime;

    public Pattern(String name, int duration, int powerOnTime, int powerOffTime) {
        this.name = name;
        this.duration = duration;
        this.powerOnTime = powerOnTime;
        this.powerOffTime = powerOffTime;
    }

    public Pattern() {
    }

    @Override
    public String toString(){
        return getName() + " " + getDuration() + " " + getPowerOnTime() + " " + getPowerOffTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPowerOnTime() {
        return powerOnTime;
    }

    public void setPowerOnTime(int powerOnTime) {
        this.powerOnTime = powerOnTime;
    }

    public int getPowerOffTime() {
        return powerOffTime;
    }

    public void setPowerOffTime(int powerOffTime) {
        this.powerOffTime = powerOffTime;
    }
}
