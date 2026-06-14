package pe.upn.gps_project.ui.empleado;

import android.location.Address;
import android.location.Geocoder;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pe.upn.gps_project.R;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Ubicacion;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener {

    private GoogleMap mMap;

    // Controles de la interfaz
    private EditText txtLatitud, txtLongitud, txtSitio, txtPais;
    private Button btnVerificar, btnRegistrar;

    private AppDao appDao;
    private int usuarioIdActual = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Recibir el ID del usuario que inició sesión (Joao)
        usuarioIdActual = getIntent().getIntExtra("USER_ID", -1);

        // 2. Inicializar la base de datos
        AppDatabase db = AppDatabase.getInstancia(this);
        appDao = db.appDao();

        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        txtSitio = findViewById(R.id.txtSitio);
        txtPais = findViewById(R.id.txtPais);
        btnVerificar = findViewById(R.id.btnVerificar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnVerificar.setOnClickListener(v -> verificarCoordenadas());

        btnRegistrar.setOnClickListener(v -> registrarPunto());
    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        // Al tocar el mapa, SOLO llenamos el formulario y ponemos la marca visual
        double lat = latLng.latitude;
        double lng = latLng.longitude;

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Punto seleccionado"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        txtLatitud.setText(String.valueOf(lat));
        txtLongitud.setText(String.valueOf(lng));

        // Obtener texto de la dirección para autocompletar
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> direcciones = geocoder.getFromLocation(lat, lng, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);
                txtSitio.setText(direccion.getAddressLine(0));
                txtPais.setText(direccion.getCountryName() != null ? direccion.getCountryName() : "");
            } else {
                txtSitio.setText("Desconocido");
                txtPais.setText("Desconocido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtSitio.setText("Desconocido");
            txtPais.setText("Desconocido");
        }
    }

    private void verificarCoordenadas() {
        try {
            double latitud = Double.parseDouble(txtLatitud.getText().toString());
            double longitud = Double.parseDouble(txtLongitud.getText().toString());
            LatLng ubicacion = new LatLng(latitud, longitud);

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación manual"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
        } catch (Exception e) {
            Toast.makeText(this, "Por favor, ingrese coordenadas válidas", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarPunto() {
        String latStr = txtLatitud.getText().toString().trim();
        String lngStr = txtLongitud.getText().toString().trim();
        String sitio = txtSitio.getText().toString().trim();
        String pais = txtPais.getText().toString().trim();

        // VALIDACIÓN: Evitar campos vacíos
        if (latStr.isEmpty() || lngStr.isEmpty() || sitio.isEmpty() || pais.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un punto y llenar todos los datos.", Toast.LENGTH_LONG).show();
            return;
        }

        if (usuarioIdActual == -1) {
            Toast.makeText(this, "Error: No se identificó al usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double lat = Double.parseDouble(latStr);
            double lng = Double.parseDouble(lngStr);
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Insertar en Base de Datos
            Ubicacion nuevaUbicacion = new Ubicacion(lat, lng, sitio, pais, fechaHora, usuarioIdActual);
            appDao.insertarUbicacion(nuevaUbicacion);

            Toast.makeText(this, "¡Punto registrado con éxito!", Toast.LENGTH_SHORT).show();

            // Limpiar formulario para evitar registros duplicados por error
            txtLatitud.setText("");
            txtLongitud.setText("");
            txtSitio.setText("");
            txtPais.setText("");
            mMap.clear();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar el punto.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng lima = new LatLng(-12.0464, -77.0428);
        mMap.setOnMapClickListener(this);
        mMap.setTrafficEnabled(true);
        mMap.setTransitEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lima, 12));
    }
}