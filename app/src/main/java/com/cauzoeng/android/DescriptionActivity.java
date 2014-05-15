package com.cauzoeng.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.koushikdutta.ion.Ion;


public class DescriptionActivity extends FragmentActivity {

    @InjectView(R.id.textTitle)         TextView titleTextView;
    @InjectView(R.id.textPrice)         TextView priceTextView;
    @InjectView(R.id.textCurrency)      TextView currencyTextView;
    @InjectView(R.id.textDescription)   TextView descriptionTextView;
    @InjectView(R.id.img)               ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.description);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String item_id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ITEM_ID);
        String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
        Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
        String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
        String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);
        Log.d("INTENT", "item_id: " + item_id + ", title: " + title);

        titleTextView.setText(title);
        priceTextView.setText(price.toString());
        currencyTextView.setText(currency);
        descriptionTextView.setText(description);

        String imageUrl = "http://img.1ting.com/images/album/0706/s300_20066574366.jpg";
        Ion.with(imageView)
                .placeholder(R.drawable.ic_launcher)
                .load(imageUrl);
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
            String item_id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ITEM_ID);
            String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
            Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
            String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
            String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);

            Intent new_intent = new Intent(this, FormActivity.class);
            new_intent.putExtra(Constants.EXTRA_MESSAGE_ITEM_ID, item_id);
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
