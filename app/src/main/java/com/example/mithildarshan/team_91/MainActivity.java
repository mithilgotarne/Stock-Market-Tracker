package com.example.mithildarshan.team_91;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mithildarshan.team_91.adapter.StockAdapter;
import com.example.mithildarshan.team_91.background.BackgroundService;
import com.example.mithildarshan.team_91.background.FetchDailyData;
import com.example.mithildarshan.team_91.background.RefreshService;
import com.example.mithildarshan.team_91.background.RefreshTask;
import com.example.mithildarshan.team_91.model.Stock;
import com.example.mithildarshan.team_91.util.Utils;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mithildarshan.team_91.util.Utils.KEY_FIRST_START;
import static com.example.mithildarshan.team_91.util.Utils.KEY_REF_RATE;
import static com.example.mithildarshan.team_91.util.Utils.PREFS;

public class MainActivity extends AppCompatActivity {

    //private TextView txt;
    private Realm realm;

    private static final int JOB_ID = 100;
    private SharedPreferences prefs;
    private JobScheduler mJobScheduler;
    private RealmResults<Stock> stocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        checkFirstStart();
        //realm = Realm.getDefaultInstance();
        //new FetchDailyData(this).execute();

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


        //
        //constructJob();

       /* SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int n = prefs.getInt("key", 0);*/

        //test.setText("Jobscheduler triggerd " + n + " times");

        //constructJob();

        Realm realm = Realm.getDefaultInstance();
        stocks = realm.where(Stock.class).findAll();
        StockAdapter stockAdapter = new StockAdapter(MainActivity.this, stocks);
        ListView listView = (ListView) findViewById(R.id.stock_listview);
        listView.setAdapter(stockAdapter);

        createRefreshJob(prefs.getInt(KEY_REF_RATE, 1));

    }

    private void checkFirstStart() {

        if (!prefs.contains(KEY_FIRST_START)) {

            new FetchDailyData() {
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    prefs.edit().putBoolean(KEY_FIRST_START, false).apply();
                }
            }.execute();

            constructJob();
        }
    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, BackgroundService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(24 * 60 * 60 * 1000);
        mJobScheduler.schedule(builder.build());
    }

    private void createRefreshJob(int refreshRate) {
        JobInfo.Builder builder = new JobInfo.Builder(200, new ComponentName(this, RefreshService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(refreshRate * 60 * 1000);
        mJobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                if (Utils.isMarketClosed()) {

                    Toast.makeText(MainActivity.this, "Market Closed", Toast.LENGTH_SHORT).show();
                    return true;

                }

                new RefreshTask() {
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                    }
                }.execute();

                return true;
            case R.id.action_refresh_rate:
                createDialog();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Enter Refresh Rate in minutes");
        alert.setTitle("Refresh Rate");

        int min = prefs.getInt(KEY_REF_RATE, 1);


        View view = getLayoutInflater().inflate(R.layout.dialogedit, null);
        final EditText edittext = (EditText) view.findViewById(R.id.rate_edittext);
        edittext.setText(String.valueOf(min));

        alert.setView(view);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                if (!TextUtils.isEmpty(YouEditTextValue)) {
                    if (TextUtils.isDigitsOnly(YouEditTextValue)) {
                        if (Integer.parseInt(YouEditTextValue) > 1) {
                            prefs.edit().putInt(KEY_REF_RATE, Integer.parseInt(YouEditTextValue)).apply();
                            createRefreshJob(Integer.parseInt(YouEditTextValue));
                        } else

                            Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

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
