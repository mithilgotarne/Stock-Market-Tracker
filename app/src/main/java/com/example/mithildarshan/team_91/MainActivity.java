package com.example.mithildarshan.team_91;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mithildarshan.team_91.adapter.StockAdapter;
import com.example.mithildarshan.team_91.background.BackgroundService;
import com.example.mithildarshan.team_91.background.FetchDailyData;
import com.example.mithildarshan.team_91.model.Company;
import com.example.mithildarshan.team_91.model.Record;
import com.example.mithildarshan.team_91.model.Stock;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    //private TextView txt;
    private Realm realm;

    private static final int JOB_ID = 100;
    private JobScheduler mJobScheduler;
    private RealmResults<Stock> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //realm = Realm.getDefaultInstance();
        new FetchDailyData(this).execute();

        /*txt = (TextView) findViewById(R.id.text_view);
        txt.setText(" ");
        for (Company company : realm.where(Company.class).findAll()) {

            txt.append(company.getName());
            RealmList<Record> records = company.getRecords();
            int cnt = 0;
            for (Record rec : records) {
                if (cnt > 10) break;
                txt.append("\n" + rec.getTimestamp() + ":" + rec.getClose() + "\n");
                cnt++;
            }
        }*/

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


       // mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        //constructJob();

       /* SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int n = prefs.getInt("key", 0);*/

        //test.setText("Jobscheduler triggerd " + n + " times");

        //constructJob();

        /*Realm realm = Realm.getDefaultInstance();

        stocks = realm.where(Stock.class).findAll();

        StockAdapter stockAdapter = new StockAdapter(MainActivity.this, stocks);

        ListView listView = (ListView) findViewById(R.id.stock_listview);
        listView.setAdapter(stockAdapter);
*/
    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, BackgroundService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setPeriodic(1000).setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    /*private void getData() {

        TextView test = (TextView) findViewById(R.id.test);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < stocks.size(); i++) {

            stringBuilder.append(stocks.get(i).getTimestamp()).append(" stock ").append(stocks.get(i).getVolume());
            stringBuilder.append("\n");
        }

        test.setText(stringBuilder.toString());
    }*/
}
