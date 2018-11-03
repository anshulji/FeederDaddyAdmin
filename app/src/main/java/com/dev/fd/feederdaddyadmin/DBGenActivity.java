package com.dev.fd.feederdaddyadmin;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.fd.feederdaddyadmin.model.AdminModel;
import com.dev.fd.feederdaddyadmin.model.DeliveryBoysModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DBGenActivity extends AppCompatActivity {

    private EditText username, password, name, fname, mname, pa, ca, ad, dl,pn, doj;
    private Spinner cityspinner;
    private Button btnsave;

    private String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbgen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        name = findViewById(R.id.edtname);
        fname  = findViewById(R.id.edtfathername);
        mname = findViewById(R.id.edtmothername);
        pa = findViewById(R.id.edtpermanentaddress);
        ca = findViewById(R.id.edtcorrespondanceaddress);
        ad = findViewById(R.id.edtaadhardetail);
        dl= findViewById(R.id.edtdrivinglicence);
        pn = findViewById(R.id.edtphonenumber);
        doj = findViewById(R.id.edtdateofjoining);
        username = findViewById(R.id.edtusername);
        password = findViewById(R.id.edtpassword);
        cityspinner = findViewById(R.id.spinnerselectcity);
        btnsave = findViewById(R.id.btnsave);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference citiesref = firebaseDatabase.getReference("Cities");
        final DatabaseReference databaseReference = firebaseDatabase.getReference("DeliverBoys");

        final List<String> cities = new ArrayList<String>();

        citiesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    cities.add(postSnapshot.getValue().toString());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DBGenActivity.this, android.R.layout.simple_spinner_item, cities);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                cityspinner.setAdapter(dataAdapter);

                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        city  = "Varanasi";
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeliveryBoysModel deliveryBoysModel = new DeliveryBoysModel(
                        username.getText().toString(),
                        password.getText().toString(),
                        name.getText().toString(),
                        fname.getText().toString(),
                        mname.getText().toString(),
                        pa.getText().toString(),
                        ca.getText().toString(),
                        ad.getText().toString(),
                        dl.getText().toString(),
                        pn.getText().toString(),
                        city,
                        doj.getText().toString()
                );

                databaseReference.push().setValue(deliveryBoysModel);
                Toast.makeText(DBGenActivity.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
