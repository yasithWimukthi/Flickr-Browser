package com.androidstudio.java.flickrbrowser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnDownloadComplete {

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("https://www.flickr.com/services/feeds/photos_public.gne?tags=android&tagmode=any&format=json&nojsoncallback=1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        if (status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete: data is "+ data);
        }
        else{
            Log.d(TAG, "onDownloadComplete: Failed with status "+status);
        }
    }
}