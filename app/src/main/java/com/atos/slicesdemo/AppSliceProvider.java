package com.atos.slicesdemo;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;


import androidx.slice.SliceProvider;
import androidx.slice.builders.GridRowBuilder;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.builders.ListBuilder.*;
import androidx.slice.builders.GridRowBuilder.*;



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
            case "/kotlin":
                return  createSliceGridRow(sliceUri);
            case "/launchactivity":
                return createLaunchActivity(sliceUri);
            default:
                return createLaunchActivity(sliceUri);
        }
    }

    @SuppressLint("RestrictedApi")
    public Slice createLaunchActivity(Uri sliceUri) {
        Intent intent = new Intent(getContext(), LaunchActivity.class);
        SliceAction sliceAction = new SliceAction(PendingIntent.getActivity(getContext(), 0, intent, 0),
                IconCompat.createWithResource(getContext(), R.drawable.ic_launcher_background),
                "Launch MainActivity");

        if (sliceUri.getPath().equals("/launchactivity")) {
            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(new RowBuilder().setTitle("Launch Activity").setPrimaryAction(sliceAction)).build();

        } else {
            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(new RowBuilder().setTitle("URI Not Found").setPrimaryAction(sliceAction)).build();
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
                .addRow(new RowBuilder().setTitle("Wifi").setSubtitle(subTitle)
                        .setPrimaryAction(createWiFiToggleAction(isWifiEnabled))).build();

    }

    @SuppressLint("RestrictedApi")
    public SliceAction createWiFiToggleAction(Boolean isWifiEnabled){

        Intent intent = new Intent(getContext(), MyBroadCastReceiver.class).setAction(MyBroadCastReceiver.TOGGLE_WIFI).putExtra(MyBroadCastReceiver.EXTRA_VALUE_KEY,isWifiEnabled);
        return new SliceAction(PendingIntent.getBroadcast(getContext(),0,intent,0),"Toggle Wifi",isWifiEnabled);
    }


    @SuppressLint("RestrictedApi")
    public Slice createSliceGridRow(Uri sliceUri){

        Intent primaryIntent = new Intent(getContext(), LaunchActivity.class);
        SliceAction primarySliceAction = new SliceAction(PendingIntent.getActivity(getContext(), 0, primaryIntent, 0),
                IconCompat.createWithResource(getContext(), R.drawable.ic_launcher_background),
                "Launch MainActivity");

        Intent nullSafetyIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://kotlinlang.org/docs/reference/null-safety.html"));
        PendingIntent pi1 = PendingIntent.getActivity(getContext(),0,nullSafetyIntent,0);

        Intent coroutineIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://kotlinlang.org/docs/reference/coroutines/basics.html"));
        PendingIntent pi2 = PendingIntent.getActivity(getContext(),0,coroutineIntent,0);

        Intent extensionIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://kotlinlang.org/docs/reference/extensions.html"));
        PendingIntent pi3 = PendingIntent.getActivity(getContext(),0,extensionIntent,0);

        GridRowBuilder gridRowBuilder = new GridRowBuilder();
        gridRowBuilder.addCell(new CellBuilder()
                                .addImage(IconCompat.createWithResource(getContext(),R.drawable.ic_kotlin),ListBuilder.SMALL_IMAGE)
                                .addText("Null Safety")
                                .setContentIntent(pi1))
                .addCell(new CellBuilder()
                        .addImage(IconCompat.createWithResource(getContext(),R.drawable.ic_kotlin),ListBuilder.SMALL_IMAGE)
                        .addText("Coroutines")
                        .setContentIntent(pi2))
                .addCell(new CellBuilder()
                        .addImage(IconCompat.createWithResource(getContext(),R.drawable.ic_kotlin),ListBuilder.SMALL_IMAGE)
                        .addText("Extensions")
                        .setContentIntent(pi3));

              ListBuilder listBuilder =new ListBuilder(getContext(),sliceUri,ListBuilder.INFINITY);

                listBuilder.setHeader(new HeaderBuilder()
                        .setTitle("Want to start Learning Kotlin?")
                        .setSubtitle("Check out these articles")
                        .setPrimaryAction(primarySliceAction));

                listBuilder.addGridRow(gridRowBuilder);


           return listBuilder.build();

    }

}




