package com.full.wasah.Modelo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.full.wasah.Interface.InterfaceReserva;
import com.full.wasah.Presentador.ReservaPresentador;
import com.full.wasah.R;
import com.full.wasah.Util.ReservaApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReservaModelo implements InterfaceReserva.Modelo {

    InterfaceReserva.Presentador presentador;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenciaTurno;



    public ReservaModelo(ReservaPresentador reservaPresentador) {
        presentador = reservaPresentador;
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenciaTurno = firebaseDatabase.getReference().child("turno");
    }

    @Override
    public void guardarTurno(String nya, String patente, String vehiculo, String telefono, String fecha, String hora) {
        referenciaTurno.child(fecha).child(hora).child("nya").setValue(nya);
        referenciaTurno.child(fecha).child(hora).child("telefono").setValue(telefono);
        referenciaTurno.child(fecha).child(hora).child("patente").setValue(patente);
        referenciaTurno.child(fecha).child(hora).child("estado").setValue("reservado");
        referenciaTurno.child(fecha).child(hora).child("vehiculo").setValue(vehiculo);
        presentador.guardadoCorrecto();
    }



}
