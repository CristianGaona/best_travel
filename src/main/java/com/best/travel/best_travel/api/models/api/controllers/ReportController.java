package com.best.travel.best_travel.api.models.api.controllers;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.infraestructure.asbtract_services.IReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping(path = "report")
@AllArgsConstructor
@Tag(name = "Report", description = "Report API")
public class ReportController {
    
    private final IReportService reportService;

    @GetMapping
    public ResponseEntity<Resource> get(){
        var headers = new HttpHeaders();
        headers.setContentType(FORCE_DOWNLOAD);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, FORCE_DOWNLOAD_HEADER_VALUE);
        var fileInBytes = this.reportService.readFile();
        ByteArrayResource response = new ByteArrayResource(fileInBytes);
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(fileInBytes.length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(response);

    }

    private static final MediaType FORCE_DOWNLOAD = new MediaType("application", "force-download");
    private static final String FORCE_DOWNLOAD_HEADER_VALUE ="attachment; filename=report.xlsx";

}