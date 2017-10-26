package com.dev.toxa.integrate.FragmentListServers;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.MainActivity.MainActivity;
import com.dev.toxa.integrate.R;

import static android.content.Context.BIND_AUTO_CREATE;


public class FragmentListServers extends Fragment implements MVPfragmentListServers.view,  ActivityCompat.OnRequestPermissionsResultCallback {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    String actionReceive = "com.dev.toxa.integrate.FragmentListServers";
    String actionToMainService = "com.dev.toxa.integrate.Services.MainService";
    String getActionToFragmentConnectToServer = "com.dev.toxa.integrate.FragmentConnectToServer";
    String IP = null;
    String parameters = "parameters";

    CallbackToActivity callbackToActivity;

    Intent intentSearchServer;

    Context context;

    boolean serviceBound = false;

    ServiceConnection serviceConnection;
    ServiceFindServers serviceFindServers;

    TextView text_listServers;
    TextView text_serverName;
    ImageView imageView_distr;
    LinearLayout layout_listServers;

    MainActivity activity;

    PresenterFragmentListServers presenter;

    public static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    //==================================================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity = (MainActivity) getActivity();
        presenter = activity.setFragmentListServers(this);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                serviceFindServers = ((ServiceFindServers.ServiceFindBinder) iBinder).getService();
                if (!serviceFindServers.getConnection()) {
                    serviceFindServers.setPresenter(presenter);
                    serviceFindServers.searchingServers();
                }
                serviceBound = true;
                Log.d(LOG_TAG, "Подключение к сервису поиска серверов установлено");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceBound = false;
                Log.d(LOG_TAG, "Сервис поиска отключен");
            }
        };
        context = this.getContext();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        View rootView = inflater.inflate(R.layout.fragment_layout_list_servers, container, false);
        View fragment_connectToServer = inflater.inflate(R.layout.fragment_layout_connect_to_server, container, false);
        text_serverName = (TextView) fragment_connectToServer.findViewById(R.id.text_serverName);
        imageView_distr = (ImageView) fragment_connectToServer.findViewById(R.id.imageView_distr);
        text_listServers = (TextView) rootView.findViewById(R.id.text_servers);
        layout_listServers = (LinearLayout) rootView.findViewById(R.id.layout_listServers);
        intentSearchServer = new Intent(context, ServiceFindServers.class);
        return rootView;
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        presenter.clearData();
        super.onDestroy();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void displayFoundServer(final String IP, final int ID, final String serverName, final String distr) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                final Button buttonSrv = new Button(context);
                buttonSrv.setLayoutParams(params);
                buttonSrv.setId(ID);
                buttonSrv.setTag(IP + "," + distr);
                Log.d(LOG_TAG, "Текст кнопки " + serverName);
                buttonSrv.setText(serverName);
                layout_listServers.addView(buttonSrv);

                buttonSrv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
                        presenter.buttonServerClicked((String) buttonSrv.getTag(), (String) buttonSrv.getText());
                    }
                });
            }
        });
        presenter.fragmentLoaded();
    }

    @Override
    public void bindSearchService() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        context.bindService(intentSearchServer, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void unBindSearchService() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (serviceBound) {
            serviceFindServers.setPresenter(null);
            context.unbindService(serviceConnection);
            Log.d(LOG_TAG, "Сервис отключен");
        } else {
            Log.d(LOG_TAG, "Сервис не запущен");
        }
    }

    @Override
    public void sendIPtoConnectFragment(final String IP, final String serverName, final String distr) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        callbackToActivity = (CallbackToActivity) activity;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callbackToActivity.connectToServer(IP, serverName, distr);
            }
        });
    }



    public interface CallbackToActivity {
        void connectToServer(String IP, String serverName, String distr);
    }

}
