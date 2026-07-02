package com.example.moneytrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moneytrack.db.DatabaseHelper;
import com.example.moneytrack.modelo.Transaccion;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class TransactionFormActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText etConcepto;
    private TextInputEditText etMonto;
    private RadioGroup rgTipo;
    private RadioButton rbGasto;
    private RadioButton rbIngreso;
    private Button btnGuardar;
    private Button btnEliminar;

    private int transaccionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = DatabaseHelper.getInstance(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        etConcepto = findViewById(R.id.etConcepto);
        etMonto = findViewById(R.id.etMonto);
        rgTipo = findViewById(R.id.rgTipo);
        rbGasto = findViewById(R.id.rbGasto);
        rbIngreso = findViewById(R.id.rbIngreso);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);

        toolbar.setNavigationOnClickListener(v -> finish());

        if (getIntent().hasExtra(MainActivity.EXTRA_TRANSACCION_ID)) {
            transaccionId = getIntent().getIntExtra(MainActivity.EXTRA_TRANSACCION_ID, -1);
        }

        if (transaccionId != -1) {
            toolbar.setTitle(R.string.titulo_editar);
            btnEliminar.setVisibility(View.VISIBLE);
            cargarTransaccion();
        } else {
            toolbar.setTitle(R.string.titulo_nueva);
            rbGasto.setChecked(true);
        }

        btnGuardar.setOnClickListener(v -> guardarTransaccion());
        btnEliminar.setOnClickListener(v -> eliminarTransaccion());
    }

    private void cargarTransaccion() {
        Transaccion transaccion = databaseHelper.obtenerPorId(transaccionId);
        if (transaccion == null) {
            Toast.makeText(this, R.string.error_transaccion_no_encontrada, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etConcepto.setText(transaccion.getConcepto());
        etMonto.setText(String.valueOf(transaccion.getMonto()));

        if (transaccion.esIngreso()) {
            rbIngreso.setChecked(true);
        } else {
            rbGasto.setChecked(true);
        }
    }

    private void guardarTransaccion() {
        String concepto = etConcepto.getText() != null ? etConcepto.getText().toString().trim() : "";
        String montoTexto = etMonto.getText() != null ? etMonto.getText().toString().trim() : "";

        if (TextUtils.isEmpty(concepto)) {
            etConcepto.setError(getString(R.string.error_concepto_vacio));
            etConcepto.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(montoTexto)) {
            etMonto.setError(getString(R.string.error_monto_vacio));
            etMonto.requestFocus();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoTexto);
        } catch (NumberFormatException e) {
            etMonto.setError(getString(R.string.error_monto_invalido));
            etMonto.requestFocus();
            return;
        }

        if (monto <= 0) {
            etMonto.setError(getString(R.string.error_monto_invalido));
            etMonto.requestFocus();
            return;
        }

        int tipo = rbIngreso.isChecked() ? Transaccion.TIPO_INGRESO : Transaccion.TIPO_GASTO;
        Transaccion transaccion = new Transaccion(concepto, monto, tipo);

        if (transaccionId != -1) {
            transaccion.setId(transaccionId);
            databaseHelper.actualizar(transaccion);
            Toast.makeText(this, R.string.mensaje_actualizado, Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.insertar(transaccion);
            Toast.makeText(this, R.string.mensaje_guardado, Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    private void eliminarTransaccion() {
        databaseHelper.eliminar(transaccionId);
        Toast.makeText(this, R.string.mensaje_eliminado, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
