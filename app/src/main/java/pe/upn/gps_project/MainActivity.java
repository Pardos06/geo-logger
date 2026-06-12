package pe.upn.gps_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap mMap;

    private EditText txtLatitud;
    private EditText txtLongitud;

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        LatLng sitio = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(sitio).title("x: " + 1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitio, 15f));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        Button btnVerificar = findViewById(R.id.btnVerificar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnVerificar.setOnClickListener(v -> {

            try {

                double latitud = Double.parseDouble(
                        txtLatitud.getText().toString());

                double longitud = Double.parseDouble(
                        txtLongitud.getText().toString());

                LatLng ubicacion = new LatLng(latitud, longitud);

                mMap.clear();

                mMap.addMarker(
                        new MarkerOptions()
                                .position(ubicacion)
                                .title("Ubicación"));

                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                ubicacion, 15));

            } catch (Exception e) {

                Toast.makeText(
                        MainActivity.this,
                        "Ingrese coordenadas válidas",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng lima = new LatLng(-12.0464, -77.0428);

        mMap.setOnMapClickListener(this);
        // mMap.setOnMapLongClickListener(this);
        mMap.setTrafficEnabled(true);
        mMap.setTransitEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(
                new MarkerOptions()
                        .position(lima)
                        .title("Lima"));

        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(lima, 12));
    }
}