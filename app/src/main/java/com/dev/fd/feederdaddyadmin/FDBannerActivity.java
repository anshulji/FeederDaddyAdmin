package com.dev.fd.feederdaddyadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.dev.fd.feederdaddyadmin.Common.Common;
import com.dev.fd.feederdaddyadmin.ViewHolder.DeleteFDBannerViewHolder;
import com.dev.fd.feederdaddyadmin.model.FDBanner;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FDBannerActivity extends AppCompatActivity {

    EditText edtbannertitle;
    Button btnaddimage, btnupload;
    ImageView imggoback;
    Spinner cityspinner;

    RecyclerView recycler_fdbanner;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference citiesref, bannerref;

    FirebaseRecyclerAdapter<FDBanner,DeleteFDBannerViewHolder> banneradapter;
    String city ="Varanasi";

    Uri saveUri=null;
    private  final int PICK_IMAGE_REQUEST =71;

    FirebaseStorage storage;
    StorageReference storageReference;

    String downloadurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdbanner);

        edtbannertitle = findViewById(R.id.edtbannertitle);
        btnaddimage = findViewById(R.id.btnaddimage);
        btnupload = findViewById(R.id.btnuploadbanner);
        cityspinner  =findViewById(R.id.cityspinner);
        imggoback = findViewById(R.id.imggoback);
        imggoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        recycler_fdbanner = findViewById(R.id.recycler_deletebanner);
        layoutManager = new LinearLayoutManager(this);
        recycler_fdbanner.setLayoutManager(layoutManager);

        //init firebase
        firebaseDatabase  = FirebaseDatabase.getInstance();
        citiesref = firebaseDatabase.getReference("Cities");

        //init storage
        storage=FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final List<String> cities = new ArrayList<String>();

        citiesref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    cities.add(postSnapshot.getValue().toString());
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FDBannerActivity.this, android.R.layout.simple_spinner_item, cities);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                cityspinner.setAdapter(dataAdapter);

                cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = parent.getItemAtPosition(position).toString();
                        loadbanners(city);
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


        btnaddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    chooseImage();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveUri==null)
                    Toast.makeText(FDBannerActivity.this, "Please select a image first!", Toast.LENGTH_SHORT).show();
                else
                {
                    uploadImage();
                }
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            saveUri = data.getData();
            btnaddimage.setText("Image Selected !");
        }
    }

    private void uploadImage() {

        if(saveUri!=null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imagefolder = storageReference.child("images/fdbanners/"+city+"/"+imageName);
            imagefolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FDBannerActivity.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                            imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadurl = uri.toString();

                                    FDBanner fdBanner = new FDBanner(uri.toString(),edtbannertitle.getText().toString());

                                    bannerref.push().setValue(fdBanner);

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FDBannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    String progressstr =  String.format("%.0f",progress);

                    mDialog.setMessage("Uploaded "+progressstr+"%");
                }
            });
        }
    }


    private void loadbanners(String city) {


        bannerref = FirebaseDatabase.getInstance().getReference(city).child("FeederDaddyBanner");

        banneradapter = new FirebaseRecyclerAdapter<FDBanner, DeleteFDBannerViewHolder>(FDBanner.class, R.layout.fdbanneritem_layout, DeleteFDBannerViewHolder.class,
                bannerref
        ) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                recycler_fdbanner.setAdapter(banneradapter);
            }

            @Override
            protected void populateViewHolder(final DeleteFDBannerViewHolder viewHolder, final FDBanner model, final int position) {

                viewHolder.txtbannername.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).fit().centerCrop().into(viewHolder.imgbanner);

                viewHolder.imgdeletebanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerref.child(banneradapter.getRef(position).getKey()).setValue(null);
                    }
                });
            }
        };

    }
}
