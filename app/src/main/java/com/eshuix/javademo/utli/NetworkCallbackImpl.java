package com.eshuix.javademo.utli;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static class NetworkCallbackImplHolder {
        private static final NetworkCallbackImpl INSTANCE = new NetworkCallbackImpl();
    }
    private NetworkCallbackImpl (){}
    public static NetworkCallbackImpl getInstance() {
        return NetworkCallbackImplHolder.INSTANCE;
    }

    private boolean isNetworkConnected;

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        //网络已连接
        isNetworkConnected = true;
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        //网络已断开
        isNetworkConnected = false;
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {//网络类型为wifi

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {//蜂窝网络

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)){//蓝牙

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){//以太网

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)){//vpn

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)){//Wi-Fi Aware

            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)){//LoWPAN

            } else { //其他网络

            }
        }
    }

    /**
     * 判断网络是否连接
     */
    public boolean isNetworkConnected(){
        return isNetworkConnected;
    }
}
