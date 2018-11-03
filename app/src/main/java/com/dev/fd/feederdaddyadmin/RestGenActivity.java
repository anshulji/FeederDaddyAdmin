package com.dev.fd.feederdaddyadmin;

import android.content.Intent;
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

import com.dev.fd.feederdaddyadmin.model.RestaurantModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestGenActivity extends AppCompatActivity {

    EditText edtusername,edtrestname, edtphone,edtpassword,edtrestaddress,edtrestareaname,edtdateofjoining;
    Spinner cityspinner,resttypespinner;
    Button btnsave;

    private String city,restid,restauranttype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_gen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        edtusername = findViewById(R.id.edtusername);
        edtrestname = findViewById(R.id.edtrestaurantname);
        edtphone = findViewById(R.id.edtphonenumber);
        edtpassword = findViewById(R.id.edtpassword);
        edtrestaddress = findViewById(R.id.edtrestaurantaddress);
        edtrestareaname = findViewById(R.id.edtrestareaname);
        edtdateofjoining = findViewById(R.id.edtdateofjoining);
        cityspinner = findViewById(R.id.spinnerselectcity);
        edtusername = findViewById(R.id.edtusername);
        btnsave = findViewById(R.id.btnsave);
        resttypespinner = findViewById(R.id.spinnerselectresttype);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference citiesref = firebaseDatabase.getReference("Cities");
        final DatabaseReference databaseReference = firebaseDatabase.getReference("RestaurantList");

        final List<String> resttype = new ArrayList<String>();

        //set rest type spinner
        resttype.add("restaurant");
        resttype.add("bakery");
        resttype.add("hotdeals");
        resttype.add("nightorders");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RestGenActivity.this, android.R.layout.simple_spinner_item, resttype);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        resttypespinner.setAdapter(dataAdapter);

        resttypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 restauranttype = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                restauranttype  = "restaurant";
            }
        });




        final List<String> cities = new ArrayList<String>();

        citiesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    cities.add(postSnapshot.getValue().toString());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RestGenActivity.this, android.R.layout.simple_spinner_item, cities);

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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                        restid = dataSnapshot1.getRef().getKey();
                }

                int restid1 = Integer.parseInt(restid);
                restid1+=1;
                restid = String.valueOf(restid1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resttid;
                if(restauranttype.equals("hotdeals"))
                    resttid = "-1";
                else if(restauranttype.equals("nightorders"))
                    resttid = "-2";
                else
                    resttid=restid;

                RestaurantModel restaurantModel = new RestaurantModel(
                        city,
                        edtpassword.getText().toString(),
                        edtphone.getText().toString(),
                        resttid,
                        edtrestname.getText().toString(),
                        edtusername.getText().toString(),
                        edtrestaddress.getText().toString(),
                        edtrestareaname.getText().toString(),
                        edtdateofjoining.getText().toString(),
                        restauranttype
                );

                databaseReference.child(restid).setValue(restaurantModel);

                Toast.makeText(RestGenActivity.this, "Uploaded successfully !", Toast.LENGTH_SHORT).show();
                finish();
            }

        });



    }
}
