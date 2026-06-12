package pe.upn.gps_project.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ubicaciones")
public class Ubicacion {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "latitud")
    public double latitud;

    @ColumnInfo(name = "longitud")
    public double longitud;

    @ColumnInfo(name = "nombre_sitio")
    public String nombreSitio;

    @ColumnInfo(name = "pais")
    public String pais;

    @ColumnInfo(name = "fecha_hora")
    public String fechaHora;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    public Ubicacion(double latitud, double longitud, String nombreSitio, String pais, String fechaHora, int usuarioId) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreSitio = nombreSitio;
        this.pais = pais;
        this.fechaHora = fechaHora;
        this.usuarioId = usuarioId;
    }
}
