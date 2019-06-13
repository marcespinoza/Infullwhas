package com.full.wasah.Modelo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.full.wasah.Interface.InterfaceAdmin;
import com.full.wasah.Presentador.AdminPresentador;
import com.full.wasah.R;
import com.full.wasah.Util.ReservaApplication;
import com.full.wasah.Util.Turno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;

public class AdminModelo implements InterfaceAdmin.Modelo {

    InterfaceAdmin.Presentador presentador;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenciaTurno;

    public AdminModelo(AdminPresentador adminPresentador) {
        presentador = adminPresentador;
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenciaTurno = firebaseDatabase.getReference().child("turno");
    }

    @Override
    public void turnoTerminado(String fecha, String hora, String telefono) {
        referenciaTurno.child(fecha).child(hora).child("estado").setValue("terminado");
        try {
            String url = "https://api.whatsapp.com/send?phone=54"+telefono+"&text=" + URLEncoder.encode("Su vehículo está listo. Quejas y sugerencias al mismo número. MUCHAS GRACIAS POR SU PREFERENCIA!!!");
            Intent intent = new Intent(Intent.ACTION_VIEW);
           intent.setData(Uri.parse(url));
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           ReservaApplication.getAppContext().startActivity(intent);

        } catch (Exception e){
            Crashlytics.logException(new RuntimeException("FULLWASH"+e.getMessage().toString()));
            e.printStackTrace();
        }
    }

}
