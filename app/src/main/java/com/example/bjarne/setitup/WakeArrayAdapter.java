package com.example.bjarne.setitup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bjarne on 10/26/15.
 */
public class WakeArrayAdapter extends ArrayAdapter<Wake> {

    private LayoutInflater inflater;

    public WakeArrayAdapter(Context context, List<Wake> wakeList) {
        super(context, R.layout.simple_row, R.id.rowTextView, wakeList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Wake to display
        Wake wake = this.getItem(position);

        TextView messageView;
        TextView wakeView;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.simple_row, null);

            // Find the child views
            messageView = (TextView)convertView.findViewById(R.id.rowTextView);
            wakeView = (TextView)convertView.findViewById(R.id.subRowTextView);

            // Optimization: Tag the row with it's child views
            convertView.setTag(new WakeViewHolder(messageView, wakeView));
        }
        else {
            WakeViewHolder viewHolder = (WakeViewHolder)convertView.getTag();
            messageView = viewHolder.getMessageView();
            wakeView = viewHolder.getWakeView();
        }

        messageView.setText(wake.getSetMessage());
        wakeView.setText(wake.getWakeTime());

        return convertView;
    }
}