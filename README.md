# Gas El Volcán — API backend

API REST de productos y autenticación para el sitio web Gas El Volcán.
Implementada con Java 21, Spring Boot, Spring Security, JPA, MySQL y Flyway.

## Requisitos y base de datos

- Java 21.
- Laragon con MySQL iniciado para ejecutar la aplicación localmente.

La configuración local usa MySQL en `localhost:3306`, base de datos
`gas_el_volcan` y, por defecto, el usuario local `root` sin contraseña. La base
se crea al iniciar la aplicación si ese usuario tiene permisos. Estos valores
son solo para un entorno de desarrollo local; no reutilices una cuenta sin
contraseña en producción. Si tu instalación difiere, define `DB_URL`,
`DB_USERNAME` y `DB_PASSWORD`.

Flyway administra el esquema con migraciones SQL en
`src/main/resources/db/migration`:

- V1 crea la tabla de productos.
- V2 crea la tabla de usuarios con restricciones únicas para RUN y correo.
- V3 carga los 14 productos iniciales que no existan ya, sin duplicar sus
  códigos.

Hibernate valida el esquema y no lo modifica. H2 se utiliza únicamente en las
pruebas automatizadas; la ejecución local normal utiliza MySQL.

## Ejecutar localmente

En PowerShell, inicia MySQL desde Laragon. Desde la carpeta del backend genera
una clave JWT de al menos 32 bytes y ejecuta Spring Boot:

```powershell
$jwtSecretBytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($jwtSecretBytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($jwtSecretBytes)
.\mvnw.cmd spring-boot:run
```

`JWT_SECRET` es obligatoria; no la guardes en el repositorio ni la compartas.
La API se inicia en `http://localhost:8080`.

Para iniciar el frontend, sigue el README del
[repositorio Gas El Volcán](https://github.com/JOTAROchl/Gas-Volcan). En
desarrollo, Vite reenvía `/api` a esta API local.

## Arquitectura

Las operaciones siguen la separación Controlador → Servicio → Repositorio y
persisten las entidades de productos y usuarios en MySQL. Las migraciones
Flyway versionan el esquema.

## Autenticación y permisos

- `POST /api/auth/registro`: registra una cuenta de cliente. Valida RUN, correo
  y contraseña; el RUN y el correo deben ser únicos. La contraseña se guarda
  con hash BCrypt.
- `POST /api/auth/login`: valida las credenciales y devuelve un JWT firmado
  con rol y vencimiento de 15 minutos.
- El registro público nunca permite asignar el rol Administrador.
- `GET` de productos es público. `POST`, `PUT` y `DELETE` requieren un JWT de
  una cuenta con rol `ADMINISTRADOR`; la autorización se valida en el backend.

No se crea una cuenta administradora automáticamente. Para un entorno local,
registra primero la cuenta del responsable y, con acceso autorizado a MySQL,
asigna el rol:

```sql
UPDATE usuarios
SET rol = 'ADMINISTRADOR'
WHERE email = '<correo-del-administrador>';
```

Luego cierra la sesión e inicia sesión de nuevo para recibir un token con el
rol actualizado. Nunca publiques credenciales ni habilites la asignación del
rol de administrador en el formulario público.

## Endpoints

| Método | Ruta | Acceso | Propósito |
|---|---|---|---|
| `POST` | `/api/auth/registro` | Público | Registrar un cliente |
| `POST` | `/api/auth/login` | Público | Autenticar y obtener JWT |
| `GET` | `/api/productos` | Público | Listar productos |
| `GET` | `/api/productos/{codigo}` | Público | Consultar un producto |
| `POST` | `/api/productos` | Administrador | Crear un producto; el código lo genera el backend |
| `PUT` | `/api/productos/{codigo}` | Administrador | Actualizar un producto |
| `DELETE` | `/api/productos/{codigo}` | Administrador | Eliminar un producto |

Los errores de validación responden `400`, la falta de autenticación `401`, los
permisos insuficientes `403`, un recurso inexistente `404` y los conflictos de
datos `409`.

## Swagger / OpenAPI

Con la API iniciada:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Especificación OpenAPI: `http://localhost:8080/v3/api-docs`

Para probar operaciones administrativas en Swagger, usa **Authorize** con
`Bearer <token>` obtenido mediante el inicio de sesión.

## Alcance actual y límites

El frontend consume el catálogo y la autenticación de esta API. Las operaciones
administrativas de productos envían el JWT como Bearer. La selección de
productos, la solicitud de reserva y el formulario de contacto continúan siendo
funciones demostrativas del frontend: no se persisten ni se procesan como
pedidos reales.

La configuración local utiliza el proxy de Vite. Si frontend y API se
despliegan en orígenes distintos, configura CORS para los orígenes permitidos y
HTTPS antes de publicar.

## Pruebas

```powershell
.\mvnw.cmd test
```
