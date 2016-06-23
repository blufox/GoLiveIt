package me.captivelabs.goliveit.events;

import me.captivelabs.goliveit.models.Artist;

/**
 * Created by Gilbert on 28/10/2015.
 */
public class FavoriteArtist {
    private Artist artist;

    public FavoriteArtist(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
