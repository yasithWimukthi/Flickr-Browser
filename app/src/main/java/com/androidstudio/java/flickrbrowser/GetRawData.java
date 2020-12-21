package com.androidstudio.java.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE,PROCESSING,NOT_INITIALIZED,FAILED_OR_EMPTY,OK}

public class GetRawData extends AsyncTask<String,Void,String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;

    public GetRawData() {
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(urls == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();

            Log.d(TAG, "doInBackground : Response code is " + response);
            StringBuilder result = new StringBuilder();
            reader = new  BufferedReader(new InputStreamReader(connection.getInputStream()));

        }catch( MalformedURLException e){
            e.printStackTrace();
        }catch(IOException | SecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}