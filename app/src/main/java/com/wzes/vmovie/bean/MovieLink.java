package com.wzes.vmovie.bean;

import com.alibaba.fastjson.JSON;

/**
 * @author Create by xuantang
 * @date on 11/2/17
 */
public class MovieLink {
    public MovieLink(String name, String link, String secret) {
        this.name = name;
        this.link = link;
        this.secret = secret;
    }

    public MovieLink() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    private String name;
    private String link;
    private String secret;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
