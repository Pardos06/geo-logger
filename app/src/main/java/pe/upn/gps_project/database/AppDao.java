package pe.upn.gps_project.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pe.upn.gps_project.models.Acceso;
import pe.upn.gps_project.models.Ubicacion;
import pe.upn.gps_project.models.Usuario;

@Dao
public interface AppDao {

    // --- CONSULTAS DE USUARIOS ---
    @Insert
    void insertarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE username = :user AND password = :pass LIMIT 1")
    Usuario login(String user, String pass);

    @Query("SELECT * FROM usuarios")
    List<Usuario> obtenerTodosLosUsuarios();

    @Delete
    void eliminarUsuario(Usuario usuario);

    @Query("SELECT COUNT(*) FROM ubicaciones WHERE usuario_id = :userId")
    int contarUbicacionesPorUsuario(int userId);

    // --- CONSULTAS DE UBICACIONES ---
    @Insert
    void insertarUbicacion(Ubicacion ubicacion);

    @Query("SELECT * FROM ubicaciones ORDER BY fecha_hora DESC")
    List<Ubicacion> obtenerTodasLasUbicaciones();

    @Update
    void actualizarUbicacion(Ubicacion ubicacion);

    @Delete
    void eliminarUbicacion(Ubicacion ubicacion);

    // --- CONSULTAS DE AUDITORIA ---
    @Insert
    void insertarAcceso(Acceso acceso);

    @Query("SELECT * FROM accesos_auditoria ORDER BY fecha_hora_consulta DESC")
    List<Acceso> obtenerRegistroAccesos();
}
