package com.best.travel.best_travel.api.models.api.controllers;

import java.util.Map;

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
import com.best.travel.best_travel.infraestructure.services.ReactivePdfReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Controlador reactivo para generar reportes PDF usando WebFlux
 * Implementa programación reactiva para operaciones no bloqueantes
 * 
 * @author Senior Developer
 * @version 1.0
 */
@RestController
@RequestMapping(path = "/reactive-report")
@Tag(name = "Reactive Report", description = "Reactive Report API using WebFlux")
@Slf4j
public class ReactiveReportController {
    
    private final ReactivePdfReportService reactivePdfReportService;
    
    public ReactiveReportController(ReactivePdfReportService reactivePdfReportService) {
        this.reactivePdfReportService = reactivePdfReportService;
    }

    @Operation(summary = "Generar reporte PDF reactivo de clientes", 
               description = "Genera y descarga un reporte en formato PDF con información de clientes usando programación reactiva")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente")
    @GetMapping(path = "pdf")
    public Mono<ResponseEntity<Resource>> getPdfReportReactive() {
        return reactivePdfReportService.generatePdfReportReactive()
            .map(pdfBytes -> {
                String fileName = reactivePdfReportService.generateFileName();
                
                // Configurar headers para descarga
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                headers.set(HttpHeaders.PRAGMA, "no-cache");
                headers.set(HttpHeaders.EXPIRES, "0");
                
                // Crear resource con el contenido del PDF
                ByteArrayResource resource = new ByteArrayResource(pdfBytes);
                
                log.info("Reporte PDF reactivo generado exitosamente. Tamaño: {} bytes", pdfBytes.length);
                
                return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body((Resource) resource);
            })
            .onErrorResume(throwable -> {
                log.error("Error al generar el reporte PDF reactivo: {}", throwable.getMessage(), throwable);
                return Mono.just(ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ByteArrayResource(("{\"error\": \"Error al generar el reporte PDF reactivo: " + throwable.getMessage() + "\"}").getBytes())));
            });
    }

    @Operation(summary = "Obtener información del reporte PDF reactivo", 
               description = "Retorna información del reporte PDF generado de forma reactiva incluyendo URL de descarga para el frontend")
    @ApiResponse(responseCode = "200", description = "Información del reporte obtenida exitosamente")
    @GetMapping(path = "pdf/info")
    public Mono<ResponseEntity<ReportResponse>> getPdfReportInfoReactive() {
        return reactivePdfReportService.generatePdfReportReactive()
            .map(pdfBytes -> {
                String fileName = reactivePdfReportService.generateFileName();
                
                // Crear respuesta con información del reporte
                ReportResponse response = ReportResponse.success(
                    fileName,
                    reactivePdfReportService.getContentType(),
                    (long) pdfBytes.length,
                    "PDF"
                );
                
                // Agregar URL de descarga (el frontend puede usar esta URL para descargar)
                response.setDownloadUrl("/api/v1/reactive-report/pdf");
                
                log.info("Información del reporte PDF reactivo obtenida exitosamente");
                
                return ResponseEntity.ok(response);
            })
            .onErrorResume(throwable -> {
                log.error("Error al obtener información del reporte PDF reactivo: {}", throwable.getMessage(), throwable);
                ReportResponse errorResponse = ReportResponse.error("Error al obtener información del reporte PDF reactivo: " + throwable.getMessage());
                return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
            });
    }

    @Operation(summary = "Generar reporte PDF reactivo desde datos JSON del frontend", 
               description = "Genera un reporte PDF usando datos enviados desde el frontend en formato JSON con programación reactiva")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente")
    @PostMapping(path = "pdf/from-json")
    public Mono<ResponseEntity<Resource>> generatePdfFromJsonReactive(@Valid @RequestBody ReportDataRequest reportData) {
        return reactivePdfReportService.generatePdfReportFromJsonReactive(reportData)
            .map(pdfBytes -> {
                String fileName = reactivePdfReportService.generateFileName();
                
                // Configurar headers para descarga
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                headers.set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                headers.set(HttpHeaders.PRAGMA, "no-cache");
                headers.set(HttpHeaders.EXPIRES, "0");
                
                // Crear resource con el contenido del PDF
                ByteArrayResource resource = new ByteArrayResource(pdfBytes);
                
                log.info("Reporte PDF reactivo desde JSON generado exitosamente. Tamaño: {} bytes", pdfBytes.length);
                
                return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body((Resource) resource);
            })
            .onErrorResume(throwable -> {
                log.error("Error al generar el reporte PDF reactivo desde JSON: {}", throwable.getMessage(), throwable);
                return Mono.just(ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ByteArrayResource(("{\"error\": \"Error al generar el reporte PDF reactivo desde JSON: " + throwable.getMessage() + "\"}").getBytes())));
            });
    }

    @Operation(summary = "Obtener información del reporte PDF reactivo desde JSON", 
               description = "Retorna información del reporte PDF generado de forma reactiva desde datos JSON del frontend")
    @ApiResponse(responseCode = "200", description = "Información del reporte obtenida exitosamente")
    @PostMapping(path = "pdf/from-json/info")
    public Mono<ResponseEntity<ReportResponse>> getPdfReportInfoFromJsonReactive(@Valid @RequestBody ReportDataRequest reportData) {
        return reactivePdfReportService.generatePdfReportFromJsonReactive(reportData)
            .map(pdfBytes -> {
                String fileName = reactivePdfReportService.generateFileName();
                
                // Crear respuesta con información del reporte
                ReportResponse response = ReportResponse.success(
                    fileName,
                    reactivePdfReportService.getContentType(),
                    (long) pdfBytes.length,
                    "PDF"
                );
                
                // Agregar URL de descarga
                response.setDownloadUrl("/pdf/from-json");
                
                log.info("Información del reporte PDF reactivo desde JSON obtenida exitosamente");
                
                return ResponseEntity.ok(response);
            })
            .onErrorResume(throwable -> {
                log.error("Error al obtener información del reporte PDF reactivo desde JSON: {}", throwable.getMessage(), throwable);
                ReportResponse errorResponse = ReportResponse.error("Error al obtener información del reporte PDF reactivo desde JSON: " + throwable.getMessage());
                return Mono.just(ResponseEntity.internalServerError().body(errorResponse));
            });
    }

    /*@Operation(summary = "Generar reporte PDF reactivo en Base64", 
               description = "Genera un reporte PDF de forma reactiva y lo retorna codificado en Base64 como string")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente en Base64")
    @GetMapping(path = "pdf/base64")
    public Mono<ResponseEntity<String>> getPdfReportBase64Reactive() {
        return reactivePdfReportService.generatePdfReportBase64Reactive()
            .map(base64 -> {
                log.info("Reporte PDF reactivo convertido a Base64 exitosamente. Longitud: {} caracteres", base64.length());
                
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(base64);
            })
            .onErrorResume(throwable -> {
                log.error("Error al generar el reporte PDF reactivo en Base64: {}", throwable.getMessage(), throwable);
                return Mono.just(ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error al generar el reporte PDF reactivo en Base64: " + throwable.getMessage()));
            });
    }*/
    

    @Operation(summary = "Generar reporte PDF reactivo desde JSON en Base64", 
               description = "Genera un reporte PDF de forma reactiva desde datos JSON del frontend y lo retorna codificado en Base64 como string")
    @ApiResponse(responseCode = "200", description = "Reporte PDF generado exitosamente en Base64")
    @PostMapping(path = "pdf/from-json/base64")
    public Mono<ResponseEntity<Map<String, String>>> generatePdfFromJsonBase64Reactive(@Valid @RequestBody ReportDataRequest reportData) {
        return reactivePdfReportService.generatePdfReportFromJsonBase64Reactive(reportData)
            .map(base64 -> {
                log.info("Reporte PDF reactivo desde JSON convertido a Base64 exitosamente. Longitud: {} caracteres", base64.length());
                
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                        "fileName", "reporte.pdf",
                        "contentType", "application/pdf",
                        "pdfBase64", base64
                    ));
            })
            .onErrorResume(throwable -> {
            log.error("Error al generar el reporte PDF reactivo desde JSON en Base64: {}", throwable.getMessage(), throwable);
            return Mono.just(ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Error al generar el PDF: " + throwable.getMessage())));
        });
    }
    /*public Mono<ResponseEntity<String>> generatePdfFromJsonBase64Reactive(@Valid @RequestBody ReportDataRequest reportData) {
        return reactivePdfReportService.generatePdfReportFromJsonBase64Reactive(reportData)
            .map(base64 -> {
                log.info("Reporte PDF reactivo desde JSON convertido a Base64 exitosamente. Longitud: {} caracteres", base64.length());
                
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(base64);
            })
            .onErrorResume(throwable -> {
                log.error("Error al generar el reporte PDF reactivo desde JSON en Base64: {}", throwable.getMessage(), throwable);
                return Mono.just(ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error al generar el reporte PDF reactivo desde JSON en Base64: " + throwable.getMessage()));
            });
    }*/
}
