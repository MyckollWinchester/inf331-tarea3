# INF331 - Tarea 3: Tarjeta de Fidelidad Gamificada

Sistema de gestión de programa de fidelidad para tiendas de conveniencia desarrollado en Java con arquitectura orientada a objetos y pruebas unitarias.

Este proyecto implementa un sistema completo de gestión de programas de fidelidad que permite:

- **Gestión de clientes**: Registro, actualización y eliminación de clientes con validación de datos.
- **Sistema de puntos**: Acumulación automática de puntos basada en compras con multiplicadores por nivel.
- **Niveles de fidelidad**: Sistema progresivo (Bronce, Plata, Oro, Platino) con beneficios incrementales.
- **Registro de compras**: Historial completo de transacciones con cálculo automático de bonificaciones.
- **Interfaz CLI**: Menú interactivo por consola para todas las operaciones.

## Diseño del Sistema

### Arquitectura

El sistema sigue principios de diseño orientado a objetos con separación clara de responsabilidades:

> [!NOTE]
> WIP

```
src/
├── main/java/com/tienda/fidelidad/
│   ├── App.java                    # Punto de entrada de la aplicación
│   ├── modelo/                     # Entidades del dominio
│   │   ├── Cliente.java
│   │   ├── Compra.java
│   │   └── Nivel.java
│   ├── repository/                 # Capa de persistencia
│   │   ├── ClienteRepository.java
│   │   └── CompraRepository.java
│   ├── service/                    # Lógica de negocio
│   │   ├── ClienteService.java
│   │   ├── CompraService.java
│   │   └── PuntosService.java
│   └── ui/                         # Interfaz de usuario
│       └── ConsoleUI.java
└── test/java/                      # Pruebas unitarias
    └── com/tienda/fidelidad/
        ├── model/
        ├── service/
        └── repository/
```

### Entidades Principales

- **Cliente**: Representa un cliente con atributos ID, nombre, correo, puntos acumulados, nivel actual
- **Compra**: Registra las transacciones con monto, fecha y cliente asociado
- **Nivel**: Enumera los niveles de fidelidad con sus respectivos multiplicadores

### Reglas de Negocio

1. **Puntos base**: 1 punto por cada $100 de compra (redondeo hacia abajo).
2. **Multiplicadores por nivel**:
   - Bronce: ×1.0 (0-499 puntos)
   - Plata: ×1.2 (500-1499 puntos)
   - Oro: ×1.5 (1500-2999 puntos)
   - Platino: ×2.0 (3000+ puntos)
3. **Bonus streak**: +10 puntos por 3 compras seguidas en el mismo día.
4. **Validaciones**: Correo electrónico válido, montos positivos.

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
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.268 s -- in com.tarea3.modelo.ClienteTest
[INFO] Running com.tarea3.modelo.NivelTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.030 s -- in com.tarea3.modelo.NivelTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- jacoco:0.8.11:report (report) @ inf331-tarea3 ---
[INFO] Loading execution data file C:\workspace\inf331-tarea3\target\jacoco.exec
[INFO] Analyzed bundle 'Tarea 3: Tarjeta de Fidelidad Gamificada' with 3 classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.289 s
[INFO] Finished at: 2025-06-18T19:29:03-04:00
[INFO] ------------------------------------------------------------------------
```

## Medición de Cobertura

### Tipo de Cobertura Medida

Este proyecto utiliza **JaCoCo (Java Code Coverage)** para medir la cobertura de código, específicamente:

#### 1. **Cobertura de Líneas (Line Coverage)**
- **Qué mide**: Porcentaje de líneas de código ejecutadas durante las pruebas
- **Por qué**: Es una métrica fácil de interpretar. Indica qué código se ejecutó al menos una vez.

#### 2. **Cobertura de Ramas (Branch Coverage)**
- **Qué mide**: Porcentaje de ramas condicionales (if/else, switch, loops) ejecutadas
- **Por qué**: Más sofisticada que la cobertura de líneas, asegura que se prueban todos los caminos lógicos del código.

#### 3. **Cobertura de Métodos (Method Coverage)**
- **Qué mide**: Porcentaje de métodos invocados durante las pruebas
- **Por qué**: Garantiza que cada método público tiene al menos una prueba que lo invoque.

## Uso de la Aplicación

### Menú Principal

```
=== SISTEMA DE FIDELIDAD ===
1. Gestión de Clientes
2. Gestión de Compras  
3. Mostrar Puntos/Nivel de Cliente
4. Salir
```

### Funcionalidades Disponibles

1. **Gestión de Clientes**
   - Agregar nuevo cliente
   - Listar todos los clientes
   - Actualizar información de cliente
   - Eliminar cliente

2. **Gestión de Compras**
   - Registrar nueva compra
   - Ver historial de compras
   - Actualizar compra existente
   - Eliminar compra

3. **Consulta de Estado** (al haber seleccionado un cliente)
   - Ver puntos acumulados
   - Verificar nivel actual
   - Historial de transacciones

## Estructura de Pruebas

Las pruebas están organizadas por capas:

- **Pruebas de Modelo**: Validación de entidades y sus comportamientos
- **Pruebas de Servicio**: Lógica de negocio y reglas de puntuación
- **Pruebas de Repositorio**: Operaciones CRUD y persistencia en memoria
- **Pruebas de Integración**: Flujos completos de casos de uso

### Estrategias de Testing

- **Pruebas Unitarias**: Aislamiento de componentes individuales
- **Pruebas Parametrizadas**: Verificación de múltiples escenarios con JUnit 5
- **Mocks y Stubs**: Simulación de dependencias cuando es necesario
- **Assertions Fluidas**: Uso de AssertJ para pruebas más legibles

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
