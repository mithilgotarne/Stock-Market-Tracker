package com.example.mithildarshan.team_91.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mithildarshan.team_91.model.Stock;

import java.util.Random;


import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mithishri on 1/7/2017.
 */

public class BackgroundService extends JobService {
    private Random random;
    @Override
    public boolean onStartJob(final JobParameters params) {
        random = new Random();
        /*SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int n = prefs.getInt("key", 0);
        editor.putInt("key", n+1);
        editor.apply();*/

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Stock stock = new Stock();
                stock.setName("Some" + random.nextInt());
                stock.setHigh(random.nextFloat());
                stock.setLow(random.nextFloat());
                stock.setClose(random.nextFloat());
                stock.setOpen(random.nextFloat());
                stock.setVolume(random.nextInt());
                stock.setTimestamp(System.currentTimeMillis());

                realm.copyToRealm(stock);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                jobFinished(params, false);
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}

