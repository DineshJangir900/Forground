package com.example.forground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import static com.example.forground.App.CHANNEL_ID;

public class ExampleService extends Service {
    String input;
    Notification notification;
    MediaRecorder recorder;
    double amplitude,value;
    private boolean isThreadRun = true;
    Thread thread;
    static final private double EMA_FILTER = 0.6;
    private static double mEMA = 0.0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("dev/null");
        try{
            recorder.prepare();
        }catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();


        startListenAudio();
            if (value >= 60) {
                if (value > 60) {
                    if (value >= 61) {
                        if (value >= 65) {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            long vibratePattern[] = {0, 5000};
                            v.vibrate(vibratePattern, 1);
                        }
                    }
                }
            }


        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private double convertdDb(double amplitude) {
        double mEMAValue = EMA_FILTER * amplitude + (1.0 - EMA_FILTER) * mEMA;
        return 20 * (float) Math.log10((mEMAValue/51805.5336)/ 0.000028251);
    }

    private double startListenAudio() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                    try {
                        amplitude = getAmplitude();
                        if(amplitude > 0 && amplitude < 1000000) {
                            value = convertdDb(amplitude);
                            input = String.valueOf(value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return value;

    }
    public double getAmplitude() {
        if (recorder != null)
            return  (recorder.getMaxAmplitude());
        else
            return 0;
    }
}
