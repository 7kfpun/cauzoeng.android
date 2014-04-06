package com.cauzoeng.android;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PreviousList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] subjects;
    private final String[] descriptions;
    private final String[] finish_dates;
    private final String[] winners;

    public PreviousList(Activity context,
                        String[] subjects, String[] descriptions,
                        String[] finish_dates, String[] winners) {
        super(context, R.layout.listview_current_item, subjects);
        this.context = context;

        this.subjects = subjects;
        this.descriptions = descriptions;
        this.finish_dates = finish_dates;
        this.winners = winners;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_previous_item, null, true);
        TextView txtSubject = (TextView) rowView.findViewById(R.id.subject);
        TextView txtYouWin = (TextView) rowView.findViewById(R.id.you_win);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);
        TextView txtFinishDate = (TextView) rowView.findViewById(R.id.finish_date);
        TextView txtWinner = (TextView) rowView.findViewById(R.id.winner);

        txtSubject.setText(subjects[position]);
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wm.getConnectionInfo().getMacAddress();
        if ( macAddress == null || macAddress.isEmpty() ) {
            macAddress = "FAKE_USER";
        }

        if ( macAddress.equals(winners[position]) ) {
            txtYouWin.setText("Lucky you!");
            Log.i("WINNER", macAddress + " " + winners[position] + " wins " + subjects[position]);
        } else {
            txtYouWin.setText("");
        }
        txtDescription.setText(descriptions[position]);
        txtFinishDate.setText(finish_dates[position].split("T")[0]);
        txtWinner.setText("Winner from " + "Causeway bay");
        return rowView;
    }
}