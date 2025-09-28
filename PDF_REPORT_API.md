# API de Reportes PDF - Best Travel

## Descripción

Esta implementación proporciona endpoints para generar reportes PDF usando JasperReports, permitiendo al frontend descargar archivos PDF sin almacenamiento físico en el servidor.

## Características

- ✅ **Generación en memoria**: Los PDFs se generan en memoria sin crear archivos físicos
- ✅ **Descarga inmediata**: El frontend puede descargar el archivo directamente
- ✅ **Información del reporte**: Endpoint para obtener metadatos del reporte antes de la descarga
- ✅ **Manejo de errores**: Respuestas estructuradas para errores
- ✅ **Documentación Swagger**: Endpoints documentados con OpenAPI
- ✅ **Logging**: Logs detallados para monitoreo y debugging

## Endpoints Disponibles

### 1. Descarga Directa de PDF (desde Base de Datos)
```
GET /api/v1/report/pdf
```

**Descripción**: Genera y descarga inmediatamente un reporte PDF con información de clientes desde la base de datos.

**Respuesta**: 
- Content-Type: `application/pdf`
- Content-Disposition: `attachment; filename="customer_report_YYYYMMDD.pdf"`

### 2. Información del Reporte (desde Base de Datos)
```
GET /api/v1/report/pdf/info
```

**Descripción**: Retorna información del reporte PDF incluyendo metadatos y URL de descarga.

**Respuesta**:
```json
{
  "fileName": "customer_report_20241201.pdf",
  "contentType": "application/pdf",
  "fileSize": 15420,
  "generatedAt": "2024-12-01T10:30:00",
  "reportType": "PDF",
  "downloadUrl": "/api/v1/report/pdf",
  "status": "SUCCESS",
  "message": "Reporte generado exitosamente"
}
```

### 3. 🆕 Generar PDF desde JSON del Frontend
```
POST /api/v1/report/pdf/from-json
```

**Descripción**: Genera un reporte PDF usando datos enviados desde el frontend en formato JSON.

**Request Body**:
```json
{
  "reportTitle": "Reporte Personalizado - Best Travel",
  "reportSubtitle": "Generado desde el frontend",
  "customers": [
    {
      "dni": "12345678",
      "fullName": "Juan Pérez",
      "totalLodgings": 5,
      "totalFlights": 3,
      "totalTours": 2
    },
    {
      "dni": "87654321",
      "fullName": "María García",
      "totalLodgings": 2,
      "totalFlights": 4,
      "totalTours": 1
    }
  ]
}
```

**Respuesta**: 
- Content-Type: `application/pdf`
- Content-Disposition: `attachment; filename="customer_report_YYYYMMDD.pdf"`

### 4. 🆕 Información del Reporte desde JSON
```
POST /api/v1/report/pdf/from-json/info
```

**Descripción**: Retorna información del reporte PDF generado desde datos JSON del frontend.

**Request Body**: Mismo que el endpoint anterior

**Respuesta**:
```json
{
  "fileName": "customer_report_20241201.pdf",
  "contentType": "application/pdf",
  "fileSize": 15420,
  "generatedAt": "2024-12-01T10:30:00",
  "reportType": "PDF",
  "downloadUrl": "/api/v1/report/pdf/from-json",
  "status": "SUCCESS",
  "message": "Reporte generado exitosamente"
}
```

## Uso en Frontend

### Opción 1: Reporte desde Base de Datos
```javascript
// Descarga directa
window.open('/api/v1/report/pdf', '_blank');

// Obtener información primero
fetch('/api/v1/report/pdf/info')
  .then(response => response.json())
  .then(data => {
    if (data.status === 'SUCCESS') {
      console.log(`Reporte: ${data.fileName} (${data.fileSize} bytes)`);
      window.open(data.downloadUrl, '_blank');
    }
  });
```

### Opción 2: 🆕 Reporte desde JSON (Recomendado)
```javascript
// Datos preparados en el frontend
const reportData = {
  reportTitle: "Reporte Personalizado - Best Travel",
  customers: [
    {
      dni: "12345678",
      fullName: "Juan Pérez",
      totalLodgings: 5,
      totalFlights: 3,
      totalTours: 2
    }
    // ... más clientes
  ]
};

// Generar reporte
fetch('/api/v1/report/pdf/from-json', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(reportData)
})
.then(response => {
  if (response.ok) {
    return response.blob();
  }
  throw new Error('Error al generar el reporte');
})
.then(blob => {
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'customer_report.pdf';
  a.click();
  window.URL.revokeObjectURL(url);
})
.catch(error => console.error('Error:', error));
```

## Estructura del Reporte

El reporte PDF incluye:

- **Encabezado**: Título del reporte y fecha de generación
- **Tabla de datos** con las siguientes columnas:
  - DNI del cliente
  - Nombre completo
  - Total de hospedajes
  - Total de vuelos
  - Total de tours
  - Total de compras (suma de los anteriores)

## Configuración Técnica

### Solución de Conflictos de Beans

Para evitar conflictos entre los servicios que implementan `IReportService`, se implementaron las siguientes soluciones:

1. **@Primary en ExcelService**: Se marcó `ExcelService` como bean primario para mantener compatibilidad con código existente
2. **Inyección específica**: Se cambió la inyección de dependencias en `ReportController` para usar tipos específicos en lugar de la interfaz

```java
// ExcelService marcado como @Primary
@Service
@Primary
public class ExcelService implements IReportService { ... }

// ReportController con inyección específica
public class ReportController {
    private final ExcelService excelService;        // Para reportes Excel
    private final PdfReportService pdfReportService; // Para reportes PDF
}
```

### Dependencias Agregadas
```xml
<!-- JasperReports dependencies for PDF generation -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.20.6</version>
</dependency>
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports-fonts</artifactId>
    <version>6.20.6</version>
</dependency>
<dependency>
    <groupId>com.lowagie</groupId>
    <artifactId>itext</artifactId>
    <version>2.1.7</version>
</dependency>
```

### Archivos Creados

1. **Template JRXML**: `src/main/resources/reports/customer_report.jrxml`
2. **Servicio PDF**: `src/main/java/.../services/PdfReportService.java`
3. **DTO de Respuesta**: `src/main/java/.../responses/ReportResponse.java`
4. **Controlador actualizado**: `src/main/java/.../controllers/ReportController.java`

## Manejo de Errores

### Errores Comunes

1. **Template no encontrado**: Verificar que el archivo JRXML esté en `src/main/resources/reports/`
2. **Error de compilación**: Revisar la sintaxis del template JRXML
3. **Error de datos**: Verificar que existan clientes en la base de datos
4. **Error de memoria**: Para reportes muy grandes, considerar paginación
5. **Error de esquema XSD**: "Unknown entity http://jasperreports.sourceforge.net/xsd/jasperreports.xsd"
6. **Error de atributo splitType**: "No está permitido que el atributo 'splitType' aparezca en el elemento 'band'"

#### Solución para Error de Esquema XSD

Este error ocurre con versiones recientes de JasperReports (6.20.6+). La solución es usar un template JRXML simplificado sin referencias al esquema XSD:

```xml
<!-- ❌ Problemático -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" 
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
              xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports 
              http://jasperreports.sourceforge.net/xsd/jasperreports.xsd">

<!-- ✅ Solucionado -->
<jasperReport name="customer_report" pageWidth="595" pageHeight="842">
```

#### Solución para Error de splitType

El atributo `splitType` no está permitido en elementos `band` en versiones recientes de JasperReports:

```xml
<!-- ❌ Problemático -->
<band height="30" splitType="Stretch">

<!-- ✅ Solucionado -->
<band height="30">
```

El template actual ya está configurado con ambas soluciones.

### Respuestas de Error

```json
{
  "generatedAt": "2024-12-01T10:30:00",
  "status": "ERROR",
  "message": "Error al generar el reporte PDF: [descripción del error]"
}
```

## Personalización

### Modificar el Template

Para personalizar el reporte, edita el archivo `customer_report.jrxml`:

1. **Cambiar diseño**: Modifica los elementos visuales en el template
2. **Agregar campos**: Añade nuevos campos en la sección `<field>`
3. **Cambiar datos**: Modifica el mapeo en `PdfReportService.mapCustomerToReportData()`

### Agregar Nuevos Tipos de Reporte

1. Crea un nuevo template JRXML
2. Crea un nuevo servicio que implemente `IReportService`
3. Agrega un nuevo endpoint en `ReportController`

## Monitoreo y Logs

El servicio incluye logs detallados:

```
INFO  - Iniciando generación de reporte PDF de clientes
INFO  - Se encontraron 25 clientes para el reporte
DEBUG - Compilando template JRXML: /reports/customer_report.jrxml
DEBUG - Parámetros del reporte preparados: [REPORT_TITLE, GENERATED_DATE]
INFO  - Reporte PDF generado exitosamente. Tamaño: 15420 bytes
```

## Consideraciones de Rendimiento

- **Memoria**: Los reportes se generan en memoria, considera el tamaño de los datos
- **Tiempo de respuesta**: Para grandes volúmenes de datos, considera implementar paginación
- **Cache**: Para reportes que no cambian frecuentemente, considera implementar cache

## Seguridad

- Los endpoints están protegidos por la configuración de seguridad existente
- Los archivos PDF se generan dinámicamente, no se almacenan en el servidor
- Headers de cache configurados para evitar almacenamiento en navegador

## 🎯 **¿Cuándo usar cada enfoque?**

### 📊 **Reporte desde Base de Datos** (GET endpoints)
**Usar cuando:**
- Necesitas datos siempre actualizados
- El reporte es estándar y no requiere filtros
- Quieres simplicidad en el frontend
- Los datos son confidenciales y no deben enviarse al frontend

**Ejemplo de uso:**
```javascript
// Simple y directo
window.open('/api/v1/report/pdf', '_blank');
```

### 🎨 **Reporte desde JSON** (POST endpoints) - **RECOMENDADO**
**Usar cuando:**
- El frontend ya tiene los datos cargados
- Necesitas filtros dinámicos o personalización
- Quieres mejor performance (no consultas adicionales)
- El usuario puede seleccionar qué datos incluir
- Necesitas reportes personalizados con títulos diferentes

**Ejemplo de uso:**
```javascript
// Flexible y personalizable
const filteredData = customers.filter(c => c.totalPurchases > 10);
const reportData = {
  reportTitle: "Clientes VIP - Best Travel",
  customers: filteredData
};

fetch('/api/v1/report/pdf/from-json', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(reportData)
});
```

## 🔒 **Consideraciones de Seguridad**

### Para endpoints desde JSON:
1. **Validación**: Los datos se validan con `@Valid` y `@NotNull`
2. **Sanitización**: Los strings se procesan de forma segura
3. **Límites**: Considera implementar límites de tamaño para el JSON
4. **Autenticación**: Asegúrate de que solo usuarios autorizados puedan generar reportes

### Ejemplo de validación adicional:
```java
@Size(max = 1000, message = "Máximo 1000 clientes por reporte")
private List<CustomerReportData> customers;
```

## 📈 **Ventajas del Enfoque JSON**

1. **Performance**: No hay consultas adicionales a la BD
2. **Flexibilidad**: El frontend controla exactamente qué datos incluir
3. **UX**: El usuario puede aplicar filtros en tiempo real
4. **Reutilización**: El mismo endpoint sirve para diferentes tipos de reportes
5. **Escalabilidad**: Menos carga en el servidor de base de datos

## Próximos Pasos

1. **Implementar filtros**: Agregar parámetros para filtrar datos por fecha, cliente, etc.
2. **Múltiples formatos**: Agregar soporte para Excel, CSV, etc.
3. **Reportes programados**: Implementar generación automática de reportes
4. **Cache de reportes**: Para reportes que no cambian frecuentemente
5. **Compresión**: Implementar compresión para reportes grandes
6. **Límites de seguridad**: Implementar límites de tamaño para JSON
7. **Plantillas personalizables**: Permitir diferentes templates JRXML
