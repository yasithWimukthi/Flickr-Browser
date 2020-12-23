package com.androidstudio.java.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GetFlickrJsonData implements OnDownloadComplete{

    private static final String TAG = "GetFlickrJsonData";

    private final OnDataAvailable mCallback;
    private List<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;

    public GetFlickrJsonData(OnDataAvailable callback, String baseUrl, String language, boolean matchAll) {
        mCallback = callback;
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
    }

    void executeOnSameThread (String searchCriteria){
        Log.d(TAG, "executeOnSameThread: start");
        String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: end");
    }

    private String createUri(String searchCriteria, String language, boolean matchAll) {
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode",matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang",language)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if (status == DownloadStatus.OK){
            mPhotoList = new ArrayList<Photo>();

            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length();i++){
                    JSONObject photo = itemsArray.getJSONObject(i);
                    String title = photo.getString("title");
                    String author = photo.getString("author");
                    String AuthorID = photo.getString("author_id");
                    String tags = photo.getString("tags");

                    JSONObject media = photo.optJSONObject("media");
                    String photoUrl = media.getString("m");

                    String link = photoUrl.replaceFirst("_m.","_b.");

                    Photo photoObject = new Photo(title,author,AuthorID,link,tags,photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Jsond data " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (mCallback != null){
            mCallback.onDataAvailable(mPhotoList,status);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
