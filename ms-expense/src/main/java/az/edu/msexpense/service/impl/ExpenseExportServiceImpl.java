package az.edu.msexpense.service.impl;

import az.edu.msexpense.dto.response.ExpenseResponse;
import az.edu.msexpense.service.ExpenseExportService;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ExpenseExportServiceImpl implements ExpenseExportService {

    private static final String EXPORT_DIR = "ms-expense/src/main/resources/exports";
    private static final int[] COL_WIDTHS = {80, 110, 70, 120, 170};
    private static final String[] HEADERS = {"Date", "Category", "Amount", "Title", "Description"};
    private static final int TABLE_WIDTH = Arrays.stream(COL_WIDTHS).sum();
    private static final int ROW_HEIGHT = 25;
    private static final int START_X = 50;
    private static final int TABLE_TOP_Y = 730;



    @Override
    public String saveExpensesPdfToFile(List<ExpenseResponse> expenses, String fileName) {
        byte[] pdfBytes = exportExpensesToPdf(expenses);
        File dir = new File(EXPORT_DIR);
        if (!dir.exists()) dir.mkdirs();
        String filePath = EXPORT_DIR + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save PDF file", e);
        }
        return filePath;
    }

    @Override
    public byte[] exportExpensesToPdf(List<ExpenseResponse> expenses) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                drawTitle(contentStream);
                drawTableHeader(contentStream);
                drawTableRows(contentStream, expenses);
                drawTableBorders(contentStream, expenses.size());
                drawFooter(contentStream, expenses);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("PDF export failed", e);
        }
    }

    private void drawTitle(PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.beginText();
        contentStream.newLineAtOffset(200, 780);
        contentStream.showText("Expense Report");
        contentStream.endText();
    }

    private void drawTableHeader(PDPageContentStream contentStream) throws IOException {
        contentStream.setNonStrokingColor(220, 220, 220); // Açıq boz fon
        contentStream.addRect(START_X, TABLE_TOP_Y, TABLE_WIDTH, ROW_HEIGHT);
        contentStream.fill();
        contentStream.setNonStrokingColor(0, 0, 0); // Qara

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        int textX = START_X + 5;
        int textY = TABLE_TOP_Y + 7;
        for (int i = 0; i < HEADERS.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(HEADERS[i]);
            contentStream.endText();
            textX += COL_WIDTHS[i];
        }
    }

    private void drawTableRows(PDPageContentStream contentStream, List<ExpenseResponse> expenses) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 11);
        int y = TABLE_TOP_Y - ROW_HEIGHT;
        boolean zebra = false;

        for (ExpenseResponse expense : expenses) {
            if (zebra) {
                contentStream.setNonStrokingColor(245, 245, 245); // Zebra style
                contentStream.addRect(START_X, y, TABLE_WIDTH, ROW_HEIGHT);
                contentStream.fill();
                contentStream.setNonStrokingColor(0, 0, 0);
            }
            zebra = !zebra;

            int cellX = START_X + 5;
            int cellY = y + 7;

            drawCell(contentStream, cellX, cellY, expense.getDate().toString());
            cellX += COL_WIDTHS[0];

            drawCell(contentStream, cellX, cellY, expense.getCategory() != null ? expense.getCategory().getName() : "-");
            cellX += COL_WIDTHS[1];

            drawCell(contentStream, cellX, cellY, expense.getAmount().toString());
            cellX += COL_WIDTHS[2];

            drawCell(contentStream, cellX, cellY, expense.getTitle());
            cellX += COL_WIDTHS[3];

            String desc = expense.getDescription() != null ? expense.getDescription() : "-";
            if (desc.length() > 30) desc = desc.substring(0, 30) + "...";
            drawCell(contentStream, cellX, cellY, desc);

            y -= ROW_HEIGHT;
        }
    }

    private void drawCell(PDPageContentStream contentStream, int x, int y, String text) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void drawTableBorders(PDPageContentStream contentStream, int rowCount) throws IOException {
        contentStream.setStrokingColor(180, 180, 180); // Açıq boz
        int tableBottomY = TABLE_TOP_Y - (rowCount + 1) * ROW_HEIGHT;

        // Yatay xətlər
        for (int i = 0; i <= rowCount + 1; i++) {
            int lineY = TABLE_TOP_Y - i * ROW_HEIGHT;
            contentStream.moveTo(START_X, lineY);
            contentStream.lineTo(START_X + TABLE_WIDTH, lineY);
        }
        // Dikey xətlər
        int lineX = START_X;
        for (int w : COL_WIDTHS) {
            contentStream.moveTo(lineX, TABLE_TOP_Y);
            contentStream.lineTo(lineX, tableBottomY);
            lineX += w;
        }
        contentStream.moveTo(START_X + TABLE_WIDTH, TABLE_TOP_Y);
        contentStream.lineTo(START_X + TABLE_WIDTH, tableBottomY);
        contentStream.stroke();
    }

    private void drawFooter(PDPageContentStream contentStream, List<ExpenseResponse> expenses) throws IOException {
        BigDecimal total = expenses.stream()
                .map(ExpenseResponse::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int tableBottomY = TABLE_TOP_Y - (expenses.size() + 1) * ROW_HEIGHT;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(START_X, tableBottomY - 20);
        contentStream.showText("Total: " + total);
        contentStream.endText();
    }
}