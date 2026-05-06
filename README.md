# WorkSync - Proyecto de Gestión Híbrida (Acceso a Datos)

## 1. Introducción
**WorkSync** es una aplicación Android diseñada para la gestión eficiente de empleados y sus tareas. El proyecto destaca por el uso de una **arquitectura de base de datos híbrida**, combinando la robustez de un sistema objeto-relacional (PostgreSQL) con la flexibilidad de uno orientado a documentos (MongoDB).

## 2. Modelo de Datos Híbrido
- **PostgreSQL (Estructurado):** Gestiona la entidad `Empleado`. Ideal para datos que requieren integridad referencial y seguridad en la autenticación.
    - **Tabla:** `empleados` (id, nombre, email, password).
- **MongoDB (Documental):** Gestiona la entidad `Tarea`. Permite almacenar descripciones extensas y metadatos variables de forma escalable.
    - **Colección:** `tareas` (titulo, descripcion, idEmpleadoRelacional).

## 3. Arquitectura del Sistema
El proyecto sigue el patrón DAO (Data Access Object) y está organizado en paquetes:
- **`model`**: Clases POJO (`Empleado`, `Tarea`) que representan las entidades.
- **`config`**: Singletons (`PostgresConfig`, `MongoConfig`) que gestionan conexiones y hilos (`ExecutorService`).
- **`dao`**: Lógica de acceso a datos (`EmpleadoDAO` para login, `TareaDAO` para CRUD de tareas).
- **`logic`**: El `GestorHibrido` unifica ambas bases de datos, garantizando que los datos lleguen a la UI de forma asíncrona y segura.

## 4. Configuración de la Base de Datos

### PostgreSQL
Ejecuta estos comandos para preparar el entorno:
```sql
CREATE DATABASE worksync;
\c worksync;

CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

-- Usuario de prueba
INSERT INTO empleados (nombre, email, password) VALUES 
('Usuario Prueba', 'test@worksync.com', '1234');
```

### MongoDB
Inserta datos de prueba iniciales:
```javascript
use worksync;

db.tareas.insertMany([
  {
    titulo: "Configurar Servidor",
    descripcion: "Instalar dependencias de red",
    idEmpleadoRelacional: 1
  },
  {
    titulo: "Validar Login",
    descripcion: "Comprobar conexión con PostgreSQL",
    idEmpleadoRelacional: 1
  }
]);
```

## 5. Instrucciones de Ejecución
1.  **Conectividad**: Asegúrate de que tus servicios de base de datos permitan conexiones desde la IP `10.0.2.2` (puente del emulador Android).
2.  **Compilación**: Abre el proyecto en Android Studio y sincroniza Gradle.
3.  **Lanzamiento**: Ejecuta la aplicación. Usa las credenciales `test@worksync.com` / `1234` para probar la integración completa.

## 6. Seguridad y Control de Versiones
- **Seguridad**: Autenticación básica con manejo de excepciones para prevenir cierres inesperados (Force Close).
- **Git**: Se han seguido convenciones de commits semánticos (`feat`, `refactor`, `docs`) para mantener la trazabilidad del desarrollo.
