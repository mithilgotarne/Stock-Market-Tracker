package com.example.mithildarshan.team_91.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.mithildarshan.team_91.R;
import com.example.mithildarshan.team_91.model.Stock;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;


/**
 * Created by mithishri on 1/7/2017.
 */

public class StockAdapter extends RealmBaseAdapter<Stock> implements ListAdapter {

    private static class ViewHolder {

        TextView stockName;
        TextView stockOpen;
        TextView stockClose;
        TextView stockVolume;
        TextView stockChange;
        ImageView stockfav;
    }

    public StockAdapter(Context context, OrderedRealmCollection<Stock> realmResults) {
        super(context, realmResults);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stock_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stockName = (TextView) convertView.findViewById(R.id.stock_name_textview);
            viewHolder.stockOpen = (TextView) convertView.findViewById(R.id.stock_open_textview);
            viewHolder.stockClose = (TextView) convertView.findViewById(R.id.stock_close_textview);
            viewHolder.stockVolume = (TextView) convertView.findViewById(R.id.stock_volume_textview);
            viewHolder.stockChange = (TextView) convertView.findViewById(R.id.stock_change_textview);
            viewHolder.stockfav = (ImageView) convertView.findViewById(R.id.stock_fav_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Stock stock = adapterData.get(position);
        viewHolder.stockName.setText(String.valueOf(stock.getName()));
        viewHolder.stockVolume.setText("Volume: " + String.valueOf(stock.getVolume()));
        viewHolder.stockOpen.setText(String.format("Open : %.2f", stock.getOpen()));
        viewHolder.stockClose.setText(String.format("Close : %.2f", stock.getClose()));
        if (stock.isFavourite())
            viewHolder.stockfav.setVisibility(View.VISIBLE);

        GradientDrawable magnitudeCircle = (GradientDrawable) viewHolder.stockChange.getBackground();

        if (stock.getOpen() < stock.getClose()) {
            magnitudeCircle.setColor(ContextCompat.getColor(context, R.color.positiveDiff));
            viewHolder.stockChange.setText(String.format("%.1f", stock.getClose() - stock.getOpen()));
        } else {
            magnitudeCircle.setColor(ContextCompat.getColor(context, R.color.negDiff));
            viewHolder.stockChange.setText(String.format("%.1f", stock.getOpen() - stock.getClose()));
        }


        return convertView;
    }
}
