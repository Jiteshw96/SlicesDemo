package com.example.slicesdemo;

import android.app.slice.Slice;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;

public class MyBroadCastReceiver extends BroadcastReceiver {

    public static final String TOGGLE_WIFI = "com.example.slicesdemo.TOGGLE_WIFI";
    public static final String EXTRA_VALUE_KEY = "extra_value";
    public static final Uri wifiToggleUri = Uri.parse("content://com.example.slicesdemo/wifi");

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean wifiState =  false;
        if(intent.getAction().equals(TOGGLE_WIFI)){

            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert wifiManager != null;
            wifiState =  intent.getBooleanExtra(Slice.EXTRA_TOGGLE_STATE,false);

            wifiManager.setWifiEnabled(wifiState);
            context.getApplicationContext().getContentResolver().notifyChange(wifiToggleUri,null);

        }

    }


}
