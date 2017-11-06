package com.dev.toxa.integrate.ActivitySharing;

public class PresenterActivitySharing implements MVPactivitySharing.presenter {

//=================================Переменные==============================
    private MVPactivitySharing.view view;
//=========================================================================
    public PresenterActivitySharing (MVPactivitySharing.view view) {
        this.view = view;
    }


    @Override
    public void activityCreated(String action, String type) {
        String actionAndroidSend = "android.intent.action.SEND";
        String actionAndroidMultiple = "android.intent.action.SEND_MULTIPLE";
        if (actionAndroidSend.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String text = view.getSharedText();
                getSharedLink(text);
            } else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
            }
        } else if (actionAndroidMultiple.equals(action) && type != null) {
            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent);
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    private void getSharedLink(String sharedText) {
        if (sharedText != null) {
            String[] arr = sharedText.split(" ");
            if (arr[0].contains("http://") || arr[0].contains("https://") || arr[0].contains("ftp://") || arr[0].contains("ftp://")) {
                String link = "share_link////" + sharedText;
                ObservableShare observableShare = ObservableShare.getInstance();
                observableShare.setShareChanged(link);
            } else {
                String text = "share_text////" + sharedText;
                ObservableShare observableShare = ObservableShare.getInstance();
                observableShare.setShareChanged(text);
            }
        }
        view.finishActivity();
    }
}
