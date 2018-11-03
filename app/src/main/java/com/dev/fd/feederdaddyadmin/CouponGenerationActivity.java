package com.dev.fd.feederdaddyadmin;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.fd.feederdaddyadmin.ViewHolder.CouponGenerationViewHolder;
import com.dev.fd.feederdaddyadmin.ViewHolder.DeleteFDBannerViewHolder;
import com.dev.fd.feederdaddyadmin.model.CouponCode;
import com.dev.fd.feederdaddyadmin.model.FDBanner;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CouponGenerationActivity extends AppCompatActivity {

    EditText edtcouponcode, edtcoupondiscount;
    Button btnaddcoupon;
    Spinner cityspinner;
    ImageView imggoback;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String city ="Varanasi";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference citiesref, couponref;

    FirebaseRecyclerAdapter<CouponCode,CouponGenerationViewHolder> couponadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_generation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        edtcouponcode = findViewById(R.id.edtsetcouponcode);
        edtcoupondiscount = findViewById(R.id.edtcoupondiscount);
        btnaddcoupon = findViewById(R.id.btnaddcoupon);
        cityspinner  =findViewById(R.id.cityspinner);
        imggoback = findViewById(R.id.imggoback);
        recyclerView = findViewById(R.id.recycler_coupon);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        imggoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //init firebase
        firebaseDatabase  = FirebaseDatabase.getInstance();
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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CouponGenerationActivity.this, android.R.layout.simple_spinner_item, cities);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                cityspinner.setAdapter(dataAdapter);

                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = parent.getItemAtPosition(position).toString();
                        loadCouponList(city);
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

        btnaddcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                couponref = FirebaseDatabase.getInstance().getReference(city).child("CouponCodes");
                String key = couponref.push().getKey();
                if(!edtcouponcode.getText().toString().equals("") && !edtcoupondiscount.getText().toString().equals("")) {
                    couponref.child(key).child("name").setValue(edtcouponcode.getText().toString());
                    couponref.child(key).child("discount").setValue(edtcoupondiscount.getText().toString());
                }
                else
                    Toast.makeText(CouponGenerationActivity.this, "Please fill the required details", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadCouponList(String city) {
        couponref = FirebaseDatabase.getInstance().getReference(city).child("CouponCodes");

        couponadapter = new FirebaseRecyclerAdapter<CouponCode, CouponGenerationViewHolder>(CouponCode.class, R.layout.couponcode_itemlayout, CouponGenerationViewHolder.class,
                couponref
        ) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                recyclerView.setAdapter(couponadapter);
            }

            @Override
            protected void populateViewHolder(final CouponGenerationViewHolder viewHolder, final CouponCode model, final int position) {

                viewHolder.txtcouponcode.setText(model.getName());
                viewHolder.txtcoupondiscount.setText(model.getDiscount());
                viewHolder.imgdeletecoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        couponref.child(couponadapter.getRef(position).getKey()).setValue(null);
                    }
                });
            }
        };
    }
}
