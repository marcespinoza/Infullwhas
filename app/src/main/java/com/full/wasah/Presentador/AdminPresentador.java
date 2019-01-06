package com.full.wasah.Presentador;

import com.full.wasah.Interface.InterfaceAdmin;
import com.full.wasah.Modelo.AdminModelo;
import com.full.wasah.Vista.AdminActivity;

public class AdminPresentador implements InterfaceAdmin.Presentador {

    InterfaceAdmin.Vista vista;
    InterfaceAdmin.Modelo modelo;

    public AdminPresentador(AdminActivity adminActivity) {
        vista = adminActivity;
        modelo = new AdminModelo(this);
    }

    @Override
    public void modificarTurno(String fecha, String hora, String telefono) {
        modelo.turnoTerminado(fecha, hora, telefono);
    }
}
