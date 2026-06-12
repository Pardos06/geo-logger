package pe.upn.gps_project.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accesos_auditoria")
public class Acceso {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "fecha_hora_consulta")
    public String fechaHoraConsulta; // Registra cuándo Paula (u otros) entran al sistema

    public Acceso(int usuarioId, String fechaHoraConsulta) {
        this.usuarioId = usuarioId;
        this.fechaHoraConsulta = fechaHoraConsulta;
    }
}
