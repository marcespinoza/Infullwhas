package com.full.wasah.Vista;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.full.wasah.Adapter.TurnoAdapter;
import com.full.wasah.Adapter.TurnoAdapterAdmin;
import com.full.wasah.R;
import com.full.wasah.Util.Turno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity {

    RecyclerView adminRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText fecha;
    Dialog dialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String myFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat);
    String dbFormat = "yyyy-MM-dd";
    ArrayList<Turno> listaTurno;
    TurnoAdapterAdmin turnoAdapterAdmin;
    SimpleDateFormat sdf = new SimpleDateFormat(dbFormat, Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        adminRecycler = findViewById(R.id.adminRecycler);
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
    }

    private void mostrarTurnos(){
        databaseReference = firebaseDatabase.getReference("turno/"+sdf.format(myCalendar.getTime()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaTurno.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    if(dataSnapshot1.exists()){
                        Turno t = dataSnapshot1.getValue(Turno.class);
                        listaTurno.add(t);}
                }
                turnoAdapterAdmin = new TurnoAdapterAdmin(AdminActivity.this, listaTurno, new TurnoAdapterAdmin.OnLongItemListener() {
                    @Override
                    public void onLong(String item) {
                        dialog = new Dialog(AdminActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.popup_terminado);
                        dialog.show();
                    }
                });
                adminRecycler.setHasFixedSize(true);
                adminRecycler.setAdapter(turnoAdapterAdmin);
                adminRecycler.setItemAnimator(new DefaultItemAnimator());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
