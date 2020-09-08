package com.example.slicesdemo;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.slice.SliceSpec;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;


import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;

import java.util.Set;

import static com.example.slicesdemo.MyBroadCastReceiver.wifiToggleUri;

public class AppSliceProvider extends SliceProvider {

    @Override
    public boolean onCreateSliceProvider() {
        return true;
    }


    @NonNull
    @Override
    public Uri onMapIntentToUri(Intent intent) {
        Uri.Builder uriBuilder = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT);
        if (intent == null) return uriBuilder.build();
        Uri data = intent.getData();

        if (data != null) {
            uriBuilder = uriBuilder.authority(getContext().getPackageName());
        }
        return uriBuilder.build();

    }


    @Override
    public Slice onBindSlice(Uri sliceUri) {

        switch (sliceUri.getPath()) {
            case "/wifi":
                return createWifiToggleActionSlice(sliceUri);
            default:
                return createLaunchActivity(sliceUri);
        }
    }

    @SuppressLint("RestrictedApi")
    public Slice createLaunchActivity(Uri sliceUri) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        SliceAction sliceAction = new SliceAction(PendingIntent.getActivity(getContext(), 0, intent, 0),
                IconCompat.createWithResource(getContext(), R.drawable.ic_launcher_background),
                "Launch MainActivity");

        if (sliceUri.getPath().equals("/launchactivity")) {
            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(new ListBuilder.RowBuilder().setTitle("Launch Activity").setPrimaryAction(sliceAction)).build();

        } else {
            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(new ListBuilder.RowBuilder().setTitle("URI Not Found").setPrimaryAction(sliceAction)).build();
        }
    }


    @SuppressLint("RestrictedApi")
    public Slice createWifiToggleActionSlice(Uri sliceUri) {

        boolean isWifiEnabled = false;
        String subTitle= "";
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            isWifiEnabled = wifiManager.isWifiEnabled();
        }
        if (isWifiEnabled) {
            subTitle = "Enabled";
        } else {
            subTitle = "Not Enabled";
        }

        return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                .addRow(new ListBuilder.RowBuilder().setTitle("Wifi").setSubtitle(subTitle)
                        .setPrimaryAction(createWiFiToggleAction(isWifiEnabled))).build();

    }

    @SuppressLint("RestrictedApi")
    public SliceAction createWiFiToggleAction(Boolean isWifiEnabled){

        Intent intent = new Intent(getContext(),MyBroadCastReceiver.class).setAction(MyBroadCastReceiver.TOGGLE_WIFI).putExtra(MyBroadCastReceiver.EXTRA_VALUE_KEY,isWifiEnabled);
        return new SliceAction(PendingIntent.getBroadcast(getContext(),0,intent,0),"Toggle Wifi",isWifiEnabled);
    }

}




