package net.jspiner.crowd.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import net.jspiner.crowd.R;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;
import net.jspiner.crowd.ui.login.LoginActivity;
import net.jspiner.crowd.ui.map.MapActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected BasePresenterInterface createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startMainActivity();
    }

    private void startMainActivity() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Completable.timer(1000 * 2, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Intent intent;
                    if (accessToken == null) {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);

                    finish();
                });

    }
}
