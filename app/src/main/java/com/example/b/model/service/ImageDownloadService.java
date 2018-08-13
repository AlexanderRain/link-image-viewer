package com.example.b.model.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.b.utils.Constants;

import java.io.File;
import java.io.IOException;

public class ImageDownloadService extends IntentService {

    public ImageDownloadService() {
        super(Constants.SERVICE_NAME);
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String imageUrl = intent.getStringExtra(Constants.IMAGE_URL);
        file_download(imageUrl);
    }

    public void file_download(String imageUrl) {
        File file = new File(Environment.getExternalStorageDirectory() + Constants.PATH);

        if (file.exists() && file.isDirectory()) {
            DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(imageUrl);

            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir(Constants.PATH, Constants.JPG);

            downloadManager.enqueue(request);

        } else {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                Log.e("Log",  "Exception " + ioe);
            }

            DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(imageUrl);

            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir(Constants.PATH, Constants.JPG);

            downloadManager.enqueue(request);
        }
    }

    public void onDestroy () {
        super.onDestroy();
    }

}