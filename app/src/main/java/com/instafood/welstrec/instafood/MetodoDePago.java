package com.instafood.welstrec.instafood;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MetodoDePago extends AppCompatActivity{

    public Button botonEfectivo;
    public Button botonTarjeta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_de_pago);
        botonEfectivo= (Button)findViewById(R.id.botonPagoEfectivo);
        botonTarjeta=(Button) findViewById(R.id.botonTarjetaDeCredito);
        //----------------------------
        botonEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPreferenciaPago("efectivo");
                Toast.makeText(MetodoDePago.this,"Pago preferido Efectivo",Toast.LENGTH_LONG).show();
                Intent p= new Intent(MetodoDePago.this, Main2Activity.class);
                startActivity(p);
            }
        });
        botonTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPreferenciaPago("tarjeta");
                Toast.makeText(MetodoDePago.this,"Pago preferido Tarjeta de credito",Toast.LENGTH_LONG).show();
                Intent p= new Intent(MetodoDePago.this, Main2Activity.class);
                startActivity(p);
            }
        });

    }
    ///---------------
    private void guardarPreferenciaPago(String preferencia)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferenciax", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PAGO", preferencia);
        editor.commit();
    }
}
