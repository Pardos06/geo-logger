package pe.upn.gps_project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pe.upn.gps_project.models.Acceso;
import pe.upn.gps_project.models.Ubicacion;
import pe.upn.gps_project.models.Usuario;

@Database(entities = {Usuario.class, Ubicacion.class, Acceso.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "gps_database.db")
                    .fallbackToDestructiveMigration() // Elimina y recrea las tablas si cambiamos la versión
                    .allowMainThreadQueries() // Nota: Solo para desarrollo rápido de 3 días.
                    .build();
        }
        return instancia;
    }
}