package me.captivelabs.goliveit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aron on 10/23/2015.
 */
public class Artist implements Serializable {
    private int id;
    private String name;
    private String description;
    private String description_source;
    private ArrayList<Image> images;
    private ArrayList<String> genres;
    private double familiarity;
    private double hotttnesss;
    private double discovery;
    private Urls urls;
    private boolean is_favorite = false;

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public double getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(double familiarity) {
        this.familiarity = familiarity;
    }

    public double getHotttnesss() {
        return hotttnesss;
    }

    public void setHotttnesss(double hotttnesss) {
        this.hotttnesss = hotttnesss;
    }

    public double getDiscovery() {
        return discovery;
    }

    public void setDiscovery(double discovery) {
        this.discovery = discovery;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_source() {
        return description_source;
    }

    public void setDescription_source(String description_source) {
        this.description_source = description_source;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }
}
