package com.cauzoeng.android;


import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by jp on 5/7/14.
 */
public class DownloadItemTask extends AsyncTask<String, Long, String> {
    protected String doInBackground(String... urls) {
        try {
            HttpRequest request =  HttpRequest.get(urls[0]);

            if (request.ok()) {
                return request.body();
            }
        } catch (HttpRequest.HttpRequestException exception) {
            Log.e(Constants.HTTP_TAG, "HttpRequestException: " + exception.toString());
            return null;
        }
        return "";
    }

    protected void onProgressUpdate(Long... progress) {
        Log.d("MyApp", "Downloaded bytes: " + progress[0]);
    }

    protected void onPostExecute(String string) {
        if (string != null)
            Log.d("MyApp", "Downloaded file to: " + string);
        else
            Log.d("MyApp", "Download failed");
    }
}
