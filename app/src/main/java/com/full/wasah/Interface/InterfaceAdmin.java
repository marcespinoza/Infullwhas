package com.full.wasah.Interface;

public interface InterfaceAdmin {

    interface Vista{
        void toastExito();
    };

    interface Presentador{
        void modificarTurno(String fecha, String hora, String telefono);
    };

    interface Modelo{
        void turnoTerminado(String fecha, String hora, String telefono);
    };

}
