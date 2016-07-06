package com.olebokolo.findmycar.core.location.service;

import android.location.Location;
import android.location.LocationManager;

import lombok.Setter;

public class CoordinatesService {

    @Setter private LocationManager locationManager;

    @SuppressWarnings("MissingPermission")
    public Location getDeviceLocation() {
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

}
