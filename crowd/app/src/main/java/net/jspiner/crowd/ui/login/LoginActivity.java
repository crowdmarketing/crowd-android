package net.jspiner.crowd.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.ActivityLoginBinding;
import net.jspiner.crowd.ui.login.adapter.LoginBackgroundAdapter;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenter;
import net.jspiner.crowd.ui.map.MapActivity;

import java.util.Arrays;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, BasePresenter> {

    private CallbackManager callbackManager;
    private LoginBackgroundAdapter adapter;
    private boolean isScrollingDown = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        adapter = new LoginBackgroundAdapter();
        binding.backgroundRecyclerview.setLayoutManager(
                new ScrollGridLayoutManager(
                        getBaseContext(),
                        3
                )
        );
        binding.backgroundRecyclerview.setAdapter(adapter);
        binding.backgroundRecyclerview.setOnTouchListener((view, motionEvent) -> true);
        autoScroll();

        initFbLogin();
    }

    private void initFbLogin() {
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    public void autoScroll() {
        binding.backgroundRecyclerview.smoothScrollToPosition(
                adapter.getItemCount()
        );
        binding.backgroundRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrollingDown = !isScrollingDown;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        final int speedScroll = 500;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (isScrollingDown) {
                    binding.backgroundRecyclerview.smoothScrollToPosition(
                            0
                    );
                } else {
                    binding.backgroundRecyclerview.smoothScrollToPosition(
                            adapter.getItemCount()
                    );
                }
                handler.postDelayed(this, speedScroll);
            }

        };
        handler.postDelayed(runnable, speedScroll);
    }

    private class ScrollGridLayoutManager extends GridLayoutManager {
        public ScrollGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public ScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        public ScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(LoginActivity.this) {
                private static final float SPEED = 4000f;// Change this                value (default=25f)

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return SPEED / displayMetrics.densityDpi;
                }

                @Nullable
                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return ScrollGridLayoutManager.this.computeScrollVectorForPosition(targetPosition);
                }
            };
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
