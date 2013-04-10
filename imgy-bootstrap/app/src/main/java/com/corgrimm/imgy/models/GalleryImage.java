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
public class GalleryImage implements Serializable{
    @JsonProperty("account_url") private String account_url;
    @JsonProperty("animated") private Boolean animated;
    @JsonProperty("bandwidth") private Number bandwidth;
    @JsonProperty("datetime") private Number datetime;
    @JsonProperty("description") private String description;
    @JsonProperty("downs") private Number downs;
    @JsonProperty("favorite") private Boolean favorite;
    @JsonProperty("height") private Number height;
    @JsonProperty("id") private String id;
    @JsonProperty("is_album") private Boolean is_album;
    @JsonProperty("layout") private String layout;
    @JsonProperty("link") private String link;
    @JsonProperty("score") private Number score;
    @JsonProperty("title") private String title;
    @JsonProperty("type") private String type;
    @JsonProperty("ups") private Number ups;
    @JsonProperty("views") private Number views;
    @JsonProperty("vote") private Boolean vote;
    @JsonProperty("width") private Number width;

    public String getAccount_url() {
        return account_url;
    }

    public void setAccount_url(String account_url) {
        this.account_url = account_url;
    }

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

    public Number getDowns() {
        return downs;
    }

    public void setDowns(Number downs) {
        this.downs = downs;
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

    public Boolean getIs_album() {
        return is_album;
    }

    public void setIs_album(Boolean is_album) {
        this.is_album = is_album;
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

    public Number getScore() {
        return score;
    }

    public void setScore(Number score) {
        this.score = score;
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

    public Number getUps() {
        return ups;
    }

    public void setUps(Number ups) {
        this.ups = ups;
    }

    public Number getViews() {
        return views;
    }

    public void setViews(Number views) {
        this.views = views;
    }

    public Boolean getVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }

    public Number getWidth() {
        return width;
    }

    public void setWidth(Number width) {
        this.width = width;
    }
}
