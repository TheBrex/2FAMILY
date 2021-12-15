package com.example.a2family.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.a2family.Classes.Position;
import com.example.a2family.R;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_FINE_LOCATION = 99;
    public static final int DEFAULT_UPDATE_INTERVAL = 1000;
    private GoogleMap mMap;

    private double latitude;
    private double longitude;

    //API google per servizio localizzazione
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    GoogleApi mGoogleApi;
    //variabile per memorizzare se stiamo tracciando la posizione o no
    private boolean updateOn = false;
    private FloatingActionButton power;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        this.power = (FloatingActionButton) findViewById(R.id.power);
        //set propriety location
        locationRequest = LocationRequest.create()
                .setInterval(MapsActivity.DEFAULT_UPDATE_INTERVAL * 5)
                .setFastestInterval(MapsActivity.DEFAULT_UPDATE_INTERVAL * 3)
                .setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //richiamata ogni qual volta l'intervallo scade
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Position position = new Position(location.getLatitude(), location.getLongitude());
                Toast.makeText(MapsActivity.this, position.toString(), Toast.LENGTH_LONG).show();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        };


        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                Toast.makeText(MapsActivity.this, "attivato", Toast.LENGTH_LONG).show();
            }
        });

        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        updateGPS();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }


    //metodo che specifica il comportamento una volta concessi o meno i permessi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Questa funzionalità richiede l'accesso alla posizione per poter funzionare correttamente", Toast.LENGTH_LONG).show();
                }
        }
    }


    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(MapsActivity.this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Position position = new Position(task.getResult().getLatitude(), task.getResult().getLongitude());

                        latitude = task.getResult().getLatitude();
                        longitude = task.getResult().getLongitude();
                        LatLng pos = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(pos).title("Marker in Sydney"));
                    }
                    else{}
                }
            });
        } else {
            //richiedi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}