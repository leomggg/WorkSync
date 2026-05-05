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

## Configuración
Asegúrate de tener las bases de datos corriendo en `localhost` (accesible vía `10.0.2.2` desde el emulador Android).
- PostgreSQL: puerto 5432, BD `worksync`.
- MongoDB: puerto 27017, BD `worksync`.
