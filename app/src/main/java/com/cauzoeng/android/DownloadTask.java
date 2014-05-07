package com.cauzoeng.android;

import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by jp on 5/7/14.
 */
public class DownloadTask extends AsyncTask<String, Long, File> {
    protected File doInBackground(String... urls) {
        try {
            HttpRequest request =  HttpRequest.get(urls[0]);

            File file = null;
            if (request.ok()) {
                try {
                    file = File.createTempFile("download", ".tmp");
                    request.receive(file);
                    publishProgress(file.length());
                } catch (IOException t) {
                    Log.e(Constants.FILE_TAG, "Could not create file" + t.toString());
                }
            }
            return file;
        } catch (HttpRequest.HttpRequestException exception) {
            Log.e(Constants.HTTP_TAG, "HttpRequestException: " + exception.toString());
            return null;
        }
    }

    protected void onProgressUpdate(Long... progress) {
        Log.d("MyApp", "Downloaded bytes: " + progress[0]);
    }

    protected void onPostExecute(File file) {
        if (file != null)
            Log.d("MyApp", "Downloaded file to: " + file.getAbsolutePath());
        else
            Log.d("MyApp", "Download failed");
    }
}
