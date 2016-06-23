package me.captivelabs.goliveit.models;

import java.io.Serializable;

/**
 * Created by Aron on 10/24/2015.
 */
public class Image implements Serializable{
    private String url;
    private String StringurlSsl;
    private String type;
    private boolean resizableInd;
    private String credit;
    private String source;
    private boolean primaryInd;
    private int height;
    private int width;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStringurlSsl() {
        return StringurlSsl;
    }

    public void setStringurlSsl(String stringurlSsl) {
        StringurlSsl = stringurlSsl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isResizableInd() {
        return resizableInd;
    }

    public void setResizableInd(boolean resizableInd) {
        this.resizableInd = resizableInd;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isPrimaryInd() {
        return primaryInd;
    }

    public void setPrimaryInd(boolean primaryInd) {
        this.primaryInd = primaryInd;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
