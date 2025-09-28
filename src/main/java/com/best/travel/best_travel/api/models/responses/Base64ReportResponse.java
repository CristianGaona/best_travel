package com.best.travel.best_travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para reportes PDF codificados en Base64
 * 
 * @author Senior Developer
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Base64ReportResponse {
    
    private String pdfBase64;
    private String contentType;
    private String fileName;
    private Long fileSize;
    private String format;
    private String message;
    private boolean success;
    
    /**
     * Crea una respuesta exitosa con el PDF en Base64
     * 
     * @param pdfBase64 - Contenido del PDF codificado en Base64
     * @param contentType - Tipo de contenido del archivo
     * @param fileName - Nombre del archivo
     * @param fileSize - Tamaño del archivo en bytes
     * @param format - Formato del archivo
     * @return Base64ReportResponse - Respuesta exitosa
     */
    public static Base64ReportResponse success(String pdfBase64, String contentType, String fileName, Long fileSize, String format) {
        return Base64ReportResponse.builder()
            .pdfBase64(pdfBase64)
            .contentType(contentType)
            .fileName(fileName)
            .fileSize(fileSize)
            .format(format)
            .message("Reporte generado exitosamente")
            .success(true)
            .build();
    }
    
    /**
     * Crea una respuesta de error
     * 
     * @param errorMessage - Mensaje de error
     * @return Base64ReportResponse - Respuesta de error
     */
    public static Base64ReportResponse error(String errorMessage) {
        return Base64ReportResponse.builder()
            .message(errorMessage)
            .success(false)
            .build();
    }
}
