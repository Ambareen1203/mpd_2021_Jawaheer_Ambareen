package org.me.gcu.equakestartercode;

//Student name: Ambareen Shabnam Jawaheer
//Student ID: S1903330

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Details extends AppCompatActivity {
    EarthQuake earthQuake;
    final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Bundle bundleObject = getIntent().getExtras();
        this.earthQuake = (EarthQuake) bundleObject.getSerializable("earthquake");
        System.out.println(this.earthQuake);

        ((ImageView) findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.locationTitle2)).setText(earthQuake.getLocation());
        ((TextView) findViewById(R.id.magnitudeValue)).setText(Double.toString(earthQuake.getMagnitude()));
        ((TextView) findViewById(R.id.depthValue)).setText(Integer.toString(earthQuake.getDepth()));
        String startDate = format1.format(earthQuake.getStartDate().getTime());
        ((TextView) findViewById(R.id.startDateValue)).setText(startDate);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .scrollGesturesEnabled(false)
                .tiltGesturesEnabled(false)
                .tiltGesturesEnabled(false);



        final MapView mapview = (MapView) findViewById(R.id.mapViewDetailed);

        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng target = new LatLng(earthQuake.getLatitude(),earthQuake.getLongitude());
                if (earthQuake.getLocation().contains("islands")){
                    googleMap.setMinZoomPreference(14.0f);
                }
                else {
                    googleMap.setMinZoomPreference(8.0f);
                }
                googleMap.addMarker(new MarkerOptions().position(target)
                        .title("Earthquake Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(target));
                mapview.onResume();
            }
        });
    }
}
