package com.blito.view;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class BlitReceiptPdfView extends AbstractPdfView {

	private String fontPath = "src/main/resources/static/assets/fonts/website-fonts/IranSans.ttf";
	private String boldFontPath = "src/main/resources/static/assets/fonts/website-fonts/IranSans Bold.ttf";
	private String fontelloPath = "src/main/resources/static/assets/fonts/website-fonts/fontello.ttf";
	private String iconicPath = "src/main/resources/static/assets/fonts/website-fonts/IconicFonts.ttf";
	private String imagePath = "src/main/resources/static/assets/img/logoTicket2.jpg";

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String eventName = (String) model.get("event name");
		String customerName = (String) model.get("customer name");
		String eventDate = (String) model.get("event date");
		String trackCode = (String) model.get("track code");

		String eventPhotoId = (String) model.get("event photo");
		int count = (int) model.get("count");
		String address = (String) model.get("event address");
		String blitType = "no blit type";
		if (model.containsKey("blit type"))
            blitType = (String) model.get("blit type");

		String seat = "no seats";
		if (model.containsKey("seats"))
			seat = (String) model.get("seats");

		String telephone = "no telephone";
		if (model.containsKey("customer mobile"))
		    telephone = (String) model.get("customer mobile");

        String email = "no email";
        if (model.containsKey("customer email"))
            email = (String) model.get("customer email");


        document.setPageSize(PageSize.A4);

		PdfPTable table = new PdfPTable(2);
		table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		table.setSpacingBefore(200);

		Image image = Image.getInstance(imagePath);
		image.setAbsolutePosition(65f, 780f);
		image.scalePercent(18);

		document.add(image);
		
//		String base64 = new Scanner(new File("images/" + eventPhotoId + ".txt")).useDelimiter("\\Z").next();
//		base64 = base64.split(",")[1];
//
//		byte[] decoded = Base64.decodeBase64(base64);
//		Image eventImage = Image.getInstance(decoded);

		BaseFont textBf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font textFont = new Font(textBf, 12);
		textFont.setColor(Color.GRAY);
		BaseFont boldBf = BaseFont.createFont(boldFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font boldFont = new Font(boldBf, 12);
		Font titleFont = new Font(boldBf, 16);
		BaseFont fontelloBf = BaseFont.createFont(fontelloPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font fontello = new Font(fontelloBf, 10);
		fontello.setColor(Color.GRAY);
        BaseFont iconicBf = BaseFont.createFont(iconicPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font iconic = new Font(iconicBf, 11);
        iconic.setColor(Color.GRAY);
		
		
//		////////////////////////////////////////////////////////////////
//		PdfPCell imageCell = new PdfPCell(eventImage, true);
//		imageCell.setColspan(1);
//		imageCell.setRowspan(10);
//		imageCell.setBorder(Rectangle.NO_BORDER);
//		table.addCell(imageCell);
		////////////////////////////////////////
		Phrase eventNamePhrase = new Phrase();
		eventNamePhrase.setFont(titleFont);
		eventNamePhrase.add(eventName);

		PdfPCell eventNameCell = new PdfPCell(eventNamePhrase);
		eventNameCell.setColspan(2);
		eventNameCell.setBorder(Rectangle.BOTTOM);
		eventNameCell.setBorderColor(new Color(252, 183, 49));
		eventNameCell.setBorderWidth(2);
		eventNameCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		eventNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		eventNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		eventNameCell.setPaddingBottom(10);
		table.addCell(eventNameCell);

		////////////////////////////////////////////////////////////////
		Phrase customerNameTitlePhrase = new Phrase();
		customerNameTitlePhrase.add(new Chunk("\ue806", fontello));
		customerNameTitlePhrase.add(new Chunk("    نام: ", boldFont));

		PdfPCell customerNameTitleCell = new PdfPCell(customerNameTitlePhrase);
		customerNameTitleCell.setBorder(Rectangle.NO_BORDER);
		customerNameTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		customerNameTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		customerNameTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		customerNameTitleCell.setPaddingBottom(8);
		table.addCell(customerNameTitleCell);
		///////////////////////////////////////////////////////////////
		Phrase eventDateTitlePhrase = new Phrase();
		eventDateTitlePhrase.add(new Chunk("\ue802", fontello));
		eventDateTitlePhrase.add(new Chunk("    تاریخ رویداد: ", boldFont));

		PdfPCell eventDateTitleCell = new PdfPCell(eventDateTitlePhrase);
		eventDateTitleCell.setBorder(Rectangle.NO_BORDER);
		eventDateTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		eventDateTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		eventDateTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		eventDateTitleCell.setPaddingBottom(8);
		table.addCell(eventDateTitleCell);
		//////////////////////////////////////////////////////////////
		Phrase customerNamePhrase = new Phrase();
		customerNamePhrase.setFont(textFont);
		customerNamePhrase.add(customerName);

		PdfPCell customerNameCell = new PdfPCell(customerNamePhrase);
		customerNameCell.setBorder(Rectangle.NO_BORDER);
		customerNameCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		customerNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		customerNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		customerNameCell.setPaddingRight(20);
		customerNameCell.setPaddingBottom(8);
		table.addCell(customerNameCell);
		///////////////////////////////////////////////////////////////
		Phrase eventDatePhrase = new Phrase();
		eventDatePhrase.setFont(textFont);
		eventDatePhrase.add(eventDate);

		PdfPCell eventDateCell = new PdfPCell(eventDatePhrase);
		eventDateCell.setBorder(Rectangle.NO_BORDER);
		eventDateCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		eventDateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		eventDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		eventDateCell.setPaddingRight(20);
		eventDateCell.setPaddingBottom(8);
		table.addCell(eventDateCell);
		//////////////////////////////////////////////////////////////
		if (!email.equals("no email") && !telephone.equals("no telephone")) {
            Phrase telephoneTitlePhrase = new Phrase();
            telephoneTitlePhrase.add(new Chunk("\ue800", fontello));
            telephoneTitlePhrase.add(new Chunk("    تلفن: ", boldFont));

            PdfPCell telephoneTitleCell = new PdfPCell(telephoneTitlePhrase);
            telephoneTitleCell.setBorder(Rectangle.NO_BORDER);
            telephoneTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            telephoneTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            telephoneTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            telephoneTitleCell.setPaddingBottom(8);
            table.addCell(telephoneTitleCell);
            ///////////////////////////////////////////////////////////////
            Phrase emailTitlePhrase = new Phrase();
            emailTitlePhrase.add(new Chunk("\ue801", fontello));
            emailTitlePhrase.add(new Chunk("    ایمیل: ", boldFont));

            PdfPCell emailTitleCell = new PdfPCell(emailTitlePhrase);
            emailTitleCell.setBorder(Rectangle.NO_BORDER);
            emailTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            emailTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            emailTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emailTitleCell.setPaddingBottom(8);
            table.addCell(emailTitleCell);
            //////////////////////////////////////////////////////////////
            Phrase telephonePhrase = new Phrase();
            telephonePhrase.setFont(textFont);
            telephonePhrase.add(telephone);

            PdfPCell telephoneCell = new PdfPCell(telephonePhrase);
            telephoneCell.setBorder(Rectangle.NO_BORDER);
            telephoneCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            telephoneCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            telephoneCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            telephoneCell.setPaddingRight(20);
            telephoneCell.setPaddingBottom(8);
            table.addCell(telephoneCell);
            ///////////////////////////////////////////////////////////////
            Phrase emailPhrase = new Phrase();
            emailPhrase.setFont(textFont);
            emailPhrase.add(email);

            PdfPCell emailCell = new PdfPCell(emailPhrase);
            emailCell.setBorder(Rectangle.NO_BORDER);
            emailCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            emailCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            emailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emailCell.setPaddingRight(20);
            emailCell.setPaddingBottom(8);
            table.addCell(emailCell);
        }
		//////////////////////////////////////////////////////////////
        Phrase trackCodeTitlePhrase = new Phrase();
        trackCodeTitlePhrase.add(new Chunk("\ue803", fontello));
        trackCodeTitlePhrase.add(new Chunk("    کد پیگیری: ", boldFont));

        PdfPCell trackCodeTitleCell = new PdfPCell(trackCodeTitlePhrase);
        trackCodeTitleCell.setBorder(Rectangle.NO_BORDER);
        trackCodeTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        trackCodeTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        trackCodeTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        trackCodeTitleCell.setPaddingBottom(8);
        table.addCell(trackCodeTitleCell);
		///////////////////////////////////////////////////////////////
		Phrase countTitlePhrase = new Phrase();
		countTitlePhrase.add(new Chunk("\ue804", fontello));
		countTitlePhrase.add(new Chunk("    تعداد: ", boldFont));

		PdfPCell countTitleCell = new PdfPCell(countTitlePhrase);
		countTitleCell.setBorder(Rectangle.NO_BORDER);
		countTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		countTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		countTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		countTitleCell.setPaddingBottom(8);
		table.addCell(countTitleCell);
		//////////////////////////////////////////////////////////////
        Phrase trackCodePhrase = new Phrase();
        trackCodePhrase.setFont(textFont);
        trackCodePhrase.add(trackCode);

        PdfPCell trackCodeCell = new PdfPCell(trackCodePhrase);
        trackCodeCell.setBorder(Rectangle.NO_BORDER);
        trackCodeCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        trackCodeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        trackCodeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        trackCodeCell.setPaddingRight(20);
        trackCodeCell.setPaddingBottom(8);
        table.addCell(trackCodeCell);
		///////////////////////////////////////////////////////////////
		Phrase countPhrase = new Phrase();
		countPhrase.setFont(textFont);
		countPhrase.add(String.valueOf(count));

		PdfPCell countCell = new PdfPCell(countPhrase);
		countCell.setBorder(Rectangle.NO_BORDER);
		countCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		countCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		countCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		countCell.setPaddingRight(20);
		countCell.setPaddingBottom(8);
		table.addCell(countCell);
		//////////////////////////////////////////////////////////////
		if (!blitType.equals("no blit type")) {
            Phrase blankTitlePhrase = new Phrase();
            blankTitlePhrase.setFont(boldFont);
            blankTitlePhrase.add("");

            PdfPCell blankTitleCell = new PdfPCell(blankTitlePhrase);
            blankTitleCell.setBorder(Rectangle.NO_BORDER);
            blankTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            blankTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            blankTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            blankTitleCell.setPaddingBottom(8);
            table.addCell(blankTitleCell);
            ///////////////////////////////////////////////////////////////
            Phrase blitTypeTitlePhrase = new Phrase();
            blitTypeTitlePhrase.add(new Chunk("\uf145", fontello));
            blitTypeTitlePhrase.add(new Chunk("    نوع بلیت: ", boldFont));

            PdfPCell blitTypeTitleCell = new PdfPCell(blitTypeTitlePhrase);
            blitTypeTitleCell.setBorder(Rectangle.NO_BORDER);
            blitTypeTitleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            blitTypeTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            blitTypeTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            blitTypeTitleCell.setPaddingBottom(8);
            table.addCell(blitTypeTitleCell);
            //////////////////////////////////////////////////////////////
            Phrase blankPhrase = new Phrase();
            blankPhrase.setFont(textFont);
            blankPhrase.add("");

            PdfPCell blankCell = new PdfPCell(blankPhrase);
            blankCell.setBorder(Rectangle.NO_BORDER);
            blankCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            blankCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            blankCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            blankCell.setPaddingRight(20);
            blankCell.setPaddingBottom(8);
            table.addCell(blankCell);
            ///////////////////////////////////////////////////////////////
            Phrase blitTypePhrase = new Phrase();
            blitTypePhrase.setFont(textFont);
            blitTypePhrase.add(blitType);

            PdfPCell blitTypeCell = new PdfPCell(blitTypePhrase);
            blitTypeCell.setBorder(Rectangle.NO_BORDER);
            blitTypeCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            blitTypeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            blitTypeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            blitTypeCell.setPaddingRight(20);
            blitTypeCell.setPaddingBottom(8);
            table.addCell(blitTypeCell);
        }
		//////////////////////////////////////////////////////////////

        if (!seat.equals("no seats")) {
            Phrase seatsPhrase = new Phrase();
            seatsPhrase.add(new Chunk("\uf1c4", iconic));
            seatsPhrase.add(new Chunk("    جایگاه: ", boldFont));
            seatsPhrase.add(new Chunk(seat, textFont));


            PdfPCell seatsCell = new PdfPCell(seatsPhrase);
            seatsCell.setColspan(2);
            seatsCell.setBorder(Rectangle.NO_BORDER);
            seatsCell.enableBorderSide(Rectangle.TOP);
            seatsCell.setBorderColor(Color.LIGHT_GRAY);
            seatsCell.setBorderWidth(0.5f);
            seatsCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            seatsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            seatsCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            seatsCell.setPaddingTop(10);
            seatsCell.setPaddingBottom(10);
            seatsCell.setLeading(0,2);
            seatsCell.setPaddingBottom(23);
            table.addCell(seatsCell);
        }

        /////////////////////////////////////////////////////////////

		Phrase addressPhrase = new Phrase();
		addressPhrase.add(new Chunk("\ue805", fontello));
		addressPhrase.add(new Chunk("    آدرس: ", boldFont));
		addressPhrase.add(new Chunk(address, textFont));

		PdfPCell addressCell = new PdfPCell(addressPhrase);
		addressCell.setColspan(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.enableBorderSide(Rectangle.TOP);
		addressCell.setBorderColor(Color.LIGHT_GRAY);
		addressCell.setBorderWidth(0.5f);
		addressCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		addressCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		addressCell.setPaddingTop(10);
		addressCell.setPaddingBottom(10);
		table.addCell(addressCell);

		/////////////////////////////////////////////////////////////
		document.add(table);

	}

}
