package pe.upn.gps_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pe.upn.gps_project.R;
import pe.upn.gps_project.models.Ubicacion;

public class PuntosAdapter extends RecyclerView.Adapter<PuntosAdapter.PuntoViewHolder> {
    private List<Ubicacion> listaPuntos;
    private OnItemClickListener listener;

    // Interfaz para manejar los clics en los botones de la actividad
    public interface OnItemClickListener {
        void onEditarClick(Ubicacion ubicacion);
        void onEliminarClick(Ubicacion ubicacion);
    }

    public PuntosAdapter(List<Ubicacion> listaPuntos, OnItemClickListener listener) {
        this.listaPuntos = listaPuntos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PuntoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_punto, parent, false);
        return new PuntoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntoViewHolder holder, int position) {
        Ubicacion ubicacion = listaPuntos.get(position);

        String info = "Sitio: " + ubicacion.nombreSitio + "\n" +
                "País: " + ubicacion.pais + "\n" +
                "Coord: " + ubicacion.latitud + ", " + ubicacion.longitud + "\n" +
                "Fecha: " + ubicacion.fechaHora;

        holder.tvInfoPunto.setText(info);

        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(ubicacion));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(ubicacion));
    }

    @Override
    public int getItemCount() {
        return listaPuntos.size();
    }

    public static class PuntoViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfoPunto;
        Button btnEditar, btnEliminar;

        public PuntoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfoPunto = itemView.findViewById(R.id.tvInfoPunto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
