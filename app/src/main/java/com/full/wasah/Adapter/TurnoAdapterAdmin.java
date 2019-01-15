package com.full.wasah.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.full.wasah.Interface.ItemLongClickListener;
import com.full.wasah.R;
import com.full.wasah.Util.ReservaApplication;
import com.full.wasah.Util.Turno;

import java.util.ArrayList;

public class TurnoAdapterAdmin extends RecyclerView.Adapter<TurnoAdapterAdmin.MyViewHolder>{

    Context context;
    ArrayList<Turno> turnos;
    public OnLongItemListener onLongItemListener;

    public TurnoAdapterAdmin(Context context, ArrayList<Turno> turnos, OnLongItemListener onLongItemListener) {
        this.context = context;
        this.turnos = turnos;
        this.onLongItemListener=onLongItemListener;
    }

    public interface OnLongItemListener {
        void onLong(String estado, String fecha, String hora, String telefono);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_turno2, viewGroup, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        switch (turnos.get(i).getEstado()){
            case "libre": myViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ReservaApplication.getAppContext(), R.color.green_500)); break;
            case "reservado": myViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ReservaApplication.getAppContext(), R.color.red_500)); break;
            case "terminado": myViewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(ReservaApplication.getAppContext(), R.color.blue_500)); break;
            default:break;
        }
        myViewHolder.nya.setText(turnos.get(i).getNya());
        myViewHolder.hora.setText(turnos.get(i).getHora());
        myViewHolder.patente.setText(turnos.get(i).getPatente());
        myViewHolder.vehiculo.setText(turnos.get(i).getVehiculo());
        if (turnos.get(i).getVehiculo().toLowerCase().contains("fiat")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_fiat);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("toyota")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_toyota);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("renault")){ myViewHolder.marca.setBackgroundResource(R.drawable.ic_renault);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("peugeot")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_peugeot);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("chevrolet")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_chevrolet);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("volkswagen")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_volkswagen);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("ford")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_ford);}
        else if (turnos.get(i).getVehiculo().toLowerCase().contains("kia")) {myViewHolder.marca.setBackgroundResource(R.drawable.ic_kia);}
        else{myViewHolder.marca.setBackgroundResource(android.R.color.transparent);}
        myViewHolder.setItemLongClickListener(new ItemLongClickListener() {
            @Override
            public void onLongItemClick(int i) {
                onLongItemListener.onLong(turnos.get(i).getEstado(), turnos.get(i).getFecha(), turnos.get(i).getHora(), turnos.get(i).getTelefono());

            }
        });
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

       TextView nya, hora, patente, estado, vehiculo;
       CardView cardView;
       ImageView marca;
       ItemLongClickListener itemLongClickListener;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        nya = itemView.findViewById(R.id.nya);
        hora = itemView.findViewById(R.id.hora);
        patente = itemView.findViewById(R.id.patente);
        vehiculo = itemView.findViewById(R.id.vehiculo);
        cardView = itemView.findViewById(R.id.cardviewturno);
        marca = itemView.findViewById(R.id.marca);
        itemView.setOnLongClickListener(this);
    }

    public void setItemLongClickListener(ItemLongClickListener ic){
        this.itemLongClickListener=ic;
    }

        @Override
        public boolean onLongClick(View view) {
        this.itemLongClickListener.onLongItemClick(getAdapterPosition());
            return false;
        }
    }
}
