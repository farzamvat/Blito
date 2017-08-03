package com.blito.view;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfView extends AbstractPdfView {

	private String fontPath = "src/main/resources/static/assets/fonts/website-fonts/IranSans.ttf";
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sheetName = (String) model.get("sheetname");
		List<String> headers = (List<String>) model.get("headers");
		Map<Long, List<String>> results = (Map<Long, List<String>>) model.get("results");
		
		PdfPTable table = new PdfPTable(headers.size());
		
		BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bf,12);
		
		headers.forEach(header-> {
			Phrase phrase = new Phrase();
			phrase.setFont(font);
			phrase.add(header);
			PdfPCell cell = new PdfPCell(phrase);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(Color.LIGHT_GRAY);
			table.addCell(cell);
		});
		
		results.forEach((key, value) -> {
			value.forEach(v -> {
				Phrase phrase = new Phrase();
				phrase.setFont(font);
				try {
					phrase.add(new String(v.getBytes(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				PdfPCell cell = new PdfPCell(phrase);
				cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			});
		});
		
		document.add(table);

	}

}
