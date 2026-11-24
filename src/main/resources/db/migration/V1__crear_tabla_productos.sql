CREATE TABLE IF NOT EXISTS productos (
    codigo VARCHAR(35) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    unidad VARCHAR(30) NOT NULL,
    precio_residencial DECIMAL(12, 2) NOT NULL,
    precio_comercial DECIMAL(12, 2) NOT NULL,
    stock INTEGER NOT NULL,
    imagen VARCHAR(500) NOT NULL,
    PRIMARY KEY (codigo)
);
