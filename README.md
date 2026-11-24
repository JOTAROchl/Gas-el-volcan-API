# Gas El Volcan API

Backend REST para el sitio web Gas El Volcan, desarrollado con Spring Boot y Java 21.

## Requisitos

- Java 21
- Laragon con MySQL iniciado

La configuracion local espera MySQL en `localhost:3306`, una base de datos llamada `gas_el_volcan` y el usuario `root` sin contrasena, que son los valores habituales de Laragon. La base de datos se crea al iniciar la aplicacion si el usuario tiene permisos. Si tu configuracion es diferente, define `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` antes de iniciar Spring Boot.

Flyway administra el esquema con migraciones SQL versionadas en `src/main/resources/db/migration`. La migracion `V1__crear_tabla_productos.sql` crea la tabla de productos y `V2__crear_tabla_usuarios.sql` crea la tabla de usuarios. Como esta base local ya tenia la tabla de productos generada anteriormente por Hibernate, la aplicacion la adopta como version 0 y aplica las migraciones sin eliminarla. Hibernate queda en modo `validate` y no modifica el esquema.

## Ejecutar

En PowerShell, crea una clave local aleatoria de 256 bits y ejecuta la aplicacion:

```powershell
$jwtSecretBytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($jwtSecretBytes)
$rng.Dispose()
$env:JWT_SECRET = [Convert]::ToBase64String($jwtSecretBytes)
.\mvnw.cmd spring-boot:run
```

La variable `JWT_SECRET` es obligatoria y no debe publicarse. La API se ejecutara en `http://localhost:8080`.

## Swagger / OpenAPI

Con la aplicacion iniciada, abre `http://localhost:8080/swagger-ui/index.html` para explorar y probar la API. La especificacion OpenAPI en JSON esta disponible en `http://localhost:8080/v3/api-docs`.

## API de productos

La API permite consultar, crear, editar y eliminar productos:

- `GET /api/productos`
- `GET /api/productos/{codigo}`
- `POST /api/productos`
- `PUT /api/productos/{codigo}`
- `DELETE /api/productos/{codigo}`

El cuerpo de POST y PUT usa `categoria`, `nombre`, `descripcion`, `unidad`, `precioResidencial`, `precioComercial`, `stock` e `imagen`. El backend genera el `codigo` al crear el producto. Las validaciones devuelven `400`, los productos inexistentes `404` y los conflictos de datos `409`.

Las consultas GET son publicas. Crear, actualizar o eliminar productos requiere un JWT con el rol `ADMINISTRADOR`; usa el boton **Authorize** de Swagger con el valor `Bearer <token>`.

## Autenticacion

- `POST /api/auth/registro`: crea una cuenta con rol `CLIENTE` y devuelve un JWT.
- `POST /api/auth/login`: valida correo y contrasena y devuelve un JWT.

El registro recibe `run`, `nombre`, `apellidos`, `email`, `password`, `region`, `comuna` y `direccion`. El RUN se valida en el backend, los correos y RUN duplicados se rechazan, y la contrasena se guarda con hash BCrypt. El registro no acepta ni asigna el rol de administrador.

La tabla de usuarios ya esta preparada en MySQL. Para promover una cuenta a administradora en el entorno local, hazlo manualmente desde una cuenta con acceso autorizado a la base de datos; nunca expongas esa accion en el registro publico. Aun falta integrar estos endpoints con los formularios React.

Los tests usan H2 en memoria y ejecutan las migraciones; la ejecucion normal usa MySQL de Laragon.

## Pruebas

```powershell
.\mvnw.cmd test
```
