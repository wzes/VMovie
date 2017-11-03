package com.wzes.vmovie.service;

import com.wzes.vmovie.bean.Movie;
import com.wzes.vmovie.bean.MovieLink;
import com.wzes.vmovie.util.MyLog;
import com.wzes.vmovie.util.OnDownLoadListener;
import com.wzes.vmovie.util.OnLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuantang on 11/3/17.
 */

public class DownloadService {

    public static void execute(String title, final OnDownLoadListener onDownLoadListener) {

        List<MovieLink> list = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();


        final Request request = new Request.Builder()
                .url("http://59.110.136.134:10001/vmovie/search/" + title)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onDownLoadListener.onFail();
                MyLog.i(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onDownLoadListener.onSuccess(response.toString());
            }
        });
    }

}
