package com.instafood.welstrec.instafood;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.instafood.welstrec.instafood.Common.Common;
import com.instafood.welstrec.instafood.Model.User;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private ImageView imagenPerfil;
    private TextView nombrePerfil;
    private FirebaseUser currentUser;
    private Button menuDia;
    private Button programadorCliclos;
    private Button botonGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationViewx=(NavigationView) findViewById(R.id.nav_viewPrincipal);
        navigationViewx.setNavigationItemSelectedListener(this);
        menuDia=(Button) findViewById(R.id.botonOrdenDia);
        programadorCliclos=(Button) findViewById(R.id.botonProgramador);
        botonGPS= (Button) findViewById(R.id.botongps);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        View headerView = navigationViewx.getHeaderView(0);
        imagenPerfil=(ImageView) headerView.findViewById(R.id.imagenPerfil);
        nombrePerfil= (TextView)headerView.findViewById(R.id.nombrePerfil);
        nombrePerfil.setText(currentUser.getDisplayName().toString());
        User usuario= new User();
        ///----------------
        //-----------------
        usuario.setName(currentUser.getDisplayName().toString());
        usuario.setPassword(currentUser.getUid().toString());
        Common.currentUser= usuario;
        if (!currentUser.getProviderData().isEmpty() && currentUser.getProviderData().size() > 1)
        {
            String URL = "https://graph.facebook.com/" + currentUser.getProviderData().get(1).getUid() + "/picture?type=large";
            Picasso.with(getBaseContext()).load(URL).into(imagenPerfil);
        }

        String [] primerNombre= usuario.getName().split(" ");
        setTitle("Bienvenido, "+primerNombre[0]);
        programadorCliclos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Main2Activity.this, Calendario.class);
                startActivity(i);
            }
        });
        menuDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Main2Activity.this, Home.class);
                startActivity(i);
            }
        });
        botonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Main2Activity.this, gps.class);
                startActivity(i);
            }
        });

        updateMenu();

    }
    private void updateMenu()
    {
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_almuerzo) {
            Intent i= new Intent(Main2Activity.this, Home.class);
            startActivity(i);
        } else if (id == R.id.nav_organizador) {
            Intent i= new Intent(Main2Activity.this, Calendario.class);
            startActivity(i);
        }  else if (id == R.id.nav_salir) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            updateUI(null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent singUp= new Intent(Main2Activity.this, MainActivity.class);
        startActivity(singUp);
        finish();
    }
}
