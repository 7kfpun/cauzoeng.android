package com.cauzoeng.android;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;


public class FormActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ID);
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
        submitFormButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(Constants.EVENT_TAG, "click send form");

                StringEntity json = null;
                try {
                    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    String macAddress = wm.getConnectionInfo().getMacAddress();
                    if ( macAddress == null || macAddress.isEmpty() ) {
                        macAddress = "FAKE_USER";
                    }

                    ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
                    RadioGroup radioConditionGroup = (RadioGroup) findViewById(R.id.radioCondition);
                    int selectedId = radioConditionGroup.getCheckedRadioButtonId();
                    RadioButton radioConditionButton = (RadioButton) findViewById(selectedId);

                    EditText textEmailAddress = (EditText)findViewById(R.id.textEmailAddress);
                    EditText textPhone = (EditText)findViewById(R.id.textPhone);
                    EditText textPostalAddress = (EditText)findViewById(R.id.editTextDescription);

                    JSONObject obj = new JSONObject();
                    obj.put("user", macAddress);

                    obj.put("title", textItem.getText().toString());
                    obj.put("price", textPrice.getText().toString());
                    obj.put("currency", spinnerCurrency.getSelectedItem().toString());
                    obj.put("description", textDescription.getText().toString());

                    obj.put("second_hand", toggleButton.isChecked());
                    obj.put("condition", radioConditionButton.getText().toString());
                    obj.put("available_days", 15);

                    /*obj.put("email", textEmailAddress.getText().toString());
                    obj.put("phone", textPhone.getText().toString());
                    obj.put("location", textPostalAddress.getText().toString());*/

                    json = new StringEntity(obj.toString());
                    Log.i(Constants.JSON_TAG, obj.toString());

                } catch (UnsupportedEncodingException e) {
                    Log.e(Constants.JSON_TAG, "UnsupportedEncodingException " + e.toString());
                } catch (JSONException e) {
                    Log.e(Constants.JSON_TAG, "Error parsing data " + e.toString());
                }

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(null, Constants.ITEM_API_URL, null, json, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        Log.e(Constants.HTTP_TAG, "New item failure: " + response.toString());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
