package com.full.wasah.Interface;

public interface InterfaceReserva {

    interface Modelo{
        void guardarTurno(String nya, String patente, String vehiculo, String celular, String fecha, String hora);
    }
    interface Presentador{
        void getDatos(String nya, String patente, String vehiculo, String celular, String fecha, String hora);
        void guardadoCorrecto();
    };
    interface Vista{
        void getDatos();
        void errorCampoVacio();
        void mensajeExito();
    };

}
