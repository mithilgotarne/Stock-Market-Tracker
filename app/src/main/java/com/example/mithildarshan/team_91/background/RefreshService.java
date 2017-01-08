package com.example.mithildarshan.team_91.background;

import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.mithildarshan.team_91.R;
import com.example.mithildarshan.team_91.model.Stock;

import io.realm.Realm;

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

                Realm realm = Realm.getDefaultInstance();

                for (Stock stock : realm.where(Stock.class).findAll()) {

                    if (stock.getHighPoint() > 0.0 && stock.getLowPoint() > 0.0) {

                        if (stock.getClose() > stock.getHighPoint()) {


                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(RefreshService.this)
                                            .setSmallIcon(R.drawable.ic_star_border_black_24dp)
                                            .setContentTitle("InvestoMaster")
                                            .setContentText(stock.getName() + " Price exceeded " + stock.getHighPoint());

                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            mNotificationManager.notify(0, mBuilder.build());
                        }

                        if (stock.getClose() < stock.getLowPoint()) {


                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(RefreshService.this)
                                            .setSmallIcon(R.drawable.ic_star_border_black_24dp)
                                            .setContentTitle("InvestoMaster")
                                            .setContentText(stock.getName() + " Price below " + stock.getLowPoint());

                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            mNotificationManager.notify(0, mBuilder.build());
                        }


                    }

                }
            }
        };

        refreshTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        refreshTask.cancel(true);
        return false;
    }
}
