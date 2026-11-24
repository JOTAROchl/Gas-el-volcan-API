# Gas El Volcan API

Backend REST para el sitio web Gas El Volcan, desarrollado con Spring Boot y Java 21.

## Requisitos

- Java 21
- Laragon con MySQL iniciado

La configuracion local espera MySQL en `localhost:3306`, una base de datos llamada `gas_el_volcan` y el usuario `root` sin contrasena, que son los valores habituales de Laragon. La base de datos se crea al iniciar la aplicacion si el usuario tiene permisos. Si tu configuracion es diferente, define `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` antes de iniciar Spring Boot.

Flyway administra el esquema con migraciones SQL versionadas en `src/main/resources/db/migration`. La migracion `V1__crear_tabla_productos.sql` crea la tabla de productos. Como esta base local ya tenia la tabla generada anteriormente por Hibernate, la aplicacion la adopta como version 0 y aplica la migracion inicial sin eliminarla. Hibernate queda en modo `validate` y no modifica el esquema.

## Ejecutar

Desde esta carpeta, en PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

La API se ejecutara en `http://localhost:8080`.

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

Los tests usan H2 en memoria y ejecutan las migraciones; la ejecucion normal usa MySQL de Laragon. La autenticacion JWT y la integracion con React se agregaran en etapas posteriores.

## Pruebas

```powershell
.\mvnw.cmd test
```
