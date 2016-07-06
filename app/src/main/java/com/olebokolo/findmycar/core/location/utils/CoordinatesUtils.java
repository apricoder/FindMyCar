package com.olebokolo.findmycar.core.location.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class CoordinatesUtils {
    public LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
