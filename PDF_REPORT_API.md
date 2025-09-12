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

### 1. Descarga Directa de PDF
```
GET /api/v1/report/pdf
```

**Descripción**: Genera y descarga inmediatamente un reporte PDF con información de clientes.

**Respuesta**: 
- Content-Type: `application/pdf`
- Content-Disposition: `attachment; filename="customer_report_YYYYMMDD.pdf"`

**Uso en Frontend**:
```javascript
// Opción 1: Descarga directa
window.open('/api/v1/report/pdf', '_blank');

// Opción 2: Fetch con manejo de errores
fetch('/api/v1/report/pdf')
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

### 2. Información del Reporte
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

**Uso en Frontend**:
```javascript
// Obtener información del reporte
fetch('/api/v1/report/pdf/info')
  .then(response => response.json())
  .then(data => {
    if (data.status === 'SUCCESS') {
      console.log(`Reporte: ${data.fileName} (${data.fileSize} bytes)`);
      // Usar la URL de descarga
      window.open(data.downloadUrl, '_blank');
    } else {
      console.error('Error:', data.message);
    }
  });
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

## Próximos Pasos

1. **Implementar filtros**: Agregar parámetros para filtrar datos por fecha, cliente, etc.
2. **Múltiples formatos**: Agregar soporte para Excel, CSV, etc.
3. **Reportes programados**: Implementar generación automática de reportes
4. **Cache de reportes**: Para reportes que no cambian frecuentemente
5. **Compresión**: Implementar compresión para reportes grandes
