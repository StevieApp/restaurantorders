package com.kilimanjarofood.restaurantorders;

import android.app.Application;
import android.content.Intent;

public class Alerting extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, Alert.class));
    }

}
