package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment supportMapFragment1;
    FusedLocationProviderClient client;
    DatabaseReference ref,ref1;
    Button navigation;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get the data from the previous activity
        if(getIntent().hasExtra("name")){
            name = getIntent().getStringExtra("name");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        navigation = (Button)findViewById(R.id.buttonLetNavigation);

        //navigation onclick listener that intent to google map
     navigation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //get firebase reference
             ref = FirebaseDatabase.getInstance().getReference().child("Location");
             ref1 = ref.child(name);
             //Get a DataSnapshot for the location at the specified relative path.
             ref1.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                     //get latitude and longitude of selected sport centre
                     String latitude = snapshot.child("latitude").getValue().toString();
                     String longitude = snapshot.child("longitude").getValue().toString();

                     double lati= getCount(latitude);
                     double longi = getCount(longitude);
                     String packageName = "com.google.android.apps.maps";
                     String query = "google.navigation:q="+lati+","+longi;

                     // intent to google map navigation
                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
                     intent.setPackage(packageName);
                     startActivity(intent);
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
         }
     });

    }

    // show the map
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //get firebase reference
        ref = FirebaseDatabase.getInstance().getReference().child("Location");
        ref1 = ref.child(name);

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get latitude and longitude of selected sport centre
                String latitude = snapshot.child("latitude").getValue().toString();
                String longitude = snapshot.child("longitude").getValue().toString();

                double lati= getCount(latitude);
                double longi = getCount(longitude);

                map = googleMap;
                // set the latitude and longitude that needed to display
                LatLng LavanaSportCentre = new LatLng(lati, longi);
                //add the marker and the latitude and longitude
                map.addMarker(new MarkerOptions().position(LavanaSportCentre).title("Lavana Sport Centre"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LavanaSportCentre,18F));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public double getCount(String c){

        double count;

        try {
            count = Double.parseDouble(c);
            return count;
        }catch (NumberFormatException ex){
            return 0;
        }
    }

}