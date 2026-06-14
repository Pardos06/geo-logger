package pe.upn.gps_project.ui.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pe.upn.gps_project.R;
import pe.upn.gps_project.adapters.AccesosAdapter;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Acceso;

public class RegistroAccesosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_accesos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppDatabase db = AppDatabase.getInstancia(this);
        AppDao appDao = db.appDao();

        recyclerView = findViewById(R.id.recyclerViewAccesos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener datos y pasarlos al adaptador
        List<Acceso> listaAccesos = appDao.obtenerRegistroAccesos();
        AccesosAdapter adapter = new AccesosAdapter(listaAccesos);
        recyclerView.setAdapter(adapter);
    }

}