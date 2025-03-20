package com.ipl.prediction.iplprediction.service;

import com.ipl.prediction.iplprediction.dto.IplUserDto;
import com.ipl.prediction.iplprediction.entity.IplUser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    public List<IplUserDto> parseExcelFile(MultipartFile file) {
        List<IplUserDto> employees = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0); // Read first sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                IplUserDto emp = new IplUserDto();
                emp.setUserId(row.getCell(0).getStringCellValue());
                emp.setName(row.getCell(1).getStringCellValue());
                emp.setLocation(row.getCell(2).getStringCellValue());

                employees.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }
}
