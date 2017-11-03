package com.wzes.vmovie.activity;

import android.annotation.SuppressLint;
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

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.wzes.vmovie.R;
import com.wzes.vmovie.adapter.CollectionAdapter;
import com.wzes.vmovie.base.Preferences;
import com.wzes.vmovie.bean.Movie;
import com.wzes.vmovie.bean.User;
import com.wzes.vmovie.util.MyLog;
import com.wzes.vmovie.util.XmlUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.collection_back)
    ImageButton collectionBack;
    @BindView(R.id.collection_recycler)
    RecyclerView collectionRecycler;

    private List<Movie> list;
    private CollectionAdapter collectionAdapter;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        initData();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    list = XmlUtils.parser(data);
                    MyLog.i(list.size() + "");
                    collectionAdapter = new CollectionAdapter(R.layout.collection_item, list);

                    ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(collectionAdapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                    itemTouchHelper.attachToRecyclerView(collectionRecycler);


                    // 开启滑动删除
                    collectionAdapter.enableSwipeItem();
                    collectionAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
                        @Override
                        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

                        }

                        @Override
                        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

                        }

                        @Override
                        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                            removeCollection(list.get(pos).getId());
                        }

                        @Override
                        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

                        }
                    });

                    collectionRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    collectionRecycler.setAdapter(collectionAdapter);

                    collectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(CollectionActivity.this, MovieDetailActivity.class);
                            intent.putExtra("id", list.get(position).getId());
                            intent.putExtra("title", list.get(position).getTitle());
                            intent.putExtra("image", list.get(position).getImage());
                            intent.putExtra("rating", list.get(position).getRating());
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    Toast.makeText(CollectionActivity.this, "网络不太好", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(CollectionActivity.this, "网络不太好", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(CollectionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    public void removeCollection(String id){
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://59.110.136.134:10001/vmovie/" + Preferences.getUserAccount()+ "/movie_collection/" + id)
                .delete()
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(3);
                MyLog.i(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    data = response.body().string();
                } catch (IOException e) {
                    MyLog.i(data);
                }
                MyLog.i(data);
                handler.sendEmptyMessage(4);
            }
        });
    }

    public void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://59.110.136.134:10001/vmovie/" + Preferences.getUserAccount()+ "/movie_collections")
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(1);
                MyLog.i(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    data = response.body().string();
                } catch (IOException e) {
                    MyLog.i(data);
                }
                MyLog.i(data);
                handler.sendEmptyMessage(0);
            }
        });
    }

    @OnClick(R.id.collection_back)
    public void onViewClicked() {
        finish();
    }
}
