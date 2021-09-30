package sample.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelHandler {
    static String protocolDir;

    public static String readFUNSN(FUN fun) {
        String path = protocolDir + fun.getName();
        return readSN(path);
    }

    public static String readCrateSN(Crate crate) {
        String path = protocolDir + crate.getName();
        return readSN(path);
    }

    private static String readSN(String path) {
        String SN;
        String file = path + "\\_Серийники.xlsx";
        Workbook workbook;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Cell indexCell = getCell("D1", sheet);
            int index = (int) indexCell.getNumericCellValue();
            indexCell.setCellValue(index + 1);
            Cell SNCell = getCell("A" + index, sheet);
            if (SNCell == null) {
                return "SN";
            }
            SN = SNCell.getStringCellValue();
            Cell OKCell = createCell("B" + index, sheet);
            OKCell.setCellValue("OK");
            Cell dateCell = createCell("C" + index, sheet);
            dateCell.setCellValue(new SimpleDateFormat("dd.MM.YYYY").format(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
            return "READ";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            SN = "WRITE";
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    SN = "WRITE";
                }
            }
        }
        return SN;
    }

    public static int createFUNProtocol(FUN fun, String SN, String SNP, String fanModel, String engineer) {
        Map<String, String> cells = new HashMap<>();
        cells.put(fun.getSNCell(), SN);
        cells.put(fun.getNameCell(), fun.getName());
        cells.put(fun.getSNPCell(), SNP);
        cells.put(fun.getHWCell(), fun.getHW());
        cells.put(fun.getFanModelCell(), fanModel);
        cells.put(fun.getEngineerCell(), engineer);
        cells.put(fun.getDateCell(), new SimpleDateFormat("dd.MM.YYYY").format(new Date()));
        return createProtocol(protocolDir + fun.getName() + "\\" + SN.substring(SN.lastIndexOf(".") + 1) + ".xlsx", cells);
    }

    public static int createCrateProtocol(Crate crate, String SN, String SNBP, String engineer) {
        Map<String, String> cells = new HashMap<>();
        cells.put(crate.getSNCell(), SN);
        cells.put(crate.getNameCell(), crate.getName());
        cells.put(crate.getSNBPCell(), SNBP);
        cells.put(crate.getBPrevCell(), crate.getBPRev());
        cells.put(crate.getEngineerCell(), engineer);
        cells.put(crate.getDateCell(), new SimpleDateFormat("dd.MM.YYYY").format(new Date()));
        return createProtocol(protocolDir + crate.getName() + "\\" + SN.substring(SN.lastIndexOf(".") + 1) + ".xlsx", cells);
    }

    private static int createProtocol(String path, Map<String, String> args) {
        String file = path.substring(0, path.lastIndexOf("\\")) + "\\_Шаблон.xlsx";
        Workbook workbook = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            workbook.setForceFormulaRecalculation(true);
            Sheet sheet = workbook.getSheetAt(0);
            for (Map.Entry<String, String> i : args.entrySet()) {
                getCell(i.getKey(), sheet).setCellValue(i.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            outputStream = new FileOutputStream(path);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 2;
                }
            }
        }
        return 0;
    }

    private static Cell getCell(String reference, Sheet sheet) {
        CellReference CR = new CellReference(reference);
        Row row = sheet.getRow(CR.getRow());
        return row == null ? null : row.getCell(CR.getCol());
    }

    private static Cell createCell(String reference, Sheet sheet) {
        CellReference CR = new CellReference(reference);
        Row row = sheet.getRow(CR.getRow());
        return row.createCell(CR.getCol());
    }
}
