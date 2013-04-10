package com.corgrimm.imgy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 3/21/13
 * Time: 1:18 PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Album implements Serializable{
    @JsonProperty("account_url") private String account_url;
    @JsonProperty("cover") private String cover;
    @JsonProperty("datetime") private Number datetime;
    @JsonProperty("description") private String description;
    @JsonProperty("favorite") private Boolean favorite;
    @JsonProperty("id") private String id;
    @JsonProperty("images") private List<AlbumImage> images;
    @JsonProperty("images_count") private Number images_count;
    @JsonProperty("layout") private String layout;
    @JsonProperty("link") private String link;
    @JsonProperty("privacy") private String privacy;
    @JsonProperty("title") private String title;
    @JsonProperty("views") private Number views;

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AlbumImage> getImages() {
        return images;
    }

    public void setImages(List<AlbumImage> images) {
        this.images = images;
    }

    public Number getImages_count() {
        return images_count;
    }

    public void setImages_count(Number images_count) {
        this.images_count = images_count;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Number getViews() {
        return views;
    }

    public void setViews(Number views) {
        this.views = views;
    }
}
