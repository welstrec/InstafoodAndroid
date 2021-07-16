package com.instafood.welstrec.instafood;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instafood.welstrec.instafood.Common.Common;
import com.instafood.welstrec.instafood.Database.Database;
import com.instafood.welstrec.instafood.Model.Order;
import com.instafood.welstrec.instafood.Model.Request;
import com.instafood.welstrec.instafood.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalPrice;
    Button btnPlace;
    List<Order> cart= new ArrayList<>();
    CartAdapter adapter;
    private String metodoPago;


    //-------------
    BillingProcessor bp;
    private static final String ITEM_SKU="android.test.purchased";
    //------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bp= new BillingProcessor(Cart.this, null,Cart.this);
        //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        //Init
        recyclerView=(RecyclerView)findViewById(R.id.listCar);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice= (TextView) findViewById(R.id.total);
        btnPlace= (Button)findViewById(R.id.btnPlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String laDir= cargarPosicion();
                String prefernciaPago=cargarPreferenciaDePago();
                Log.e("","Metodo"+ prefernciaPago);
                if(prefernciaPago.isEmpty())
                {
                    Intent p= new Intent(Cart.this, MetodoDePago.class);
                    startActivity(p);
                }
                else
                {
                    metodoPago=prefernciaPago;
                    if(prefernciaPago.equalsIgnoreCase("efectivo"))
                    {
                        hacerPedido(prefernciaPago);
                    }
                    else
                    {
                        if((!laDir.isEmpty()))
                        {

                            bp.consumePurchase(ITEM_SKU);
                            bp.purchase(Cart.this, ITEM_SKU);
                        }
                        else
                        {
                            Toast.makeText(Cart.this,"Aun no se ha añadido una dirección ó contiene mas de un almuerzo",Toast.LENGTH_LONG).show();
                            Intent p= new Intent(Cart.this, gps.class);
                            startActivity(p);
                        }
                    }
                }


            }
        });

        loadListFood();
    }
    private void hacerPedido(String metodo)
    {
        String laDir= cargarPosicion();
        if((!laDir.isEmpty()))
        {
            if(!(cart.size()>1))
            {
                Request request= new Request("zona1",
                        Common.currentUser.getName(),
                        laDir,
                        txtTotalPrice.getText().toString(),
                        cart,
                        metodoPago
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Listo, tu almuerzo estara listo para mañana a las 1 P.M", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(Cart.this,"El carro contiene mas de un almuerzo",Toast.LENGTH_LONG).show();
                new Database(getBaseContext()).cleanCart();
            }
        }
        else
        {
            Toast.makeText(Cart.this,"Aun no se ha añadido una dirección",Toast.LENGTH_LONG).show();
            Intent p= new Intent(this,gps.class);
            startActivity(p);
        }
    }

    private void loadListFood() {

        cart= new Database(this).getCarts();
        adapter= new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);
        int total=0;
        for(Order order:cart)
        {
            total+=(Integer.parseInt(order.getPrice())*(Integer.parseInt(order.getQuatity())));
        }
        Locale locale= new Locale("es","CO");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
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
        return dire;
    }
    private String cargarPreferenciaDePago()
    {
        String fileNameString = "Preferenciax";
        SharedPreferences sharedPreferences;
        String pref="";
        if(!fileNameString.isEmpty()) {
            sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
            pref=sharedPreferences.getString("PAGO","");
        }
        return pref;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        hacerPedido(metodoPago);
        Intent p= new Intent(Cart.this, Main2Activity.class);
        bp.release();
        bp=null;
        startActivity(p);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(!bp.handleActivityResult(requestCode,resultCode,data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if(bp!=null)
        {
            bp.release();
        }
        super.onDestroy();
    }
}
