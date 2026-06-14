package pe.upn.gps_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pe.upn.gps_project.R;
import pe.upn.gps_project.models.Acceso;

public class AccesosAdapter extends RecyclerView.Adapter<AccesosAdapter.AccesoViewHolder> {
    private final List<Acceso> accesos;

    public AccesosAdapter(List<Acceso> accesos) {
        this.accesos = accesos;
    }

    @NonNull
    @Override
    public AccesoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acceso, parent, false);
        return new AccesoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccesoViewHolder holder, int position) {
        Acceso acceso = accesos.get(position);

        // Lógica para separar la fecha de la hora usando el espacio en blanco
        String[] partes = acceso.fechaHoraConsulta.split(" ");
        String fecha = partes.length > 0 ? partes[0] : "Desconocida";
        String hora = partes.length > 1 ? partes[1] : "Desconocida";

        // Asignar los valores a los nuevos TextViews
        holder.tvAccion.setText("Acción: " + acceso.accion);
        holder.tvFecha.setText("Fecha: " + fecha);
        holder.tvHora.setText("Hora: " + hora);
    }

    @Override
    public int getItemCount() {
        return accesos.size();
    }

    static class AccesoViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccion, tvFecha, tvHora;

        public AccesoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Conectamos las variables con los IDs del XML
            tvAccion = itemView.findViewById(R.id.tvAccion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
        }
    }

}
