package me.captivelabs.goliveit.models;

import java.io.Serializable;

/**
 * Created by Aron on 10/24/2015.
 */
public class TicketInfo implements Serializable {
    private double minPrice;
    private double maxPrice;
    private double minListPrice;
    private double maxListPrice;
    private int totalTickets;
    private int totalPostings;
    private int popularity;
    private int currentCode;


    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinListPrice() {
        return minListPrice;
    }

    public void setMinListPrice(double minListPrice) {
        this.minListPrice = minListPrice;
    }

    public double getMaxListPrice() {
        return maxListPrice;
    }

    public void setMaxListPrice(double maxListPrice) {
        this.maxListPrice = maxListPrice;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTotalPostings() {
        return totalPostings;
    }

    public void setTotalPostings(int totalPostings) {
        this.totalPostings = totalPostings;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(int currentCode) {
        this.currentCode = currentCode;
    }
}
