package pe.upn.gps_project.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pe.upn.gps_project.R;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Ubicacion;

public class DashboardAdminActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AppDao appDao;
    Button btnGestionarPuntos, btnGestionarUsuarios, btnVerAccesos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppDatabase db = AppDatabase.getInstancia(this);
        appDao = db.appDao();

        btnGestionarPuntos = findViewById(R.id.btnGestionarPuntos);
        btnGestionarUsuarios = findViewById(R.id.btnGestionarUsuarios);
        btnVerAccesos = findViewById(R.id.btnVerAccesos);

        int adminId = getIntent().getIntExtra("USER_ID", -1);

        btnGestionarPuntos.setOnClickListener(v -> {
            Intent intent = new Intent(this, GestionPuntosActivity.class);
            intent.putExtra("USER_ID", adminId); // Pasamos el ID a la lista
            startActivity(intent);
        });

        btnGestionarUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(this, GestionUsuariosActivity.class);
            startActivity(intent);
        });

        btnVerAccesos.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroAccesosActivity.class);
            startActivity(intent);
        });

        // Cargar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAdmin);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        cargarPuntosEnElMapa();
    }
    private void cargarPuntosEnElMapa() {
        List<Ubicacion> listaUbicaciones = appDao.obtenerTodasLasUbicaciones();

        if (listaUbicaciones.isEmpty()) {
            Toast.makeText(this, "No hay ubicaciones registradas aún.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener fechas para comparar (Hoy y Ayer en formato yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fechaHoy = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String fechaAyer = sdf.format(cal.getTime());

        LatLng ultimaUbicacion = null;

        for (Ubicacion u : listaUbicaciones) {
            LatLng posicion = new LatLng(u.latitud, u.longitud);
            ultimaUbicacion = posicion;

            // Construir el texto de información que verá Paula al tocar la marca
            String info = "Sitio: " + u.nombreSitio + " | País: " + u.pais + " | Fecha: " + u.fechaHora;

            // Determinar el color según el intervalo de fecha
            float colorMarca;
            if (u.fechaHora.startsWith(fechaHoy)) {
                colorMarca = BitmapDescriptorFactory.HUE_GREEN; // Hoy
            } else if (u.fechaHora.startsWith(fechaAyer)) {
                colorMarca = BitmapDescriptorFactory.HUE_YELLOW; // Ayer
            } else {
                colorMarca = BitmapDescriptorFactory.HUE_RED; // Más antiguo
            }

            // Añadir la marca al mapa
            mMap.addMarker(new MarkerOptions()
                    .position(posicion)
                    .title("Punto ID: " + u.id)
                    .snippet(info) // El 'snippet' es el texto que aparece debajo del título
                    .icon(BitmapDescriptorFactory.defaultMarker(colorMarca)));
        }

        // Mover la cámara al último punto registrado para que Paula no vea un mapa vacío
        if (ultimaUbicacion != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ultimaUbicacion, 10f));
        }
    }
}