package com.wzes.vmovie.activity;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.wzes.vmovie.R;
import com.wzes.vmovie.adapter.CollectionAdapter;
import com.wzes.vmovie.adapter.DownloadAdapter;
import com.wzes.vmovie.bean.Movie;
import com.wzes.vmovie.bean.MovieLink;
import com.wzes.vmovie.service.DownloadService;
import com.wzes.vmovie.util.MyLog;
import com.wzes.vmovie.util.OnDownLoadListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class DownloadActivity extends AppCompatActivity {

    @BindView(R.id.download_back)
    ImageButton downloadBack;
    @BindView(R.id.download_recycler)
    RecyclerView downloadRecycler;

    private DownloadAdapter downloadAdapter;
    private List<MovieLink> list;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        initData();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    if(list.size() == 0) {
                        Toast.makeText(DownloadActivity.this, "没有找到相关下载", Toast.LENGTH_SHORT).show();
                    }else {
                        downloadAdapter = new DownloadAdapter(R.layout.download_item, list);

                        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(downloadAdapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                        itemTouchHelper.attachToRecyclerView(downloadRecycler);

                        downloadRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        downloadRecycler.setAdapter(downloadAdapter);

                        downloadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(list.get(position).getLink());
                                Toast.makeText(DownloadActivity.this, "已复制下载链接到剪贴板", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    break;
                case 1:
                    Toast.makeText(DownloadActivity.this, "网络不太好", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void initData() {
        DownloadService.execute(title, new OnDownLoadListener() {
            @Override
            public void onSuccess(String data) {
                MyLog.i(data);
                list = JSON.parseArray(data, MovieLink.class);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFail() {
                handler.sendEmptyMessage(1);
            }
        });
    }

    @OnClick(R.id.download_back)
    public void onViewClicked() {
        finish();
    }
}
