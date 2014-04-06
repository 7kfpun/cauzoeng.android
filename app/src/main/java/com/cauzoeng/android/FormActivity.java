package com.cauzoeng.android;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class FormActivity extends FragmentActivity {

    private static final String JSON_TAG = "JSON";
    private static final String HTTP_TAG = "HTTP";
    private static final String EVENT_TAG = "EVENT";

    public final static String LOTTERY_USER_API_URL = "https://cauzoeng.appspot.com/_ah/api/lottery/v1/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_form);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_LOTTERY);
        String subject = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_SUBJECT);
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_URL);
        Log.d("INTENT", "lottery id: " + id + ", subject: " + subject + ", url: " + url);

        TextView subjectTextView = (TextView) findViewById(R.id.subject);
        subjectTextView.setText(subject);

        Button clickPopupFormButton = (Button) findViewById(R.id.button);
        clickPopupFormButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(EVENT_TAG, "click send form");

                StringEntity json = null;
                try {
                    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    String macAddress = wm.getConnectionInfo().getMacAddress();
                    if ( macAddress == null || macAddress.isEmpty() ) {
                        macAddress = "FAKE_USER";
                    }

                    EditText textPersonName = (EditText)findViewById(R.id.textPersonName);
                    EditText textEmailAddress = (EditText)findViewById(R.id.textEmailAddress);
                    EditText textPhone = (EditText)findViewById(R.id.textPhone);
                    EditText textPostalAddress = (EditText)findViewById(R.id.textPostalAddress);

                    JSONObject obj = new JSONObject();
                    obj.put("mac", macAddress);
                    obj.put("name", textPersonName.getText().toString());
                    obj.put("email", textEmailAddress.getText().toString());
                    obj.put("phone", textPhone.getText().toString());
                    obj.put("address", textPostalAddress.getText().toString());

                    json = new StringEntity(obj.toString());
                    Log.i(JSON_TAG, obj.toString());

                } catch (UnsupportedEncodingException e) {
                    Log.e(JSON_TAG, "UnsupportedEncodingException " + e.toString());
                } catch (JSONException e) {
                    Log.e(JSON_TAG, "Error parsing data " + e.toString());
                }

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(null, LOTTERY_USER_API_URL, null, json, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable e, String response) {
                        Log.e(HTTP_TAG, "Bet failure: " + response.toString());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            };
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
