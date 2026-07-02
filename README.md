# Mis Finanzas — Gestor de Gastos Personales

**Estudiante:** [Nombre completo del estudiante]

## Descripción

**Mis Finanzas** es una aplicación nativa para Android que permite registrar ingresos y gastos personales de forma diaria. La app persiste la información en una base de datos SQLite local y muestra el historial en una lista dinámica con RecyclerView.

### Funcionalidades

- Registrar ingresos y gastos con concepto, monto y tipo
- Visualizar todas las transacciones en una lista con indicador visual por tipo
- Editar transacciones existentes
- Eliminar transacciones que ya no correspondan
- Resumen de balance, total de ingresos y total de gastos
- Persistencia de datos al cerrar y reabrir la aplicación

## Requisitos técnicos implementados

| Requisito | Implementación |
|-----------|----------------|
| Layouts | `activity_main.xml`, `activity_transaction_form.xml`, `item_transaccion.xml` |
| SQLite CRUD | `DatabaseHelper.java` — insertar, leer (todas y por ID), actualizar, eliminar |
| RecyclerView | `TransaccionAdapter.java` con patrón ViewHolder |
| Modelo | `Transaccion.java` |
| Separación de responsabilidades | Paquetes `modelo`, `db`, `adapter` y Activities |

## Estructura del proyecto

```
app/src/main/java/com/example/moneytrack/
├── MainActivity.java              # Pantalla principal con RecyclerView y FAB
├── TransactionFormActivity.java   # Formulario de creación/edición
├── adapter/
│   └── TransaccionAdapter.java    # Adaptador con ViewHolder
├── db/
│   └── DatabaseHelper.java        # SQLite — operaciones CRUD
└── modelo/
    └── Transaccion.java           # Modelo de datos
```

## Tabla SQLite: `transacciones`

| Columna   | Tipo    | Descripción                          |
|-----------|---------|--------------------------------------|
| id        | INTEGER | Primary Key, autoincremental         |
| concepto  | TEXT    | Descripción breve (ej. "Almuerzo")   |
| monto     | REAL    | Valor de la transacción              |
| tipo      | INTEGER | 0 = Gasto, 1 = Ingreso               |

## Capturas de pantalla

> **Nota:** Reemplaza las imágenes en `docs/screenshots/` con capturas reales de tu emulador o dispositivo antes de entregar.

### Lista de transacciones

![Lista de transacciones](docs/screenshots/lista_transacciones.png)

### Formulario de registro

![Formulario de registro](docs/screenshots/formulario_registro.png)

## Cómo ejecutar

1. Abre el proyecto en Android Studio
2. Sincroniza Gradle
3. Ejecuta la app en un emulador o dispositivo con API 24+

## Tecnologías

- Java
- Android SDK (minSdk 24)
- SQLite (`SQLiteOpenHelper`)
- RecyclerView + Material Design Components
