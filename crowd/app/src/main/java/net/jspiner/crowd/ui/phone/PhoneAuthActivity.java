package net.jspiner.crowd.ui.phone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;

import net.jspiner.crowd.R;
import net.jspiner.crowd.api.Api;
import net.jspiner.crowd.databinding.ActivityPhoneBinding;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;
import net.jspiner.crowd.ui.map.MapActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        binding.phone.setText(getDevicePhoneNumber());
        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1101);
        if (profile != null) {
            onProfileReceived(profile);
        } else {
            new ProfileTracker() {

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
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            HashMap<String, String> params = new HashMap<>();
            params.put("access_token", accessToken.getToken());
            params.put("id", accessToken.getUserId());
            params.put("name", Profile.getCurrentProfile().getName());
            params.put("phone_number", getDevicePhoneNumber());
            params.put("profile_image_url", Profile.getCurrentProfile().getProfilePictureUri(500, 500).toString());
            Api.getService().createUser(
                    params
            ).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        startMapActivity();
                    });
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

    public String getDevicePhoneNumber() {
        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }

        String phoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = telephonyManager.getLine1Number();
        String formatPhoneNumber;
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.startsWith("+82")) {
            formatPhoneNumber = "0" + phoneNumber.substring(3);
            phoneNumber = formatPhoneNumber;
        }
        return phoneNumber;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        binding.phone.setText(getDevicePhoneNumber());
    }
}
