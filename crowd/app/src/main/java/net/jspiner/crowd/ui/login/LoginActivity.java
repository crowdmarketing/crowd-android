package net.jspiner.crowd.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.ActivityLoginBinding;
import net.jspiner.crowd.ui.base.BaseActivity;

import java.util.Arrays;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, Contract.Presenter> implements Contract.View {

    private CallbackManager callbackManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Contract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getBaseContext(), "로그인되었습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "로그인이 취소되었습니다.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getBaseContext(), "에러가 발생하였습니다.", Toast.LENGTH_LONG).show();
                exception.printStackTrace();
                Log.i("TAG", "onError : " + exception.getMessage());

            }
        });

    }

    private void startMapActivity() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
