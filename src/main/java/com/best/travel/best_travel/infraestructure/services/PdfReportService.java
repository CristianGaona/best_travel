package com.best.travel.best_travel.infraestructure.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.best.travel.best_travel.domain.entity.jpa.CustomerEntity;
import com.best.travel.best_travel.domain.repository.jpa.CustomerRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.IReportService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Servicio para generar reportes PDF usando JasperReports
 * Implementa la interfaz IReportService para mantener consistencia con el patrón existente
 * 
 * @author Senior Developer
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class PdfReportService implements IReportService {

    private final CustomerRepository customerRepository;
    
    // Constantes para configuración
    private static final String REPORT_TEMPLATE_PATH = "/reports/customer_report.jrxml";
    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String PDF_FILE_EXTENSION = ".pdf";
    private static final String REPORT_NAME_PREFIX = "customer_report_";

    /**
     * Genera un reporte PDF con los datos de clientes
     * 
     * @return byte[] - Contenido del archivo PDF generado
     * @throws RuntimeException si ocurre algún error durante la generación
     */
    @Override
    public byte[] readFile() {
        try {
            log.info("Iniciando generación de reporte PDF de clientes");
            
            // 1. Compilar el template JRXML
            JasperReport jasperReport = compileReportTemplate();
            
            // 2. Obtener datos de la base de datos
            Iterable<CustomerEntity> customersIterable = customerRepository.findAll();
            List<CustomerEntity> customers = new ArrayList<>();
            customersIterable.forEach(customers::add);
            log.info("Se encontraron {} clientes para el reporte", customers.size());
            
            // 3. Preparar parámetros del reporte
            Map<String, Object> parameters = prepareReportParameters();
            
            // 4. Crear datasource con los datos
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                customers.stream()
                    .map(this::mapCustomerToReportData)
                    .toList()
            );
            
            // 5. Llenar el reporte con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, 
                parameters, 
                dataSource
            );
            
            // 6. Exportar a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            
            byte[] pdfBytes = outputStream.toByteArray();
            log.info("Reporte PDF generado exitosamente. Tamaño: {} bytes", pdfBytes.length);
            
            return pdfBytes;
            
        } catch (JRException e) {
            log.error("Error al generar el reporte PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar el reporte PDF", e);
        } catch (Exception e) {
            log.error("Error inesperado al generar el reporte PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al generar el reporte PDF", e);
        }
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
        parameters.put("REPORT_TITLE", "Reporte de Clientes - Best Travel");
        
        // Fecha de generación
        parameters.put("GENERATED_DATE", new java.util.Date());
        
        log.debug("Parámetros del reporte preparados: {}", parameters.keySet());
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
     * Genera un nombre de archivo único para el reporte PDF
     * 
     * @return String - Nombre del archivo con timestamp
     */
    public String generateFileName() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return REPORT_NAME_PREFIX + timestamp + PDF_FILE_EXTENSION;
    }

    /**
     * Retorna el tipo de contenido MIME para archivos PDF
     * 
     * @return String - Content type para PDF
     */
    public String getContentType() {
        return PDF_CONTENT_TYPE;
    }
}
