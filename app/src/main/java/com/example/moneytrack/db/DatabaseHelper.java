package com.example.moneytrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moneytrack.modelo.Transaccion;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mis_finanzas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TRANSACCIONES = "transacciones";
    private static final String COL_ID = "id";
    private static final String COL_CONCEPTO = "concepto";
    private static final String COL_MONTO = "monto";
    private static final String COL_TIPO = "tipo";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_TRANSACCIONES + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CONCEPTO + " TEXT NOT NULL, "
                + COL_MONTO + " REAL NOT NULL, "
                + COL_TIPO + " INTEGER NOT NULL"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACCIONES);
        onCreate(db);
    }

    public long insertar(Transaccion transaccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONCEPTO, transaccion.getConcepto());
        values.put(COL_MONTO, transaccion.getMonto());
        values.put(COL_TIPO, transaccion.getTipo());
        return db.insert(TABLE_TRANSACCIONES, null, values);
    }

    public List<Transaccion> obtenerTodas() {
        List<Transaccion> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TRANSACCIONES,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                lista.add(mapCursorToTransaccion(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public Transaccion obtenerPorId(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_TRANSACCIONES,
                null,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Transaccion transaccion = null;
        if (cursor.moveToFirst()) {
            transaccion = mapCursorToTransaccion(cursor);
        }
        cursor.close();
        return transaccion;
    }

    public int actualizar(Transaccion transaccion) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONCEPTO, transaccion.getConcepto());
        values.put(COL_MONTO, transaccion.getMonto());
        values.put(COL_TIPO, transaccion.getTipo());
        return db.update(
                TABLE_TRANSACCIONES,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(transaccion.getId())}
        );
    }

    public int eliminar(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                TABLE_TRANSACCIONES,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    private Transaccion mapCursorToTransaccion(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String concepto = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONCEPTO));
        double monto = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_MONTO));
        int tipo = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TIPO));
        return new Transaccion(id, concepto, monto, tipo);
    }
}
