package com.androidstudio.java.flickrbrowser;

import java.util.List;

public interface OnDataAvailable {
    void onDataAvailable(List<Photo> data, DownloadStatus status);
}
