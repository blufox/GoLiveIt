package me.captivelabs.goliveit.models;

import java.util.ArrayList;

/**
 * Created by Gilbert on 30/10/2015.
 */
public class AutoSuggestSearchResult
{
    private ArrayList<Performer> performers;
    private ArrayList<Event> events;
    private ArrayList<Venue> venues;
    private boolean performerExpansion;
    private boolean eventExpansion;
    private boolean venueExpansion;
    private int totalPerformersFound;
    private int totalVenuesFound;
    private int totalEventsFound;

    public ArrayList<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(ArrayList<Performer> performers) {
        this.performers = performers;
    }

    public boolean isPerformerExpansion() {
        return performerExpansion;
    }

    public void setPerformerExpansion(boolean performerExpansion) {
        this.performerExpansion = performerExpansion;
    }

    public boolean isEventExpansion() {
        return eventExpansion;
    }

    public void setEventExpansion(boolean eventExpansion) {
        this.eventExpansion = eventExpansion;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public int getTotalVenuesFound() {
        return totalVenuesFound;
    }

    public void setTotalVenuesFound(int totalVenuesFound) {
        this.totalVenuesFound = totalVenuesFound;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public void setVenues(ArrayList<Venue> venues) {
        this.venues = venues;
    }

    public int getTotalPerformersFound() {
        return totalPerformersFound;
    }

    public void setTotalPerformersFound(int totalPerformersFound) {
        this.totalPerformersFound = totalPerformersFound;
    }

    public boolean isVenueExpansion() {
        return venueExpansion;
    }

    public void setVenueExpansion(boolean venueExpansion) {
        this.venueExpansion = venueExpansion;
    }

    public int getTotalEventsFound() {
        return totalEventsFound;
    }

    public void setTotalEventsFound(int totalEventsFound) {
        this.totalEventsFound = totalEventsFound;
    }
}