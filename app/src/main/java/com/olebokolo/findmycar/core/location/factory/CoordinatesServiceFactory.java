package com.olebokolo.findmycar.core.location.factory;

import android.location.LocationManager;

import com.olebokolo.findmycar.core.app.FindMyCar;
import com.olebokolo.findmycar.core.location.service.CoordinatesService;
import com.olebokolo.findmycar.core.permission.RequestLocationPermissionService;

import lombok.Getter;
import lombok.Setter;

public class CoordinatesServiceFactory {

    private CoordinatesService coordinatesService;

    public CoordinatesService getCoordinatesService() {
        getLocationManager();
        setupCoordinatesService();
        return coordinatesService;
    }

    private LocationManager locationManager;
    private FindMyCar application;

    public CoordinatesServiceFactory() {
        getAppInstance();
    }

    private void setupCoordinatesService() {
        coordinatesService = new CoordinatesService();
        coordinatesService.setLocationManager(locationManager);
    }

    private void getAppInstance() {
        application = FindMyCar.getInstance();
    }

    private void getLocationManager() {
        LocationManagerFactory locationManagerFactory = application.getLocationManagerFactory();
        locationManager = locationManagerFactory.getLocationManager();
    }

}
