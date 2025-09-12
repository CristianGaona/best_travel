package com.best.travel.best_travel.api.models.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas de reportes generados
 * 
 * @author Senior Developer
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    
    private String fileName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime generatedAt;
    private String reportType;
    private String downloadUrl;
    private String status;
    private String message;
    
    /**
     * Constructor para reportes exitosos
     */
    public static ReportResponse success(String fileName, String contentType, Long fileSize, String reportType) {
        return ReportResponse.builder()
            .fileName(fileName)
            .contentType(contentType)
            .fileSize(fileSize)
            .generatedAt(LocalDateTime.now())
            .reportType(reportType)
            .status("SUCCESS")
            .message("Reporte generado exitosamente")
            .build();
    }
    
    /**
     * Constructor para reportes con error
     */
    public static ReportResponse error(String message) {
        return ReportResponse.builder()
            .generatedAt(LocalDateTime.now())
            .status("ERROR")
            .message(message)
            .build();
    }
}
