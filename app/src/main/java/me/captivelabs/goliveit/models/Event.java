package me.captivelabs.goliveit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aron on 10/23/2015.
 */
public class Event implements Serializable {
    private int id;
    private String name;
    private String eventDateLocal;
    private String eventDateUTC;
    private Venue venue;
    private TicketInfo ticketInfo;
    private double distance;
    ArrayList<Image> images;
    ArrayList<Category> categories;
    private String imageUrl;
    private boolean is_favorite = false;
    private ArrayList<Performer> performers = new ArrayList<>();
    private TicketUrls ticketUrls;

    public TicketUrls getTicketUrls() {
        return ticketUrls;
    }

    public void setTicketUrls(TicketUrls ticketUrls) {
        this.ticketUrls = ticketUrls;
    }

    public ArrayList<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(ArrayList<Performer> performers) {
        this.performers = performers;
    }

    public Event() {
    }

    public String getEventDateUTC() {
        return eventDateUTC;
    }

    public void setEventDateUTC(String eventDateUTC) {
        this.eventDateUTC = eventDateUTC;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventDateLocal() {
        return eventDateLocal;
    }

    public void setEventDateLocal(String eventDateLocal) {
        this.eventDateLocal = eventDateLocal;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public TicketInfo getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(TicketInfo ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

}
