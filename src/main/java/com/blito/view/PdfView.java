package com.blito.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sheetName = (String) model.get("sheetname");
		List<String> headers = (List<String>) model.get("headers");
		Map<Long, List<String>> results = (Map<Long, List<String>>) model.get("results");
		
		PdfPTable table = new PdfPTable(headers.size());
		
		headers.forEach(header-> {
			Phrase phrase = new Phrase(header);
			PdfPCell cell = new PdfPCell(phrase);
			table.addCell(cell);
		});
		
		results.forEach((key, value) -> {
			value.forEach(v -> {
				Phrase phrase = new Phrase(v);
				PdfPCell cell = new PdfPCell(phrase);
				table.addCell(cell);
			});
		});
		
		document.add(table);

	}

}
