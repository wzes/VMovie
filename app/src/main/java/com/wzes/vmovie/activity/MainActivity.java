package com.wzes.vmovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wzes.vmovie.R;
import com.wzes.vmovie.adapter.MainPagerAdapter;
import com.wzes.vmovie.base.Preferences;
import com.wzes.vmovie.fragment.ComingSoonFragment;
import com.wzes.vmovie.fragment.InTheaterFragment;
import com.wzes.vmovie.fragment.Top250Fragment;
import com.wzes.vmovie.fragment.UsBoxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tabs)
    TabLayout mainTabs;
    @BindView(R.id.main_pager)
    ViewPager mainPager;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        // header name
        View headerView = navView.getHeaderView(0);
        TextView titleTxt = headerView.findViewById(R.id.header_title);
        titleTxt.setText(Preferences.getUserAccount());
        // pager
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                getApplicationContext(), InTheaterFragment.newInstance(),
                ComingSoonFragment.newInstance(), Top250Fragment.newInstance(),
                UsBoxFragment.newInstance());
        mainPager.setAdapter(mainPagerAdapter);
        mainTabs.setupWithViewPager(mainPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, CollectionActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Preferences.saveUserAccount(null);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean mIsExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
