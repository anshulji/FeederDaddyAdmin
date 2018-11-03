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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryChargesAtivity extends AppCompatActivity {

    EditText edtdc,edtmindc,edtmindcdistance;
    Button btnsave;
    ImageView imggoback;
    Spinner cityspinner;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference citiesref, dcref;
    String city ="Varanasi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_charges_ativity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        edtdc = findViewById(R.id.edtdcperkm);
        edtmindc = findViewById(R.id.edtmindc);
        edtmindcdistance = findViewById(R.id.edtmindcdistance);
        cityspinner  =findViewById(R.id.cityspinner);
        btnsave = findViewById(R.id.btnsave);
        imggoback = findViewById(R.id.imggoback);
        imggoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //init firebase
        firebaseDatabase =FirebaseDatabase.getInstance();
        citiesref = firebaseDatabase.getReference("Cities");

        final List<String> cities = new ArrayList<String>();
        citiesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    cities.add(postSnapshot.getValue().toString());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DeliveryChargesAtivity.this, android.R.layout.simple_spinner_item, cities);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                cityspinner.setAdapter(dataAdapter);

                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = parent.getItemAtPosition(position).toString();
                        dcref = firebaseDatabase.getReference(city).child("DeliveryCharges");
                        loaddata();
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

                if(!edtdc.getText().toString().equals("") && !edtmindc.getText().toString().equals("") && !edtmindcdistance.getText().toString().equals(""))
                {
                    dcref.child("deliverychargeperkm").setValue(edtdc.getText().toString());
                    dcref.child("mindcdistance").setValue(edtmindcdistance.getText().toString());
                    dcref.child("mindeliverycharge").setValue(edtmindc.getText().toString());
                    Toast.makeText(DeliveryChargesAtivity.this, "Saved !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(DeliveryChargesAtivity.this, "Please fill all the required details !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loaddata() {

        dcref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    edtdc.setText(dataSnapshot.child("deliverychargeperkm").getValue().toString());
                    edtmindc.setText(dataSnapshot.child("mindeliverycharge").getValue().toString());
                    edtmindcdistance.setText(dataSnapshot.child("mindcdistance").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
