package com.wzes.vmovie.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.wzes.vmovie.R;
import com.wzes.vmovie.activity.MovieDetailActivity;
import com.wzes.vmovie.adapter.MovieAdapter;
import com.wzes.vmovie.bean.Movie;
import com.wzes.vmovie.service.BaseUrlService;
import com.wzes.vmovie.service.MyRetrofit;
import com.wzes.vmovie.util.CustomLoadMoreView;
import com.wzes.vmovie.util.DoubanData;
import com.wzes.vmovie.util.MyLog;

import com.alibaba.fastjson.JSONObject;
import com.wzes.vmovie.util.OnLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class ComingSoonFragment extends Fragment {

    private List<Movie> list;
    private MovieAdapter movieAdapter;

    private static int TOTAL_COUNTER = 0;
    private static int mCurrentCounter = 0;

    @BindView(R.id.coming_soon_recyclerView)
    RecyclerView comingSoonRecyclerView;
    @BindView(R.id.coming_soon_refresh)
    SwipeRefreshLayout comingSoonRefresh;
    Unbinder unbinder;

    public ComingSoonFragment() {
    }

    public static ComingSoonFragment newInstance() {
        ComingSoonFragment fragment = new ComingSoonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     *
     * @param onLoadMoreListener
     */
    public void loadMore(final OnLoadMoreListener onLoadMoreListener){
        MyRetrofit.getInstance().getNormalRetrofit(BaseUrlService.DOUBAN)
                .getComingSoonList(mCurrentCounter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            onLoadMoreListener.onSuccess(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadMoreListener.onFail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * refresh data from douban
     */
    public void refreshData() {
        mCurrentCounter = 0;
        MyRetrofit.getInstance().getNormalRetrofit(BaseUrlService.DOUBAN)
                .getComingSoonList(mCurrentCounter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String data = "";
                        try {
                            data = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        list = DoubanData.parser(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        comingSoonRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        movieAdapter.notifyDataSetChanged();
                        comingSoonRefresh.setRefreshing(false);
                    }
                });
    }

    /**
     *
     */
    public void initData() {
        comingSoonRefresh.setRefreshing(true);
        MyRetrofit.getInstance().getNormalRetrofit(BaseUrlService.DOUBAN)
                .getComingSoonList(mCurrentCounter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String data = "";
                        try {
                            data = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        list = DoubanData.parser(data);
                        TOTAL_COUNTER = DoubanData.getTotal(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "网络不太好", Toast.LENGTH_LONG).show();
                        comingSoonRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        movieAdapter = new MovieAdapter(R.layout.movie_item, list);
                        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(movieAdapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                        itemTouchHelper.attachToRecyclerView(comingSoonRecyclerView);
                        movieAdapter.setEnableLoadMore(false);
                        movieAdapter.setLoadMoreView(new CustomLoadMoreView());
                        movieAdapter.setUpFetchEnable(true);
                        movieAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                            @Override
                            public void onLoadMoreRequested() {
                                mCurrentCounter += 20;
                                if (mCurrentCounter >= TOTAL_COUNTER) {
                                    //Data are all loaded.
                                    movieAdapter.loadMoreEnd();
                                } else {
                                    //list.add()
                                    // load more
                                    loadMore(new OnLoadMoreListener() {
                                        @Override
                                        public void onSuccess(String data) {
                                            movieAdapter.addData(DoubanData.parser(data));
                                            movieAdapter.loadMoreComplete();
                                        }

                                        @Override
                                        public void onFail() {
                                            Toast.makeText(getContext(), "网络不太好", Toast.LENGTH_LONG).show();
                                            movieAdapter.loadMoreFail();
                                        }
                                    });
                                }
                            }
                        }, comingSoonRecyclerView);

                        if(comingSoonRefresh != null) {
                            comingSoonRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                            comingSoonRecyclerView.setAdapter(movieAdapter);

                            movieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                                    intent.putExtra("id", list.get(position).getId());
                                    intent.putExtra("title", list.get(position).getTitle());
                                    intent.putExtra("image", list.get(position).getImage());
                                    intent.putExtra("rating", list.get(position).getRating());
                                    startActivity(intent);
                                }
                            });
                        }

                        if(comingSoonRefresh != null) {
                            comingSoonRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    refreshData();
                                }
                            });
                            comingSoonRefresh.setRefreshing(false);
                        }

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
