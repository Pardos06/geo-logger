package pe.upn.gps_project.ui.admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pe.upn.gps_project.R;
import pe.upn.gps_project.adapters.PuntosAdapter;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Acceso;
import pe.upn.gps_project.models.Ubicacion;

public class GestionPuntosActivity extends AppCompatActivity {
    private AppDao appDao;
    private RecyclerView recyclerView;
    private PuntosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_puntos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppDatabase db = AppDatabase.getInstancia(this);
        appDao = db.appDao();

        recyclerView = findViewById(R.id.recyclerViewPuntos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Registrar la consulta cada vez que se lista
        int adminId = getIntent().getIntExtra("USER_ID", -1);
        if (adminId != -1) {
            String fechaHoraActual = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
            appDao.insertarAcceso(new Acceso(adminId, fechaHoraActual, "Consulta de tabla de puntos"));
        }
        cargarLista();
    }

    private void cargarLista() {
        List<Ubicacion> ubicaciones = appDao.obtenerTodasLasUbicaciones();

        adapter = new PuntosAdapter(ubicaciones, new PuntosAdapter.OnItemClickListener() {
            @Override
            public void onEditarClick(Ubicacion ubicacion) {
                mostrarDialogoEditar(ubicacion);
            }

            @Override
            public void onEliminarClick(Ubicacion ubicacion) {
                appDao.eliminarUbicacion(ubicacion);
                Toast.makeText(GestionPuntosActivity.this, "Punto eliminado", Toast.LENGTH_SHORT).show();
                cargarLista(); // Refrescar lista
            }
        });

        recyclerView.setAdapter(adapter);
    }
    private void mostrarDialogoEditar(Ubicacion ubicacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Sitio");

        final EditText input = new EditText(this);
        input.setText(ubicacion.nombreSitio);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                ubicacion.nombreSitio = nuevoNombre;
                appDao.actualizarUbicacion(ubicacion);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                cargarLista(); // Refrescar lista
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}