package com.olebokolo.findmycar.activities.maps;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.olebokolo.findmycar.R;
import com.olebokolo.findmycar.core.app.FindMyCar;
import com.olebokolo.findmycar.core.location.factory.CoordinatesServiceFactory;
import com.olebokolo.findmycar.core.location.service.CoordinatesService;
import com.olebokolo.findmycar.core.location.utils.CoordinatesUtils;
import com.olebokolo.findmycar.core.permission.RequestLocationPermissionService;
import com.olebokolo.findmycar.core.sharedpreferences.SharedPreferencesManager;

public class ParkCarActivity extends FragmentActivity implements OnMapReadyCallback {

    private FindMyCar application;
    private GoogleMap map;
    private CoordinatesService coordinatesService;
    private CoordinatesUtils coordinatesUtils;
    private RequestLocationPermissionService permissionService;
    private SharedPreferencesManager preferencesManager;

    public ParkCarActivity() {
        getAppInstance();
        getCoordinatesService();
        getCoordinatesUtils();
        getPermissionService();
        getSharedPreferencesManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupMap();
        setupParkButton();
        askPermissionsIfNeeded();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        animateSlideRight();
    }

    private void animateSlideRight() {
        overridePendingTransition(R.anim.slide_in_from_left_fast, R.anim.slide_out_to_right_fast);
    }

    private void setupParkButton() {
        findViewById(R.id.park_here_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parkingCoordinates = getCurrentMapPosition();
                preferencesManager.putString("parkingCoordinates", parkingCoordinates);
                finishActivityWithMessage("Parked at " + parkingCoordinates);
            }
        });
    }

    private void finishActivityWithMessage(String text) {
        showToast(text);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        animateSlideRight();
    }

    private void showToast(String text) {
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private String getCurrentMapPosition() {
        return ((TextView) findViewById(R.id.coordinates)).getText().toString();
    }

    private void askPermissionsIfNeeded() {
        if (permissionService.dontHaveLocationPermission()) {
            permissionService.requestPermissionsFor(this);
        }
    }

    private void setupMap() {
        setContentView(R.layout.activity_park_car);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (permissionService.haveLocationPermission()) {
            Location deviceLocation = coordinatesService.getDeviceLocation();
            if (deviceLocation != null) moveCameraTo(deviceLocation);
            else showCantFindCurrentPositionOnMap();
        }
        setUpMapPositionChangeListener();
    }

    private void setUpMapPositionChangeListener() {
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                String lat = String.valueOf(map.getCameraPosition().target.latitude);
                String lngString = String.valueOf(map.getCameraPosition().target.longitude);
                String coordinates = cutToSevenChars(lat) + " : " + cutToSevenChars(lngString);
                ((TextView) findViewById(R.id.coordinates)).setText(coordinates);
            }
        });
    }

    private String cutToSevenChars(String lat) {
        return lat.substring(0, Math.min(7, lat.length()));
    }

    private void showCantFindCurrentPositionOnMap() {
        showToast("Can't find where You are. Please set parking coordinates");
    }

    private void moveCameraTo(Location deviceLocation) {
        LatLng nextPosition = coordinatesUtils.toLatLng(deviceLocation);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(nextPosition, 17);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestLocationPermissionService.ACCESS_FINE_REQUEST_CODE) {
            if (permissionGranted(grantResults[0])) {
                Location deviceLocation = coordinatesService.getDeviceLocation();
                moveCameraTo(deviceLocation);
            } else showCantFindCurrentPositionOnMap();
        }
    }

    private boolean permissionGranted(int grantResult) {
        return grantResult == PackageManager.PERMISSION_GRANTED;
    }

    private void getAppInstance() {
        application = FindMyCar.getInstance();
    }

    private void getCoordinatesService() {
        CoordinatesServiceFactory coordinatesServiceFactory = new CoordinatesServiceFactory();
        coordinatesService = coordinatesServiceFactory.getCoordinatesService();
    }

    private void getCoordinatesUtils() {
        coordinatesUtils = application.getCoordinatesUtils();
    }

    private void getPermissionService() {
        permissionService = application.getLocationPermissionService();
    }

    private void getSharedPreferencesManager() {
        preferencesManager = new SharedPreferencesManager();
    }
}
