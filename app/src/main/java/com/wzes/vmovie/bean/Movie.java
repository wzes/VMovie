package com.wzes.vmovie.bean;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by xuantang on 11/2/17.
 */

public class Movie {

    private String title;
    /**
     * 图片链接
     */
    private String image;
    /**
     * 评分
     */
    private String rating;
    /**
     * 类型
     */
    private String genres;

    /**
     * 豆瓣电影id
     */
    private String id;

    /**
     * 概述
     */
    private String summary;

    /**
     * url
     */
    private String alt;

    /**
     * 下载链接
     */
    private List<MovieLink> downloadLinks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public List<MovieLink> getDownloadLinks() {
        return downloadLinks;
    }

    public void setDownloadLinks(List<MovieLink> downloadLinks) {
        this.downloadLinks = downloadLinks;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
