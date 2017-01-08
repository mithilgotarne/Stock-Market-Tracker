package com.example.mithildarshan.team_91.background;

import android.os.AsyncTask;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 7/1/17.
 */

public class FetchGraphData extends AsyncTask<String, Void, DataPoint[]> {

    public  Date[] XaxisDateValues;
    public  Double[] YaxisCloseValues;
    public DataPoint[] dataPoints;
    @Override
    protected DataPoint[] doInBackground(String... params) {
        XaxisDateValues = new Date[3700];
        YaxisCloseValues = new Double[3700];


        try {
            URL url = new URL("https://www.quandl.com/api/v3/datasets/NSE/"+params[0]+".json?api_key=C5pxGvrPME6FGd4xQB8N&start_date=2007-12-1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.getJSONObject("dataset").getJSONArray("data");

                String data = "";
                int j = 0;
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                dataPoints = new DataPoint[jsonArray.length()];
                for(int i=jsonArray.length()-1;i>=0;i--){
                    JSONArray array = jsonArray.getJSONArray(i);
                    data = data + " " + array.getString(0) + " " + array.getDouble(5) + "\n";
                    XaxisDateValues[j] = df.parse(array.getString(0));
                    YaxisCloseValues[j] = array.getDouble(5);
                    dataPoints[j] = new DataPoint(XaxisDateValues[j], YaxisCloseValues[j]);
                    j++;
                }

                for(int i=0;i<jsonArray.length();i++){
                    Log.d("FetchGraphData", dataPoints[i]  + "\n");
                }

                Log.d("FetchGraphData", data);

                return dataPoints;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
