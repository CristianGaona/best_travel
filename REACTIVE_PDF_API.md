# API Reactiva de Generación de PDF con WebFlux

## Descripción
Esta implementación proporciona una API reactiva para generar reportes PDF usando Spring WebFlux y JasperReports, aplicando programación reactiva para operaciones no bloqueantes.

## Características Principales

### 🚀 Programación Reactiva
- **WebFlux**: Manejo no bloqueante de peticiones HTTP
- **Mono**: Flujos reactivos para operaciones asíncronas
- **Schedulers**: Ejecución en hilos separados para operaciones bloqueantes

### 📄 Generación de PDF
- **JasperReports**: Motor de reportes robusto
- **Base64**: Codificación para transferencia segura
- **Múltiples fuentes**: Base de datos o datos JSON del frontend

## Endpoints Disponibles

### 1. Generar PDF desde Base de Datos
```http
GET /api/v1/reactive-report/pdf
```
**Respuesta**: Archivo PDF para descarga directa

### 2. Información del Reporte
```http
GET /api/v1/reactive-report/pdf/info
```
**Respuesta**: Información del reporte (tamaño, nombre, etc.)

### 3. Generar PDF desde JSON
```http
POST /api/v1/reactive-report/pdf/from-json
Content-Type: application/json

{
  "reportTitle": "Mi Reporte Personalizado",
  "customers": [
    {
      "dni": "12345678",
      "fullName": "Juan Pérez",
      "totalLodgings": 2,
      "totalFlights": 3,
      "totalTours": 1
    }
  ]
}
```

### 4. PDF en Base64 (Recomendado)
```http
GET /api/v1/reactive-report/pdf/base64
```
**Respuesta**:
```json
{
  "pdfBase64": "JVBERi0xLjQKJcfsj6IKNSAwIG9iago8PAovVHlwZSAvUGFnZQovUGFyZW50IDMgMCBSCi9SZXNvdXJjZXMgPDwKL0ZvbnQgPDwKL0YxIDIgMCBSCj4+Cj4+Ci9NZWRpYUJveCBbMCAwIDU5NSA4NDJdCi9Db250ZW50cyA2IDAgUgo+PgplbmRvYmoKNiAwIG9iago8PAovTGVuZ3RoIDQ0Cj4+CnN0cmVhbQpCVApxCjU5NSA4NDIgVGQKL0YxIDEyIFRmCihIZWxsbyBXb3JsZCkgVGoKRVQKZW5kc3RyZWFtCmVuZG9iagp4cmVmCjAgNwowMDAwMDAwMDAwIDY1NTM1IGYKMDAwMDAwMDAwOSAwMDAwMCBuCjAwMDAwMDAwNTggMDAwMDAgbgowMDAwMDAwMTE1IDAwMDAwIG4KMDAwMDAwMDI3OCAwMDAwMCBuCjAwMDAwMDAzNzIgMDAwMDAgbgowMDAwMDAwNDQ3IDAwMDAwIG4KdHJhaWxlcgo8PAovU2l6ZSA3Ci9Sb290IDEgMCBSCj4+CnN0YXJ0eHJlZgo1NDAKJSVFT0YK",
  "contentType": "application/pdf",
  "fileName": "customer_report_20241201_reactive.pdf",
  "fileSize": 1024,
  "format": "PDF",
  "message": "Reporte generado exitosamente",
  "success": true
}
```

### 5. PDF desde JSON en Base64
```http
POST /api/v1/reactive-report/pdf/from-json/base64
Content-Type: application/json

{
  "reportTitle": "Reporte Personalizado",
  "customers": [...]
}
```

## Ventajas de la Implementación Reactiva

### ⚡ Rendimiento
- **No bloqueante**: No bloquea hilos durante operaciones I/O
- **Escalabilidad**: Mejor manejo de concurrencia
- **Backpressure**: Control automático del flujo de datos

### 🔧 Mantenibilidad
- **Separación de responsabilidades**: Servicio y controlador independientes
- **Manejo de errores**: Propagación reactiva de excepciones
- **Logging**: Trazabilidad completa del proceso

### 🛡️ Robustez
- **Manejo de errores**: Recuperación automática con `onErrorResume`
- **Timeouts**: Control de tiempo de respuesta
- **Validación**: Validación de entrada con Bean Validation

## Uso en Frontend

### JavaScript/TypeScript
```javascript
// Generar PDF en Base64
async function generatePdfBase64() {
  try {
    const response = await fetch('/api/v1/reactive-report/pdf/base64');
    const data = await response.json();
    
    if (data.success) {
      // Crear blob desde Base64
      const byteCharacters = atob(data.pdfBase64);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: 'application/pdf' });
      
      // Descargar o mostrar PDF
      const url = URL.createObjectURL(blob);
      window.open(url);
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

// Generar PDF desde datos personalizados
async function generateCustomPdf(customers) {
  try {
    const response = await fetch('/api/v1/reactive-report/pdf/from-json/base64', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        reportTitle: 'Mi Reporte',
        customers: customers
      })
    });
    
    const data = await response.json();
    // Procesar respuesta...
  } catch (error) {
    console.error('Error:', error);
  }
}
```

## Configuración Técnica

### Dependencias Requeridas
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.20.6</version>
</dependency>
```

### Configuración de Schedulers
```java
// El servicio usa Schedulers.boundedElastic() para operaciones bloqueantes
.subscribeOn(Schedulers.boundedElastic())
```

## Comparación con Implementación Tradicional

| Aspecto | Tradicional | Reactivo |
|---------|-------------|----------|
| **Bloqueo** | Bloquea hilos | No bloquea |
| **Escalabilidad** | Limitada por hilos | Mejor escalabilidad |
| **Memoria** | Más consumo | Menor consumo |
| **Complejidad** | Más simple | Más complejo |
| **Rendimiento** | Bueno | Excelente |

## Mejores Prácticas Implementadas

1. **Separación de responsabilidades**: Servicio reactivo independiente
2. **Manejo de errores**: Propagación reactiva con `onErrorResume`
3. **Logging estructurado**: Trazabilidad completa
4. **Validación de entrada**: Bean Validation
5. **Documentación API**: OpenAPI/Swagger
6. **Tipado fuerte**: DTOs específicos para respuestas
7. **Operaciones no bloqueantes**: Uso de `Schedulers.boundedElastic()`

## Monitoreo y Observabilidad

- **Logs estructurados**: Información detallada del proceso
- **Métricas de rendimiento**: Tiempo de generación y tamaño
- **Manejo de errores**: Logs de errores con contexto
- **Trazabilidad**: Seguimiento completo de las operaciones
