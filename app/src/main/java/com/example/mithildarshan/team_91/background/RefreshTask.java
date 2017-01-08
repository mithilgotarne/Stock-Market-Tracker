package com.example.mithildarshan.team_91.background;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mithildarshan.team_91.R;
import com.example.mithildarshan.team_91.model.Company;
import com.example.mithildarshan.team_91.model.Record;
import com.example.mithildarshan.team_91.model.Stock;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

/**
 * Created by mithishri on 1/8/2017.
 */

public class RefreshTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("RF", "Started Job");
        Realm realm = Realm.getDefaultInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int miniusHours = -24;
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            miniusHours = -48;
        else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            miniusHours = -72;
        calendar.add(Calendar.HOUR, miniusHours);
        long currTime = calendar.getTimeInMillis() / 1000;
        Log.e("RFD", String.valueOf(currTime));
        for (Company company : realm.where(Company.class).findAll()) {
            Log.d("RF", "Started " + company.getName());
            RealmList<Record> records = company.getRecords();
            Record record = records.where()
                    .lessThanOrEqualTo("timestamp", currTime)
                    .findAllSorted("timestamp", Sort.DESCENDING).first();
            if (record != null) {
                Log.d("RS", "Compsny " + record.getCompanyName());
                Log.e("RFD", String.valueOf(record.getTimestamp()));
                Stock stock = realm.where(Stock.class).equalTo("name", record.getCompanyName()).findFirst();
                if (stock == null) {
                    stock = new Stock();
                    stock.setName(record.getCompanyName());
                    stock.setFavourite(false);
                }
                realm.beginTransaction();
                stock.setVolume(record.getVolume());
                stock.setName(record.getCompanyName());
                stock.setLow(record.getLow());
                stock.setHigh(record.getHigh());
                stock.setClose(record.getClose());
                stock.setOpen(record.getOpen());
                realm.copyToRealm(stock);
                realm.commitTransaction();
            } else {
                Log.d("RS", "JobFinished");
                return false;
            }

        }

        return true;

    }
}
