package com.example.mithildarshan.team_91.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by mithishri on 1/7/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
