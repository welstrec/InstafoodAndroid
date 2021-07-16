package com.instafood.welstrec.instafood;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GPStracker implements LocationListener{

    Context context;
    public GPStracker(Context c)
    {
        context=c;
    }

    public Location getLocation()
    {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(context,"No se le ha dado permiso a tu aplicacion", Toast.LENGTH_LONG);
            return null;
        }
        LocationManager lm=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable= lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable)
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
            Location l= lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return  l;
        }
        else
        {
            Toast.makeText(context,"Habilita el GPS", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
