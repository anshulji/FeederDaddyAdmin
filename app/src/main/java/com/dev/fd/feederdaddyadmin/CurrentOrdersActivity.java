package com.dev.fd.feederdaddyadmin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.fd.feederdaddyadmin.Common.Common;
import com.dev.fd.feederdaddyadmin.Remote.APIService;
import com.dev.fd.feederdaddyadmin.Service.MyAlarm;
import com.dev.fd.feederdaddyadmin.ViewHolder.ChooseDBViewHolder;
import com.dev.fd.feederdaddyadmin.ViewHolder.OrderViewHolder;
import com.dev.fd.feederdaddyadmin.model.ChooseDeliveryBoy;
import com.dev.fd.feederdaddyadmin.model.MyResponse;
import com.dev.fd.feederdaddyadmin.model.Notification;
import com.dev.fd.feederdaddyadmin.model.Request;
import com.dev.fd.feederdaddyadmin.model.Sender;
import com.dev.fd.feederdaddyadmin.model.Token;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentOrdersActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public  RecyclerView.LayoutManager layoutManager;
    private ImageView imggoback;
    private TextView txttbhead;

    private static final int REQUEST_PHONE_CALL = 1;

    private int lastpos=-1;

    private String dbphone,dbcurrentload,iscaopage="0";

    private FirebaseDatabase database;
    private Query requests;
    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    private FirebaseRecyclerAdapter<ChooseDeliveryBoy,ChooseDBViewHolder> choosedbadapter;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //init service
        mService = Common.getFCMClient();

        txttbhead = findViewById(R.id.txttoolbarhead);


        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String city = sharedPreferences.getString("city","N/A");
        String zone = sharedPreferences.getString("zone","N/A");
        String username = sharedPreferences.getString("name","N/A");

        //firebase init
        database = FirebaseDatabase.getInstance();
        if(username.equals("fdadmin"))
        {   if(getIntent().getStringExtra("orderstatus").equals("co")) {
                requests = database.getReference("CurrentRequests").orderByChild("orderstatus").equalTo("1");
                txttbhead.setText("Current Orders");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("oncbr")){
                requests = database.getReference("CurrentRequests").orderByChild("orderstatus").equalTo("-2");
                txttbhead.setText("ONC By Restaurant");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("ao")){
                requests = database.getReference("CurrentRequests").orderByChild("orderstatus").equalTo("11");
                txttbhead.setText("Advance Orders");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("oa")){
                    requests = database.getReference("CurrentRequests").orderByChild("adminstatus").equalTo("3");
                txttbhead.setText("Orders Assigned");
            }

            else if(getIntent().getStringExtra("orderstatus").equals("cao")){
                    requests = database.getReference("CurrentRequests").orderByChild("orderstatus").equalTo("-1");
                    iscaopage="1";
                txttbhead.setText("Cancelled Orders");
            }

            else if(getIntent().getStringExtra("orderstatus").equals("po")){
                    requests = database.getReference("CurrentRequests").orderByChild("adminstatus").equalTo("4");
                txttbhead.setText("Past Orders");
            }
        }
        else {  if(getIntent().getStringExtra("orderstatus").equals("co")) {
            requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"1");
            txttbhead.setText("Current Orders");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("oncbr")){
                requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"-2");
                txttbhead.setText("ONC By Restaurant");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("ao")){
                requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"11");
                txttbhead.setText("Advance Orders");
            }
            else if(getIntent().getStringExtra("orderstatus").equals("oa")){
                requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"3");
                txttbhead.setText("Orders Assigned");
            }

            else if(getIntent().getStringExtra("orderstatus").equals("cao")){
            iscaopage="1";
                requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"-1");
                txttbhead.setText("Cancelled Orders");
            }

            else if(getIntent().getStringExtra("orderstatus").equals("po")){
                requests = database.getReference("CurrentRequests").orderByChild("city_zone_status").equalTo(city+zone+"4");
                txttbhead.setText("Past Orders");
            }
        }
        recyclerView = findViewById(R.id.recycler_currentorders);
        imggoback = findViewById(R.id.imggoback);

        imggoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadCurrentOrders();
    }

    private void loadCurrentOrders() {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.currentorder_item_layout,
                OrderViewHolder.class,
                requests
        ) {

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            protected void populateViewHolder(final OrderViewHolder viewHolder, final Request model, final int position) {

                if(!model.getOrderreceivetime().equals("ASAP")) {
                    viewHolder.rlort.setVisibility(View.VISIBLE);
                    viewHolder.txtorderreceivetime.setText("Order to be delivered at "+model.getOrderreceivetime()+" today.");
                }
                else viewHolder.rlort.setVisibility(View.GONE);

                if(iscaopage.equals("1"))
                {
                    viewHolder.rldbinfo.setVisibility(View.GONE);
                }

                String orderidstr = adapter.getRef(position).getKey();
                orderidstr = "#"+orderidstr.substring(1,orderidstr.length());
                viewHolder.txtorderid.setText(orderidstr);
                viewHolder.txtordertime.setText(Common.getDate(Long.parseLong(model.getTimeinms())));

                viewHolder.txtcustaddress.setText(model.getCustomeraddress());
                viewHolder.txtcustphone.setText(model.getCustomerphone());
                viewHolder.imgcallcust.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getCustomerphone()));
                        if (ContextCompat.checkSelfPermission(CurrentOrdersActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CurrentOrdersActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            startActivity(intent);
                        }
                    }
                });

                viewHolder.txtrestname.setText(model.getRestaurantname());
                viewHolder.txtrestphone.setText(model.getRestaurantphone());
                viewHolder.imgcallrest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getRestaurantphone()));
                        if (ContextCompat.checkSelfPermission(CurrentOrdersActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CurrentOrdersActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            startActivity(intent);
                        }
                    }
                });

                if(model.getAdminstatus().equals("2") || model.getAdminstatus().equals("3") || model.getAdminstatus().equals("4"))
                {
                    viewHolder.btncancel.setVisibility(View.GONE);
                    viewHolder.btnaccept.setVisibility(View.VISIBLE);
                    viewHolder.btnaccept.setText("ACCEPTED");
                    viewHolder.btnaccept.setEnabled(false);
                    viewHolder.txtdeliveryboyname.setText(model.getDeliveryboyname());
                    String otp = model.getTimeinms();
                    otp = otp.substring(otp.length()-8,otp.length()-4);
                    viewHolder.txtotp.setText("OTP : "+otp);
                }
                else if(model.getAdminstatus().equals("-1"))
                {
                    viewHolder.btnaccept.setVisibility(View.GONE);
                    viewHolder.btncancel.setVisibility(View.VISIBLE);
                    viewHolder.btncancel.setText("CANCELLED");
                    viewHolder.txtordertime.setText(Common.getDate(Long.parseLong(model.getTimeinms()))+"\n"+model.getOrderstatusmessage());
                    viewHolder.btncancel.setEnabled(false);
                    viewHolder.rldbinfo.setVisibility(View.GONE);
                }
                else if(model.getAdminstatus().equals("1") || model.getAdminstatus().equals("11"))
                {
                    viewHolder.btnaccept.setVisibility(View.VISIBLE);
                    viewHolder.btncancel.setVisibility(View.VISIBLE);
                    viewHolder.btncancel.setEnabled(true);
                    viewHolder.btnaccept.setEnabled(true);
                    viewHolder.btncancel.setText("Cancel");
                    viewHolder.btnaccept.setText("Accept");
                    viewHolder.rldbinfo.setVisibility(View.GONE);
                }


                int totalamount =Integer.parseInt(model.getTotalamount());
                viewHolder.txttotalamount.setText("Total: ₹"+totalamount);

                viewHolder.btnaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        lastpos=-1;

                        final DatabaseReference orderstatusref = database.getReference("CurrentRequests")
                                .child(adapter.getRef(position).getKey());

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CurrentOrdersActivity.this);
                        alertDialog.setTitle("Choose Delivery Boy");

                        final DatabaseReference dbref = database.getReference(model.getCity()).child("DeliveryBoys");

                        final LayoutInflater inflater = getLayoutInflater();
                        final View add_menu_lauout = inflater.inflate(R.layout.choosedeliveryboylayout,null);

                        final RecyclerView recycler_choosedb = add_menu_lauout.findViewById(R.id.recycler_choosedb);
                        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(CurrentOrdersActivity.this);
                        recycler_choosedb.setLayoutManager(layoutManager);

                        dbphone="0";
                        choosedbadapter = new FirebaseRecyclerAdapter<ChooseDeliveryBoy, ChooseDBViewHolder>(
                                ChooseDeliveryBoy.class,
                                R.layout.choosedbitemlayout,
                                ChooseDBViewHolder.class,
                                dbref.orderByChild("isavailable").equalTo("1")
                        ) {
                            @Override
                            protected void onDataChanged() {
                                super.onDataChanged();
                                recycler_choosedb.setAdapter(choosedbadapter);
                            }

                            @Override
                            protected void populateViewHolder(final ChooseDBViewHolder viewHolder, final ChooseDeliveryBoy model, final int position) {
                                viewHolder.txtdbname.setText(model.getName());
                                viewHolder.txtdbname.setTextColor(getResources().getColor(android.R.color.black));

                                viewHolder.txtdbcurrentload.setText(model.getCurrentload());

                                viewHolder.txtdbname.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        orderstatusref.child("deliveryboyname").setValue(model.getName());
                                        orderstatusref.child("deliveryboyphone").setValue(model.getDbphone());
                                        dbphone = model.getDbphone();
                                        dbcurrentload = model.getCurrentload();
                                        viewHolder.txtdbname.setTextColor(getResources().getColor(R.color.colorAccent));
                                        if(lastpos!=-1)
                                        {notifyItemChanged(lastpos); lastpos=position;
                                        }
                                        else
                                            lastpos=position;

                                        Toast.makeText(CurrentOrdersActivity.this, ""+lastpos, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };

                        alertDialog.setView(add_menu_lauout);
                        alertDialog.setIcon(R.drawable.ic_person_black_24dp);

                        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!dbphone.equals("0"))
                                {
                                orderstatusref.child("didrestaurantaccepted").setValue("0");
                                orderstatusref.child("restaurantid_co").setValue(model.getRestaurantid()+"YES");
                                //orderstatusref.child("city_zone_status").setValue(model.getCity()+model.getZone()+"2");
                                orderstatusref.child("adminstatus").setValue("2");

                                orderstatusref.child("dbphone_delivered").setValue(dbphone+"NO");
                                viewHolder.btncancel.setVisibility(View.GONE);
                                viewHolder.btnaccept.setText("ACCEPTED");
                                viewHolder.btnaccept.setEnabled(false);
                                viewHolder.rldbinfo.setVisibility(View.VISIBLE);
                                int dbcl = Integer.parseInt(dbcurrentload);
                                dbcl+=1;
                                dbref.child(dbphone).child("currentload").setValue(String.valueOf(dbcl));
                                dialogInterface.dismiss();

                                sendAcceptedOrderStatusToRestaurant(adapter.getRef(position).getKey(),model.getRestaurantphone());
                                sendAcceptedOrderStatusToDeliveryBoy(adapter.getRef(position).getKey(),dbphone);

                                //set 4min timer to check whether order is not confirmed by restaurant?

                                    Calendar calendar = Calendar.getInstance();
                                    long time = calendar.getTimeInMillis();
                                    time+=240000;

                                    //getting the alarm manager
                                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                                    //creating a new intent specifying the broadcast receiver
                                    Intent inte = new Intent(CurrentOrdersActivity.this, MyAlarm.class);
                                    inte.putExtra("currentrequest",adapter.getRef(position).getKey());

                                    //creating a pending intent using the intent
                                    PendingIntent pi = PendingIntent.getBroadcast(CurrentOrdersActivity.this, 0, inte, 0);

                                    //setting the repeating alarm that will be fired

                                    //We need a calendar object to get the specified time in millis
                                    //as the alarm manager method takes time in millis to setup the alarm

                                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                                13, 39, 0);
                                    } else {
                                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                                13, 39, 0);
                                    }
                                    am.set(AlarmManager.RTC, time, pi);

                                    //am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);

                                }
                                else
                                    Toast.makeText(CurrentOrdersActivity.this, "Please select a Delivery Boy !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                orderstatusref.child("deliveryboyname").setValue("null");
                                orderstatusref.child("deliveryboyphone").setValue("null");
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });

                viewHolder.btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrentOrdersActivity.this);
                        alertDialogBuilder.setTitle("Are You Sure?");
                        alertDialogBuilder.setMessage("Order will get permanently cancelled !");
                        final EditText input = new EditText(CurrentOrdersActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        input.setLayoutParams(lp);
                        input.setHint("Enter reason for cancelling this order...");
                        alertDialogBuilder.setView(input);

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(!input.getText().equals(null)){
                                DatabaseReference orderstatusref = database.getReference("CurrentRequests")
                                        .child(adapter.getRef(position).getKey());
                                orderstatusref.child("orderstatus").setValue("-1");
                                orderstatusref.child("orderstatusmessage").setValue("Order Cancelled : "+input.getText().toString());
                                orderstatusref.child("adminstatus").setValue("-1");
                                orderstatusref.child("city_zone_status").setValue(model.getCity()+model.getZone()+"-1");


                                viewHolder.btnaccept.setVisibility(View.GONE);
                                viewHolder.btncancel.setText("Cancelled");
                                viewHolder.btncancel.setEnabled(false);

                                    sendCancelledOrderStatusToUser(adapter.getRef(position).getKey(),model.getCustomerphone(),input.getText().toString());

                                }
                                else
                                    Toast.makeText(CurrentOrdersActivity.this, "Please enter reason to cancel order!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialogBuilder.show();
                    }
                });

                viewHolder.imgcalldeliveryboy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getDeliveryboyphone()));
                        if (ContextCompat.checkSelfPermission(CurrentOrdersActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CurrentOrdersActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                        }
                        else
                        {
                            startActivity(intent);
                        }
                    }

                });

                viewHolder.txtviewbill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Viewbill = new Intent(CurrentOrdersActivity.this, ViewBillActivity.class);
                        Viewbill.putExtra("viewbill",adapter.getRef(position).getKey());
                        startActivity(Viewbill);
                    }
                });
            }
        };

    }

    private void sendAcceptedOrderStatusToDeliveryBoy(final String key, String dbphone) {

        DatabaseReference tokens = database.getReference("Tokens");
        tokens.orderByKey().equalTo(dbphone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot  : dataSnapshot.getChildren())
                        {
                            Token token = postSnapshot.getValue(Token.class);

                            //make raw payload
                            Notification notification = new Notification("Waiting for order confirmation...","A New Order #"+key.substring(1,key.length())+" is assigned to you!","ORDERS");
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.body().success==1)
                                            {
                                                //do nothing notification sent ot delivery boy
                                            }
                                            else
                                            {
                                                Toast.makeText(CurrentOrdersActivity.this, "Order was updated but failed to send notification", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void sendAcceptedOrderStatusToRestaurant(final String key, String restaurantphone) {


        DatabaseReference tokens = database.getReference("Tokens");
        tokens.orderByKey().equalTo(restaurantphone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot  : dataSnapshot.getChildren())
                        {
                            Token token = postSnapshot.getValue(Token.class);

                            //make raw payload
                            Notification notification = new Notification("Waiting for your order confirmation...","A New Order #"+key.substring(1,key.length())+" is requested!","ORDERS");
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.body().success==1)
                                                Toast.makeText(CurrentOrdersActivity.this, "Order sent to restaurant successfully!", Toast.LENGTH_SHORT).show();
                                            else
                                            {
                                                Toast.makeText(CurrentOrdersActivity.this, "Order was updated but failed to send notification", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void sendCancelledOrderStatusToUser(final String key, String customerphone, final String reasoncancelled) {


        DatabaseReference tokens = database.getReference("Tokens");
        tokens.orderByKey().equalTo(customerphone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot  : dataSnapshot.getChildren())
                        {
                            Token token = postSnapshot.getValue(Token.class);

                            //make raw payload
                            Notification notification = new Notification(reasoncancelled,"Your order #"+key.substring(1,key.length())+" is cancelled!","ORDERS");
                            Sender content = new Sender(token.getToken(),notification);

                            mService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.body().success==1)
                                                Toast.makeText(CurrentOrdersActivity.this, "Order Cancelled Successfully!", Toast.LENGTH_SHORT).show();
                                            else
                                            {
                                                Toast.makeText(CurrentOrdersActivity.this, "Order was updated but failed to send notification", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR", t.getMessage());
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }


}
