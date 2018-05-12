package net.jspiner.crowd.ui.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.ActivityPhoneBinding;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;
import net.jspiner.crowd.ui.map.MapActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.view.View.VISIBLE;

public class PhoneAuthActivity extends BaseActivity<ActivityPhoneBinding, BasePresenterInterface> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone;
    }

    @Override
    protected BasePresenterInterface createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null){
            onProfileReceived(profile);
        }
        else {
            new ProfileTracker(){

                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    onProfileReceived(currentProfile);
                }
            }.startTracking();
        }

        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 9) {
                    binding.send.setAlpha(1f);
                    binding.send.setClickable(true);
                } else {
                    binding.send.setAlpha(0.7f);
                    binding.send.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.authNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    binding.auth.setAlpha(1f);
                    binding.auth.setClickable(true);
                } else {
                    binding.auth.setAlpha(0.7f);
                    binding.auth.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.send.setOnClickListener(v -> {
            startCountDown();
            binding.send.setClickable(false);
            binding.send.setAlpha(0.7f);
            binding.authNumber.setVisibility(VISIBLE);
            binding.auth.setVisibility(VISIBLE);
        });
        binding.auth.setOnClickListener(__ -> {
            startMapActivity();
        });
    }

    private void onProfileReceived(Profile profile) {
        Glide.with(this)
                .load(profile.getProfilePictureUri(500, 500))
                .into(binding.profileImage);
        binding.name.setText(profile.getName());

    }

    private void startCountDown() {
        binding.time.setVisibility(VISIBLE);
        Observable.intervalRange(0, 100, 0, 1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        value -> {
                            binding.time.setText(
                                    "남은시간 : " + (100 - value) + "초"
                            );
                        }
                );
    }

    private void startMapActivity() {
        Intent intent = new Intent(PhoneAuthActivity.this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}
