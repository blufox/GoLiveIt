package me.captivelabs.goliveit.models;

import java.io.Serializable;

/**
 * Created by Aron on 10/24/2015.
 */
public class Urls implements Serializable {

    private String official_url = "";
    private String lastfm_url = "";
    private String twitter_url = "";
    private String myspace_url = "";
    private String wikipedia_url = "";
    private String mb_url = "";
    private String facebook_url = "";

    public String getOfficial_url() {

        return official_url;
    }

    public void setOfficial_url(String official_url) {
        this.official_url = official_url;
    }

    public String getLastfm_url() {
        return lastfm_url;
    }

    public void setLastfm_url(String lastfm_url) {
        this.lastfm_url = lastfm_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }

    public String getMyspace_url() {
        return myspace_url;
    }

    public void setMyspace_url(String myspace_url) {
        this.myspace_url = myspace_url;
    }

    public String getWikipedia_url() {
        return wikipedia_url;
    }

    public void setWikipedia_url(String wikipedia_url) {
        this.wikipedia_url = wikipedia_url;
    }

    public String getMb_url() {
        return mb_url;
    }

    public void setMb_url(String mb_url) {
        this.mb_url = mb_url;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }
}
