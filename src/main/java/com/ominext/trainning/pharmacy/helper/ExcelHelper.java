package com.ominext.trainning.pharmacy.helper;

import com.ominext.trainning.pharmacy.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsh";
    static String[] HEADERS = {"name", "discount", "price", "img", "content", "unit", "quantity", "deleted"};
    static String SHEET = "product";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<Product> excelToProduct(InputStream is) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Product> products = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                if (checkIfRowIsEmpty(currentRow)) {
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Product product = new Product();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            product.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            product.setDiscount((float) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            product.setPrice(currentCell.getNumericCellValue());
                            break;

                        case 3:
                            product.setImg(currentCell.getStringCellValue());
                            break;
                        case 4:
                            product.setContent(currentCell.getStringCellValue());
                            break;
                        case 5:
                            product.setUnit(currentCell.getStringCellValue());
                            break;
                        case 6:
                            product.setQuantity((int) currentCell.getNumericCellValue());
                            break;
                        case 7:
                            product.setDeleted(currentCell.getBooleanCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                products.add(product);
            }

            workbook.close();

            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    private static boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    //EXPORT EXCEL
    private static void writeHeaderLine(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(SHEET);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        for (int i = 0; i < HEADERS.length; i++) {
            createCell(sheet, row, i, HEADERS[i], style);
        }
    }

    private static void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((boolean) value);
        }
        cell.setCellStyle(style);
    }

    private static void writeData(List<Product> productList, XSSFWorkbook workbook) {
        try {
            int rowCount = 1;
            XSSFSheet sheet = workbook.getSheet(SHEET);
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);
            for (Product product : productList) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(sheet, row, columnCount++, product.getName(), style);
                createCell(sheet, row, columnCount++, product.getDiscount(), style);
                createCell(sheet, row, columnCount++, product.getPrice(), style);
                createCell(sheet, row, columnCount++, product.getImg(), style);
                createCell(sheet, row, columnCount++, product.getContent(), style);
                createCell(sheet, row, columnCount++, product.getUnit(), style);
                createCell(sheet, row, columnCount++, product.getQuantity(), style);
                createCell(sheet, row, columnCount++, product.isDeleted(), style);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportExcelFile(HttpServletResponse response, List<Product> productList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        writeHeaderLine(workbook);
        writeData(productList, workbook);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
