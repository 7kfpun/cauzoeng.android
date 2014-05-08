package com.cauzoeng.android;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;


public class DescriptionActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        Intent intent = getIntent();
        String item_id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ID);
        String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
        Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
        String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
        String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);
        Log.d("INTENT", "item_id: " + item_id + ", title: " + title);

        setContentView(R.layout.description);
        TextView titleTextView = (TextView) findViewById(R.id.textTitle);
        titleTextView.setText(title);
        TextView priceTextView = (TextView) findViewById(R.id.textPrice);
        priceTextView.setText(price.toString());
        TextView currencyTextView = (TextView) findViewById(R.id.textCurrency);
        currencyTextView.setText(currency);
        TextView descriptionTextView = (TextView) findViewById(R.id.textDescription);
        descriptionTextView.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent intent = getIntent();
            String item_id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ID);
            String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
            Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
            String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
            String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);

            Intent new_intent = new Intent(this, FormActivity.class);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_ID, item_id);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_TITLE, title);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_PRICE, price);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_CURRENCY, currency);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_DESCRIPTION, description);
            this.startActivity(new_intent);
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
