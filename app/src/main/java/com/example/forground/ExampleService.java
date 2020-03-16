package com.example.forground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import static com.example.forground.App.CHANNEL_ID;

public class ExampleService extends Service {
    double aData;
    Thread thread;
    String input = "12";
    MediaRecorder recorder;
    Notification notification;
    @Override
    public void onCreate() {
        super.onCreate();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("dev/null");
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    aData = recorder.getMaxAmplitude();
                    input =String.valueOf(aData);
                    Log.d("ccc",input);
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();



    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentText("Forground Service")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        recorder.stop();
        recorder.release();
        recorder = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
