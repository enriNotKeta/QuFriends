package app.model;


import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class StatisticsPDFExporter {
    private Map<Hobby,Double> mapHobbyAvgRatings;

    public StatisticsPDFExporter(Map<Hobby,Double> mapHobbyAvgRatings) {
        this.mapHobbyAvgRatings = mapHobbyAvgRatings;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Hobby ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Hobby Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Avg. Rating", font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        for (var entry : mapHobbyAvgRatings.entrySet()) {
            table.addCell(String.valueOf(entry.getKey().getId()));
            table.addCell(entry.getKey().getName());
            table.addCell(entry.getValue().toString());
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("General Statistics", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);


        document.add(p);
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2f, 2f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}