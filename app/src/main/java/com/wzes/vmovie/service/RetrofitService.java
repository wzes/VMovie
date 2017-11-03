package com.wzes.vmovie.service;

import com.wzes.vmovie.bean.MovieLink;
import com.wzes.vmovie.bean.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {


    /**
     * Search
     * @param moviename
     * @return
     */
    @GET("/search/{moviename}")
    Observable<List<MovieLink>> getMovieLinkList(@Query("moviename") String moviename);

    /**
     * Register
     * @param username
     * @param password
     * @return
     */
    @POST("register")
    @Multipart
    Observable<ResponseBody> register(@Field("username") String username,
                                      @Field("password") String password);

    @GET("login")
    Observable<User> login(@Query("username") String username,
                           @Query("password") String password);

    @GET("movie_collection")
    Observable<ResponseBody> addCollection(@Query("username") String username,
                                           @Query("movie_id") String movie_id,
                                           @Query("data") String data);

    @DELETE("{username}/collection/{movie_id}")
    @Multipart
    Observable<ResponseBody> removeCollection(@Field("username") String username,
                                           @Field("movie_id") String movie_id);
    /**
     * 豆瓣
     */

    @GET("v2/movie/in_theaters")
    Observable<ResponseBody> getInTheaterList(@Query("start") int start);

    @GET("v2/movie/coming_soon")
    Observable<ResponseBody> getComingSoonList(@Query("start") int start);

    @GET("v2/movie/top250")
    Observable<ResponseBody> getTop250List(@Query("start") int start);

    @GET("v2/movie/us_box")
    Observable<ResponseBody> getUsBoxList(@Query("start") int start);

    @GET("v2/movie/subject/{id}")
    Observable<ResponseBody> getSubject(@Path("id") String id);

}
