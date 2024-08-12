package com.example.fileconversion.service;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class PdfToExcelService {

    public ByteArrayInputStream generateExcelFromPdf(InputStream pdfInputStream) throws Exception {
        PdfReader reader = new PdfReader(pdfInputStream);
        StringBuilder text = new StringBuilder();

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
            text.append("\n");
        }
        reader.close();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("PDF Data");

        String[] lines = text.toString().split("\n");
        int rowIndex = 0;
        for (String line : lines) {
            Row row = sheet.createRow(rowIndex++);
            String[] cells = line.split("\\s+"); 
            int colIndex = 0;
            for (String cellValue : cells) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellValue(cellValue);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
