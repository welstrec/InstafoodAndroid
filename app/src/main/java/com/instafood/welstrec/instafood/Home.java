package com.instafood.welstrec.instafood;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instafood.welstrec.instafood.Common.Common;
import com.instafood.welstrec.instafood.Interface.ItemClickListener;
import com.instafood.welstrec.instafood.Model.Category;
import com.instafood.welstrec.instafood.Model.Food;
import com.instafood.welstrec.instafood.ViewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        //init Firebase
        database= FirebaseDatabase.getInstance();
        category= database.getReference("Category");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCanasta);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntet= new Intent(Home.this,Cart.class);
                startActivity(cartIntet);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView= navigationView.getHeaderView(0);
        txtFullName=(TextView) headerView.findViewById(R.id.txtFullName);
        Log.e("nombre","EL nombre es"+Common.currentUser.getName() );
        txtFullName.setText(Common.currentUser.getName());

        recycler_menu= (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();
    }

    private void loadMenu() {

         adapter= new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category clicItem=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                      //  Toast.makeText(Home.this, "ENTRROOOO"+clicItem.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodList= new Intent(Home.this, FoodList.class);
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.home, menu);
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
        if(id== R.id.nav_gps)
        {
             Intent i= new Intent(Home.this, gps.class);
             startActivity(i);
        }
        if(id== R.id.nav_pago)
        {
            Intent i= new Intent(Home.this, MetodoDePago.class);
            startActivity(i);
        }
        if(id==R.id.nav_cart)
        {
            Intent cartIntet= new Intent(Home.this,Cart.class);
            startActivity(cartIntet);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
