package com.wzes.vmovie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wzes.vmovie.R;
import com.wzes.vmovie.base.Preferences;
import com.wzes.vmovie.bean.User;
import com.wzes.vmovie.util.MyLog;

import java.io.IOException;

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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.follow_back)
    ImageButton followBack;
    @BindView(R.id.register_username)
    EditText registerUsername;
    @BindView(R.id.register_password)
    EditText registerPassword;
    @BindView(R.id.register_password_again)
    EditText registerPasswordAgain;
    @BindView(R.id.register_sign)
    Button registerSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    finish();
                    break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "网络不太好～", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @OnClick({R.id.follow_back, R.id.register_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.follow_back:
                finish();
                break;
            case R.id.register_sign:
                submit();
                break;
        }
    }

    private boolean checkPassword(String password, String passwordAgain) {

        if (TextUtils.isEmpty(password)) {
            registerPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }
        else if (TextUtils.isEmpty(passwordAgain)) {
            registerUsername.setError(getString(R.string.error_field_required));
            return false;
        }
        if(!password.equals(passwordAgain)){
            Toast.makeText(this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private void submit(){
        String username = registerUsername.getText().toString();
        String password = registerPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            registerPassword.setError(getString(R.string.error_invalid_password));
            return;
        }
        else if (TextUtils.isEmpty(username)) {
            registerUsername.setError(getString(R.string.error_field_required));
            return;
        }
        if(!checkPassword(password, registerPasswordAgain.getText().toString())){
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("username", username);
        formBody.add("password", password);
        RequestBody requestBody = formBody.build();

        Request request = new Request.Builder()
                .url("http://59.110.136.134:10001/vmovie/register")
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(1);
                MyLog.i(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                User user = JSON.parseObject(response.body().string(), User.class);
                if(user != null){
                    handler.sendEmptyMessage(0);
                }else {
                    MyLog.i(response.body().string());
                }
            }
        });
    }

}
