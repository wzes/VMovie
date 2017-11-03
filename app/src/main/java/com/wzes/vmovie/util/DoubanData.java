package com.wzes.vmovie.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzes.vmovie.bean.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuantang on 11/3/17.
 */

public class DoubanData {


    public static int getCurrent(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        return Integer.valueOf(jsonObject.get("start").toString());
    }

    public static int getTotal(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        return Integer.valueOf(jsonObject.get("total").toString());
    }

    public static String parserArray(String data){
        return data.replace("\"", "")
                .replace("[", "")
                .replace("]", "");

    }

    public static List<Movie> parserBox(String data) {
        List<Movie> movies = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data);
        // jsonObject.get("subjects");
        //JSONObject subjects = JSONObject.parseObject(jsonObject.get("subjects").toString());
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("subjects"));
        for( Object jsonObject1 : jsonArray) {
            JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.toString());
            // rank
            String rank = jsonObject2.get("rank").toString();
            //　box
            String box = jsonObject2.get("box").toString();

            JSONObject jsonObject3 = JSONObject.parseObject(jsonObject2.getString("subject"));

            JSONObject ratings = JSONObject.parseObject(jsonObject3.get("rating").toString());

            String rating = ratings.get("average").toString();

            // title
            String title = jsonObject3.get("title").toString();
            // images
            JSONObject images = JSONObject.parseObject(jsonObject3.get("images").toString());
            // large image
            String image = images.get("large").toString();
            // id
            String id = jsonObject3.get("id").toString();
            Movie movie = new Movie();
            movie.setId(id);
            movie.setTitle(title);
            movie.setImage(image);
            movie.setRating(rating);
            movies.add(movie);
        }
        return movies;
    }

    public static List<Movie> parser(String data){
        List<Movie> movies = new ArrayList<>();
        // System.out.println(response);
        JSONObject jsonObject = JSONObject.parseObject(data);
        // jsonObject.get("subjects");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("subjects"));
        for( Object jsonObject1 : jsonArray) {
            try {
                JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.toString());
                // images
                JSONObject images = JSONObject.parseObject(jsonObject2.get("images").toString());
                // large image
                String image = images.get("large").toString();

                JSONObject ratings = JSONObject.parseObject(jsonObject2.get("rating").toString());
                //　average rating
                String rating = ratings.get("average").toString();

                // title
                String title = jsonObject2.get("title").toString();

                // id
                String id = jsonObject2.get("id").toString();
                MyLog.i(title + " " + id + " " + rating + " " + image);
                Movie movie = new Movie();
                movie.setId(id);
                movie.setTitle(title);
                movie.setImage(image);
                movie.setRating(rating);
                movies.add(movie);
            }catch (Exception e){
                MyLog.i(e.getMessage());
            }

        }
        MyLog.i(data);
        return movies;
    }
}
