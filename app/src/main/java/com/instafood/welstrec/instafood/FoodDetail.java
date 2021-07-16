package com.instafood.welstrec.instafood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instafood.welstrec.instafood.Common.Common;
import com.instafood.welstrec.instafood.Database.Database;
import com.instafood.welstrec.instafood.Model.Food;
import com.instafood.welstrec.instafood.Model.Order;
import com.instafood.welstrec.instafood.Model.Request;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    private TextView food_name,food_price;
    private ImageView food_image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    private Button btnComprar;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase

        database= FirebaseDatabase.getInstance();
        btnComprar= findViewById(R.id.BtnComprarPlato);
        foods = database.getReference("Foods");

        //Init View
        numberButton= (ElegantNumberButton) findViewById(R.id.number_button);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this,"Añadido al pedido", Toast.LENGTH_SHORT).show();
                Intent cartIntet= new Intent(FoodDetail.this,Cart.class);
                startActivity(cartIntet);
            }
        });
        food_name= (TextView)findViewById(R.id.food_name);
        food_price= (TextView)findViewById(R.id.food_price);
        food_image= (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);


        if(getIntent()!=null)
        {
            foodId= getIntent().getStringExtra("FoodId");
            if(!foodId.isEmpty())
            {
                getDetailFood(foodId);
            }
        }



    }

    private void getDetailFood(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood= dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
