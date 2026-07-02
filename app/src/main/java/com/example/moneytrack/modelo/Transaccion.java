package com.example.moneytrack.modelo;

public class Transaccion {

    public static final int TIPO_GASTO = 0;
    public static final int TIPO_INGRESO = 1;

    private int id;
    private String concepto;
    private double monto;
    private int tipo;

    public Transaccion() {
    }

    public Transaccion(String concepto, double monto, int tipo) {
        this.concepto = concepto;
        this.monto = monto;
        this.tipo = tipo;
    }

    public Transaccion(int id, String concepto, double monto, int tipo) {
        this.id = id;
        this.concepto = concepto;
        this.monto = monto;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean esIngreso() {
        return tipo == TIPO_INGRESO;
    }
}
