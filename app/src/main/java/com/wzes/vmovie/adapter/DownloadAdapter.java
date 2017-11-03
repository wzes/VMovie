package com.wzes.vmovie.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzes.vmovie.R;
import com.wzes.vmovie.bean.Movie;
import com.wzes.vmovie.bean.MovieLink;

import java.util.List;

/**
 * Created by xuantang on 11/3/17.
 */

public class DownloadAdapter extends BaseItemDraggableAdapter<MovieLink, BaseViewHolder> {


    public DownloadAdapter(List<MovieLink> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MovieLink item) {
        helper.setText(R.id.download_item_title, item.getName());
        helper.setText(R.id.download_item_link, item.getLink());
        if(!TextUtils.isEmpty(item.getLink())){
            helper.setText(R.id.download_item_secret, item.getSecret());
        }
    }

    public DownloadAdapter(int layoutResId, List<MovieLink> data) {
        super(layoutResId, data);
    }

}
