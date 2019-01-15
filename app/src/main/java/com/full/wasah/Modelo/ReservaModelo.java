package com.full.wasah.Modelo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.full.wasah.Interface.InterfaceReserva;
import com.full.wasah.Presentador.ReservaPresentador;
import com.full.wasah.R;
import com.full.wasah.Util.ReservaApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaModelo implements InterfaceReserva.Modelo {

    InterfaceReserva.Presentador presentador;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenciaTurno;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat newDate = new SimpleDateFormat("dd-MM-yyyy");

    public ReservaModelo(ReservaPresentador reservaPresentador) {
        presentador = reservaPresentador;
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenciaTurno = firebaseDatabase.getReference().child("turno");
    }

    @Override
    public void guardarTurno(String nya, String patente, String vehiculo, String telefono, String fecha, String hora) {
        Date date = null;
        try {
           date = oldDate.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        referenciaTurno.child(fecha).child(hora).child("nya").setValue(nya);
        referenciaTurno.child(fecha).child(hora).child("telefono").setValue(telefono);
        referenciaTurno.child(fecha).child(hora).child("patente").setValue(patente);
        referenciaTurno.child(fecha).child(hora).child("estado").setValue("reservado");
        referenciaTurno.child(fecha).child(hora).child("vehiculo").setValue(vehiculo);
        createNotification("Turno reservado","El día "+newDate.format(date)+" a las "+hora+".");
        presentador.guardadoCorrecto();
    }

    public void createNotification(String title, String message) {
        /**Creates an explicit intent for an Activity in your app**/

        mBuilder = new NotificationCompat.Builder(ReservaApplication.getAppContext());
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setContentTitle(title)
                .setContentText(message)
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
