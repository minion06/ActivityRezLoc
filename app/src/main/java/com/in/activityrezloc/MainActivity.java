package com.in.activityrezloc;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    private static final String TAG ="ACREZ" ;
    protected GoogleApiClient mGoogleApiClient;
    protected PendingIntent mPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildApiClient();
    }

    private synchronized void buildApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(ActivityRecognition.API).build();
    }
    private PendingIntent getActivityDetectionPendingIntent()
    {
        String ACTIVITY_REZ ="com.in.activityrezloc.RECEIVE_RECOGNIZED_ACTIVITY";

        if(null!=mPendingIntent) {
            return mPendingIntent;
        }
        else
        {
            Intent intent = new Intent(ACTIVITY_REZ);
            return PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mGoogleApiClient.isConnected())
        {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient,0,getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.d(TAG,"status "+status.getStatusMessage());
    }
}
