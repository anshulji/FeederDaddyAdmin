package com.dev.fd.feederdaddyadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginCredentialsActivity extends AppCompatActivity {

    Button btnguest,btnrest,btndb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_credentials);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        btndb = findViewById(R.id.btndbgeneration);
        btnguest = findViewById(R.id.btnguestgeneration);
        btnrest = findViewById(R.id.btnrestaurantgeneration);


        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name","N/A");

        if(!username.equals("fdadmin"))
        {
            btnguest.setVisibility(View.GONE);
        }

        btnguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginCredentialsActivity.this,GuestGenActivity.class);
                startActivity(intent);
            }
        });

        btnrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginCredentialsActivity.this,RestGenActivity.class);
                startActivity(intent);
            }
        });

        btndb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginCredentialsActivity.this,DBGenActivity.class);
                startActivity(intent);
            }
        });


    }





}
