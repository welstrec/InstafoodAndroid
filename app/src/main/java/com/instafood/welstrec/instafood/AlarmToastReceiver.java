package com.instafood.welstrec.instafood;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AlarmToastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        calcularFecha(context);
    }

    public void calcularFecha(Context context){
        Calendar cal= Calendar.getInstance();
        int hora= cal.getTime().getHours();
        int min=cal.getTime().getMinutes();
        if(hora>14)
        {
            Log.e("entro","aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            Toast.makeText(context,"ALARMAAAAAAAAAAAAAAA",Toast.LENGTH_LONG).show();
        }
    }

}
