package com.wzes.vmovie.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wzes.vmovie.R;
import com.wzes.vmovie.base.AppManager;
import com.wzes.vmovie.base.Preferences;
import com.wzes.vmovie.bean.User;
import com.wzes.vmovie.service.BaseUrlService;
import com.wzes.vmovie.service.MyRetrofit;
import com.wzes.vmovie.util.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

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

    // check login
    private void attemptLogin() {
        if(loginSign.getText().toString().equals("正在登录...")) {
            Toast.makeText(this, "正在登录", Toast.LENGTH_SHORT).show();
        }else{
            String username = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                loginPassword.setError(getString(R.string.error_invalid_password));
                return;
            }
            else if (TextUtils.isEmpty(username)) {
                loginUsername.setError(getString(R.string.error_field_required));
                return;
            }
            loginSign.setText("正在登录...");
            MyRetrofit.getInstance().getGsonRetrofit(BaseUrlService.SERVER)
                    .login(username, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull User user) {
                            if(user != null){
                                loginState = true;
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Toast.makeText(LoginActivity.this, "网络不太好", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            if(loginState){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                loginState = false;
                            }else{
                                loginState = false;
                            }
                        }
                    });
        }
    }



    @OnClick({R.id.login_register, R.id.login_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_register:
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
