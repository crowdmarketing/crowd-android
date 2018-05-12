package net.jspiner.crowd.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.jspiner.crowd.R;
import net.jspiner.crowd.databinding.ActivityMapBinding;
import net.jspiner.crowd.ui.base.BaseActivity;
import net.jspiner.crowd.ui.base.BasePresenterInterface;

public class MapActivity extends BaseActivity<ActivityMapBinding, BasePresenterInterface> {

    private CardAdapter adapter;

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
            Log.i("TAG", "onMapReady");

            LatLng sydney = new LatLng(37.4882187, 127.0626);
            googleMap.addMarker(new MarkerOptions().position(sydney)
                    .title("개포동포동"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            LatLng sydney2 = new LatLng(37.4893342, 127.0627197);
            googleMap.addMarker(new MarkerOptions().position(sydney2)
                    .title("개포동포동2")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney2, 15f));
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
