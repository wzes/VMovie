package com.wzes.vmovie.adapter;

import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzes.vmovie.R;
import com.wzes.vmovie.bean.Movie;

import java.util.List;

/**
 * Created by xuantang on 11/3/17.
 */

public class CollectionAdapter extends BaseItemDraggableAdapter<Movie, BaseViewHolder> {


    public CollectionAdapter(List<Movie> data) {
        super(data);
    }

    public CollectionAdapter(int layoutResId, List<Movie> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Movie item) {
        helper.setText(R.id.collection_item_title, item.getTitle());
        helper.setText(R.id.collection_item_rating_score, item.getRating() + "/10");
        ((RatingBar)helper.getView(R.id.collection_item_rating))
                .setRating(Float.valueOf(item.getRating()) / 2);
        // 加载网络图片
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.collection_item_image));
    }
}
