package com.olebokolo.findmycar.core.location.factory;

import android.content.Context;
import android.location.LocationManager;

import com.olebokolo.findmycar.core.app.FindMyCar;

public class LocationManagerFactory {

    private FindMyCar application;

    public LocationManagerFactory() {
        application = FindMyCar.getInstance();
    }

    public LocationManager getLocationManager() {
        return (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }
}
