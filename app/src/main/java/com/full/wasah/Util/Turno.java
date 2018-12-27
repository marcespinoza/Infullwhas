package com.full.wasah.Util;

import java.sql.Time;
import java.util.Date;

public class Turno {

String vehiculo;
String patente;
String telefono;
String fecha;
String hora;
String estado;
String nya;

    public Turno() {
    }

    public String getNya() {
        return nya;
    }

    public void setNya(String nya) {
        this.nya = nya;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String  fecha) {
        this.fecha = fecha;
    }

    public String  getHora() {
        return hora;
    }

    public void setHora(String  hora) {
        this.hora = hora;
    }


}
