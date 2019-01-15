package com.full.wasah.Vista;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.full.wasah.Adapter.TurnoAdapter;
import com.full.wasah.Adapter.TurnoAdapterAdmin;
import com.full.wasah.Interface.InterfaceAdmin;
import com.full.wasah.Interface.InterfaceReserva;
import com.full.wasah.Presentador.AdminPresentador;
import com.full.wasah.Presentador.ReservaPresentador;
import com.full.wasah.R;
import com.full.wasah.Util.ReservaApplication;
import com.full.wasah.Util.Turno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity implements InterfaceAdmin.Vista{

    RecyclerView adminRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText fecha;
    Dialog dialog;
    Calendar myCalendar;
    TextView cerrarSesion;
    DatePickerDialog.OnDateSetListener date;
    String myFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat);
    String dbFormat = "yyyy-MM-dd";
    ArrayList<Turno> listaTurno;
    private InterfaceAdmin.Presentador presentador;
    TurnoAdapterAdmin turnoAdapterAdmin;
    SimpleDateFormat sdf = new SimpleDateFormat(dbFormat, Locale.US);
    ImageView logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminRecycler = findViewById(R.id.adminRecycler);
        initPrefs();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putBoolean("login", false);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        myCalendar = Calendar.getInstance();
        listaTurno = new ArrayList<>();
        adminRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        fecha = findViewById(R.id.fecha);
        fecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(AdminActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fecha.setText(sdf2.format(myCalendar.getTime()));
                mostrarTurnos();
            }

        };
        presentador = new AdminPresentador(AdminActivity.this);
    }

    private void mostrarTurnos(){
        databaseReference = firebaseDatabase.getReference("turno/"+sdf.format(myCalendar.getTime()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaTurno.clear();
                if(dataSnapshot.exists()){
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        Turno t = dataSnapshot1.getValue(Turno.class);
                        listaTurno.add(t);

                }
                turnoAdapterAdmin = new TurnoAdapterAdmin(AdminActivity.this, listaTurno, new TurnoAdapterAdmin.OnLongItemListener() {
                    @Override
                    public void onLong(String estado, final String fecha, final String hora,final String telefono) {
                        if(estado.equals("reservado")) {
                        dialog = new Dialog(AdminActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.popup_terminado);
                        Button correcto = dialog.findViewById(R.id.correcto);
                        correcto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                presentador.modificarTurno(fecha, hora, telefono);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();}
                        else{
                            Toast.makeText(AdminActivity.this, "Seleccione un turno reservado",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                   adminRecycler.setHasFixedSize(true);
                   adminRecycler.setAdapter(turnoAdapterAdmin);
                   adminRecycler.setItemAnimator(new DefaultItemAnimator());
                }else{
                    Toast.makeText(ReservaApplication.getAppContext(), "No hay turnos reservados para la fecha seleccionada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void initPrefs(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @Override
    public void toastExito() {


    }
}
