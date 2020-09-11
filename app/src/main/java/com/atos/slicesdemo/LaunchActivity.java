package com.atos.slicesdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.slice.SliceManager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    private static final String SLICE_AUTHORITY = "com.example.slicesdemo";
    Button basicLaunchBtn, toggleLaunchButton, gridLaunchButton;
    String sliceViewerPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantSlicePermissions();

        basicLaunchBtn = findViewById(R.id.button);
        toggleLaunchButton = findViewById(R.id.button2);
        gridLaunchButton = findViewById(R.id.button3);
        sliceViewerPackageName = getString(R.string.slice_viewer_application_package_name);

        basicLaunchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                launchSliceViewerApp();
            }
        });


        toggleLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSliceViewerAppToggle();
            }
        });
        gridLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSliceViewerAppGrid();
            }
        });
    }


    public boolean isSliceViewerInstalled() {

        PackageManager packageManager = getApplicationContext().getPackageManager();

        try {
            packageManager.getPackageInfo(sliceViewerPackageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Slice-Viewer app not Installed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }


    public boolean isSliceViewerApplicationEnabled() {
        boolean status = false;

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(sliceViewerPackageName, 0);

            if (applicationInfo != null) {
                status = applicationInfo.enabled;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Slice-Viewer app not Enabled", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return status;

    }

    private void launchSliceViewerApp() {

        if (isSliceViewerInstalled() && isSliceViewerApplicationEnabled()) {
            String uri = "slice-content://com.example.slicesdemo/launchactivity";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);


        }
    }

    private void launchSliceViewerAppToggle() {

        if (isSliceViewerInstalled() && isSliceViewerApplicationEnabled()) {
            String uri = "slice-content://com.example.slicesdemo/wifi";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);


        }
    }

    private void launchSliceViewerAppGrid() {

        if (isSliceViewerInstalled() && isSliceViewerApplicationEnabled()) {
            String uri = "slice-content://com.example.slicesdemo/kotlin";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);


        }
    }

    private void grantSlicePermissions() {

        Context context = getApplicationContext();
        Uri sliceProviderUri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(SLICE_AUTHORITY)
                .build();

        String assistantPackage = getAssistantPackage(context);
        if(assistantPackage == null){
            return;

        }

        SliceManager.getInstance(context).grantSlicePermission(assistantPackage,sliceProviderUri);
    }


    private String getAssistantPackage(Context context){
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfosList = packageManager.queryIntentServices(
                new Intent(VoiceInteractionService.SERVICE_INTERFACE),0);

        if(resolveInfosList.isEmpty()){
            return null;
        }
        return  resolveInfosList.get(0).serviceInfo.packageName;
    }
}
