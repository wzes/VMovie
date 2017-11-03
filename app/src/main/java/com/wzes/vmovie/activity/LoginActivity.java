package com.wzes.vmovie.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.wzes.vmovie.R;
import com.wzes.vmovie.base.AppManager;
import com.wzes.vmovie.base.Preferences;
import com.wzes.vmovie.bean.User;
import com.wzes.vmovie.service.BaseUrlService;
import com.wzes.vmovie.service.MyRetrofit;
import com.wzes.vmovie.util.MyLog;
import com.wzes.vmovie.util.NetworkUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static okhttp3.internal.http.HttpHeaders.*;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.login_register)
    Button loginRegister;
    @BindView(R.id.login_toolbar)
    Toolbar loginToolbar;
    @BindView(R.id.login_image)
    CircleImageView loginImage;
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_sign)
    Button loginSign;

    private static boolean loginState = false;
    private static final int INTERNET_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AppManager.getAppManager().addActivity(this);

        /*
         * request for database if local not username
         */
        if (Preferences.getUserAccount() != null) {

            // show defalut image and enter the main
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if ( !NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "网络不太好 ^_^", Toast.LENGTH_SHORT).show();
        }


    }

    private void internetTask() {
        String perm = Manifest.permission.INTERNET;
        if (hasInternetPermission()) {
            attemptLogin();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.internet_permission),
                    INTERNET_PERM, perm);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Preferences.saveUserAccount(loginUsername.getText().toString());
                    finish();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    loginSign.setText("立即登录");
                    break;
            }
        }
    };

    // check login
    private void attemptLogin() {
        if(loginSign.getText().toString().equals("正在登录...")) {
            Toast.makeText(this, "正在登录", Toast.LENGTH_SHORT).show();
        }else{
            final String username = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                loginPassword.setError(getString(R.string.error_invalid_password));
                return;
            }
            else if (TextUtils.isEmpty(username)) {
                loginUsername.setError(getString(R.string.error_field_required));
                return;
            }
            loginSign.setText("正在登录..");

            OkHttpClient okHttpClient = new OkHttpClient();

            FormBody.Builder formBody = new FormBody.Builder();
            formBody.add("username", username);
            formBody.add("password", password);
            RequestBody requestBody = formBody.build();

            Request request = new Request.Builder()
                    .url("http://59.110.136.134:10001/vmovie/login")
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
                public void onResponse(Call call, Response response) {
                    String res = null;
                    try {
                        res = response.body().string();
                    } catch (IOException e) {
                        MyLog.i(e.getMessage());
                    }
                    User user = JSON.parseObject(res, User.class);
                    if(user != null && user.getUsername() != null){
                        handler.sendEmptyMessage(0);
                    }else {
                        handler.sendEmptyMessage(1);
                    }
                }
            });

        }
    }



    @OnClick({R.id.login_register, R.id.login_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_sign:
                internetTask();
                break;
        }
    }

    private boolean hasInternetPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.INTERNET);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasInternetPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
