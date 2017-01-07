package com.example.mithildarshan.team_91;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mithildarshan.team_91.background.FetchDailyData;
import com.example.mithildarshan.team_91.model.Company;
import com.example.mithildarshan.team_91.model.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {

    private TextView txt;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        new FetchDailyData(this).execute();

        txt = (TextView) findViewById(R.id.text_view);
        txt.setText(" ");
        for (Company company : realm.where(Company.class).findAll()) {

            txt.append(company.getName());
            RealmList<Record> records = company.getRecords();
            int cnt=0;
            for (Record rec : records) {
                if (cnt > 10) break;
                txt.append( "\n" + rec.getTimestamp() + ":" + rec.getClose() + "\n");
                cnt++;
            }
        }

       /* new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                realm = Realm.getDefaultInstance();
                String status = "";
                for (Company company : realm.where(Company.class).findAll()) {
                    RealmList<Record> records = company.getRecords();

                    for (Record rec : records) {
                        if (rec.getTimestamp() > 10) break;
                        status += "\n" + rec.getTimestamp() + ":" + rec.getClose() + "\n";
                    }
                }
                return status;
            }
            */




    }
}
