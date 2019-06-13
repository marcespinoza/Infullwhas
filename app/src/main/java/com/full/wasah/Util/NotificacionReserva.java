package com.full.wasah.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.full.wasah.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificacionReserva extends Service {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenciaTurno;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
    Date newDate;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10002";
    public int counter=0;

    public NotificacionReserva(){
        super();
    }

    public NotificacionReserva(Context context) {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        firebaseReference();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("EXIT", "ontaskremo!");
        Intent broadcastIntent = new Intent(this, NotificacionReceiver.class);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, NotificacionReceiver.class);
        sendBroadcast(broadcastIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    private void firebaseReference(){
        Log.i("EXIT", "onFirebase!");
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenciaTurno = firebaseDatabase.getReference().child("turno");
        referenciaTurno.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    newDate = dateFormat.parse(dataSnapshot.getKey().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createNotification(dateFormat2.format(newDate));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createNotification(String fecha) {
        /**Creates an explicit intent for an Activity in your app**/

        mBuilder = new NotificationCompat.Builder(ReservaApplication.getAppContext());
        mBuilder.setSmallIcon(R.drawable.ic_alert);
        mBuilder.setContentTitle("Atención")
                .setContentText("Tiene una nueva reserva el día "+fecha)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        mNotificationManager = (NotificationManager) ReservaApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }

}
