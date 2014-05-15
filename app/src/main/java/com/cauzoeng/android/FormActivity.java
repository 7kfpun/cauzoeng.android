package com.cauzoeng.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Arrays;
import java.util.List;


public class FormActivity extends FragmentActivity {

    @InjectView(R.id.button)                    Button submitFormButton;
    @InjectView(R.id.editTextItem)              EditText editTextItem;
    @InjectView(R.id.editTextPrice)             EditText editTextPrice;
    @InjectView(R.id.spinnerCurrency)           Spinner spinnerCurrency;
    @InjectView(R.id.editTextDescription)       EditText editTextDescription;
    @InjectView(R.id.editTextEmailAddress)      EditText textEmailAddress;
    @InjectView(R.id.editTextPhone)             EditText textPhone;

    @InjectView(R.id.button_gps)                Button gpsButton;

    @InjectView(R.id.toggleButton)              ToggleButton toggleButton;
    @InjectView(R.id.radioGroupCondition)       RadioGroup radioConditionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ITEM_ID);
        String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
        Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
        String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
        String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);

        editTextItem.setText(title);
        editTextPrice.setText(price.toString());
        editTextDescription.setText(description);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currency_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        final String[] currency_arrays = getResources().getStringArray(R.array.currency_arrays);
        List<String> currency_arraysList = Arrays.asList(currency_arrays);

        if (currency != null) {
            int position = currency_arraysList.indexOf(currency);
            if (position != -1) spinnerCurrency.setSelection(position);
            Log.d(Constants.MESSAGE_TAG, "Currency: " + currency);
            Log.d(Constants.MESSAGE_TAG, "Currency position: " + position);
        } else {
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String defaultCurrency = sharedPrefs.getString("defaultCurrency", "NONE");
            Log.d(Constants.MESSAGE_TAG, "Default currency: " + defaultCurrency);

            int position = currency_arraysList.indexOf(defaultCurrency);
            if (position != -1) spinnerCurrency.setSelection(position);
            Log.d(Constants.MESSAGE_TAG, "Default currency position: " + position);
        }

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
            {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultCurrency", currency_arrays[position]);
                editor.commit();

                Log.d(Constants.EVENT_TAG, "Currency selected");
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                Log.d(Constants.EVENT_TAG, "Nothing selected");
            }
        });

        submitFormButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(Constants.EVENT_TAG, "click send form");

                String user = Utils.getMacAddress(getSystemService(Context.WIFI_SERVICE));

                int selectedId = radioConditionGroup.getCheckedRadioButtonId();
                RadioButton radioConditionButton = (RadioButton) findViewById(selectedId);

                final JsonObject json = new JsonObject();

                if (id != "") {
                    json.addProperty("id", id);
                }

                json.addProperty("user", user);

                json.addProperty("title", editTextItem.getText().toString());
                json.addProperty("price", editTextPrice.getText().toString());
                json.addProperty("currency", spinnerCurrency.getSelectedItem().toString());
                json.addProperty("description", editTextDescription.getText().toString());

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
                                Log.i(Constants.HTTP_TAG, "Form sent: " + json.toString());
                                // close the activity
                                finish();
                            }
                        });
            }
        });

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
