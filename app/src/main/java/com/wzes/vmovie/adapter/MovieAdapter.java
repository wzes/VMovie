package com.wzes.vmovie.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzes.vmovie.R;
import com.wzes.vmovie.bean.Movie;

import java.util.List;

/**
 * Created by xuantang on 11/2/17.
 */

public class MovieAdapter extends BaseItemDraggableAdapter<Movie, BaseViewHolder> {

    public MovieAdapter(List<Movie> data) {
        super(data);
    }

    public MovieAdapter(int layoutResId, List<Movie> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Movie item) {
        helper.setText(R.id.movie_item_title, item.getTitle());
        if(item.getRating().equals("0")) {
            helper.getView(R.id.movie_item_rating).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.movie_item_rating).setVisibility(View.VISIBLE);
            helper.setText(R.id.movie_item_rating, item.getRating());
        }
        // 加载网络图片
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.movie_item_image));
    }
}
