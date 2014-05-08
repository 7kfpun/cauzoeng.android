package com.cauzoeng.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class FormActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ITEM_ID);
        String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
        Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
        String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);

        final EditText textItem = (EditText)findViewById(R.id.editTextItem);
        final EditText textPrice = (EditText)findViewById(R.id.editTextPrice);
        final Spinner spinnerCurrency = (Spinner)findViewById(R.id.spinnerCurrency);
        final EditText textDescription = (EditText)findViewById(R.id.editTextDescription);

        textItem.setText(title);
        textPrice.setText(price.toString());
        textDescription.setText(description);

        Button submitFormButton = (Button) findViewById(R.id.button);
        submitFormButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(Constants.EVENT_TAG, "click send form");

                String user = Utils.getMacAddress(getSystemService(Context.WIFI_SERVICE));

                ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
                RadioGroup radioConditionGroup = (RadioGroup) findViewById(R.id.radioCondition);
                int selectedId = radioConditionGroup.getCheckedRadioButtonId();
                RadioButton radioConditionButton = (RadioButton) findViewById(selectedId);

                EditText textEmailAddress = (EditText)findViewById(R.id.textEmailAddress);
                EditText textPhone = (EditText)findViewById(R.id.textPhone);
                EditText textPostalAddress = (EditText)findViewById(R.id.editTextDescription);

                JsonObject json = new JsonObject();

                if (id != "") {
                    json.addProperty("id", id);
                }

                json.addProperty("user", user);

                json.addProperty("title", textItem.getText().toString());
                json.addProperty("price", textPrice.getText().toString());
                json.addProperty("currency", spinnerCurrency.getSelectedItem().toString());
                json.addProperty("description", textDescription.getText().toString());

                json.addProperty("second_hand", toggleButton.isChecked());
                json.addProperty("condition", radioConditionButton.getText().toString());
                json.addProperty("available_days", 15);

                Double[] lat_lon = Utils.getGpsLocation(getSystemService(Context.LOCATION_SERVICE));
                if (lat_lon[0] != -1 && lat_lon[1] != -1) {
                    JsonObject location = new JsonObject();
                    location.addProperty("lat", lat_lon[0]);
                    location.addProperty("lon", lat_lon[1]);
                    json.add("location", location);
                }

                Ion.with(getBaseContext())
                        .load(Constants.ITEM_API_URL)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });

        Button gpsButton = (Button) findViewById(R.id.button_gps);
        gpsButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textGps = (TextView)findViewById(R.id.textGps);

                Double[] lat_lon = Utils.getGpsLocation(getSystemService(LOCATION_SERVICE));
                textGps.setText("lat: " + lat_lon[0] + " lon: " + lat_lon[1]);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
