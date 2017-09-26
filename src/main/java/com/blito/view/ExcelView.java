package com.blito.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExcelView extends AbstractXlsView {

    private static final Logger logger = LoggerFactory.getLogger(ExcelView.class);
	@Override
    @SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        String sheetName = (String)model.get("sheetname");
        List<String> headers = (List<String>)model.get("headers");
        Map<Long,List<String>> results = (Map<Long,List<String>>)model.get("results");
        logger.info("map value *************** '{0}'" , new ObjectMapper().writeValueAsString(results));
        List<String> numericColumns = new ArrayList<String>();
        if (model.containsKey("numericcolumns"))
            numericColumns = (List<String>)model.get("numericcolumns");

        Sheet sheet = workbook.createSheet(sheetName);
        
        sheet.setDefaultColumnWidth((short) 12);
        
        int currentRow = 0;
        short currentColumn = 0;
        
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        
        Row headersRow = sheet.createRow(currentRow);
        currentRow++;
        for(String header : headers) {
        	headersRow.createCell(currentColumn, CellType.STRING).setCellValue(header);
        	currentColumn++;
        }
        
        
        for (Entry<Long, List<String>> entry : results.entrySet()) {
        	currentColumn = 0;
        	Row row = sheet.createRow(currentRow);
        	for(String value: entry.getValue()) {
        		Cell cell = null;
        		if(numericColumns.contains(headers.get(currentColumn))) {
        			cell = row.createCell(currentColumn, CellType.NUMERIC);
        			cell.setCellValue(Double.parseDouble(value));
        		}
        		else {
        			cell = row.createCell(currentColumn, CellType.STRING);
        			cell.setCellValue(value);
        		}
        		currentColumn++;
        	}
        	currentRow++;
		}

	}

}
