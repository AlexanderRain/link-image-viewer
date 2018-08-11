package com.example.b.activity.model.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.b.activity.utils.Constants.DELETE;
import static com.example.b.activity.utils.Constants.IMAGE_ACTION;
import static com.example.b.activity.utils.Constants.IMAGE_DATE;
import static com.example.b.activity.utils.Constants.IMAGE_ID;
import static com.example.b.activity.utils.Constants.IMAGE_STATUS;
import static com.example.b.activity.utils.Constants.IMAGE_URL;
import static com.example.b.activity.utils.Constants.INSERT;
import static com.example.b.activity.utils.Constants.RECEIVE;
import static com.example.b.activity.utils.Constants.UPDATE;

public class MainRepository {

    Context context;

    public MainRepository(Context context) {
        this.context = context;
    }

    public void insertImage(String imageUrl, int imageStatus) {
        Intent intent = new Intent(RECEIVE);

        intent.putExtra(IMAGE_ACTION, INSERT);
        intent.putExtra(IMAGE_URL, imageUrl);
        intent.putExtra(IMAGE_STATUS, imageStatus);
        intent.putExtra(IMAGE_DATE, getImageDate());

        context.sendBroadcast(intent);
        Log.e("Log", " SENDED INSERT");
    }

    public void updateImage(String imageUrl, int imageStatus) {
        Intent intent = new Intent(RECEIVE);

        intent.putExtra(IMAGE_ACTION, UPDATE);
        intent.putExtra(IMAGE_URL, imageUrl);
        intent.putExtra(IMAGE_STATUS, imageStatus);

        context.sendBroadcast(intent);
        Log.e("Log", " SENDED UPDATE");
    }

    public void deleteImage(String imageUrl, int imageStatus, long imageId) {
        Intent intent = new Intent(RECEIVE);

        intent.putExtra(IMAGE_ACTION, DELETE);
        intent.putExtra(IMAGE_URL, imageUrl);
        intent.putExtra(IMAGE_STATUS, imageStatus);
        intent.putExtra(IMAGE_ID, imageId);

        context.sendBroadcast(intent);
        Log.e("Log", " SENDED DELETE");
    }

    public String getImageDate() {
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String imageDate = df.format(today);
        return imageDate;
    }
}