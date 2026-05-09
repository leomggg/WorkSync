# WorkSync - Gestión de Tareas Híbrida

**WorkSync** es una aplicación Android profesional diseñada para la gestión de empleados y tareas. El proyecto destaca por su **arquitectura de base de datos híbrida**, combinando la integridad de un sistema relacional con la flexibilidad de uno documental.

---

## ✨ Funcionalidades Principales

- **Autenticación Segura**: Sistema de login conectado a PostgreSQL.
- **Gestión de Tareas Real-Time**: Creación, visualización y actualización de tareas almacenadas en MongoDB.
- **Priorización de Trabajo**: Clasificación de tareas por niveles de prioridad (Alta, Media, Baja) con códigos de color.
- **Control de Estado**: Marcado rápido de tareas como completadas con efecto visual de tachado y reordenación automática.
- **Sincronización Híbrida**: Integración fluida entre datos de usuario (SQL) y datos de actividad (NoSQL).

---

## 🏗️ Arquitectura Técnica

El proyecto sigue el patrón **DAO (Data Access Object)** garantizando un código limpio y escalable:

- **PostgreSQL**: Gestiona la entidad `Empleado` y la seguridad de acceso.
- **MongoDB**: Gestiona la entidad `Tarea`, permitiendo esquemas flexibles y descripciones ricas.
- **Conectividad**: Configurado para conectar el emulador Android con servidores locales mediante el puente `10.0.2.2`.
- **Multihilo**: Uso de `ExecutorService` para evitar bloqueos en la interfaz de usuario (UI) durante las consultas a bases de datos.

---

## 🛠️ Requisitos e Instalación

### 1. Requisitos Previos
- **PostgreSQL**: Instalado y corriendo en el puerto `5432`.
- **MongoDB**: Instalado y corriendo en el puerto `27017`.
- **Android Studio**: Versión Ladybug o superior.

### 2. Configuración de Base de Datos
La aplicación cuenta con un sistema de **auto-inicialización**. La primera vez que inicies sesión:
1. La app intentará crear la base de datos `worksync` automáticamente en PostgreSQL.
2. Se creará la tabla de empleados y un usuario de prueba:
   - **Email:** `test@worksync.com`
   - **Password:** `1234`
3. Se conectará automáticamente a MongoDB para gestionar las colecciones de tareas.

> [!IMPORTANT]
> Las credenciales por defecto para la conexión son:
> - **Usuario:** `postgres`
> - **Contraseña:** `admin`
> Si tu configuración es distinta, ajusta los valores en `PostgresConfig.java` y `MongoConfig.java`.

---

## 📱 Guía de Uso

1. **Acceso**: Usa las credenciales de prueba (`test@worksync.com` / `1234`).
2. **Crear Tarea**: Pulsa el botón flotante **(+)** abajo a la derecha, asigna un título, descripción y nivel de prioridad.
3. **Completar Tarea**: Toca el icono de verificación (**v**) a la derecha de cualquier tarea. Verás como se tacha y se desplaza al final de la lista.
4. **Prioridad**: Las tareas se visualizan en colores según su urgencia (Rojo para Alta, Azul para Media).
5. **Cerrar Sesión**: Usa el botón "Salir" en la parte superior para volver al login de forma segura.

---

## 🚀 Despliegue

1. Clona el repositorio.
2. Sincroniza el proyecto con **Gradle**.
3. Asegúrate de que tus servicios de base de datos acepten conexiones locales.
4. Ejecuta en un emulador o dispositivo físico con acceso a la red local del host.

---
*Desarrollado como solución integral de gestión para entornos corporativos modernos.*
