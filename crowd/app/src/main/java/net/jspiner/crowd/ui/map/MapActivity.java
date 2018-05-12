package net.jspiner.crowd.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
                return false;
            });

            loadCompanies();

        });
    }

    private void loadCompanies() {
        Api.getService().getCompanys()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(companies -> {
                    Log.i("TAG", "companyies : " + new Gson().toJson(companies));

                    for (Company company : companies) {
                        LatLng position = new LatLng(company.la, company.lo);
                        Marker marker = googleMap.addMarker(
                                new MarkerOptions().position(position)
                                        .title(company.name)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        );
                        marker.setTag(company);

                    }

                    LatLng cameraPosition = new LatLng(37.4882187, 127.0626);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 15f));

                });
    }

    private void initReviewView() {
        adapter = new CardAdapter();
        binding.pager.setAdapter(adapter);

        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(150, 0, 150, 0);
        binding.pager.setPageMargin(5);
    }
}
