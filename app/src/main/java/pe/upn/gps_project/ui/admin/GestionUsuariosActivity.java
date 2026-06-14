package pe.upn.gps_project.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import pe.upn.gps_project.adapters.UsuariosAdapter;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Usuario;

public class GestionUsuariosActivity extends AppCompatActivity {
    private AppDao appDao;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppDatabase db = AppDatabase.getInstancia(this);
        appDao = db.appDao();

        recyclerView = findViewById(R.id.recyclerViewUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);
        btnNuevoUsuario.setOnClickListener(v -> mostrarDialogoCrearUsuario());

        cargarLista();
    }
    private void cargarLista() {
        List<Usuario> usuarios = appDao.obtenerTodosLosUsuarios();

        UsuariosAdapter adapter = new UsuariosAdapter(usuarios, usuario -> {

            // 1. NUEVA VALIDACIÓN: Proteger al administrador
            if (usuario.rol.equals("administrador")) {
                Toast.makeText(GestionUsuariosActivity.this,
                        "Acción denegada: No se puede eliminar a un administrador principal.",
                        Toast.LENGTH_LONG).show();
                return; // Corta la ejecución aquí, no avanza al paso 2
            }

            // 2. Validación de puntos (La que ya tenías)
            int cantidadPuntos = appDao.contarUbicacionesPorUsuario(usuario.id);
            if (cantidadPuntos > 0) {
                Toast.makeText(GestionUsuariosActivity.this,
                        "No se puede eliminar: El usuario tiene " + cantidadPuntos + " puntos registrados.",
                        Toast.LENGTH_LONG).show();
            } else {
                // 3. Eliminación exitosa
                appDao.eliminarUsuario(usuario);
                Toast.makeText(GestionUsuariosActivity.this, "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
                cargarLista(); // Refrescar lista
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void mostrarDialogoCrearUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear Nuevo Empleado");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText etUsername = new EditText(this);
        etUsername.setHint("Nombre de usuario");
        layout.addView(etUsername);

        final EditText etPassword = new EditText(this);
        etPassword.setHint("Contraseña");
        layout.addView(etPassword);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String user = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (!user.isEmpty() && !pass.isEmpty()) {
                // Creamos por defecto rol empleado para el MVP
                appDao.insertarUsuario(new Usuario(user, pass, "empleado"));
                Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show();
                cargarLista();
            } else {
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}