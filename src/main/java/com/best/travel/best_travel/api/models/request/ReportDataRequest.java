package com.best.travel.best_travel.api.models.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos del reporte desde el frontend
 * 
 * @author Senior Developer
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataRequest {
    
    @NotNull(message = "La lista de clientes no puede ser nula")
    @NotEmpty(message = "La lista de clientes no puede estar vacía")
    @Valid
    private List<CustomerReportData> customers;
    
    @Builder.Default
    private String reportTitle = "Reporte de Clientes - Best Travel";
    
    @Builder.Default
    private String reportSubtitle = "Generado desde el frontend";
    
    /**
     * DTO para datos de cliente en el reporte
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerReportData {
        
        @NotNull(message = "El DNI no puede ser nulo")
        private String dni;
        
        @NotNull(message = "El nombre completo no puede ser nulo")
        private String fullName;
        
        @Builder.Default
        private Integer totalLodgings = 0;
        
        @Builder.Default
        private Integer totalFlights = 0;
        
        @Builder.Default
        private Integer totalTours = 0;
        
        @Builder.Default
        private Integer totalPurchases = 0;
        
        /**
         * Calcula el total de compras automáticamente
         */
        public Integer getTotalPurchases() {
            return (totalLodgings != null ? totalLodgings : 0) +
                   (totalFlights != null ? totalFlights : 0) +
                   (totalTours != null ? totalTours : 0);
        }
    }
}

