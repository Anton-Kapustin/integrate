package com.dev.toxa.integrate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivitySharing extends AppCompatActivity {

    String LOG_TAG = "ActivitySharing";
    String actionToCommandService = "com.dev.toxa.integrate.CommnadService";
    String parameters = "parameters";

    Intent intentToCommandServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        intentToCommandServer = new Intent(actionToCommandService);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent);
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            String [] arr = sharedText.split(" ");
            intentToCommandServer.putExtra(parameters, "share");
            Bundle bundle = new Bundle();
            if (arr[0].contains("http://") || arr[0].contains("https://") || arr[0].contains("ftp://")) {
                Log.d(LOG_TAG, "link: " + sharedText);
                bundle.putString("link", sharedText);
//                intentToCommandServer.putExtra("data", sharedText);
            } else {
                Log.d(LOG_TAG, "text: " + sharedText);
                bundle.putString("text", sharedText);
//                intentToCommandServer.putExtra(parameters, "share_text");
//                intentToCommandServer.putExtra("data", sharedText);
            }
            intentToCommandServer.putExtras(bundle);
            sendBroadcast(intentToCommandServer);
        }
        Log.d(LOG_TAG, "finish");
        finish();

    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            Log.d(LOG_TAG, "Image");
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            Log.d(LOG_TAG, "Multiple");
        }
    }
    }


