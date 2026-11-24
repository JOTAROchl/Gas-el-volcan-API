INSERT INTO productos (
    codigo,
    categoria,
    nombre,
    descripcion,
    unidad,
    precio_residencial,
    precio_comercial,
    stock,
    imagen
)
SELECT catalogo.codigo,
       catalogo.categoria,
       catalogo.nombre,
       catalogo.descripcion,
       catalogo.unidad,
       catalogo.precio_residencial,
       catalogo.precio_comercial,
       catalogo.stock,
       catalogo.imagen
FROM (
    SELECT 'CL001' AS codigo, 'Cilindros de Gas' AS categoria, 'Cilindro GLP 5 kg' AS nombre, 'Cilindro de gas licuado de petróleo 5 kg. Para uso residencial (cocina, calefacción pequeña).' AS descripcion, 'Unidad' AS unidad, 6500.00 AS precio_residencial, 6000.00 AS precio_comercial, 80 AS stock, '/images/Galon5Kg.jpg' AS imagen
    UNION ALL SELECT 'CL002', 'Cilindros de Gas', 'Cilindro GLP 11 kg', 'Cilindro estándar doméstico. El más utilizado en hogares chilenos. Compatible con reguladores estándar.', 'Unidad', 12000.00, 11000.00, 200, '/images/Galon11Kg.jpg'
    UNION ALL SELECT 'CL003', 'Cilindros de Gas', 'Cilindro GLP 15 kg', 'Cilindro de mayor capacidad para hogares de alto consumo o locales pequeños.', 'Unidad', 16000.00, 14500.00, 90, '/images/Galon15Kg.jpg'
    UNION ALL SELECT 'CL004', 'Cilindros de Gas', 'Cilindro GLP 45 kg', 'Cilindro industrial. Uso comercial: restaurantes, talleres, calefacción de locales.', 'Unidad', 45000.00, 40000.00, 30, '/images/Galon45Kg.jpg'
    UNION ALL SELECT 'RG001', 'Reguladores', 'Regulador doméstico estándar', 'Regulador de 1 etapa para cilindros 5, 11 y 15 kg. Presión de salida 28 mbar.', 'Unidad', 8990.00, 8200.00, 45, '/images/ReguladorDeGas5-11-15kg.jpg'
    UNION ALL SELECT 'RG002', 'Reguladores', 'Regulador de alta presión', 'Regulador para cocinas industriales o equipos de mayor consumo. Presión regulable.', 'Unidad', 18990.00, 17000.00, 12, '/images/RegDGasAltaPres.jpg'
    UNION ALL SELECT 'RG003', 'Reguladores', 'Regulador dual (2 salidas)', 'Permite conectar dos artefactos simultáneamente al mismo cilindro.', 'Unidad', 14990.00, 13500.00, 18, '/images/ReguladorDoble(2Salidas).jpg'
    UNION ALL SELECT 'MG001', 'Mangueras y Conexiones', 'Manguera gas 1.5 m', 'Manguera flexible homologada. Diámetro interior 9mm. Compatible con reguladores estándar.', 'Unidad', 3990.00, 3500.00, 80, '/images/MangueraGas1.5m.jpg'
    UNION ALL SELECT 'MG002', 'Mangueras y Conexiones', 'Manguera gas 3 m', 'Manguera larga para instalaciones donde el artefacto está alejado del cilindro.', 'Unidad', 6990.00, 6200.00, 50, '/images/MangueraGas3m.jpg'
    UNION ALL SELECT 'MG003', 'Mangueras y Conexiones', 'Abrazadera metálica', 'Abrazadera de acero para asegurar la conexión manguera-regulador y manguera-artefacto.', 'Unidad', 990.00, 800.00, 200, '/images/AbrazaderasGas.jpg'
    UNION ALL SELECT 'MG004', 'Mangueras y Conexiones', 'Kit conexión completo', 'Todo lo necesario para instalar un cilindro nuevo: regulador, manguera 1.5m y abrazaderas.', 'Kit', 12990.00, 11500.00, 25, '/images/KitConexionCompleto.jpg'
    UNION ALL SELECT 'AC001', 'Accesorios', 'Carro porta cilindro 11/15 kg', 'Carro metálico con ruedas para transportar cilindros dentro del hogar con seguridad.', 'Unidad', 12990.00, 11000.00, 20, '/images/CarroPortaGalones11-15Kg-.jpg'
    UNION ALL SELECT 'AC002', 'Accesorios', 'Tapa protectora para válvula', 'Tapa de plástico ABS para proteger la válvula del cilindro durante el transporte.', 'Unidad', 1490.00, 1200.00, 60, '/images/TapaProtectoraGas.jpg'
    UNION ALL SELECT 'AC003', 'Accesorios', 'Detector de gas a batería', 'Sensor electroquímico. Alarma sonora y visual ante fuga de gas GLP o metano.', 'Unidad', 19990.00, 17000.00, 8, '/images/DetectorGasBateria.jpg'
) AS catalogo
LEFT JOIN productos AS existente ON existente.codigo = catalogo.codigo
WHERE existente.codigo IS NULL;
