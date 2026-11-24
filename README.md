# Gas El Volcan API

Backend REST para el sitio web Gas El Volcan, desarrollado con Spring Boot y Java 21.

## Requisitos

- Java 21
- Laragon con MySQL iniciado

La configuracion local espera MySQL en `localhost:3306`, una base de datos llamada `gas_el_volcan` y el usuario `root` sin contrasena, que son los valores habituales de Laragon. La base de datos se crea al iniciar la aplicacion si el usuario tiene permisos. Si tu configuracion es diferente, define `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` antes de iniciar Spring Boot.

## Ejecutar

Desde esta carpeta, en PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

La API se ejecutara en `http://localhost:8080`.

## Alcance actual

Esta primera etapa contiene el proyecto base y la configuracion de conexion a MySQL. El CRUD de productos, la autenticacion JWT y la integracion con React se agregaran en etapas posteriores.
