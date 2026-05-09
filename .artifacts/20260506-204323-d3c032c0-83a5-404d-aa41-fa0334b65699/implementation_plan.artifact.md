# Plan de Reparación de Login e Integración de Tests

Este plan aborda el problema de "credenciales incorrectas" mediante la mejora de la robustez de la conexión a PostgreSQL, la actualización de la documentación de configuración y la adición de pruebas automatizadas en entorno Android.

## Proposed Changes

### [Backend Config]
Mejorar la robustez de la conexión y proporcionar logs claros para depuración.

#### [PostgresConfig.java](file:///C:/Users/Leo/Desktop/Workspaces/Android/WorkSync/app/src/main/java/com/example/worksync/config/PostgresConfig.java)
- Añadir logs de error específicos cuando falla la conexión (ej. error de contraseña vs base de datos no encontrada).
- Mantener las credenciales por defecto, pero documentar cómo cambiarlas dinámicamente si fuera necesario.

---

### [Documentation]
Asegurar que las instrucciones de configuración sean infalibles.

#### [README.md](file:///C:/Users/Leo/Desktop/Workspaces/Android/WorkSync/README.md)
- Añadir sección de "Troubleshooting" específica para el error de credenciales.
- Incluir recordatorio sobre el firewall de Windows y la configuración de `pg_hba.conf` de PostgreSQL.
- Clarificar que la contraseña del sistema PostgreSQL debe coincidir con la del código.

---

### [Testing]
Validar que el flujo de datos entre la app y la base de datos funciona.

#### [NEW] [LoginIntegrationTest.java](file:///C:/Users/Leo/Desktop/Workspaces/Android/WorkSync/app/src/androidTest/java/com/example/worksync/LoginIntegrationTest.java)
- Crear un test instrumentado que intente realizar un login usando el `EmpleadoDAO`.
- Este test verificará la conectividad real desde el entorno Android (emulador) hacia el host.

## Verification Plan

### Automated Tests
- Ejecutar el nuevo test de integración:
  ```bash
  ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.worksync.LoginIntegrationTest
  ```

### Manual Verification
- Iniciar la aplicación en el emulador.
- Intentar login con `test@worksync.com` / `1234`.
- Verificar que el Dashboard carga correctamente.
