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

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class LotteryDescriptionActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_lottery);
        TextView dummyTextView = (TextView) findViewById(R.id.hello);
        dummyTextView.setText("I am a hello text");

        Intent intent = getIntent();
        final String id = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_LOTTERY);
        String url = intent.getStringExtra(MainActivity.EXTRA_MESSAGE_URL);
        Log.d("INTENT", "lottery id: " + id + ", url: " + url);

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

        /*WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        dummyTextView.setText("MAC addr.......: " + wm.getConnectionInfo().getMacAddress());*/

        clickPostButton.setOnClickListener( new View.OnClickListener() {
            /* curl -X POST localhost:8080/_ah/api/lottery/v1/bet/
            -d '{"user": "", "lottery": ""}' -H "Content-Type: application/json""
             */

            @Override
            public void onClick(View v) {
                Log.i("POST BET", "Click post!!");
                /*HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("id", id);
                paramMap.put("user", "fake user");
                RequestParams params = new RequestParams(paramMap);*/

                StringEntity json = null;
                try {
                    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    String mac_address = wm.getConnectionInfo().getMacAddress();
                    String user = "FAKE_USER";
                    if (mac_address != null && !mac_address.isEmpty()) {
                        user = mac_address;
                    }

                    JSONObject obj = new JSONObject();
                    obj.put("lottery", id);
                    obj.put("user", user);

                    json = new StringEntity(obj.toString());
                    Log.i("JSON Parser", obj.toString());

                } catch (UnsupportedEncodingException e) {
                    Log.e("JSON Parser", "UnsupportedEncodingException " + e.toString());
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(null, "https://cauzoeng.appspot.com/_ah/api/lottery/v1/bet/", null, json, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("POST BET", "Post http" + response);
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
