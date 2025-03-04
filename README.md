# **Factu3 - Software de Facturación** 📄💼  

**Factu3** es un software de facturación **open-source** desarrollado por **David Belando Serrano**, alumno de **2º año de Desarrollo de Aplicaciones Multiplataforma (DAM)**.  

Este proyecto permite la **gestión de clientes, facturas y facturas rectificativas**, facilitando la administración y control de facturación en negocios.  

---

## **📌 Características**
✔ Gestión de **clientes** (alta, modificación, eliminación).  
✔ Creación y gestión de **facturas**.  
✔ Generación de **facturas rectificativas**.  
✔ Visualización y administración de **productos y servicios**.  
✔ Exportación de facturas en **PDF**.  
✔ Base de datos MySQL integrada con **conexión automatizada**.  
✔ **Interfaz moderna y fácil de usar** con JavaFX.  

---

## **📂 Tecnologías utilizadas**
🔹 **Java** (JDK 8 o superior)  
🔹 **JavaFX** (Interfaz gráfica)  
🔹 **MySQL** (Base de datos)  
🔹 **JDBC** (Conexión con MySQL)  
🔹 **Maven** (Gestión de dependencias)  

---

## 📌 Base de Datos
Para que Factu3 funcione correctamente, es necesario crear la base de datos en MySQL.  

📂 **Archivos disponibles** en la carpeta [`database/`](database/):  
- 🗄 **[`gestion_db.sql`](database/gestion_db.sql)** → Script SQL para crear la base de datos y sus tablas.  
- 📜 **[`diagrama.pdf`](database/diagrama.pdf)** → Diagrama entidad-relación (ERD) de la base de datos.  

### 🔹 Pasos para configurar la base de datos:
1. Abre **MySQL Workbench** o cualquier cliente SQL.
2. Ejecuta el siguiente comando para crear la base de datos:
   ```sql
   SOURCE database/gestion_db.sql;

## **🛠 Instalación y uso**
1. Clonar el repositorio:
   ```sh
   git clone https://github.com/davidi13/Factu3.git
