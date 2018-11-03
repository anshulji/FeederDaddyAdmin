package com.dev.fd.feederdaddyadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.dev.fd.feederdaddyadmin.model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MasterAdminActivity extends AppCompatActivity {

    CardView cvco,cvoncbr, cvao, cvoa, cvcao, cvcg, cvlc, cvre, cved, cvpo,cvfdb,cvdc,cvlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_admin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        cvco = findViewById(R.id.cvcurrentorders);
        cvoncbr = findViewById(R.id.cvordersnotconfirmedbyrestaurant);
        cvao = findViewById(R.id.cvadvanceorders);
        cvoa = findViewById(R.id.cvordersassigned);
        cvcao = findViewById(R.id.cvcancelledorders);
        cvcg = findViewById(R.id.cvcoupongeneration);
        cvlc = findViewById(R.id.cvlogincredentials);
        cvre = findViewById(R.id.cvrestaurants);
        cved = findViewById(R.id.cvemployeedetails);
        cvpo = findViewById(R.id.cvpastorders);
        cvfdb = findViewById(R.id.cvfdbanner);
        cvdc = findViewById(R.id.cvdeliverycharges);
        cvlogout = findViewById(R.id.cvlogout);

        cvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone", "N/A");
                    editor.commit();
                Intent intent = new Intent(MasterAdminActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name","N/A");

        if(!username.equals("fdadmin"))
        {
            cvcg.setVisibility(View.GONE);
            cvre.setVisibility(View.GONE);
            cved.setVisibility(View.GONE);
            cvfdb.setVisibility(View.GONE);
            cvdc.setVisibility(View.GONE);
        }

        cvco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","co");
                startActivity(intent);
            }
        });
        cvoncbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","oncbr");
                startActivity(intent);
            }
        });

        cvao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","ao");
                startActivity(intent);
            }
        });

        cvoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","oa");
                startActivity(intent);
            }
        });

        cvcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","cao");
                startActivity(intent);
            }
        });

        cvpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CurrentOrdersActivity.class);
                intent.putExtra("orderstatus","po");
                startActivity(intent);
            }
        });
        cvlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, LoginCredentialsActivity.class);
                startActivity(intent);
            }
        });
        cvcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, CouponGenerationActivity.class);
                startActivity(intent);
            }
        });
        cvfdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, FDBannerActivity.class);
                startActivity(intent);
            }
        });
        cvdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MasterAdminActivity.this, DeliveryChargesAtivity.class);
                startActivity(intent);
            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone","N/A");
        String city = sharedPreferences.getString("city","N/A");
        String zone = sharedPreferences.getString("zone","N/A");

        Token data = new Token(token,city+zone+"1");
        tokens.child(phone).setValue(data);
    }
}
