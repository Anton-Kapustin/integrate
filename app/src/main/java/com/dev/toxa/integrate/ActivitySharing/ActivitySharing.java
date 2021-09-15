package com.dev.toxa.integrate.ActivitySharing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.R;

import java.util.ArrayList;

public class ActivitySharing extends AppCompatActivity implements MVPactivitySharing.view {

    //===========================================Переменнные============================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    PresenterActivitySharing presenter;
    Intent intentShare;

    //==================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        presenter = new PresenterActivitySharing(this);
        intentShare = getIntent();
        String action = intentShare.getAction();
        String type = intentShare.getType();
        presenter.activityCreated(action, type);

    }

    @Override
    public String getSharedText() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        String sharedText = intentShare.getStringExtra(Intent.EXTRA_TEXT);
        return sharedText;
    }

    @Override
    public void handleSendImage() {
        Uri imageUri = (Uri) intentShare.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            Log.d(LOG_TAG, "Image");
        }
    }

    @Override
    public void handleSendMultipleImages() {
        ArrayList<Uri> imageUris = intentShare.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            Log.d(LOG_TAG, "Multiple");
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

}


