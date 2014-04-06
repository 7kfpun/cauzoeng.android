package com.cauzoeng.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;


public class FormActivity extends FragmentActivity {

    private static final String JSON_TAG = "JSON";
    private static final String HTTP_TAG = "HTTP";

    public final static String LOTTERY_BET_API_URL = "https://cauzoeng.appspot.com/_ah/api/lottery/v1/bet/";

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
