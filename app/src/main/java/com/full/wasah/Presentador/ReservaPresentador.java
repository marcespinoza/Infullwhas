package com.full.wasah.Presentador;

import com.full.wasah.Interface.InterfaceReserva;
import com.full.wasah.Modelo.ReservaModelo;
import com.full.wasah.Vista.MainActivity;

public class ReservaPresentador implements InterfaceReserva.Presentador {

    InterfaceReserva.Vista vista;
    InterfaceReserva.Modelo modelo;

    public ReservaPresentador(MainActivity mainActivity) {
        vista = mainActivity;
        modelo = new ReservaModelo(this);
    }


    @Override
    public void getDatos(String nya, String patente, String vehiculo, String telefono, String fecha, String hora) {
        if(nya.isEmpty() || patente.isEmpty() || vehiculo.isEmpty() || telefono.isEmpty()){
            vista.errorCampoVacio();
        }else{
            modelo.guardarTurno(nya, patente, vehiculo, telefono, fecha, hora);
        }
    }

    @Override
    public void guardadoCorrecto() {
        vista.mensajeExito();
    }


}
