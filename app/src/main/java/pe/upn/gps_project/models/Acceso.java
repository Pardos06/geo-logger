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
    public String fechaHoraConsulta;
    @ColumnInfo(name = "accion")
    public String accion;
    public Acceso(int usuarioId, String fechaHoraConsulta, String accion) {
        this.usuarioId = usuarioId;
        this.fechaHoraConsulta = fechaHoraConsulta;
        this.accion = accion;
    }
}
