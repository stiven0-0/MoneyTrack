package com.example.moneytrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneytrack.adapter.TransaccionAdapter;
import com.example.moneytrack.db.DatabaseHelper;
import com.example.moneytrack.modelo.Transaccion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TransaccionAdapter.OnTransaccionClickListener {

    public static final String EXTRA_TRANSACCION_ID = "transaccion_id";

    private DatabaseHelper databaseHelper;
    private TransaccionAdapter adapter;
    private TextView tvTotalIngresos;
    private TextView tvTotalGastos;
    private TextView tvBalance;
    private TextView tvListaVacia;
    private RecyclerView recyclerTransacciones;

    private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    private final ActivityResultLauncher<Intent> formularioLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarTransacciones();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = DatabaseHelper.getInstance(this);

        tvTotalIngresos = findViewById(R.id.tvTotalIngresos);
        tvTotalGastos = findViewById(R.id.tvTotalGastos);
        tvBalance = findViewById(R.id.tvBalance);
        tvListaVacia = findViewById(R.id.tvListaVacia);
        recyclerTransacciones = findViewById(R.id.recyclerTransacciones);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);

        adapter = new TransaccionAdapter(this);
        recyclerTransacciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransacciones.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> abrirFormulario(-1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTransacciones();
    }

    private void cargarTransacciones() {
        List<Transaccion> transacciones = databaseHelper.obtenerTodas();
        adapter.setTransacciones(transacciones);
        actualizarResumen(transacciones);

        boolean hayTransacciones = !transacciones.isEmpty();
        tvListaVacia.setVisibility(hayTransacciones ? View.GONE : View.VISIBLE);
        recyclerTransacciones.setVisibility(hayTransacciones ? View.VISIBLE : View.GONE);
    }

    private void actualizarResumen(List<Transaccion> transacciones) {
        double totalIngresos = 0;
        double totalGastos = 0;

        for (Transaccion transaccion : transacciones) {
            if (transaccion.esIngreso()) {
                totalIngresos += transaccion.getMonto();
            } else {
                totalGastos += transaccion.getMonto();
            }
        }

        tvTotalIngresos.setText(formatoMoneda.format(totalIngresos));
        tvTotalGastos.setText(formatoMoneda.format(totalGastos));
        tvBalance.setText(formatoMoneda.format(totalIngresos - totalGastos));
    }

    private void abrirFormulario(int transaccionId) {
        Intent intent = new Intent(this, TransactionFormActivity.class);
        if (transaccionId != -1) {
            intent.putExtra(EXTRA_TRANSACCION_ID, transaccionId);
        }
        formularioLauncher.launch(intent);
    }

    @Override
    public void onEditarClick(Transaccion transaccion) {
        abrirFormulario(transaccion.getId());
    }

    @Override
    public void onEliminarClick(Transaccion transaccion) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.eliminar_titulo)
                .setMessage(getString(R.string.eliminar_mensaje, transaccion.getConcepto()))
                .setPositiveButton(R.string.eliminar, (dialog, which) -> {
                    databaseHelper.eliminar(transaccion.getId());
                    cargarTransacciones();
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }
}
