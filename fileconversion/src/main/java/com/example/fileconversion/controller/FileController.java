package com.example.fileconversion.controller;

import com.example.fileconversion.service.PdfToExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
public class FileController {

    @Autowired
    private PdfToExcelService pdfToExcelService;
    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertPdfToExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Generate an Excel file
            ByteArrayInputStream excelFile = pdfToExcelService.generateExcelFromPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelFile.readAllBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
