# WorkSync - Proyecto de Gestión Híbrida (Acceso a Datos)

## 1. Introducción
**WorkSync** es una aplicación Android diseñada para la gestión eficiente de empleados y sus tareas. El proyecto destaca por el uso de una **arquitectura de base de datos híbrida**, combinando la robustez de un sistema objeto-relacional (PostgreSQL) con la flexibilidad de uno orientado a documentos (MongoDB).

## 2. Modelo de Datos Híbrido
- **PostgreSQL (Estructurado):** Gestiona la entidad `Empleado`. Ideal para datos que requieren integridad referencial y seguridad en la autenticación.
    - **Tabla:** `empleados` (id, nombre, email, password).
- **MongoDB (Documental):** Gestiona la entidad `Tarea`. Permite almacenar descripciones extensas y metadatos variables de forma escalable.
    - **Colección:** `tareas` (título, descripción, idEmpleadoRelacional).

## 3. Arquitectura del Sistema
El proyecto sigue el patrón **DAO (Data Access Object)** y está organizado en una estructura de paquetes profesional:
- **`model`**: Clases POJO (`Empleado`, `Tarea`) que representan las entidades del dominio.
- **`config`**: Singletons (`PostgresConfig`, `MongoConfig`) que gestionan hilos y conexiones.
- **`dao`**: Lógica de acceso a datos (`EmpleadoDAO` para SQL, `TareaDAO` para NoSQL).
- **`logic`**: El `GestorHibrido` unifica ambas bases de datos, garantizando la seguridad de hilos (Main Thread).
- **`ui`**: Actividades Android optimizadas para una experiencia de usuario fluida.

## 4. Diagrama de Flujo de Datos
1. **Usuario** introduce credenciales en la UI.
2. **GestorHibrido** autentica en **PostgreSQL** (Hilo 1).
3. Si el login es correcto, recupera tareas de **MongoDB** (Hilo 2).
4. El **Handler** sincroniza los resultados y actualiza la UI en el **Hilo Principal**.

## 5. Configuración de la Base de Datos

### PostgreSQL
Ejecuta estos comandos para preparar el entorno. **IMPORTANTE:** Asegúrate de que la base de datos se llame `worksync` y que el usuario de tu sistema PostgreSQL coincida con el configurado en la app (Usuario: `postgres`, Contraseña: `admin`).

```sql
-- Crear base de datos
CREATE DATABASE worksync;
\c worksync;

-- Crear tabla
CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

-- Insertar Usuario de prueba (OBLIGATORIO para el login)
INSERT INTO empleados (nombre, email, password) VALUES 
('Usuario Prueba', 'test@worksync.com', '1234');
```

> [!TIP]
> Si tu contraseña de PostgreSQL no es `admin`, cámbiala en el archivo `app/src/main/java/com/example/worksync/config/PostgresConfig.java` antes de ejecutar la app.

### MongoDB
Inserta datos de prueba iniciales para ver tareas en el dashboard:
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

## 6. Solución de Problemas (Troubleshooting)

### "Credenciales incorrectas o Error de conexión"
Si no puedes entrar:
1.  **Verifica la Contraseña del Sistema**: Asegúrate de que en `PostgresConfig.java` la variable `PASS` sea la contraseña real de tu usuario `postgres` de Windows/Linux.
2.  **Firewall**: El puerto `5432` debe estar abierto para que el emulador (IP `10.0.2.2`) pueda acceder al host.
3.  **pg_hba.conf**: Asegúrate de que PostgreSQL permita conexiones desde el emulador.
4.  **Wipe Data**: Si el emulador no arranca, usa la opción "Wipe Data" en el Device Manager de Android Studio.

## 5. Instrucciones de Ejecución
1.  **Conectividad**: Asegúrate de que tus servicios de base de datos permitan conexiones desde la IP `10.0.2.2` (puente del emulador Android).
2.  **Compilación**: Abre el proyecto en Android Studio y sincroniza Gradle.
3.  **Lanzamiento**: Ejecuta la aplicación. Usa las credenciales `test@worksync.com` / `1234` para probar la integración completa.

## 6. Seguridad y Control de Versiones
- **Seguridad**: Autenticación básica con manejo de excepciones para prevenir cierres inesperados (Force Close).
- **Git**: Se han seguido convenciones de commits semánticos (`feat`, `refactor`, `docs`) para mantener la trazabilidad del desarrollo.

## 7. Pruebas Automáticas y Guía de Usuario

### Pruebas (Automated Tests)
El proyecto incluye pruebas unitarias para validar la lógica del sistema:
- **`GestorHibridoTest`**: Verifica la instanciación y disponibilidad de la lógica de integración.
- **`EmpleadoDAOTest`**: Prueba conceptual de la lógica de mapeo y autenticación.
- *Ejecución*: Puedes ejecutar los tests desde Android Studio haciendo click derecho en la carpeta `test` y seleccionando "Run 'Tests in WorksSync'".

### Guía de Usuario (Paso a Paso)
1.  **Login**: Abre la app e introduce el correo `test@worksync.com` y contraseña `1234`. Pulsa el botón "Entrar".
2.  **Dashboard**: Una vez dentro, verás un mensaje de bienvenida con tu nombre y la lista de tareas asignadas (extraídas de MongoDB).
3.  **Logout**: Para salir, pulsa el botón "Cerrar Sesión" en la esquina superior derecha; esto te devolverá a la pantalla de login de forma segura.
