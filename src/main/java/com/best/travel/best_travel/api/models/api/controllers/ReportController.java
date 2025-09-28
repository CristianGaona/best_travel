package com.best.travel.best_travel.api.models.api.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.api.models.request.ReportDataRequest;
import com.best.travel.best_travel.api.models.responses.ReportResponse;
import com.best.travel.best_travel.infraestructure.services.ExcelService;
import com.best.travel.best_travel.infraestructure.services.PdfReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping(path = "report")
@Tag(name = "Report", description = "Report API")
public class ReportController {
    
    private final ExcelService excelService;
    private final PdfReportService pdfReportService;
    
    public ReportController(ExcelService excelService, PdfReportService pdfReportService) {
        this.excelService = excelService;
        this.pdfReportService = pdfReportService;
    }

    @Operation(summary = "Generar reporte Excel de clientes", 
               description = "Genera y descarga un reporte en formato Excel con información de clientes")
    @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente")
    @GetMapping
    public ResponseEntity<Resource> getExcelReport(){
        var headers = new HttpHeaders();
        headers.setContentType(FORCE_DOWNLOAD);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, FORCE_DOWNLOAD_HEADER_VALUE);
        var fileInBytes = this.excelService.readFile();
        ByteArrayResource response = new ByteArrayResource(fileInBytes);
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(fileInBytes.length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(response);
    }

    @Operation(summary = "Generar reporte PDF de clientes", 
               description = "Genera y descarga un reporte en formato PDF con información detallada de clientes usando JasperReports")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente")
    @GetMapping(path = "pdf")
    public ResponseEntity<Resource> getPdfReport(){
        try {
            // Generar el reporte PDF
            byte[] pdfBytes = pdfReportService.readFile();
            String fileName = pdfReportService.generateFileName();
            
            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.set(HttpHeaders.PRAGMA, "no-cache");
            headers.set(HttpHeaders.EXPIRES, "0");
            
            // Crear resource con el contenido del PDF
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
                
        } catch (Exception e) {
            // En caso de error, retornar respuesta de error
            return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ByteArrayResource(("{\"error\": \"Error al generar el reporte PDF: " + e.getMessage() + "\"}").getBytes()));
        }
    }

    @Operation(summary = "Obtener información del reporte PDF", 
               description = "Retorna información del reporte PDF generado incluyendo URL de descarga para el frontend")
    @ApiResponse(responseCode = "200", description = "Información del reporte obtenida exitosamente")
    @GetMapping(path = "pdf/info")
    public ResponseEntity<ReportResponse> getPdfReportInfo(){
        try {
            // Generar el reporte PDF para obtener información
            byte[] pdfBytes = pdfReportService.readFile();
            String fileName = pdfReportService.generateFileName();
            
            // Crear respuesta con información del reporte
            ReportResponse response = ReportResponse.success(
                fileName,
                pdfReportService.getContentType(),
                (long) pdfBytes.length,
                "PDF"
            );
            
            // Agregar URL de descarga (el frontend puede usar esta URL para descargar)
            response.setDownloadUrl("/api/v1/report/pdf");
            
            return ResponseEntity.ok(response);
                
        } catch (Exception e) {
            // En caso de error, retornar respuesta de error
            ReportResponse errorResponse = ReportResponse.error("Error al generar el reporte PDF: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(summary = "Generar reporte PDF desde datos JSON del frontend", 
               description = "Genera un reporte PDF usando datos enviados desde el frontend en formato JSON")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente")
    @PostMapping(path = "pdf/from-json")
    public ResponseEntity<Resource> generatePdfFromJson(@Valid @RequestBody ReportDataRequest reportData) {
        try {
            // Generar el reporte PDF desde JSON
            byte[] pdfBytes = pdfReportService.generateReportFromJson(reportData);
            String fileName = pdfReportService.generateFileName();
            
            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.set(HttpHeaders.PRAGMA, "no-cache");
            headers.set(HttpHeaders.EXPIRES, "0");
            
            // Crear resource con el contenido del PDF
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
                
        } catch (Exception e) {
            // En caso de error, retornar respuesta de error
            return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ByteArrayResource(("{\"error\": \"Error al generar el reporte PDF desde JSON: " + e.getMessage() + "\"}").getBytes()));
        }
    }

    @Operation(summary = "Obtener información del reporte PDF desde JSON", 
               description = "Retorna información del reporte PDF generado desde datos JSON del frontend")
    @ApiResponse(responseCode = "200", description = "Información del reporte obtenida exitosamente")
    @PostMapping(path = "pdf/from-json/info")
    public ResponseEntity<ReportResponse> getPdfReportInfoFromJson(@Valid @RequestBody ReportDataRequest reportData) {
        try {
            // Generar el reporte PDF para obtener información
            byte[] pdfBytes = pdfReportService.generateReportFromJson(reportData);
            String fileName = pdfReportService.generateFileName();
            
            // Crear respuesta con información del reporte
            ReportResponse response = ReportResponse.success(
                fileName,
                pdfReportService.getContentType(),
                (long) pdfBytes.length,
                "PDF"
            );
            
            // Agregar URL de descarga
            response.setDownloadUrl("pdf/from-json");
            
            return ResponseEntity.ok(response);
                
        } catch (Exception e) {
            // En caso de error, retornar respuesta de error
            ReportResponse errorResponse = ReportResponse.error("Error al generar el reporte PDF desde JSON: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    private static final MediaType FORCE_DOWNLOAD = new MediaType("application", "force-download");
    private static final String FORCE_DOWNLOAD_HEADER_VALUE ="attachment; filename=report.xlsx";

}