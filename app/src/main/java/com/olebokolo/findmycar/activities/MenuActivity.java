package com.olebokolo.findmycar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.olebokolo.findmycar.R;
import com.olebokolo.findmycar.activities.maps.FindCarActivity;
import com.olebokolo.findmycar.activities.maps.ParkCarActivity;
import com.olebokolo.findmycar.core.app.FindMyCar;

@SuppressWarnings("ConstantConditions")
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setUpParkButton();
        setupFindCarButton();
    }

    private void setupFindCarButton() {
        findViewById(R.id.find_car_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(FindCarActivity.class);
            }
        });
    }

    private void setUpParkButton() {
        findViewById(R.id.park_car_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActivity(ParkCarActivity.class);
            }
        });
    }

    private void showActivity(Class activityClass) {
        startActivity(new Intent(FindMyCar.getInstance(), activityClass));
        animateSlideLeft();
    }

    private void animateSlideLeft() {
        overridePendingTransition(R.anim.slide_in_from_right_fast, R.anim.slide_out_to_left_fast);
    }
}
