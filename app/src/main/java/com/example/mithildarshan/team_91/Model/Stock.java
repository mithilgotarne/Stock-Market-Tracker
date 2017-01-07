package com.example.mithildarshan.team_91.model;

import io.realm.RealmObject;

/**
 * Created by mithishri on 1/7/2017.
 */

public class Stock extends RealmObject {

    private float close;
    private float high;
    private float low;
    private float open;
    private int volume;
    private long timestamp;
    private boolean favourite;
    private float highPoint;
    private float lowPoint;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public float getHighPoint() {
        return highPoint;
    }

    public void setHighPoint(float highPoint) {
        this.highPoint = highPoint;
    }

    public float getLowPoint() {
        return lowPoint;
    }

    public void setLowPoint(float lowPoint) {
        this.lowPoint = lowPoint;
    }
}
