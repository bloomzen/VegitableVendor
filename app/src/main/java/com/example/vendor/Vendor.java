package com.example.vendor;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.singhajit.sherlock.core.Sherlock;
import com.singhajit.sherlock.core.investigation.AppInfo;
import com.singhajit.sherlock.core.investigation.AppInfoProvider;
import com.singhajit.sherlock.util.AppInfoUtil;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class Vendor extends Application {


    public FirebaseDatabase mDatabase;
    private static Context context;




    @Override
    public void onCreate() {
        super.onCreate();

        Sherlock.init(this);

        Vendor.context = getApplicationContext();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance();


        /*PICASSIO*/

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        Sherlock.setAppInfoProvider(new AppInfoProvider() {
            @Override
            public AppInfo getAppInfo() {
                return new AppInfo.Builder()
                        .with("Version", AppInfoUtil.getAppVersion(Vendor.getAppContext())) //You can get the actual version using "AppInfoUtil.getAppVersion(context)"
                        .build();
            }
        });

    }

    public static Context getAppContext() {
        return Vendor.context;
    }
}
