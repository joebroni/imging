package com.corgrimm.imgy.models;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 4/11/13
 * Time: 7:55 AM
 */
public class ImageIdUrl implements Serializable {
    private String id;
    private String link;

    public ImageIdUrl(String id, String link) {
        this.id = id;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
