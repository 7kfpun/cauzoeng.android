package com.cauzoeng.android;


import com.cauzoeng.android.model.MyItem;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.io.InputStream;
import java.util.List;

import org.json.JSONException;


public class MapActivity extends FragmentActivity {
    private GoogleMap mMap;

    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap != null) {
            startDemo();
        }
    }

    private ClusterManager<MyItem> mClusterManager;

    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        getMap().setOnCameraChangeListener(mClusterManager);

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mClusterManager.addItems(items);
    }

    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }

}