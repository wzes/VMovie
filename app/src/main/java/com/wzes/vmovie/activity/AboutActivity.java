package com.wzes.vmovie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.wzes.vmovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_back)
    ImageButton aboutBack;
    @BindView(R.id.about_web)
    WebView aboutWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        aboutWeb.loadUrl("https://github.com/wzes");
    }

    @OnClick(R.id.about_back)
    public void onViewClicked() {
        finish();
    }
}
