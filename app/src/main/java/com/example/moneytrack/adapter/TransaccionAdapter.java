package com.example.moneytrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneytrack.R;
import com.example.moneytrack.modelo.Transaccion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {

    public interface OnTransaccionClickListener {
        void onEditarClick(Transaccion transaccion);

        void onEliminarClick(Transaccion transaccion);
    }

    private final List<Transaccion> transacciones = new ArrayList<>();
    private final OnTransaccionClickListener listener;
    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    public TransaccionAdapter(OnTransaccionClickListener listener) {
        this.listener = listener;
    }

    public void setTransacciones(List<Transaccion> nuevasTransacciones) {
        transacciones.clear();
        if (nuevasTransacciones != null) {
            transacciones.addAll(nuevasTransacciones);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaccion, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position) {
        holder.bind(transacciones.get(position));
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    class TransaccionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvConcepto;
        private final TextView tvMonto;
        private final TextView tvTipo;
        private final View indicadorTipo;
        private final ImageButton btnEditar;
        private final ImageButton btnEliminar;

        TransaccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConcepto = itemView.findViewById(R.id.tvConcepto);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            indicadorTipo = itemView.findViewById(R.id.indicadorTipo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        void bind(Transaccion transaccion) {
            Context context = itemView.getContext();
            tvConcepto.setText(transaccion.getConcepto());
            tvMonto.setText(formatoMoneda.format(transaccion.getMonto()));

            if (transaccion.esIngreso()) {
                tvTipo.setText(R.string.tipo_ingreso);
                tvTipo.setTextColor(ContextCompat.getColor(context, R.color.ingreso));
                tvMonto.setTextColor(ContextCompat.getColor(context, R.color.ingreso));
                indicadorTipo.setBackgroundResource(R.drawable.bg_tipo_ingreso);
            } else {
                tvTipo.setText(R.string.tipo_gasto);
                tvTipo.setTextColor(ContextCompat.getColor(context, R.color.gasto));
                tvMonto.setTextColor(ContextCompat.getColor(context, R.color.gasto));
                indicadorTipo.setBackgroundResource(R.drawable.bg_tipo_gasto);
            }

            itemView.setOnClickListener(v -> listener.onEditarClick(transaccion));
            btnEditar.setOnClickListener(v -> listener.onEditarClick(transaccion));
            btnEliminar.setOnClickListener(v -> listener.onEliminarClick(transaccion));
        }
    }
}
