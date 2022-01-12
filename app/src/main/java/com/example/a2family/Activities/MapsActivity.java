package com.example.a2family.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.example.a2family.Classes.Position;
import com.example.a2family.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int PERMISSION_FINE_LOCATION = 99;
    public static final int DEFAULT_UPDATE_INTERVAL = 1000;
    public static int ON_OFF = 0;
    private GoogleMap mMap;

    private double latitude;
    private double longitude;




    //variabile per memorizzare se stiamo tracciando la posizione o no
    private FloatingActionButton power;

    private Marker marker ;
    private MarkerOptions m=new MarkerOptions();

    private HashMap<String, Marker > markerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        //inizializza il supportMap fragment che corrisponde alla mappa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        this.power = (FloatingActionButton) findViewById(R.id.power);
        this.power.setBackgroundTintList(AppCompatResources.getColorStateList(MapsActivity.this, R.color.purple_700));
        //imposta le proprietà dell'oggetto locationRequest ( tempo di aggiornamento della posizione, priorità del gps)
        locationRequest = LocationRequest.create()
                .setInterval(MapsActivity.DEFAULT_UPDATE_INTERVAL * 10)
                .setFastestInterval(MapsActivity.DEFAULT_UPDATE_INTERVAL * 5)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);




        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("info", markerMap.toString());
                if(ON_OFF == 0){
                    ON_OFF=1;
                    power.setBackgroundTintList(AppCompatResources.getColorStateList(MapsActivity.this, R.color.purple_700));
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    Toast.makeText(MapsActivity.this, "Condivisione della posizione attivata", Toast.LENGTH_LONG).show();
                    startLocationUpdates();
                }
                else{
                    ON_OFF=0;
                    power.setBackgroundTintList(AppCompatResources.getColorStateList(MapsActivity.this, R.color.grey));
                    Toast.makeText(MapsActivity.this, "Condivisione della posizione disattivata", Toast.LENGTH_LONG).show();
                    stopLocationUpdates();
                }

            }
        });
    }

    private void updateDBlocation(Position pos) {

        databaseReference= firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("members").child(getUserIdFromFile()).child("position");
        //ad ogni modifica della posizione pusho le mie coordinate nel database
        databaseReference.setValue(pos).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               // Log.d("Info", pos.toString());
            }
        });
        //TODO: prendere in considerazione solo gli utenti che fanno parte dell'attuale gruppo famiglia dell'utente loggato ( COMPLETED )
        //mi metto in ascolto delle modifiche riguardanti gli utenti per intercettare le nuove posizioni
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            //Gestiamo il caso in cui venga percepita una modifica
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Se il figlio modificato è diverso da NULL
                if(snapshot.getValue()!= null) {
                    //converto i parametri che memorizzano la latitudine e logitudine dell'utente in coordinate
                    LatLng userLocation = new LatLng(snapshot.child("position").child("latitude").getValue(Double.class), snapshot.child("position").child("longitude").getValue(Double.class));
                    //Vado alla ricerca nella MarkerHashMap del Marker corrispondente all'utente attraverso il suo ID ottenuto con snapShot.getKey()
                    Marker previousMarker=markerMap.get(snapshot.getKey());
                    if(previousMarker!= null){
                        if(snapshot.getKey().equals(getUserIdFromFile())) {
                            previousMarker.setTitle("Ti stai muovendo");
                        }
                        else{
                            previousMarker.setTitle(snapshot.child("name").getValue(String.class).toUpperCase(Locale.ROOT) + " si sta muovendo");
                        }
                        //se esiste tale marker allora modifico le sue coordinate e verrà cosi aggiornato nella mappa
                        previousMarker.setPosition(userLocation);
                    }
                    else{
                        if(snapshot.getKey().equals(getUserIdFromFile())) {
                            m.position(userLocation).title("TU sei QUI !");
                        }else {
                            //se il marker non esiste allora procedo a crearne uno nuovo con il corrispettivo Id e lo inserisco nella mia MarkerHashMap
                            m.position(userLocation).title(snapshot.child("name").getValue(String.class).toUpperCase(Locale.ROOT));
                        }
                        marker = mMap.addMarker(m);
                        markerMap.put(snapshot.getKey(), marker);
                    }

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        //updateGPS();

        //richiamata ogni qual volta l'intervallo scade per ricevere la posizione aggiornata e procedere agli aggiornamenti
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //prende l'ultima posizione registrata
                Location location = locationResult.getLastLocation();
                //crea un oggetto corrispondente di tipo Position
                Position position = new Position(location.getLatitude(), location.getLongitude());

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Position dbPos = new Position(latitude,longitude);

                //metodo che aggiorna i marker degli utenti sulla mappa
                updateDBlocation(dbPos);

            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

    }





    //metodo che specifica il comportamento una volta concessi o meno i permessi
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    ON_OFF=1;
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Questa funzionalità richiede l'accesso alla posizione per poter funzionare correttamente", Toast.LENGTH_LONG).show();
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //  TODO: caricare i marker delle ultime posizioni registrate dai membri della famiglia(Completed)

        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //per ogni membro della famiglia
                for (DataSnapshot member : snapshot.getChildren()
                ) {
                    double lastLatitude = member.child("position").child("latitude").getValue(Double.class);
                    double lastLongitude = member.child("position").child("longitude").getValue(Double.class);
                    //controllo che le coordinate siano diverse da 0 poichè sono le coordinate di default quando l'utente si registra
                    if(lastLatitude!=0.0 && lastLongitude!=0.0) {
                        LatLng lastUserLocation = new LatLng(lastLatitude, lastLongitude);
                        Marker marker = mMap.addMarker(m.position(lastUserLocation).title(member.child("name").getValue(String.class).toUpperCase() + " è stato/a visto qui l'ultima volta"));
                        markerMap.put(member.getKey(), marker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            ON_OFF=1;
            startLocationUpdates();
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(MapsActivity.this, new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        //Position position = new Position(task.getResult().getLatitude(), task.getResult().getLongitude());
                    }
                }
            });
        } else {
                //richiedi permessi
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
                }
            }

    }
}