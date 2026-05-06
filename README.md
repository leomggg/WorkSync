# WorkSync - Gestor Híbrido de Proyectos y Tareas

Aplicación Android para la gestión de empleados y tareas utilizando un modelo de datos híbrido (SQL + NoSQL).

## Arquitectura de Datos
- **PostgreSQL**: Almacena información estructurada de Empleados (id, nombre, email, password).
- **MongoDB**: Almacena información documental de Tareas (titulo, descripcion, idEmpleadoRelacional).

## Tecnologías
- Java
- Android Studio
- PostgreSQL (JDBC)
- MongoDB (Java Sync Driver)

## Estructura del Proyecto
- `Empleado.java` / `Tarea.java`: Modelos de datos.
- `PostgresConfig.java` / `MongoConfig.java`: Configuraciones de conexión Singleton.
- `EmpleadoDAO.java` / `TareaDAO.java`: Capa de acceso a datos.
- `GestorHibrido.java`: Lógica de integración entre ambas bases de datos.

## Configuración de la Base de Datos

### PostgreSQL (Empleados)
Ejecuta el siguiente script para crear la tabla y un usuario de prueba:
```sql
CREATE DATABASE worksync;
\c worksync;

CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

INSERT INTO empleados (nombre, email, password) VALUES 
('Usuario Prueba', 'test@worksync.com', '1234');
```

### MongoDB (Tareas)
Inserta documentos de prueba en la colección `tareas`:
```javascript
use worksync;

db.tareas.insertMany([
  {
    titulo: "Configurar Servidor",
    descripcion: "Instalar dependencias y configurar puerto",
    idEmpleadoRelacional: 1
  },
  {
    titulo: "Diseñar UI Login",
    descripcion: "Crear XML y lógica de validación",
    idEmpleadoRelacional: 1
  }
]);
```

## Configuración de Red
Asegúrate de tener las bases de datos corriendo en `localhost` (accesible vía `10.0.2.2` desde el emulador Android).
- PostgreSQL: puerto 5432, BD `worksync`.
- MongoDB: puerto 27017, BD `worksync`.
