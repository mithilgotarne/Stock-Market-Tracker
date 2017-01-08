package com.example.mithildarshan.team_91.background;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by mithishri on 1/7/2017.
 */

public class BackgroundService extends JobService {
    FetchDailyData fetchDailyData;

    @Override
    public boolean onStartJob(final JobParameters params) {

        /*SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int n = prefs.getInt("key", 0);
        editor.putInt("key", n+1);
        editor.apply();*/

        fetchDailyData = new FetchDailyData() {
            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(params, false);
            }
        };
        fetchDailyData.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (fetchDailyData != null) {
            fetchDailyData.cancel(true);
        }
        return true;
    }

}

