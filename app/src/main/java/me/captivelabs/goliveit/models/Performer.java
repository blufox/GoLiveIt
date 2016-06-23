package me.captivelabs.goliveit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gilbert on 26/10/2015.
 */
public class Performer implements Serializable {

    private int id;
    private String name;
    private int eventCount;
    private String webURI;
    private String url;
    private boolean is_favorite;
    private ArrayList<Image> images;

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
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

    public String getWebURI() {
        return webURI;
    }

    public void setWebURI(String webURI) {
        this.webURI = webURI;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
}