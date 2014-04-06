package com.cauzoeng.android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class LotteryDescriptionActivity extends FragmentActivity {

    private static final String JSON_TAG = "JSON";
    private static final String HTTP_TAG = "HTTP";

    public final static String LOTTERY_BET_API_URL = "https://cauzoeng.appspot.com/_ah/api/lottery/v1/bet/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_LOTTERY);
        String subject = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_SUBJECT);
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_URL);
        Log.d("INTENT", "lottery id: " + id + ", subject: " + subject + ", url: " + url);

        setContentView(R.layout.activity_lottery);
        TextView subjectTextView = (TextView) findViewById(R.id.subject);
        subjectTextView.setText(subject);

        WebViewClient myWebClient = new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url)
            {
            // This line we let me load only pages inside "com" Webpage
            if ( url.contains("com") )
                //Load new URL Don't override URL Link
                return false;

            return true;
            }
        };

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(myWebClient);

        webView.loadUrl(url);

        /*Lottery.Builder builder = new Lottery.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        service = builder.build();*/

        Button clickPostButton = (Button) findViewById(R.id.bet_button);

        clickPostButton.setOnClickListener( new View.OnClickListener() {
            /* curl -X POST localhost:8080/_ah/api/lottery/v1/bet/
            -d '{"user": "", "lottery": ""}' -H "Content-Type: application/json""
             */

            @Override
            public void onClick(View v) {
                Log.i(HTTP_TAG, "Click post!!");
                /*HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("id", id);
                paramMap.put("user", "fake user");
                RequestParams params = new RequestParams(paramMap);*/

                StringEntity json = null;
                try {
                    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    String macAddress = wm.getConnectionInfo().getMacAddress();
                    String user = "FAKE_USER";
                    if (macAddress != null && !macAddress.isEmpty()) {
                        user = macAddress;
                    }

                    JSONObject obj = new JSONObject();
                    obj.put("lottery", id);
                    //obj.put("user", user);

                    json = new StringEntity(obj.toString());
                    Log.i(JSON_TAG, obj.toString());

                } catch (UnsupportedEncodingException e) {
                    Log.e(JSON_TAG, "UnsupportedEncodingException " + e.toString());
                } catch (JSONException e) {
                    Log.e(JSON_TAG, "Error parsing data " + e.toString());
                }

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(null, LOTTERY_BET_API_URL, null, json, "application/json", new AsyncHttpResponseHandler() {
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
            }
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
