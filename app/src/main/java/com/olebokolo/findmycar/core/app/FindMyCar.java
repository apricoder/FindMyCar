package com.olebokolo.findmycar.core.app;

import android.app.Application;

import com.olebokolo.findmycar.core.location.factory.CoordinatesServiceFactory;
import com.olebokolo.findmycar.core.location.factory.LocationManagerFactory;
import com.olebokolo.findmycar.core.location.service.CoordinatesService;
import com.olebokolo.findmycar.core.location.utils.CoordinatesUtils;
import com.olebokolo.findmycar.core.permission.RequestLocationPermissionService;
import com.olebokolo.findmycar.core.sharedpreferences.SharedPreferencesManager;

import lombok.Getter;

public class FindMyCar extends Application {
    @Getter private LocationManagerFactory locationManagerFactory;
    @Getter private CoordinatesServiceFactory coordinatesServiceFactory;
    @Getter private RequestLocationPermissionService locationPermissionService;
    @Getter private CoordinatesUtils coordinatesUtils;

    @Getter private static FindMyCar instance = new FindMyCar();
    public FindMyCar() {
        initInstance();
        initFields();
    }

    private void initInstance() {
        instance = this;
    }

    private void initFields() {
        locationManagerFactory = new LocationManagerFactory();
        coordinatesServiceFactory = new CoordinatesServiceFactory();
        locationPermissionService = new RequestLocationPermissionService();
        coordinatesUtils = new CoordinatesUtils();
    }
}
