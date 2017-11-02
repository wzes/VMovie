package com.wzes.vmovie.bean;

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

}
