package com.olebokolo.findmycar.core.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.olebokolo.findmycar.core.app.FindMyCar;

public class RequestLocationPermissionService {

    public static final int ACCESS_FINE_REQUEST_CODE = 100;
    private FindMyCar application;
    private String[] locationPermissions;

    public RequestLocationPermissionService() {
        initAppReference();
        initLocationPermissions();
    }

    private void initAppReference() {
        application = FindMyCar.getInstance();
    }

    private void initLocationPermissions() {
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    }

    public boolean neededAndRequestedPermissionsFor(Activity activity) {
        if (dontHaveLocationPermission()) {
            requestPermissionsFor(activity);
            return true;
        } else return false;
    }

    public boolean dontHaveLocationPermission() {
        return ActivityCompat.checkSelfPermission(application,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }

    public boolean haveLocationPermission() {
        return !dontHaveLocationPermission();
    }

    public void requestPermissionsFor(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, locationPermissions, ACCESS_FINE_REQUEST_CODE);
    }
}
