package com.wzes.vmovie.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wzes.vmovie.R;
import com.wzes.vmovie.fragment.ComingSoonFragment;
import com.wzes.vmovie.fragment.InTheaterFragment;
import com.wzes.vmovie.fragment.Top250Fragment;
import com.wzes.vmovie.fragment.UsBoxFragment;

/**
 * Created by xuantang on 11/2/17.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private InTheaterFragment inTheaterFragment;
    private ComingSoonFragment comingSoonFragment;
    private Top250Fragment top250Fragment;
    private UsBoxFragment usBoxFragment;
    private String[] titles;
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(FragmentManager fm, Context context, InTheaterFragment inTheaterFragment,
                            ComingSoonFragment comingSoonFragment,
                            Top250Fragment top250Fragment,
                            UsBoxFragment usBoxFragment) {
        super(fm);
        this.context = context;
        this.inTheaterFragment = inTheaterFragment;
        this.comingSoonFragment = comingSoonFragment;
        this.top250Fragment = top250Fragment;
        this.usBoxFragment = usBoxFragment;
        this.titles = new String[]{context.getResources().getString(R.string.intheater),
                context.getResources().getString(R.string.comingsoon),
                context.getResources().getString(R.string.top250),
                context.getResources().getString(R.string.usbox)};
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return this.inTheaterFragment;
        }
        if (position == 1) {
            return this.comingSoonFragment;
        }
        if (position == 2) {
            return this.top250Fragment;
        }
        return this.usBoxFragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
