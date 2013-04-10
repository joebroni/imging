package com.corgrimm.imgy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/21/13
 * Time: 1:18 PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumImage implements Serializable{
    @JsonProperty("animated") private Boolean animated;
    @JsonProperty("bandwidth") private Number bandwidth;
    @JsonProperty("datetime") private Number datetime;
    @JsonProperty("description") private String description;
    @JsonProperty("favorite") private Boolean favorite;
    @JsonProperty("height") private Number height;
    @JsonProperty("id") private String id;
    @JsonProperty("link") private String link;
    @JsonProperty("size") private Number size;
    @JsonProperty("title") private String title;
    @JsonProperty("type") private String type;
    @JsonProperty("views") private Number views;
    @JsonProperty("width") private Number width;

    public Boolean getAnimated() {
        return animated;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
    }

    public Number getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Number bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Number getDatetime() {
        return datetime;
    }

    public void setDatetime(Number datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Number getHeight() {
        return height;
    }

    public void setHeight(Number height) {
        this.height = height;
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

    public Number getSize() {
        return size;
    }

    public void setSize(Number size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Number getViews() {
        return views;
    }

    public void setViews(Number views) {
        this.views = views;
    }

    public Number getWidth() {
        return width;
    }

    public void setWidth(Number width) {
        this.width = width;
    }
}
