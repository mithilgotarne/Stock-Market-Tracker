package com.example.mithildarshan.team_91.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

import com.example.mithildarshan.team_91.util.Utils;

/**
 * Created by mithishri on 1/7/2017.
 */

public class RefreshService extends JobService {
    RefreshTask refreshTask;

    @Override
    public boolean onStartJob(final JobParameters params) {

        refreshTask = new RefreshTask() {
            @Override
            protected void onPostExecute(Boolean success) {

                Toast.makeText(RefreshService.this, "Refreshing Data...", Toast.LENGTH_SHORT).show();
                jobFinished(params, !success);
            }
        };

        if (!Utils.isMarketClosed()) {

            refreshTask.execute();
        }


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        refreshTask.cancel(true);
        return false;
    }
}
