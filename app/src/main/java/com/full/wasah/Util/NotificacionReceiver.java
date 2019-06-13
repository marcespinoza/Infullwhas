package com.full.wasah.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificacionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, NotificacionReserva.class);
        context.startService(myIntent);
    }
}
