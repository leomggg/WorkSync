package com.example.worksync.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.worksync.R;
import com.example.worksync.model.Tarea;
import java.util.List;

public class TareaAdapter extends ArrayAdapter<Tarea> {
    private final List<Tarea> tareas;
    private final OnTareaActionListener listener;

    public interface OnTareaActionListener {
        void onCompletarClick(Tarea tarea);
    }

    public TareaAdapter(Context context, List<Tarea> tareas, OnTareaActionListener listener) {
        super(context, R.layout.item_tarea, tareas);
        this.tareas = tareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tarea, parent, false);
        }

        Tarea t = tareas.get(position);

        TextView tvTitulo = convertView.findViewById(R.id.tvTituloTarea);
        TextView tvDesc = convertView.findViewById(R.id.tvDescTarea);
        TextView tvPrio = convertView.findViewById(R.id.tvPrioridad);
        ImageButton btnCompletar = convertView.findViewById(R.id.btnCompletar);

        tvTitulo.setText(t.getTitulo());
        tvDesc.setText(t.getDescripcion());
        tvPrio.setText("Prioridad: " + t.getPrioridad());

        // Estilo según prioridad
        switch (t.getPrioridad()) {
            case "Alta": tvPrio.setTextColor(Color.RED); break;
            case "Media": tvPrio.setTextColor(Color.BLUE); break;
            default: tvPrio.setTextColor(Color.GRAY); break;
        }

        // Estilo si está completada
        if (t.isCompletada()) {
            tvTitulo.setPaintFlags(tvTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvTitulo.setTextColor(Color.GRAY);
            btnCompletar.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
            tvTitulo.setPaintFlags(tvTitulo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvTitulo.setTextColor(Color.BLACK);
            btnCompletar.setImageResource(android.R.drawable.checkbox_off_background);
        }

        btnCompletar.setOnClickListener(v -> {
            if (listener != null) listener.onCompletarClick(t);
        });

        return convertView;
    }
}