package net.rhari.ecomm.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.rhari.ecomm.R;

import java.util.ArrayList;
import java.util.List;

public class AndroidNetworkHelper implements NetworkHelper {

    private final Context context;
    private final List<OnNetworkStateChangeListener> networkStateChangeListeners;
    private BroadcastReceiver networkStateChangeReceiver;

    AndroidNetworkHelper(Context context) {
        this.context = context.getApplicationContext();
        this.networkStateChangeListeners = new ArrayList<>(0);
    }

    @Override
    public boolean isConnected() {
        return isConnected(context);
    }

    @Override
    public String getNetworkUnavailableString() {
        return context.getString(R.string.error_network_unavailable);
    }

    @Override
    public void addNetworkStateChangeListener(OnNetworkStateChangeListener listener) {
        networkStateChangeListeners.add(listener);
        if (networkStateChangeListeners.size() == 1) {
            networkStateChangeReceiver = new NetworkStateChangeReceiver();
            context.registerReceiver(networkStateChangeReceiver, new IntentFilter
                    (ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public void removeNetworkStateChangeListener(OnNetworkStateChangeListener listener) {
        networkStateChangeListeners.remove(listener);
        if (networkStateChangeListeners.size() == 0 && networkStateChangeReceiver != null) {
            context.unregisterReceiver(networkStateChangeReceiver);
            networkStateChangeReceiver = null;
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private class NetworkStateChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connected = isConnected(context);
            for (OnNetworkStateChangeListener listener : networkStateChangeListeners) {
                listener.onNetworkStateChange(connected);
            }
        }
    }
}