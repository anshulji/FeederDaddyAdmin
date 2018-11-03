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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuestGenActivity extends AppCompatActivity {

    private EditText name, fname, mname, pa, ca, ad, pn, doj, password;
    private Spinner cityspinner, zonespinner;
    private Button btnsave;

    private String city, zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_gen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        name = findViewById(R.id.edtguestadminname);
        fname  = findViewById(R.id.edtfathername);
        mname = findViewById(R.id.edtmothername);
        pa = findViewById(R.id.edtpermanentaddress);
        ca = findViewById(R.id.edtcorrespondanceaddress);
        ad = findViewById(R.id.edtaadhardetail);
        pn = findViewById(R.id.edtphonenumber);
        doj = findViewById(R.id.edtdateofjoining);
        password = findViewById(R.id.edtpassword);
        cityspinner = findViewById(R.id.spinnerselectcity);
        zonespinner = findViewById(R.id.spinnerselectzone);

        btnsave = findViewById(R.id.btnsave);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference citiesref = firebaseDatabase.getReference("Cities");
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Admins");

        final List<String> cities = new ArrayList<String>();

        citiesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    cities.add(postSnapshot.getValue().toString());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GuestGenActivity.this, android.R.layout.simple_spinner_item, cities);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                cityspinner.setAdapter(dataAdapter);

                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = parent.getItemAtPosition(position).toString();

                        DatabaseReference zoneref = firebaseDatabase.getReference(city).child("Zones");
                        final List<String> zoneslist = new ArrayList<String>();
                        zoneref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    zoneslist.add(postSnapshot.getValue().toString());
                                }

                                // Creating adapter for spinner
                                ArrayAdapter<String> zoneadapter = new ArrayAdapter<String>(GuestGenActivity.this, android.R.layout.simple_spinner_item, zoneslist);

                                // Drop down layout style - list view with radio button
                                zoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                zonespinner.setAdapter(zoneadapter);
                                zonespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        zone = parent.getItemAtPosition(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


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

                AdminModel adminModel = new AdminModel(
                        name.getText().toString(),
                        city,
                        zone,
                        pn.getText().toString(),
                        password.getText().toString(),
                        fname.getText().toString(),
                        mname.getText().toString(),
                        pa.getText().toString(),
                        ca.getText().toString(),
                        ad.getText().toString(),
                        doj.getText().toString()
                );

                databaseReference.push().setValue(adminModel);
                Toast.makeText(GuestGenActivity.this, "Uploaded successfully!", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
