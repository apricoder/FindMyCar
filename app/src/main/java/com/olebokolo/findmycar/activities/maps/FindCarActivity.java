package com.olebokolo.findmycar.activities.maps;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olebokolo.findmycar.R;
import com.olebokolo.findmycar.core.app.FindMyCar;
import com.olebokolo.findmycar.core.location.factory.CoordinatesServiceFactory;
import com.olebokolo.findmycar.core.location.service.CoordinatesService;
import com.olebokolo.findmycar.core.location.utils.CoordinatesUtils;
import com.olebokolo.findmycar.core.permission.RequestLocationPermissionService;
import com.olebokolo.findmycar.core.sharedpreferences.SharedPreferencesManager;

public class FindCarActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int MAP_BORDER_PADDING = 200;

    private FindMyCar application;
    private GoogleMap map;
    private CoordinatesService coordinatesService;
    private CoordinatesUtils coordinatesUtils;
    private LatLng parkingPosition;
    private SharedPreferencesManager preferencesManager;
    private RequestLocationPermissionService permissionService;

    public FindCarActivity() {
        getAppInstance();
        getCoordinatesService();
        getCoordinatesUtils();
        getSharedPreferencesManager();
        getPermissionService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String parkingCoordinates = preferencesManager.getString("parkingCoordinates");
        if (parkingCoordinates == null) {
            finishActivityWithMessage("You didn't set parking position");
        } else {
            setupMap();
            setupUnparkButton();
            askPermissionsIfNeeded();
            parkingPosition = parse(parkingCoordinates);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        animateSlideRight();
    }

    @Override
    public void finish() {
        super.finish();
        animateSlideRight();
    }

    private void animateSlideRight() {
        overridePendingTransition(R.anim.slide_in_from_left_fast, R.anim.slide_out_to_right_fast);
    }

    private void askPermissionsIfNeeded() {
        if (permissionService.dontHaveLocationPermission()) {
            permissionService.requestPermissionsFor(this);
        }
    }

    private void setupUnparkButton() {
        findViewById(R.id.unpark_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencesManager.remove("parkingCoordinates");
                finishActivityWithMessage("Cleared parking position");
            }
        });
    }

    private void setupMap() {
        setContentView(R.layout.activity_find_car);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void finishActivityWithMessage(String text) {
        showToast(text);
        finish();
    }

    private void showToast(String text) {
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
    }

    private LatLng parse(String coordinates) {
        String[] latLngParts = coordinates.trim().replaceAll(",", ".").split(":");
        double lat = Double.parseDouble(latLngParts[0]);
        double lng = Double.parseDouble(latLngParts[1]);
        return new LatLng(lat, lng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (permissionService.haveLocationPermission()) {
            LatLngBounds.Builder mapView = new LatLngBounds.Builder();
            Location deviceLocation = coordinatesService.getDeviceLocation();
            if (deviceLocation != null) addDeviceLocationOnMap(deviceLocation, mapView);
            addParkingPositionOnMap(parkingPosition, mapView);
            animateCameraTo(mapView);
        }
    }

    private void animateCameraTo(LatLngBounds.Builder mapView) {
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(mapView.build(), MAP_BORDER_PADDING));
    }

    private void addParkingPositionOnMap(LatLng parkingPosition, LatLngBounds.Builder mapView) {
        map.addMarker(getMarkerOptions(parkingPosition, "Your car is here")).showInfoWindow();
        mapView.include(this.parkingPosition);
    }

    private void addDeviceLocationOnMap(Location deviceLocation, LatLngBounds.Builder mapView) {
        LatLng location = coordinatesUtils.toLatLng(deviceLocation);
        map.addMarker(getMarkerOptions(location, "You are here"));
        mapView.include(location);
    }

    private MarkerOptions getMarkerOptions(LatLng latLng, String title) {
        return new MarkerOptions().position(latLng).title(title);
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
