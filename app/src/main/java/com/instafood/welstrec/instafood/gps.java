package com.instafood.welstrec.instafood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class gps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Button btnGetLocl;
    private Button btnGuardarLoc;
    private Button btnCambiarDir;
    private EditText textoDireccion;
    private TextView textoDireccionArriba;
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationrequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private LinearLayout layoutDirecciones;
    private LinearLayout layoutCambiarDir;
    private  TextView textoDireccionGuardada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        btnGetLocl = (Button) findViewById(R.id.btnGetLoc);
        btnGuardarLoc=(Button) findViewById(R.id.btnGuardarLoc);
        textoDireccion = (EditText) findViewById(R.id.textoDireccion);
        textoDireccionArriba = (TextView) findViewById(R.id.textViewDireccion);
        layoutDirecciones= (LinearLayout) findViewById(R.id.LayoutLinearGPS);
        layoutCambiarDir= (LinearLayout)findViewById(R.id.LayoutCambiardir);
        textoDireccionGuardada=(TextView) findViewById(R.id.textoDirG);
        btnCambiarDir=(Button)findViewById(R.id.btnCambiarDir);

        //-------------------------

        if (ActivityCompat.checkSelfPermission(gps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(gps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }

        ActivityCompat.requestPermissions(gps.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        btnGetLocl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayLocation();
                startLocationUpdates();
                guardarUbicacion(textoDireccionArriba.getText().toString());
                Intent a= new Intent(gps.this, Main2Activity.class);
                startActivity(a);
            }
        });
        btnGuardarLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textoDireccion.getText().toString().isEmpty())
                {
                   tooglePeriodicLocationUpdates();
                    Geocoder coder = new Geocoder(gps.this);
                    String dirreccionEscrita=textoDireccion.getText().toString();
                    List<Address> address;
                    try {
                        Log.e("En hora buena",""+dirreccionEscrita);
                        address = coder.getFromLocationName(dirreccionEscrita,5);
                        if(address == null || address.isEmpty())
                        {
                            Toast.makeText(getBaseContext(),"Quizas olvidaste la ciudad o el campo esta incompleto",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Address location=address.get(0);
                            double latitud= location.getLatitude();
                            double longitud= location.getLongitude();
                            validarZona(latitud,longitud);
                            guardarUbicacion(dirreccionEscrita);
                            Intent a= new Intent(gps.this, Main2Activity.class);
                            startActivity(a);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(getBaseContext(),"El campo no debe ser vacio",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCambiarDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCambiarDir.setVisibility(View.INVISIBLE);
                layoutDirecciones.setVisibility(View.VISIBLE);
            }
        });


    }
    private void guardarUbicacion(String direccion)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GPS", direccion);
        editor.commit();
    }
    private String cargarPosicion()
    {

        String fileNameString = "Preferencias";
        SharedPreferences sharedPreferences;
        String dire="";
        if(!fileNameString.isEmpty()) {
            sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
            dire=sharedPreferences.getString("GPS","");
        }
        else {

        }
        return dire;
    }

    private void tooglePeriodicLocationUpdates() {
        if(!mRequestingLocationUpdates)
        {
            mRequestingLocationUpdates= true;
            startLocationUpdates();
            stopLocationUpdates();
        }
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,gps.this);

    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(gps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(gps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            double latitud = mLastLocation.getLatitude();
            double longitud = mLastLocation.getLongitude();
            validarZona(latitud,longitud);
            //-------------------
            Geocoder geocoder = new Geocoder(gps.this, Locale.getDefault());;
            List<Address> addresses;
            ///-----------------------------

            String dire=cargarPosicion();

            textoDireccionGuardada.setText(dire);

            if(!dire.isEmpty())
            {
                layoutDirecciones.setVisibility(View.INVISIBLE);
            }
            else if(dire.isEmpty())
            {
                layoutCambiarDir.setVisibility(View.INVISIBLE);
            }
            String address="";
            try {
                addresses = geocoder.getFromLocation(latitud, longitud, 1);
                address = addresses.get(0).getAddressLine(0);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //-----------------
            textoDireccionArriba.setText("Dirección: "+address);
        } else {
            textoDireccion.setHint("No se ha podido obtener la localización");
        }
    }

    private void createLocationRequest() {
        mLocationrequest = new LocationRequest();
        mLocationrequest.setInterval(UPDATE_INTERVAL);
        mLocationrequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationrequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(gps.this).
                addConnectionCallbacks(gps.this).
                addOnConnectionFailedListener(gps.this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(gps.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, gps.this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "El equipo no soportado por Google play", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;

    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Sigiente linea error: java.lang.NullPointerException: Attempt to read from field 'int com.google.android.gms.location.LocationRequest.a' on a null object reference
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationrequest, gps.this);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if(mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }

    }



    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiClient();
                    }
                }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation= location;
        displayLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
            String dire=cargarPosicion();
            if(!dire.isEmpty())
            {
                layoutDirecciones.setVisibility(View.INVISIBLE);
            }
            else if(dire.isEmpty())
            {
                layoutCambiarDir.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,gps.this);
        if(mGoogleApiClient!= null)
        {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    private void validarZona(double latitud, double longitud)
    {
        Log.e("Dir","longitud: "+longitud+" "+"latitud: "+latitud);
        //Zona 1
        if((4.670>latitud)&&(latitud>4.625))
        {
            if((-74.065>longitud && -74.0873<longitud))
            {
                Log.e("Zona1","ENTRTRRROOOOOOOOOOOOOOO");
                FirebaseMessaging.getInstance().subscribeToTopic("zona1");
            }
        }
        else if((4.640>latitud)&&(latitud>4.625))
        {
            if((-74.085<longitud && -74.063>longitud))
            {
                Log.e("Zona2","ENTRTRRROOOOOOOOOOOOOOO");
                FirebaseMessaging.getInstance().subscribeToTopic("zona2");
            }
        }
        else
        {
            Toast.makeText(getBaseContext(),"Esta Zona aun no esta disponible", Toast.LENGTH_LONG).show();
        }
    }
}
