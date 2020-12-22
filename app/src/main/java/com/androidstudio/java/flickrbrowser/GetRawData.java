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
    private static final String LOG_TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    public GetRawData(OnDownloadComplete callback) {
        mCallback = callback;
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

            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch( MalformedURLException e){
            e.printStackTrace();
        }catch(IOException | SecurityException e){
            e.printStackTrace();
        }
        finally {
            if (connection != null){
                connection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                }catch ( IOException e){
                    Log.e(LOG_TAG, "doInBackground : error closing stream "+e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        if (mCallback != null) {
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
    }
}
