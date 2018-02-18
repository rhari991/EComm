package net.rhari.ecomm.util;

public interface NetworkHelper {

    boolean isConnected();

    String getNetworkUnavailableString();

    void addNetworkStateChangeListener(OnNetworkStateChangeListener listener);

    void removeNetworkStateChangeListener(OnNetworkStateChangeListener listener);

    interface OnNetworkStateChangeListener {

        void onNetworkStateChange(boolean connected);
    }
}