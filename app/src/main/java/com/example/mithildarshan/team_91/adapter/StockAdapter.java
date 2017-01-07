package com.example.mithildarshan.team_91.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView stockHigh;
        TextView stockLow;
        TextView stockVolume;

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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Stock stock = adapterData.get(position);
        viewHolder.stockName.setText(stock.getName());
        return convertView;
    }
}
