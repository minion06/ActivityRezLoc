package com.in.activityrezloc.broadcasts;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Switch;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.in.activityrezloc.MainActivity;
import com.in.activityrezloc.R;
import java.util.Random;

/**
 * Created by ${"Vignesh"} on 10/20/2016.
 */
public class ActivityReceiver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
      DetectedActivity detectedActivity = result.getMostProbableActivity();

        Log.d("ACREZ","Type "+detectedActivity.getType());
        Log.d("ACREZ","Confidence "+detectedActivity.getConfidence());
        int type= getDataFormPreference();
         Log.d("ACREZ"," From Pref "+type);
        if(detectedActivity.getType()!=type)
        {
            saveToPreference(detectedActivity.getType());
            skateOverDetectedActivity(detectedActivity.getType());
            Log.d("ACREZ"," A From Pref "+type);
        }

    }

    private void saveToPreference(int detectedActivity) {
        Log.d("ACREZ","SAVING");
        SharedPreferences actiPreferences = context.getSharedPreferences("ACTIVITY_PREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = actiPreferences.edit();
        editor.putInt("ACTIVITY_TYPE",detectedActivity);
        editor.apply();
    }
    private int getDataFormPreference()
    {
        SharedPreferences actiPreferences = context.getSharedPreferences("ACTIVITY_PREF",Context.MODE_PRIVATE);
        return actiPreferences.getInt("ACTIVITY_TYPE",-1);
    }

    private void skateOverDetectedActivity(int recognizedActivity)
    {
        switch(recognizedActivity)
        {
            case DetectedActivity.IN_VEHICLE:
            {
                sendActivityNotification("Riding On");
                break;
            }
            case DetectedActivity.STILL:
            {
                sendActivityNotification("In Rest!");
                break;
            }
            case DetectedActivity.ON_BICYCLE:
            {
                sendActivityNotification("Cool Ride in cycle!");
                break;
            }
            case DetectedActivity.ON_FOOT:
            {
                sendActivityNotification("In Foot");
                break;
            }
            case DetectedActivity.RUNNING:
            {
                sendActivityNotification("Running!");
                break;
            }
            case DetectedActivity.WALKING:
            {
                sendActivityNotification("Walk");
                break;
            }
            case DetectedActivity.UNKNOWN:
                break;

        }

    }
    private void sendActivityNotification(String typeOfActivity)
    {
        int requestID = (int) System.currentTimeMillis();
        Intent event_detail_intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID,
                event_detail_intent, 0);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Activity REZ")
                .setContentText(typeOfActivity)
                .setSmallIcon(R.drawable.activity_icon);


        // Set pending intent
        notificationBuilder.setContentIntent(pIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        //	defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        notificationBuilder.setDefaults(defaults);
        // Set the content for Notification
        notificationBuilder.setContentText(typeOfActivity);
        // Set autocancel
        notificationBuilder.setAutoCancel(true);
        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(randomNumber , notificationBuilder.build());
       // Log.d("ACREZ","notify created");
    }
}
