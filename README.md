# INF331 - Tarea 3: Tarjeta de Fidelidad Gamificada

Sistema de gestión de programa de fidelidad para tiendas de conveniencia desarrollado en Java con arquitectura orientada a objetos y pruebas unitarias.

Este proyecto implementa un sistema completo de gestión de programas de fidelidad que permite:

- **Gestión de clientes**: Registro, búsqueda, listado, actualización, eliminación y estadísticas de clientes con validación de datos.
- **Sistema de puntos**: Acumulación automática de puntos por compras, con cálculo de bonus diario y multiplicadores según nivel.
- **Niveles de fidelidad**: Sistema progresivo (Bronce, Plata, Oro, Platino) con beneficios y multiplicadores incrementales.
- **Gestión de compras**: Registro, búsqueda, listado, eliminación y estadísticas de compras, con historial completo y cálculo automático de puntos.
- **Interfaz CLI**: Menú interactivo por consola con submenús para clientes y compras, consulta de estado y resumen general del sistema.

## Diseño del Sistema

### Diagrama de Clases

Se recomienda abrir el diagrama como imagen en un visor externo.

![Diagrama de Clases](docs/diagrama_clases.png)

### Arquitectura

El sistema sigue principios de diseño orientado a objetos con separación clara de responsabilidades:

```
src/
├── main/java/com/tarea3/
│   ├── App.java                    # Punto de entrada de la aplicación
│   ├── cli/                        # Interfaz de línea de comandos
│   │   ├── ClienteMenu.java
│   │   ├── CompraMenu.java
│   │   ├── ConsoleUtils.java
│   │   └── MenuPrincipal.java
│   ├── modelo/                     # Entidades del dominio
│   │   ├── Cliente.java
│   │   ├── Compra.java
│   │   └── Nivel.java
│   ├── repositorio/                # Capa de persistencia
│   │   ├── ClienteRepositorio.java
│   │   └── CompraRepositorio.java
│   └── servicio/                   # Lógica de negocio
│       ├── ClienteServicio.java
│       └── CompraServicio.java
└── test/java/com/tarea3/           # Pruebas unitarias
    ├── modelo/
    │   ├── ClienteTest.java
    │   ├── CompraTest.java
    │   └── NivelTest.java
    ├── repositorio/
    │   ├── ClienteRepositorioTest.java
    │   └── CompraRepositorioTest.java
    └── servicio/
        └── CompraServicioTest.java
```

### Entidades Principales

- **Cliente**: Representa un cliente con atributos ID, nombre, correo, puntos acumulados y nivel actual
- **Compra**: Registra las transacciones con ID, monto, fecha e ID del cliente asociado
- **Nivel**: Enumera los niveles de fidelidad con sus respectivos multiplicadores

### Reglas de Negocio

1. **Puntos base**: 1 punto por cada $100 de compra (redondeo hacia abajo).
2. **Multiplicadores por nivel**:
   - Bronce: x1.0 (0-499 puntos)
   - Plata: x1.2 (500-1499 puntos)
   - Oro: x1.5 (1500-2999 puntos)
   - Platino: x2.0 (3000+ puntos)
3. **Bonus diario**: +10 puntos por 3 compras seguidas en el mismo día.
4. **Validaciones**: Correo electrónico válido, montos positivos, etc.

> [!NOTE]
> Se hacen las siguientes asunciones con respecto del bonus:

1. Para aplicar el bono se utiliza la siguiente fórmula:

   $$ \text{pts por compra} = \left(\text{pts base} + \text{pts bono}\right) \cdot \text{multiplicador}$$

   Es decir, el bono también es afectado por el multiplicador del nivel actual del cliente.

2. El bono se aplica una sola vez por día.

## Requisitos del Sistema

- **Java**: 21 o superior
- **Maven**: 3.8 o superior
- **JUnit**: 5.10.1 (incluido en dependencias)

## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/MyckollWinchester/inf331-tarea3
cd inf331-tarea3
```

### 2. Compilar el proyecto

```bash
mvn clean compile
```

### 3. Ejecutar la aplicación

```bash
mvn exec:java "-Dexec.mainClass=com.tarea3.App"
```

### 4. Crear JAR ejecutable

```bash
mvn clean package
java -jar target/inf331-tarea3-1.0.0.jar
```

también se puede ejecutar con datos de demostración:

```bash
java -jar target/inf331-tarea3-1.0.0.jar --demo
```

## Ejecución de Pruebas

### Ejecutar todas las pruebas

```bash
mvn test
```

### Generar reporte de cobertura

```bash
mvn clean test jacoco:report
```

El reporte de cobertura se genera en `target/site/jacoco/index.html`

### Ejemplo de salida de tests

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.tarea3.modelo.ClienteTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.254 s -- in com.tarea3.modelo.ClienteTest
[INFO] Running com.tarea3.modelo.CompraTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.129 s -- in com.tarea3.modelo.CompraTest
[INFO] Running com.tarea3.modelo.NivelTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in com.tarea3.modelo.NivelTest
[INFO] Running com.tarea3.repositorio.ClienteRepositorioTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.112 s -- in com.tarea3.repositorio.ClienteRepositorioTest
[INFO] Running com.tarea3.repositorio.CompraRepositorioTest
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.081 s -- in com.tarea3.repositorio.CompraRepositorioTest
[INFO] Running com.tarea3.servicio.ClienteServicioTest
[INFO] Tests run: 24, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.072 s -- in com.tarea3.servicio.ClienteServicioTest
[INFO] Running com.tarea3.servicio.CompraServicioTest
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.042 s -- in com.tarea3.servicio.CompraServicioTest
[INFO] 
[INFO] Results:
[INFO]
[INFO] Tests run: 120, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- jacoco:0.8.12:report (report) @ inf331-tarea3 ---
[INFO] Loading execution data file C:\workspace\inf331-tarea3\target\jacoco.exec
[INFO] Analyzed bundle 'Tarea 3: Tarjeta de Fidelidad Gamificada' with 12 classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.705 s
[INFO] Finished at: 2025-07-08T23:31:44-04:00
[INFO] ------------------------------------------------------------------------
```

## Medición de Cobertura

### Tipo de Cobertura Medida

Este proyecto utiliza **JaCoCo (Java Code Coverage)** para medir la cobertura de código, específicamente:

#### 1. **Cobertura de Líneas (Line Coverage)**

- **Qué mide**: Porcentaje de líneas de código ejecutadas durante las pruebas
- **Por qué**: Es una métrica fácil de interpretar. Indica qué código se ejecutó al menos una vez.

Se obtuvo una cobertura de línea promedio total del **87%**. Para un total de 1.436 líneas de código.


#### 2. **Cobertura de Ramas (Branch Coverage)**
- **Qué mide**: Porcentaje de ramas condicionales (if/else, switch, loops) ejecutadas
- **Por qué**: Más sofisticada que la cobertura de líneas, asegura que se prueban todos los caminos lógicos del código.

Se obtuvo una cobertura de ramas promedio total del **87%**. Para un total de 174 ramas.

## Uso de la Aplicación

### Menú Principal

```
==================================================
                  MENÚ PRINCIPAL
==================================================
1. Gestión de Clientes
2. Gestión de Compras
3. Mostrar Puntos/Nivel de Cliente
4. Resumen General
0. Salir
```

### Funcionalidades Disponibles

#### 1. **Gestión de Clientes**
   - **Agregar cliente**: Crear nuevo cliente con validación automática (ID autoincremental)
   - **Listar clientes**: Ver todos los clientes registrados con sus datos básicos
   - **Buscar cliente**: Buscar por ID o correo electrónico
   - **Actualizar cliente**: Modificar nombre o correo de cliente existente
   - **Eliminar cliente**: Eliminar cliente con confirmación
   - **Mostrar estadísticas**: Ver distribución por niveles y estadísticas generales

#### 2. **Gestión de Compras**
   - **Registrar compra**: Crear nueva compra con cálculo automático de puntos (ID autoincremental)
   - **Listar compras**: Ver historial completo de todas las compras
   - **Buscar compra**: Buscar compra específica por ID
   - **Compras por cliente**: Ver historial de compras de un cliente específico
   - **Eliminar compra**: Eliminar compra con confirmación
   - **Mostrar estadísticas**: Ver estadísticas de montos y puntos

#### 3. **Mostrar Puntos/Nivel de Cliente**
   - Consultar información detallada de un cliente específico
   - Ver puntos actuales, nivel y multiplicador
   - Mostrar progreso hacia el siguiente nivel
   - Historial de compras del cliente
   - Estadísticas de gastos y puntos ganados

#### 4. **Resumen General**
   - Estadísticas generales del sistema
   - Top 5 clientes por puntos
   - Estadísticas de compras (monto total, promedio)
   - Información consolidada de todos los datos

### Ejemplos de Uso

1. **Agregar un nuevo cliente**:
   - Seleccionar opción 1 (Gestión de Clientes)
   - Seleccionar opción 1 (Agregar cliente)
   - Ingresar nombre y correo electrónico
   - El sistema asignará automáticamente un ID único

2. **Registrar una compra**:
   - Seleccionar opción 2 (Gestión de Compras)
   - Seleccionar opción 1 (Registrar compra)
   - Ingresar ID del cliente y monto de la compra
   - Los puntos se calculan automáticamente según el nivel del cliente

3. **Consultar estado de un cliente**:
   - Seleccionar opción 3 (Mostrar Puntos/Nivel de Cliente)
   - Ingresar ID del cliente
   - Ver información detallada: puntos, nivel, historial de compras

4. **Ver resumen general**:
   - Seleccionar opción 4 (Resumen General)
   - Consultar estadísticas del sistema y top clientes

## Estructura de Pruebas

Las pruebas están organizadas por capas y responsabilidades:

- **Pruebas de Modelo**: Validan la lógica y restricciones de las entidades principales (`Cliente`, `Compra`, `Nivel`).
- **Pruebas de Repositorio**: Verifican operaciones CRUD, búsquedas y persistencia en memoria para clientes y compras.
- **Pruebas de Servicio**: Evalúan la lógica de negocio, reglas de puntuación, bonus, validaciones y flujos de uso en los servicios.

### Estrategias de Testing

- **Pruebas Unitarias**: Cada clase se prueba de forma aislada, usando dobles de prueba cuando es necesario.
- **Pruebas Parametrizadas**: Se utilizan para validar múltiples escenarios y combinaciones de entrada con JUnit 5.
- **Cobertura de Código**: Se mide con JaCoCo, excluyendo la capa CLI y la clase principal.

## Tecnologías Utilizadas

- **Java 21**: Lenguaje de programación principal.
- **Maven 3.11.0**: Gestión de dependencias y construcción.
- **JUnit 5.10.1**: Framework de pruebas unitarias.
- **AssertJ 3.24.2**: Biblioteca de assertions fluidas.
- **JaCoCo 0.8.11**: Medición de cobertura de código.
- **JavaMail 1.6.2**: Validación de formato de correo electrónico.

## Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

---

*Proyecto desarrollado como parte del curso de Pruebas de Software*
