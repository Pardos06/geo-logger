package pe.upn.gps_project.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pe.upn.gps_project.ui.empleado.MainActivity;
import pe.upn.gps_project.R;
import pe.upn.gps_project.database.AppDao;
import pe.upn.gps_project.database.AppDatabase;
import pe.upn.gps_project.models.Acceso;
import pe.upn.gps_project.models.Ubicacion;
import pe.upn.gps_project.models.Usuario;
import pe.upn.gps_project.ui.admin.DashboardAdminActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsuario, etPassword;
    private Button btnIngresar;
    private AppDao appDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Iniciamos las vistas e interfaces
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        AppDatabase db = AppDatabase.getInstancia(this);
        appDao = db.appDao();

        precargarUsuariosPorDefecto();

        btnIngresar.setOnClickListener(v -> ejecutarLogin());

    }
    private void precargarUsuariosPorDefecto() {
        if (appDao.obtenerTodosLosUsuarios().isEmpty()) {
            // Paula como Administrador
            appDao.insertarUsuario(new Usuario("paula", "admin123", "administrador"));
            // Joao como Empleado
            appDao.insertarUsuario(new Usuario("joao", "empleado123", "empleado"));
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            java.util.Calendar cal = java.util.Calendar.getInstance();

            String fechaHoy = sdf.format(cal.getTime());

            cal.add(java.util.Calendar.DATE, -1);
            String fechaAyer = sdf.format(cal.getTime());

            cal.add(java.util.Calendar.DATE, -4);
            String fechaAntigua = sdf.format(cal.getTime());

            appDao.insertarUbicacion(new Ubicacion(-12.0464, -77.0428, "Plaza Mayor de Lima", "Perú", fechaHoy, 2));
            appDao.insertarUbicacion(new Ubicacion(-12.0550, -77.0350, "Parque de la Exposición", "Perú", fechaAyer, 2));
            appDao.insertarUbicacion(new Ubicacion(-12.0650, -77.0250, "Estadio Nacional", "Perú", fechaAntigua, 2));
        }
    }
    private void ejecutarLogin() {
        String txtUser = etUsuario.getText().toString().trim();
        String txtPass = etPassword.getText().toString().trim();

        if (txtUser.isEmpty() || txtPass.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar en la base de datos usando Room
        Usuario usuarioLogueado = appDao.login(txtUser, txtPass);

        if (usuarioLogueado != null) {
            String fechaHoraActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (usuarioLogueado.rol.equals("administrador")) {
                // REQUISITO: Almacenar los accesos de Paula al sistema y horas de consulta
                appDao.insertarAcceso(new Acceso(usuarioLogueado.id, fechaHoraActual, "Inicio de Sesión"));

                // Redirigir a la pantalla de Paula
                Intent intent = new Intent(this, DashboardAdminActivity.class);
                intent.putExtra("USER_ID",usuarioLogueado.id);
                startActivity(intent);
            } else {
                // Redirigir a la pantalla de Joao
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_ID", usuarioLogueado.id);
                startActivity(intent);
            }
            finish(); // Cierra el Login para que no puedan regresar con el botón "atrás"
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}