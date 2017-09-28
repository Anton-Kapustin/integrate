package layout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dev.toxa.integrate.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentListServers extends Fragment {

    String LOG_TAG = "FragmentListServers";

    String actionReceive = "com.dev.toxa.integrate.FragmentListServers";
    String actionToMainService = "com.dev.toxa.integrate.MainService";
    String getActionToFragmentConnectToServer = "com.dev.toxa.integrate.FragmentConnectToServer";
    String IP = null;
    String parameters = "parameters";

    MainBroadcastReceiver receiver = new MainBroadcastReceiver();
    List<String> serversIP = new ArrayList();

    TextView text_listServers;
    TextView text_serverName;
    ImageView imageView_distr;
    LinearLayout layout_listServers;

    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = getActivity();
        activity.registerReceiver(receiver, new IntentFilter(actionReceive));
        serversIP.add("0");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_list_servers, container, false);
        View fragment_connectToServer = inflater.inflate(R.layout.fragment_layout_connect_to_server, container, false);
        text_serverName = (TextView) fragment_connectToServer.findViewById(R.id.text_serverName);
        imageView_distr = (ImageView) fragment_connectToServer.findViewById(R.id.imageView_distr);
        text_listServers = (TextView) rootView.findViewById(R.id.text_servers);
        layout_listServers = (LinearLayout) rootView.findViewById(R.id.layout_listServers);

        return rootView;
    }

    @Override
    public void onPause() {
        activity.unregisterReceiver(receiver);
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    public void onResume() {
        activity.registerReceiver(receiver, new IntentFilter(actionReceive));
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(receiver);
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
    public void uiFindServers(Intent intentServers) {
        Context context = getContext();
        final Drawable distr_logo;
        final String serverAddress = intentServers.getStringExtra("serverAddress");
        final String serverName = intentServers.getStringExtra("serverName");
        final String distr = intentServers.getStringExtra("distr");
        Log.d(LOG_TAG, "distr: " + distr);
        if (distr.contains("Debian")) {
            Log.d(LOG_TAG, "LOGO: " + distr);
            distr_logo = context.getResources().getDrawable(R.drawable.debian, context.getTheme());
        } else {
            distr_logo = context.getResources().getDrawable(R.drawable.debian, context.getTheme());
        }
        if (!serversIP.contains(serverAddress)){
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            final Button buttonSrv = new Button(context);
            buttonSrv.setLayoutParams(params);
            if (serversIP.get(0).equals("0")) {
                serversIP.set(0, serverAddress);
            } else {
                serversIP.add(serverAddress);
            }
            int id = serversIP.size();
            buttonSrv.setId(id);
            buttonSrv.setText(serverName);
            layout_listServers.addView(buttonSrv);
            IP = serverAddress;

            buttonSrv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text_serverName.setText(serverName);
                    Intent intent = new Intent(getActionToFragmentConnectToServer);
                    intent.putExtra(parameters, "distr");
                    intent.putExtra("distro", distr);
                    intent.putExtra("PCName", serverName);
                    activity.sendBroadcast(intent);
                    buttonSrv.setCompoundDrawables(distr_logo, null, null, null);
                    connectToServer();
                }
            });

        }

    }

    private void connectToServer() {
        Intent connectIntent = new Intent(actionToMainService);
        connectIntent.putExtra(parameters, "selectServer");
        connectIntent.putExtra("ip", IP);
        Log.d(LOG_TAG, "IP: " + IP);
        getActivity().sendBroadcast(connectIntent);
    }

    public class MainBroadcastReceiver extends BroadcastReceiver {
        String LOG_TAG = "BC FragmentListServers";
        String parameters = "parameters";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Broadcast in " + getClass().toString());
            context = context.getApplicationContext();
            String action = intent.getAction();
            String params = intent.getStringExtra(parameters);
            if (params.contains("foundServer")) {
                uiFindServers(intent);
                Log.d(LOG_TAG, "Сервер найден");
            }
        }
    }
}
