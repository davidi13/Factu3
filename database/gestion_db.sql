CREATE DATABASE IF NOT EXISTS gestion;
USE gestion;

CREATE TABLE articulos (
    idArticulo int NOT NULL PRIMARY KEY ,
    codigoArticulo varchar NOT NULL ,
    codigoBarrasArticulo varchar NULL ,
    descripcionArticulo varchar NULL ,
    familiaArticulo int NULL ,
    costeArticulo double NULL ,
    margenComercialArticulo double NULL ,
    pvpArticulo double NULL ,
    proveedorArticulo int NULL ,
    stockArticulo double NULL ,
    observacionesArticulo text NULL 
);

CREATE TABLE clientes (
    id int NOT NULL PRIMARY KEY ,
    nombreCliente varchar NOT NULL ,
    direccionCliente varchar NULL ,
    cpCliente varchar NULL ,
    poblacionCliente varchar NULL ,
    provinciaCliente varchar NULL ,
    paisCliente varchar NULL ,
    cifCliente varchar NOT NULL ,
    telCliente varchar NULL ,
    emailCliente varchar NULL ,
    ibanCliente varchar NULL ,
    riesgoCliente double NULL ,
    descuentoCliente double NULL ,
    observacionesCliente text NULL 
);

CREATE TABLE comerciales (
    id int NOT NULL PRIMARY KEY ,
    nombre varchar NOT NULL ,
    direccion varchar NULL ,
    telefono varchar NULL ,
    email varchar NULL ,
    comision decimal NULL ,
    observaciones text NULL 
);

CREATE TABLE distribuidores (
    id int NOT NULL PRIMARY KEY ,
    nombre varchar NOT NULL ,
    direccion varchar NULL ,
    telefono varchar NULL ,
    email varchar NULL ,
    zona varchar NULL ,
    observaciones text NULL 
);

CREATE TABLE empresa (
    id int NOT NULL PRIMARY KEY ,
    nombre varchar NOT NULL ,
    direccion varchar NULL ,
    telefono varchar NULL ,
    email varchar NULL ,
    cif varchar NULL ,
    logo varchar NULL 
);

CREATE TABLE facturasClientes (
    idFacturaCliente int NOT NULL PRIMARY KEY ,
    numeroFacturaCliente int NOT NULL ,
    fechaFacturaCliente date NOT NULL ,
    idClienteFactura int NOT NULL ,
    baseImponibleFacturaCliente double NOT NULL ,
    ivaFacturaCliente double NOT NULL ,
    totalFacturaCliente double NOT NULL ,
    hashFacturaCiente varchar NULL ,
    qrFacturaCliente varchar NULL ,
    cobradaFactura tinyint NOT NULL ,
    formaCobroFactura int NOT NULL ,
    fechaCobroFactura date NULL ,
    observacionesFacturaClientes text NULL 
);

CREATE TABLE familiaArticulos (
    idFamiliaArticulos int NOT NULL PRIMARY KEY ,
    codigoFamiliaArticulos varchar NOT NULL ,
    denominacionFamilias varchar NOT NULL 
);

CREATE TABLE formaPago (
    idFormaPago int NOT NULL PRIMARY KEY ,
    tipoFormaPago varchar NOT NULL ,
    fechaCobroFormaPago date NOT NULL ,
    observacionesFormaPago text NULL 
);

CREATE TABLE lineasFacturasClientes (
    id int NOT NULL PRIMARY KEY ,
    idFacturaCliente int NOT NULL ,
    idArticulo int NOT NULL ,
    cantidad decimal NOT NULL ,
    precioUnitario decimal NOT NULL ,
    idTipoIva int NOT NULL ,
    descuento decimal NULL DEFAULT 0.00,
    total decimal NOT NULL 
);

CREATE TABLE proveedores (
    id int NOT NULL PRIMARY KEY ,
    nombre varchar NOT NULL ,
    direccion varchar NULL ,
    cif varchar NOT NULL ,
    telefono varchar NULL ,
    email varchar NULL ,
    observaciones text NULL 
);

CREATE TABLE rectificativasClientes (
    idRectificativaCliente int NOT NULL PRIMARY KEY ,
    numeroRectificativaCliente int NOT NULL ,
    fechaRectificativaCliente date NOT NULL ,
    idClienteRectificativaCliente int NOT NULL ,
    baseImponibleRectificativaCliente double NOT NULL ,
    ivaRectificativaCliente double NOT NULL ,
    totalRectificativaCliente double NOT NULL ,
    hashRectificativaCliente varchar NULL ,
    qrRectificativaCliente varchar NULL ,
    observacionesRectificativaCliente text NULL 
);

CREATE TABLE tiposIva (
    idTipoIva int NOT NULL PRIMARY KEY ,
    iva double NOT NULL ,
    observacionesTipoIva text NULL 
);

CREATE TABLE trabajadores (
    id int NOT NULL PRIMARY KEY ,
    nombre varchar NOT NULL ,
    direccion varchar NULL ,
    telefono varchar NULL ,
    email varchar NULL ,
    puesto varchar NULL ,
    salario decimal NULL ,
    observaciones text NULL 
);

CREATE TABLE usuarios (
    id int NOT NULL PRIMARY KEY ,
    usuario varchar NOT NULL ,
    password varchar NOT NULL ,
    rol varchar NULL DEFAULT usuario
);

