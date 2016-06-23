package me.captivelabs.goliveit.models;

import java.io.Serializable;

/**
 * Created by Aron on 10/24/2015.
 */
public class DisplayAttribute implements Serializable {

    private String title;
    private boolean hiddenInd;
    private boolean venueParkingInd;
    private boolean nearbyHotelsInd;
    private boolean neverHideInd;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHiddenInd() {
        return hiddenInd;
    }

    public void setHiddenInd(boolean hiddenInd) {
        this.hiddenInd = hiddenInd;
    }

    public boolean isVenueParkingInd() {
        return venueParkingInd;
    }

    public void setVenueParkingInd(boolean venueParkingInd) {
        this.venueParkingInd = venueParkingInd;
    }

    public boolean isNearbyHotelsInd() {
        return nearbyHotelsInd;
    }

    public void setNearbyHotelsInd(boolean nearbyHotelsInd) {
        this.nearbyHotelsInd = nearbyHotelsInd;
    }

    public boolean isNeverHideInd() {
        return neverHideInd;
    }

    public void setNeverHideInd(boolean neverHideInd) {
        this.neverHideInd = neverHideInd;
    }
}
