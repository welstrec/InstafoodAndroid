package com.instafood.welstrec.instafood;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instafood.welstrec.instafood.Model.User;

public class SignUp extends AppCompatActivity {
    EditText edtPhone, edtName,edtPassword, edtPassword2;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName= (EditText)findViewById(R.id.edtName);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtPassword2=(EditText)findViewById(R.id.edtPassword2);
        btnSignUp= (Button)findViewById(R.id.btnSignUpx);
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog= new ProgressDialog(SignUp.this);
                mDialog.setMessage("Espere por favor...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this,"La cuenta ya existe",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(edtPassword.getText().toString().contentEquals(edtPassword2.getText().toString()))
                            {
                                mDialog.dismiss();
                                User user= new User(edtName.getText().toString(),edtPassword2.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this,"Se registro!!!",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this,"Las contraseñas deben ser iguales",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
