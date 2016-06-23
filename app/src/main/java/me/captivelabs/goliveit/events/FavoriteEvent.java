package me.captivelabs.goliveit.events;

import me.captivelabs.goliveit.models.Event;

/**
 * Created by Gilbert on 28/10/2015.
 */
public class FavoriteEvent {
    private Event event;

    public FavoriteEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
