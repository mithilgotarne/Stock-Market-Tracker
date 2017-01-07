package com.example.mithildarshan.team_91.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithildarshan.team_91.R;
import com.example.mithildarshan.team_91.model.Company;
import com.example.mithildarshan.team_91.model.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by root on 7/1/17.
 */

public class FetchDailyData extends AsyncTask<Void, Void, Void> {

    private Context c;
    public FetchDailyData(Context context){
        c = context;
    }
    private Realm realm;
    @Override
    protected Void doInBackground(Void... params) {

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Company.class);
                realm.delete(Record.class);
            }
        });
        //Toast.makeText(c, "Deleted successfully", Toast.LENGTH_SHORT).show();
        Log.d("FetchDaily", "Deleted");

        String[] tickers = {"MARUTI","INFY", "YESBANK", "HDFC", "TCS"};
        for(String ticker: tickers)
        {
            getData(ticker);

            Log.d("FD", ticker + " Added successfully");

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(c, "Done", Toast.LENGTH_SHORT).show();
        Log.d("FD"," Done");

    }

    public void getData(final String ticker){
        try {
            realm = Realm.getDefaultInstance();
            URL url = new URL("https://www.google.com/finance/getprices?q="+ticker+"&x=NSE&i=60&p=1d&f=d,c,v,o,h,l");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            final Company[] company = {null};
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Add a person
                    company[0] = realm.createObject(Company.class, ticker);

                }
            });

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                boolean check = false;
                long base = 0;
                //int timestamp = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.charAt(0) == 'a' || check){
                        String[] RowData = line.split(",");
                        String timestamp = RowData[0];
                         long tStamp;
                        if(timestamp.charAt(0) == 'a')
                        {
                            base = Long.parseLong(timestamp.substring(1, timestamp.length()));
                            tStamp = base;
                        }
                        else
                        {
                            tStamp = Integer.parseInt(timestamp)*(60) + base;
                        }
                        float close = Float.parseFloat(RowData[1]);
                        float high = Float.parseFloat(RowData[2]);
                        float low = Float.parseFloat(RowData[3]);
                        float open = Float.parseFloat(RowData[4]);
                        int volume = Integer.parseInt(RowData[5]);

                        InsertRecord(company[0], tStamp, close, high, low, open, volume, ticker);
                        stringBuilder.append(tStamp+" "+close+" "+high+" "+low+" "+open+" "+volume+" "+"\n");
                        check = true;

                    }

                }
                bufferedReader.close();


            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void InsertRecord(final Company company, final long timestamp, final float close, final float high,
                             final float low, final float open, final int volume, final String CompanyName){


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                Record record = realm.createObject(Record.class);
                record.setTimestamp(timestamp);
                record.setClose(close);
                record.setHigh(high);
                record.setLow(low);
                record.setOpen(open);
                record.setVolume(volume);
                record.setCompanyName(CompanyName);
                company.getRecords().add(record);
            }
        });

    }
}
