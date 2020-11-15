package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference ref,ref1;
    ArrayList<SportCentre> sportCentreArrayList;
    ArrayList<DisplaySportCentre> displaySportCentres;
    RecyclerView recyclerView;
    SearchView searchView;
    String name,userName;

    Location locationA,locationB;

    String cLan,cLog;
    double aLan,aLog;

    double latitude,longitude;

    private FusedLocationProviderClient client;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //get data from last activity
        if(getIntent().hasExtra("name") && getIntent().hasExtra("userName")){
            name = getIntent().getStringExtra("name");
            userName = getIntent().getStringExtra("userName");
        }

        //Get database reference
        ref = FirebaseDatabase.getInstance().getReference().child("Sport Centre");
        ref1 = ref.child(name);

        setContentView(R.layout.activity_search);
        //variable declaration
        recyclerView = findViewById(R.id.rvSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sportCentreArrayList =  new ArrayList<SportCentre>();
        displaySportCentres = new ArrayList<>();
        searchView = findViewById(R.id.searchViewSearch);
        locationA = new Location("locationA");
        locationB = new Location("locationB");

        requestPermission();

        //check permission
        client = LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(SearchActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        // get current latitude and longitude
        client.getLastLocation().addOnSuccessListener(SearchActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    locationA.setLatitude(latitude);
                    locationA.setLongitude(longitude);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        sportCentreArrayList.clear();
        displaySportCentres.clear();

        //Read and listen for changes to the entire contents of a path.
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            //Get a DataSnapshot for the location at the specified relative path.
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int m=0;
                for(DataSnapshot snapshot1:snapshot.getChildren()){

                    //Pass the value to the Sport Centre Class
                    SportCentre sc = snapshot1.getValue(SportCentre.class);
                    //add data into array list
                    sportCentreArrayList.add(sc);
                    //get the latitude and longitude of the sport centre
                    cLan = sportCentreArrayList.get(m).getLatitude();
                    cLog = sportCentreArrayList.get(m).getLongitude();
                    aLan = getLan(cLan);
                    aLog = getLog(cLog);
                    locationB.setLatitude(aLan);
                    locationB.setLongitude(aLog);

                    //calculate the distance between current location and the sport centre
                    double result=(locationA.distanceTo(locationB))/1000;
                    displaySportCentres.add(new DisplaySportCentre(sportCentreArrayList.get(m).getName(),sportCentreArrayList.get(m).getLocation(),df2.format(result)+"km"));
                    m++;


                }
                Collections.sort(displaySportCentres,DisplaySportCentre.dsc);
                //pass data into the adapter
                SearchAdapter sa = new SearchAdapter(SearchActivity.this,displaySportCentres,userName);
                recyclerView.setAdapter(sa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Wrong database", Toast.LENGTH_SHORT).show();
            }
        });
        //check search equal to null
       if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String s){
        ArrayList<DisplaySportCentre> displaySportCentres1 = new ArrayList<>();
        //check the word on the search compare to the title
        for(DisplaySportCentre object: displaySportCentres){
            if(object.getName().toLowerCase().contains(s.toLowerCase()) || object.getLocation().toLowerCase().contains(s.toLowerCase())){
                displaySportCentres1.add(object);

            }
        }
        Collections.sort(displaySportCentres,DisplaySportCentre.dsc);
        SearchAdapter sa = new SearchAdapter(SearchActivity.this,displaySportCentres1,userName);
        recyclerView.setAdapter(sa);
    }

    public double getLan(String latitude){

        double lan;

        try {
            lan = Double.parseDouble(latitude);
            return lan;
        }catch (NumberFormatException ex){
            return 0;
        }
    }

    public double getLog(String longitude){

        double log;

        try {
            log = Double.parseDouble(longitude);
            return log;
        }catch (NumberFormatException ex){
            return 0;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

}