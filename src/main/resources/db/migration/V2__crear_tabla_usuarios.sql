CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    run VARCHAR(12) NOT NULL,
    nombre VARCHAR(300) NOT NULL,
    apellidos VARCHAR(300) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    region VARCHAR(100),
    comuna VARCHAR(100),
    direccion VARCHAR(300),
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_run UNIQUE (run),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);
