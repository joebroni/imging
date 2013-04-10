package com.corgrimm.imgy.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Anderson
 * Date: 4/10/13
 * Time: 12:14 PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable {
    @JsonProperty("album_cover") private String album_cover;
    @JsonProperty("author") private String author;
    @JsonProperty("author_id") private Number authorId;
    @JsonProperty("children") private List<Comment> children;
    @JsonProperty("comment") private String comment;
    @JsonProperty("datetime") private Number datetime;
    @JsonProperty("deleted") private Boolean deleted;
    @JsonProperty("downs") private Number downs;
    @JsonProperty("id") private Number id;
    @JsonProperty("image_id") private String imageId;
    @JsonProperty("on_album") private Boolean onAlbum;
    @JsonProperty("parent_id") private Number parentId;
    @JsonProperty("points") private Number points;
    @JsonProperty("ups") private Number ups;

    public String getAlbum_cover() {
        return album_cover;
    }

    public void setAlbum_cover(String album_cover) {
        this.album_cover = album_cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Number getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Number authorId) {
        this.authorId = authorId;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Number getDatetime() {
        return datetime;
    }

    public void setDatetime(Number datetime) {
        this.datetime = datetime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Number getDowns() {
        return downs;
    }

    public void setDowns(Number downs) {
        this.downs = downs;
    }

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Boolean getOnAlbum() {
        return onAlbum;
    }

    public void setOnAlbum(Boolean onAlbum) {
        this.onAlbum = onAlbum;
    }

    public Number getParentId() {
        return parentId;
    }

    public void setParentId(Number parentId) {
        this.parentId = parentId;
    }

    public Number getPoints() {
        return points;
    }

    public void setPoints(Number points) {
        this.points = points;
    }

    public Number getUps() {
        return ups;
    }

    public void setUps(Number ups) {
        this.ups = ups;
    }
}
