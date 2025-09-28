package com.best.travel.best_travel.infraestructure.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.best.travel.best_travel.api.models.request.ReportDataRequest;
import com.best.travel.best_travel.domain.entity.jpa.CustomerEntity;
import com.best.travel.best_travel.domain.repository.jpa.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Servicio reactivo para generar reportes PDF usando JasperReports y WebFlux
 * Implementa programación reactiva para operaciones no bloqueantes
 * 
 * @author Senior Developer
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class ReactivePdfReportService {

    private final CustomerRepository customerRepository;
    
    // Constantes para configuración
    private static final String REPORT_TEMPLATE_PATH = "/reports/customer_report.jrxml";
    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String PDF_FILE_EXTENSION = ".pdf";
    private static final String REPORT_NAME_PREFIX = "customer_report_";

    /**
     * Genera un reporte PDF reactivo con los datos de clientes desde la base de datos
     * 
     * @return Mono<byte[]> - Contenido del archivo PDF generado de forma reactiva
     */
    public Mono<byte[]> generatePdfReportReactive() {
        return Mono.fromCallable(() -> {
            log.info("Iniciando generación reactiva de reporte PDF de clientes");
            
            // 1. Compilar el template JRXML
            JasperReport jasperReport = compileReportTemplate();
            
            // 2. Preparar parámetros del reporte
            Map<String, Object> parameters = prepareReportParameters();
            
            return new ReportGenerationContext(jasperReport, parameters);
        })
        .subscribeOn(Schedulers.boundedElastic()) // Ejecutar en hilo separado para operaciones bloqueantes
        .flatMap(context -> 
            // 3. Obtener datos de la base de datos de forma reactiva
            Mono.fromCallable(() -> customerRepository.findAll())
                .subscribeOn(Schedulers.boundedElastic())
                .map(customers -> {
                    List<CustomerEntity> customerList = new java.util.ArrayList<>();
                    customers.forEach(customerList::add);
                    log.info("Se encontraron {} clientes para el reporte", customerList.size());
                    return customerList;
                })
                .map(customers -> {
                    // 4. Crear datasource con los datos
                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                        customers.stream()
                            .map(this::mapCustomerToReportData)
                            .toList()
                    );
                    
                    // 5. Llenar el reporte con datos
                    JasperPrint jasperPrint;
                    try {
                        jasperPrint = JasperFillManager.fillReport(
                            context.jasperReport(), 
                            context.parameters(), 
                            dataSource
                        );
                    } catch (JRException e) {
                        throw new RuntimeException("Error al llenar el reporte", e);
                    }
                    
                    // 6. Exportar a PDF
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                    } catch (JRException e) {
                        throw new RuntimeException("Error al exportar el reporte a PDF", e);
                    }
                    
                    byte[] pdfBytes = outputStream.toByteArray();
                    log.info("Reporte PDF generado exitosamente de forma reactiva. Tamaño: {} bytes", pdfBytes.length);
                    
                    return pdfBytes;
                })
        )
        .onErrorMap(JRException.class, e -> {
            log.error("Error al generar el reporte PDF: {}", e.getMessage(), e);
            return new RuntimeException("Error al generar el reporte PDF", e);
        })
        .onErrorMap(Exception.class, e -> {
            log.error("Error inesperado al generar el reporte PDF: {}", e.getMessage(), e);
            return new RuntimeException("Error inesperado al generar el reporte PDF", e);
        });
    }

    /**
     * Genera un reporte PDF reactivo con datos recibidos del frontend
     * 
     * @param reportData - Datos del reporte enviados desde el frontend
     * @return Mono<byte[]> - Contenido del archivo PDF generado de forma reactiva
     */
    public Mono<byte[]> generatePdfReportFromJsonReactive(ReportDataRequest reportData) {
        return Mono.fromCallable(() -> {
            log.info("Iniciando generación reactiva de reporte PDF desde JSON del frontend");
            log.info("Se recibieron {} clientes para el reporte", reportData.getCustomers().size());
            
            // 1. Compilar el template JRXML
            JasperReport jasperReport = compileReportTemplate();
            
            // 2. Preparar parámetros del reporte
            Map<String, Object> parameters = prepareReportParametersFromJson(reportData);
            
            return new ReportGenerationContext(jasperReport, parameters);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(context -> {
            // 3. Crear datasource con los datos del JSON
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                reportData.getCustomers().stream()
                    .map(this::mapJsonCustomerToReportData)
                    .toList()
            );
            
            // 4. Llenar el reporte con datos
            JasperPrint jasperPrint;
            try {
                jasperPrint = JasperFillManager.fillReport(
                    context.jasperReport(), 
                    context.parameters(), 
                    dataSource
                );
            } catch (JRException e) {
                throw new RuntimeException("Error al llenar el reporte desde JSON", e);
            }
            
            // 5. Exportar a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            } catch (JRException e) {
                throw new RuntimeException("Error al exportar el reporte a PDF desde JSON", e);
            }
            
            byte[] pdfBytes = outputStream.toByteArray();
            log.info("Reporte PDF generado exitosamente desde JSON de forma reactiva. Tamaño: {} bytes", pdfBytes.length);
            
            return pdfBytes;
        })
        .onErrorMap(JRException.class, e -> {
            log.error("Error al generar el reporte PDF desde JSON: {}", e.getMessage(), e);
            return new RuntimeException("Error al generar el reporte PDF desde JSON", e);
        })
        .onErrorMap(Exception.class, e -> {
            log.error("Error inesperado al generar el reporte PDF desde JSON: {}", e.getMessage(), e);
            return new RuntimeException("Error inesperado al generar el reporte PDF desde JSON", e);
        });
    }

    /**no creo pueda enviarle el resource al front
     * 
     * Genera un reporte PDF reactivo y lo convierte a Base64
     * 
     * @return Mono<String> - Contenido del PDF en Base64
     */
    public Mono<String> generatePdfReportBase64Reactive() {
        return generatePdfReportReactive()
            .map(pdfBytes -> {
                String base64 = java.util.Base64.getEncoder().encodeToString(pdfBytes);
                log.info("Reporte PDF convertido a Base64. Longitud: {} caracteres", base64.length());
                return base64;
            });
    }

    /**
     * Genera un reporte PDF reactivo desde JSON y lo convierte a Base64
     * 
     * @param reportData - Datos del reporte enviados desde el frontend
     * @return Mono<String> - Contenido del PDF en Base64
     */
    public Mono<String> generatePdfReportFromJsonBase64Reactive(ReportDataRequest reportData) {
        return generatePdfReportFromJsonReactive(reportData)
            .map(pdfBytes -> {
                String base64 = java.util.Base64.getEncoder().encodeToString(pdfBytes);
                log.info("Reporte PDF desde JSON convertido a Base64. Longitud: {} caracteres", base64.length());
                return base64;
            });
    }

    /**
     * Compila el template JRXML y retorna el objeto JasperReport
     * 
     * @return JasperReport - Template compilado
     * @throws JRException si hay error en la compilación
     */
    private JasperReport compileReportTemplate() throws JRException {
        try (InputStream templateStream = getClass().getResourceAsStream(REPORT_TEMPLATE_PATH)) {
            if (templateStream == null) {
                throw new RuntimeException("No se pudo encontrar el template: " + REPORT_TEMPLATE_PATH);
            }
            
            log.debug("Compilando template JRXML: {}", REPORT_TEMPLATE_PATH);
            return JasperCompileManager.compileReport(templateStream);
        } catch (Exception e) {
            log.error("Error al compilar el template JRXML: {}", e.getMessage(), e);
            throw new JRException("Error al compilar el template del reporte", e);
        }
    }

    /**
     * Prepara los parámetros que se pasarán al reporte
     * 
     * @return Map<String, Object> - Parámetros del reporte
     */
    private Map<String, Object> prepareReportParameters() {
        Map<String, Object> parameters = new HashMap<>();
        
        // Título del reporte
        parameters.put("REPORT_TITLE", "Reporte de Clientes - Best Travel (Reactivo)");
        
        // Fecha de generación
        parameters.put("GENERATED_DATE", new java.util.Date());
        
        log.debug("Parámetros del reporte preparados: {}", parameters.keySet());
        return parameters;
    }

    /**
     * Prepara los parámetros del reporte desde los datos JSON
     * 
     * @param reportData - Datos del reporte
     * @return Map<String, Object> - Parámetros del reporte
     */
    private Map<String, Object> prepareReportParametersFromJson(ReportDataRequest reportData) {
        Map<String, Object> parameters = new HashMap<>();
        
        // Título del reporte desde el frontend
        parameters.put("REPORT_TITLE", reportData.getReportTitle() + " (Reactivo)");
        
        // Fecha de generación
        parameters.put("GENERATED_DATE", new java.util.Date());
        
        log.debug("Parámetros del reporte preparados desde JSON: {}", parameters.keySet());
        return parameters;
    }

    /**
     * Mapea una entidad CustomerEntity a un Map para el reporte
     * 
     * @param customer - Entidad de cliente
     * @return Map<String, Object> - Datos mapeados para el reporte
     */
    private Map<String, Object> mapCustomerToReportData(CustomerEntity customer) {
        Map<String, Object> reportData = new HashMap<>();
        
        reportData.put("dni", customer.getDni());
        reportData.put("fullName", customer.getFullName());
        reportData.put("totalLodgings", customer.getTotalLodgings() != null ? customer.getTotalLodgings() : 0);
        reportData.put("totalFlights", customer.getTotalFlights() != null ? customer.getTotalFlights() : 0);
        reportData.put("totalTours", customer.getTotalTours() != null ? customer.getTotalTours() : 0);
        
        // Calcular total de compras
        int totalPurchases = (customer.getTotalLodgings() != null ? customer.getTotalLodgings() : 0) +
                           (customer.getTotalFlights() != null ? customer.getTotalFlights() : 0) +
                           (customer.getTotalTours() != null ? customer.getTotalTours() : 0);
        reportData.put("totalPurchases", totalPurchases);
        
        return reportData;
    }

    /**
     * Mapea un CustomerReportData del JSON a un Map para el reporte
     * 
     * @param customer - Datos del cliente desde JSON
     * @return Map<String, Object> - Datos mapeados para el reporte
     */
    private Map<String, Object> mapJsonCustomerToReportData(ReportDataRequest.CustomerReportData customer) {
        Map<String, Object> reportData = new HashMap<>();
        
        reportData.put("dni", customer.getDni());
        reportData.put("fullName", customer.getFullName());
        reportData.put("totalLodgings", customer.getTotalLodgings() != null ? customer.getTotalLodgings() : 0);
        reportData.put("totalFlights", customer.getTotalFlights() != null ? customer.getTotalFlights() : 0);
        reportData.put("totalTours", customer.getTotalTours() != null ? customer.getTotalTours() : 0);
        reportData.put("totalPurchases", customer.getTotalPurchases());
        
        return reportData;
    }

    /**
     * Genera un nombre de archivo único para el reporte PDF
     * 
     * @return String - Nombre del archivo con timestamp
     */
    public String generateFileName() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return REPORT_NAME_PREFIX + timestamp + "_reactive" + PDF_FILE_EXTENSION;
    }

    /**
     * Retorna el tipo de contenido MIME para archivos PDF
     * 
     * @return String - Content type para PDF
     */
    public String getContentType() {
        return PDF_CONTENT_TYPE;
    }

    /**
     * Record para encapsular el contexto de generación del reporte
     * 
     * @param jasperReport - Reporte compilado
     * @param parameters - Parámetros del reporte
     */
    private record ReportGenerationContext(
        JasperReport jasperReport,
        Map<String, Object> parameters
    ) {}
}
