CREATE DATABASE RoscosVarela;
USE RoscosVarela;

-- 1. TABLA CLIENTES
CREATE TABLE Clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre_cafeteria VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    contacto VARCHAR(100) NOT NULL
);

-- 2. TABLA PEDIDOS
CREATE TABLE Pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    cantidad_vainilla INT NOT NULL DEFAULT 0,
    cantidad_cocoa INT NOT NULL DEFAULT 0,
    cantidad_cafe INT NOT NULL DEFAULT 0,
    direccion_entrega VARCHAR(200) NOT NULL,
    fecha_pedido DATE NOT NULL,
    fecha_entrega DATE NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- 3. TABLA INSUMOS DE PRODUCCIÓN
CREATE TABLE Insumos_Produccion (
    id_insumo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_insumo VARCHAR(100) NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL DEFAULT 0,
    unidad VARCHAR(20) NOT NULL
);

-- 4. TABLA DE RECETAS
CREATE TABLE Recetas (
    id_receta INT AUTO_INCREMENT PRIMARY KEY,
    sabor VARCHAR(50) NOT NULL,
    id_insumo INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    unidad VARCHAR(20) NOT NULL,
    rendimiento INT NOT NULL DEFAULT 1,

    FOREIGN KEY (id_insumo) REFERENCES Insumos_Produccion(id_insumo)
);

-- 5. TABLA INSUMOS DE EMPAQUETADO
CREATE TABLE Insumos_Empaquetado (
    id_insumo_empaque INT AUTO_INCREMENT PRIMARY KEY,
    nombre_insumo VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    unidad VARCHAR(20) NOT NULL
);

-- 6. DATOS DE CLIENTES
INSERT INTO Clientes (nombre_cafeteria, direccion, contacto) VALUES
('Café Origenes', 'De Aldama 18, Centro, 91500 Coatepec, Ver.',  ' -- '),
('Tacita de fe', '91500, Centro, 91500 Ver. (hotel Monroy)',        ' -- '),
('Frio ',   'C. Constitución 143, Rincón Coatepec, Coatepec, 91500 Coatepec, Ver.', ' -- '),
('Tienda Bambucel', 'Independencia Ote., 91615 Teocelo, Ver.',  ' -- '),
('Café Monarca', 'De Aldama 12-2da, Centro, 91500 Coatepec, Ver.',  ' -- '),
('Cafetería UV', 'Facultad de Contaduria y Administración, Paseo los Lagos, Universidad Veracruzana s/n, Zona Universitaria, 91090 Xalapa-Enríquez, Ver.',  ' -- ');

-- 7. DATOS DE INSUMOS DE PRODUCCIÓN
INSERT INTO Insumos_Produccion (nombre_insumo, cantidad, unidad) VALUES
('Manteca vegetal', 0, 'kg'),
('Huevos', 0, 'unidad'),
('Vainilla', 0, 'ml'),
('Azúcar glass', 0, 'kg'),
('Harina', 0, 'kg'),
('Cocoa', 0, 'gramos'),
('Café', 0, 'gramos');

-- 8. DATOS DE INSUMOS DE EMPAQUETADO
INSERT INTO Insumos_Empaquetado (nombre_insumo, cantidad, unidad) VALUES
('Bolsas', 0, 'unidad'),
('Etiquetas vainilla', 0, 'unidad'),
('Etiquetas cocoa', 0, 'unidad'),
('Etiquetas café', 0, 'unidad');

-- 9. DATOS DE PEDIDOS
INSERT INTO Pedidos
(id_cliente, cantidad_vainilla, cantidad_cocoa, cantidad_cafe,
 direccion_entrega, fecha_pedido, fecha_entrega, total)
VALUES
(1, 20, 10, 5,  'Centro, Xalapa, Veracruz',  '2026-10-07', '2026-10-09', 875.00),
(2, 10, 20, 10, 'Coatepec, Veracruz',        '2026-10-07', '2026-10-10', 1000.00),
(3, 30, 5, 15,  'Centro, Córdoba, Veracruz', '2026-10-07', '2026-10-12', 1250.00);

-- 10- DATOS DE RECETAS.
INSERT INTO Recetas (sabor, id_insumo, cantidad, unidad, rendimiento) VALUES
-- ================= VAINILLA =================
('Vainilla', 1, 1.00,   'kg',      1),   -- 1 kg manteca
('Vainilla', 2, 8.00,   'unidad',  1),   -- 8 huevos
('Vainilla', 3, 90.00,  'ml',      1),   -- 90 ml vainilla
('Vainilla', 4, 1.00,   'kg',      1),   -- 1 kg azúcar glass
('Vainilla', 5, 2.00,   'kg',      1),   -- 2 kg harina

-- ================= COCOA ====================
('Cocoa',    1, 1.00,   'kg',      1),
('Cocoa',    2, 8.00,   'unidad',  1),
('Cocoa',    3, 90.00,  'ml',      1),
('Cocoa',    4, 1.00,   'kg',      1),
('Cocoa',    5, 2.00,   'kg',      1),
('Cocoa',    6, 400.00, 'gramos',  1),   -- 400 g cocoa

-- ================= CAFÉ =====================
('Café',     1, 1.00,   'kg',      1),
('Café',     2, 8.00,   'unidad',  1),
('Café',     3, 90.00,  'ml',      1),
('Café',     4, 1.00,   'kg',      1),
('Café',     5, 2.00,   'kg',      1),
('Café',     7, 80.00,  'gramos',  1);   -- 80 g café

-- 11. CONSULTAS DE PRUEBA
-- Ver todos los clientes
SELECT * FROM Clientes;
-- Ver insumos de producción
SELECT * FROM Insumos_Produccion;

-- Ver insumos de empaquetado
SELECT * FROM Insumos_Empaquetado;

-- Ver todos los pedidos
SELECT * FROM Pedidos;

-- Ver pedidos con el nombre de la cafetería
SELECT
    p.id_pedido,
    c.nombre_cafeteria,
    p.cantidad_vainilla,
    p.cantidad_cocoa,
    p.cantidad_cafe,
    p.direccion_entrega,
    p.fecha_pedido,
    p.fecha_entrega,
    p.total
FROM Pedidos p
INNER JOIN Clientes c ON p.id_cliente = c.id_cliente;

-- Total de ventas
SELECT SUM(total) AS ventas_totales FROM Pedidos;

-- Cantidad de pedidos
SELECT COUNT(*) AS cantidad_pedidos FROM Pedidos;

-- Pedido de mayor valor
SELECT MAX(total) AS pedido_mayor FROM Pedidos;

-- Total vendido por cafetería
SELECT
    c.nombre_cafeteria,
    SUM(p.total) AS total_vendido
FROM Pedidos p
INNER JOIN Clientes c ON p.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.nombre_cafeteria;