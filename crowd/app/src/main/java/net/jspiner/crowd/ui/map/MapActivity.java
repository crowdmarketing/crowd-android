package net.jspiner.crowd.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import net.jspiner.crowd.R;
import net.jspiner.crowd.api.Api;
import net.jspiner.crowd.databinding.ActivityMapBinding;
import net.jspiner.crowd.model.Company;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends BaseActivity<ActivityMapBinding, BasePresenterInterface> {

    private CardAdapter adapter;
    private GoogleMap googleMap;

    private static final String CUSTOMER = "customer";
    private static final String MARKETOR = "marketor";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
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
        initMap();
        initReviewView();

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
            Log.i("TAG", "onMapReady");

            googleMap.setOnMarkerClickListener(marker -> {
                binding.container.setVisibility(View.VISIBLE);
                Company company = (Company) marker.getTag();
                binding.name.setText(company.name);
                loadCompanyFriends(company);
                return false;
            });

            loadCompanies();

        });
    }

    private void loadCompanyFriends(Company company) {
        Api.getService().getCompanyFriends(
                company.id,
                getDevicePhoneNumber()
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(users -> adapter.resetAll(users));
    }

    private void loadCompanies() {
        Api.getService().getCompanys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(companies -> {
                    Log.i("TAG", "companyies : " + new Gson().toJson(companies));

                    for (Company company : companies) {
                        loadCompanyDetail(company);
                    }

                    LatLng cameraPosition = new LatLng(37.4882187, 127.0626);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 15f));

                });
    }

    private void loadCompanyDetail(Company company) {
        Api.getService().getCompanyDetail(
                company.id,
                getDevicePhoneNumber()
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(companyDetail -> {
                    float color;
                    switch (companyDetail.position) {
                        case CUSTOMER:
                        case "temp":
                            color = BitmapDescriptorFactory.HUE_RED;
                            break;
                        case MARKETOR:
                            color = BitmapDescriptorFactory.HUE_GREEN;
                            break;
                        default:
                            color = BitmapDescriptorFactory.HUE_ORANGE;
                            break;
                    }
                    LatLng latLng = new LatLng(company.la, company.lo);
                    Marker marker = googleMap.addMarker(
                            new MarkerOptions().position(latLng)
                                    .title(company.name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    );
                    marker.setTag(company);

                });
    }

    private void initReviewView() {
        adapter = new CardAdapter();
        binding.pager.setAdapter(adapter);

        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(120, 0, 150, 0);
        binding.pager.setPageMargin(50);
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
}
