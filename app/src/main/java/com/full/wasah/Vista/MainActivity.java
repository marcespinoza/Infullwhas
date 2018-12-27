package com.full.wasah.Vista;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.full.wasah.Adapter.TurnoAdapter;
import com.full.wasah.Interface.InterfaceReserva;
import com.full.wasah.Presentador.ReservaPresentador;
import com.full.wasah.R;
import com.full.wasah.Util.Turno;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.toptas.fancyshowcase.FancyShowCaseView;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, InterfaceReserva.Vista{


    EditText edittextFecha, nyap, patente, celular, vehiculo;
    private InterfaceReserva.Presentador presentadorReserva;
    Calendar myCalendar;
    Dialog dialog, administrar;
    TextView error;
    ProgressBar progressBar;
    ImageButton imageBtn;
    String hora = "";
    DatePickerDialog.OnDateSetListener date;
    Button botonReserva, aceptar, cancelar, iniciarSesion;
    SupportMapFragment mapFragment;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, referenciaTurno;
    RecyclerView recyclerView;
    ArrayList <Turno> listaTurno;
    TurnoAdapter turnoAdapter;
    String[] horarios = {"07:00","07:20","07:40","08:00","08:20","08:40","09:00","09:20","09:40","10:00","10:20","10:40","11:00","11:20"
                        ,"11:40","12:00","12:20","12:40","13:00","13:20","13:40","14:00","14:20","14:40","15:00","15:40","16:00"
                        ,"16:20","16:40","17:00","17:20","17:40","18:00","18:20","18:40","19:00","19:20","19:40","20:00","20:20"
                        ,"20:40","21:00","21:20","21:40","22:00","22:20","22:40","23:00"};
    String myFormat = "dd/MM/yyyy";
    String dbFormat = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(dbFormat, Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCalendar = Calendar.getInstance();
        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        listaTurno = new ArrayList<>();
        edittextFecha =  findViewById(R.id.fecha);
        imageBtn = findViewById(R.id.imageBtn);
        imageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, imageBtn);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        administrar = new Dialog(MainActivity.this);
                        administrar.setContentView(R.layout.login_popup);
                        iniciarSesion = administrar.findViewById(R.id.aceptarsesion);
                        iniciarSesion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(i);
                            }
                        });
                        administrar.show();
                        return true;
                    }
                });

         popup.show();//showing popup menu
            }
        });
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        botonReserva = findViewById(R.id.botonReserva);
        botonReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edittextFecha.getText().toString().equals("")){
                //--------Inicializo el popup---------//
                  mostrarError("Seleccione una fecha para ver turnos disponibles");
                }
                else if(hora.isEmpty()){
                    mostrarError("Seleccione un horario");
                }else{dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.popup);
                    nyap = dialog.findViewById(R.id.nya);
                    patente = dialog.findViewById(R.id.patente);
                    celular = dialog.findViewById(R.id.celular);
                    vehiculo = dialog.findViewById(R.id.vehiculo);
                    aceptar = dialog.findViewById(R.id.aceptar);
                    error = dialog.findViewById(R.id.error);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getDatos();
                        }
                    });
                    cancelar = dialog.findViewById(R.id.cancelar);
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        if(!Prefs.getBoolean("init", false)){
            new FancyShowCaseView.Builder(this)
                    .focusOn(edittextFecha)
                    .title("Seleccione una fecha para ver turnos disponibles")
                    .build()
                    .show();
            Prefs.putBoolean("init", true);
        }
         date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittextFecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        referenciaTurno = firebaseDatabase.getReference().child("turno");
        presentadorReserva = new ReservaPresentador(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void mostrarError(String message){
        new Flashbar.Builder(MainActivity.this)
                .gravity(Flashbar.Gravity.TOP)
                .duration(3000)
                .backgroundColorRes(R.color.red_400)
                .message(message)
                .build()
                .show();
    }

    void initPrefs(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("reserva")
                .setUseDefaultSharedPreference(true)
                .build();
    }

    private void updateLabel() {

        final Map<String, String> map = new HashMap<>();
        map.put("nya","");
        map.put("vehiculo","");
        map.put("patente","");
        map.put("telefono","");
        map.put("fecha","");
        map.put("estado","libre");
        progressBar.setVisibility(View.VISIBLE);
        edittextFecha.setText(sdf2.format(myCalendar.getTime()));
        referenciaTurno.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.child(sdf.format(myCalendar.getTime())).exists()) {

                }else{
                    for (int i = 0; i < horarios.length; i++){
                    map.put("hora",horarios[i]);
                    firebaseDatabase.getReference().child("turno/"+sdf.format(myCalendar.getTime())+"/"+horarios[i]).setValue(map);}
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

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
                turnoAdapter = new TurnoAdapter(MainActivity.this, listaTurno, new TurnoAdapter.OnItemCheckListener() {
                    @Override
                    public void onItemCheck(String item) {
                        hora = item;
                    }
                });
                recyclerView.setAdapter(turnoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34.295195, -60.241532);
        googleMap.addMarker(new MarkerOptions().position(sydney).snippet("25 de mayo 1.041 - Salto Bs. As.")
                .title("Infullwhas")).setVisible(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34.295195, -60.241532), 15));
    }



    @Override
    public void getDatos() {
        presentadorReserva.getDatos(nyap.getText().toString(), patente.getText().toString(), vehiculo.getText().toString(), celular.getText().toString(), sdf.format(myCalendar.getTime()), hora);
    }

    @Override
    public void errorCampoVacio() {
        error.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_700));
    }

    @Override
    public void mensajeExito() {
        dialog.dismiss();
        edittextFecha.setText("");
        hora="";
        recyclerView.setAdapter(null);
        turnoAdapter.notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()  {
            @Override
            public void run() {
                new iOSDialogBuilder(MainActivity.this)
                        .setTitle("Reserva realizada")
                        .setSubtitle("Recuerde que tiene una tolerancia de 15 minutos de espera, de lo contrario perderá su turno")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener("Entiendo",new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();
            }
        }, 1000);


    }
}


