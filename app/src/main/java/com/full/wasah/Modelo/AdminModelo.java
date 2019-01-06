package com.full.wasah.Modelo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.full.wasah.Interface.InterfaceAdmin;
import com.full.wasah.Presentador.AdminPresentador;
import com.full.wasah.Util.ReservaApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        PackageManager packageManager = ReservaApplication.getAppContext().getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=54"+telefono+"&text=" + URLEncoder.encode("Su automovil esta listo para retirar.", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                ReservaApplication.getAppContext().startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
