package com.example.vendor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.vendor.AdminScreen.AdminHomeActivity;
import com.example.vendor.AdminScreen.orderPendingFragment;
import com.example.vendor.UserScreens.HomeActivity;
import com.example.vendor.UserScreens.ViewOrdersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String userId,userType, adminId;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //all notification will be received

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        SharedPreferences sh =this.getSharedPreferences("MyLogin",MODE_PRIVATE);
        userId = sh.getString("userId","");
        userType = sh.getString("userType","");


        SharedPreferences admin =this.getSharedPreferences("MyAdminLogin",MODE_PRIVATE);
        adminId = admin.getString("adminId","");

        //get Data from notication
        String notificaionType = remoteMessage.getData().get("notificationType");
        if (notificaionType.equals("NewOrder")) {
            String buyerUid = remoteMessage.getData().get("buyerUid");
            String sellerUid = remoteMessage.getData().get("sellerUid");
            String oredrId = remoteMessage.getData().get("orderId");
            String notificationTitle = remoteMessage.getData().get("NotificationTitle");
            String notificationMeassage= remoteMessage.getData().get("notificationMessage");


            Log.v("ad",""+adminId + sellerUid);
            if(userId.equals(buyerUid)){
                showNotification(oredrId,sellerUid,buyerUid,notificationTitle,notificationMeassage, notificaionType);
            }





        }
        if (notificaionType.equals("OrderStatusChange")) {

            String buyerUid = remoteMessage.getData().get("buyerUid");
            String sellerUid = remoteMessage.getData().get("sellerUid");
            String oredrId = remoteMessage.getData().get("orderId");
            String notificationTitle = remoteMessage.getData().get("NotificationTitle");
            String notificationMeassage= remoteMessage.getData().get("notificationMessage");

            Log.v("id",""+userId + buyerUid);
            if(userId.equalsIgnoreCase(buyerUid)){
                showNotification(oredrId,sellerUid,buyerUid,notificationTitle,notificationMeassage, notificaionType);
            }





        }
    }

    private void showNotification(String order, String sellerUid, String buyerUid, String notificationTitle, String notificationMessage, String notificationType){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            setupNotificationChannel(notificationManager);
        }

//        Intent intent = null;
//        if(notificationType.equals("NewOrder") && adminId.equals(sellerUid)){
//            intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
//            intent.putExtra("orderID", order);
//            intent.putExtra("orderBy",buyerUid);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        }else if(notificationType.equals("OrderStatusChange")  && userId.equals(buyerUid)){
//            intent = new Intent(getApplicationContext(), HomeActivity.class);
//            intent.putExtra("orderID", order);
//            intent.putExtra("orderBy",sellerUid);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        }
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.full_logo);

        Uri notificatiUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.full_logo)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSound(notificatiUri)
                .setAutoCancel(true);




        notificationManager.notify(notificationID,notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannel(NotificationManager notificationManager) {
        CharSequence name = "Some Sample Text";
        String description = "Channel Descripion here";
        int importance = NotificationManager.IMPORTANCE_HIGH;


        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

        if(notificationManager != null){
            notificationManager.createNotificationChannel(channel);
        }



    }
}
