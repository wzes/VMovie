package com.wzes.vmovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.wzes.vmovie.R;
import com.wzes.vmovie.service.BaseUrlService;
import com.wzes.vmovie.service.MyRetrofit;
import com.wzes.vmovie.util.MyLog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.movie_toolbar)
    Toolbar movieToolbar;
    @BindView(R.id.movie_collapsing)
    CollapsingToolbarLayout movieCollapsing;
    @BindView(R.id.movie_appBar)
    AppBarLayout movieAppBar;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.movie_genres)
    TextView movieGenres;
    @BindView(R.id.movie_year)
    TextView movieYear;
    @BindView(R.id.movie_countries)
    TextView movieCountries;
    @BindView(R.id.movie_comments_count)
    TextView movieCommentsCount;
    @BindView(R.id.movie_collect_count)
    TextView movieCollectCount;
    @BindView(R.id.movie_summary)
    TextView movieSummary;
    @BindView(R.id.movie_collect)
    FloatingActionButton movieCollect;
    @BindView(R.id.detail_layout)
    LinearLayout detailLayout;

    private String id;
    private String title;
    private String image;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");

        Glide.with(this).load(image).into(movieImage);
        movieToolbar.setTitle(title);

        setSupportActionBar(movieToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();
    }


    public void initData() {
        detailLayout.setVisibility(View.GONE);
        MyRetrofit.getInstance().getNormalRetrofit(BaseUrlService.DOUBAN)
                .getSubject(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            data = responseBody.string();
                            MyLog.i(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.i(e.getMessage());
                        Toast.makeText(MovieDetailActivity.this, "网路不太好～～", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        initView(data);
                        detailLayout.setVisibility(View.VISIBLE);
                    }
                });
    }


    public void initView(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        // jsonObject.get("subjects");
        JSONObject ratings = JSONObject.parseObject(jsonObject.get("rating").toString());
        //　average rating
        String rating = ratings.get("average").toString();

        String year = jsonObject.get("year").toString();
        String genres = jsonObject.get("genres").toString();
        String summary = jsonObject.get("summary").toString();

        // countries
        String countries = jsonObject.get("countries").toString();
        // comments_count
        String comments_count = jsonObject.get("comments_count").toString();
        // collect_count
        String collect_count = jsonObject.get("collect_count").toString();
        // id
        String id = jsonObject.get("id").toString();
        String ratings_count = jsonObject.get("ratings_count").toString();

        movieYear.setText(year + "年");
        movieCollectCount.setText(collect_count + "人收藏");
        movieCommentsCount.setText(comments_count + "人评论");
        movieGenres.setText(genres);
        movieRating.setText(rating + "/10 (" + ratings_count + "人打分)");
        movieCountries.setText(countries);
        movieSummary.setText(summary);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.movie_collect)
    public void onViewClicked() {
        Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
    }
}
