package com.example.springboot_quartz.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel文档工具类
 *
 * @author 张宇
 */

public class ExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static boolean isLinux() {
        try {
            if (StringUtils.startsWithIgnoreCase(System.getProperty("os.name"), "Lin")) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /**
     * 返回list有3级(只支持.xls)
     * </p>
     * 第一级 是  多少个sheet的list
     * 第二级 是  多少个row的list
     * 第三级 是  多少个cell的list
     * </br>
     *
     * @param is 传入的file
     * @return
     * @throws IOException
     */
    public static List<List<List<String>>> readExcelXls(InputStream is) throws IOException {
        //返回结果集
        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        try {
            HSSFWorkbook book = new HSSFWorkbook(is);  // 创建对Excel工作簿文件的引用
            int sheepCount = book.getNumberOfSheets();
            //循环每一个sheet
            for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
                HSSFSheet sheet = book.getSheetAt(sheepIndex);
                if (sheet == null) {
                    continue;
                }
                List<List<String>> rowList = new ArrayList<List<String>>();
                //循环每一行
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    HSSFRow row = sheet.getRow(rowNum);
                    List<String> cellList = new ArrayList<String>();
                    //判断该行的每个单元格是否有值，如果至少有一个值 就可以加入
                    boolean hasValue = false;
                    if (row != null) {
                        //循环每一个单元格
                        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            String value = getCellValue(cell);
                            cellList.add(StringUtils.trim(value));
                            if (StringUtils.isNotBlank(StringUtils.trim(value))) {
                                hasValue = true;
                            }
                        }
                    }
                    if (hasValue) {
                        rowList.add(cellList);
                    }
                }
                sheetList.add(rowList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return sheetList;
    }

    /**
     * 读取excel 可自定义行 列
     *
     * @param is
     * @param rowSize
     * @param cellSize
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readExcelXls(InputStream is, Integer rowSize, Integer cellSize) throws Exception {

        HSSFWorkbook book = new HSSFWorkbook(is);

        int sheepCount = book.getNumberOfSheets();

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            HSSFSheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            //判断Excel中的最后一行和自定义行数

            for (int rowNum = 0; rowNum <= (sheet.getLastRowNum() > rowSize ? rowSize : sheet.getLastRowNum()); rowNum++) {
                HSSFRow row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                if (row != null) {
                    //循环每一个单元格
                    for (int cellNum = 0; cellNum < cellSize; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                    }
                }
                rowList.add(cellList);
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }


    /**
     * 返回list有3级(只支持.xlsx)
     * 第一级 是  多少个sheet的list
     * 第二级 是  多少个row的list
     * 第三级 是  多少个cell的list
     *
     * @param is 传入的file
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readExcel(InputStream is) throws Exception {

        XSSFWorkbook book = new XSSFWorkbook(is);

        int sheepCount = book.getNumberOfSheets();

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            XSSFSheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                //判断该行的每个单元格是否有值，如果至少有一个值 就可以加入
                boolean hasValue = false;
                if (row != null) {
                    //循环每一个单元格
                    for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                        if (StringUtils.isNotBlank(StringUtils.trim(value))) {
                            hasValue = true;
                        }
                    }
                }
                if (hasValue) {
                    rowList.add(cellList);
                }
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }

    /**
     * 读取excel 可自定义 列
     *
     * @param is
     * @param cellSize
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readExcel(InputStream is, Integer cellSize) throws Exception {

        XSSFWorkbook book = new XSSFWorkbook(is);

        int sheepCount = book.getNumberOfSheets();

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            XSSFSheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            //判断Excel中的最后一行和自定义行数

            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                if (row != null) {
                    //循环每一个单元格
                    for (int cellNum = 0; cellNum < cellSize; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                    }
                }
                rowList.add(cellList);
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }

    /**
     * 读取excel 可自定义行 列
     *
     * @param is
     * @param rowSize
     * @param cellSize
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readExcel(InputStream is, Integer rowSize, Integer cellSize) throws Exception {

        XSSFWorkbook book = new XSSFWorkbook(is);

        int sheepCount = book.getNumberOfSheets();

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            XSSFSheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            //判断Excel中的最后一行和自定义行数

            for (int rowNum = 0; rowNum <= (sheet.getLastRowNum() > rowSize ? rowSize : sheet.getLastRowNum()); rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                if (row != null) {
                    //循环每一个单元格
                    for (int cellNum = 0; cellNum < cellSize; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                    }
                }
                rowList.add(cellList);
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }

    /**
     * 读取excel 可自定义行 列
     *
     * @param is
     * @param rowSize
     * @param cellSize
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readAllExcel(InputStream is, Integer rowSize, Integer cellSize) throws Exception {
        Workbook book = WorkbookFactory.create(is);
        int sheepCount = book.getNumberOfSheets();

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            Sheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            //判断Excel中的最后一行和自定义行数

            for (int rowNum = 0; rowNum <= (sheet.getLastRowNum() > rowSize ? rowSize : sheet.getLastRowNum()); rowNum++) {
                Row row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                if (row != null) {
                    //循环每一个单元格
                    for (int cellNum = 0; cellNum < cellSize; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                    }
                }
                rowList.add(cellList);
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }

    /**
     * 读取excel 可自定义行 列
     * @param is
     * @param rowSize
     * @param cellSize
     * @return
     * @throws Exception
     */
    /**
     * @param is
     * @param sheepCount 要读取的sheet 数量
     * @param rowSize    要读取的row  行 数量
     * @param cellSize   要读取的row  列 数量
     * @return
     * @throws Exception
     */
    public static List<List<List<String>>> readExcel(InputStream is, Integer sheepCount, Integer rowSize, Integer cellSize) throws Exception {

        XSSFWorkbook book = new XSSFWorkbook(is);

        if (sheepCount == null) {
            sheepCount = book.getNumberOfSheets();
        }

        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        //循环每一个sheet
        for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
            XSSFSheet sheet = book.getSheetAt(sheepIndex);
            if (sheet == null) {
                continue;
            }
            List<List<String>> rowList = new ArrayList<List<String>>();
            //循环每一行
            //判断Excel中的最后一行和自定义行数 rowSize
            if (rowSize == null || sheet.getLastRowNum() < rowSize) {
                rowSize = sheet.getLastRowNum();
            }
            for (int rowNum = 0; rowNum <= rowSize; rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);
                List<String> cellList = new ArrayList<String>();
                if (row != null) {
                    //循环每一个单元格 cellSize 自定义
                    //如果为null是默认的
                    if (cellSize == null) {
                        cellSize = (int) row.getLastCellNum();
                    }
                    for (int cellNum = 0; cellNum < cellSize; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String value = getCellValue(cell);
                        cellList.add(StringUtils.trim(value));
                    }
                }
                rowList.add(cellList);
            }
            sheetList.add(rowList);
        }
        return sheetList;
    }

    public static String getCellValue(Cell cell) {
        String value = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    value = null;
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    Workbook wb = cell.getSheet().getWorkbook();
                    CreationHelper crateHelper = wb.getCreationHelper();
                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                    value = getCellValue(evaluator.evaluateInCell(cell));
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        value = "";
                    } else {
                        value = NumberToTextConverter.toText(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                default:
                    value = null;
            }
        }
        return value;
    }

    public static Workbook createWorkBookXlsx(List<Map<String, Object>> list, String keys[], String columnNames[]) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());

        //创建标题
        Row row = sheet.createRow(0);
        //创建标题的样式
        CellStyle titleCs = createTitleStyle(wb);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(titleCs);
            //自适应宽度不不识别中文
            //sheet.autoSizeColumn(i, true);
            sheet.setColumnWidth(i, 6000);
        }

        if (list.size() > 1) {
            //设置
            CellStyle rowCs = createRowsStyle(wb);
            for (int i = 1; i < list.size(); i++) {
                Row row1 = sheet.createRow(i);
                for (short j = 0; j < keys.length; j++) {
                    Cell cell = row1.createCell(j);
                    cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                    cell.setCellStyle(rowCs);
                }
            }

        }
        return wb;
    }


    private static CellStyle createRowsStyle(Workbook wb) {
        // 创建两种字体
        Font f2 = wb.createFont();
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 11);
        f2.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cs2 = wb.createCellStyle();
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        return cs2;
    }

    private static CellStyle createTitleStyle(Workbook wb) {
        // 创建两种字体
        Font f = wb.createFont();
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 11);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle cs = wb.createCellStyle();
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        return cs;
    }

    /**
     * 创建excel文档，
     *
     * @param list        数据
     * @param keys        list中map的key数组集合
     * @param columnNames excel的列名
     */

    public static Workbook createWorkBook(List<Map<String, Object>> list, String[] keys, String columnNames[]) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys[j]) == null ? " " : list.get(i).get(keys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }


    public static <T> Workbook createWorkBookOm(List<T> list, String[] keys, String columnNames[]) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet("第一页");
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 0; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) (i + 1));
            // 在row行上创建一个方格
            T t = list.get(i);
            Method method = null;
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                String getMethodName = "get" + keys[j].substring(0, 1).toUpperCase() + keys[j].substring(1);
                String filed = keys[j];
                method = t.getClass().getMethod(getMethodName);
                Object obj = method.invoke(t);
                String value = method.invoke(t) == null ? "" : method.invoke(t).toString();
                if (obj instanceof Date) {
                    Date date = (Date) obj;
                    value = "";
                }
                if ("orderStatus".equals(filed)) {
                    value = "";
                }
                cell.setCellValue(value);
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }


    public static File createFile(Workbook book, String localPath, String localName) throws Exception {

        File localFile = new File(localPath);
        if (!localFile.exists()) {
            boolean b = localFile.mkdirs();
            if (!b) {
                log.error("创建目录失败");
                throw new Exception();
            }
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(localPath + localName);
            book.write(out);
        } catch (Exception e) {
            log.error("创建errorExcel失败", e);
            throw e;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                log.error("关闭流失败", e);
            }
        }
        return new File(localPath + localName);
    }

    public static boolean deleteFile(File file) throws Exception {
        boolean b = false;
        try {
            b = file.delete();
        } catch (Exception e) {
            log.error("文件删除失败", e);
        }
        return b;
    }


    /***********************************************************************************************/


    /**
     * 返回list有3级
     * </p>
     * 第一级 是  多少个sheet的list
     * 第二级 是  多少个row的list
     * 第三级 是  多少个cell的list
     * </br>
     *
     * @param is 传入的file
     * @return
     * @throws IOException
     */
    public static List<List<List<String>>> readAllExcel(InputStream is) {
        //返回结果集
        List<List<List<String>>> sheetList = new ArrayList<List<List<String>>>();
        try {
            // 创建对Excel工作簿文件的引用
            Workbook book = WorkbookFactory.create(is);
            int sheepCount = book.getNumberOfSheets();
            //循环每一个sheet
            for (int sheepIndex = 0; sheepIndex < sheepCount; sheepIndex++) {
                Sheet sheet = book.getSheetAt(sheepIndex);
                if (sheet == null) {
                    continue;
                }
                List<List<String>> rowList = new ArrayList<List<String>>();
                //循环每一行
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    List<String> cellList = new ArrayList<String>();
                    //判断该行的每个单元格是否有值，如果至少有一个值 就可以加入
                    boolean hasValue = false;
                    if (row != null) {
                        //循环每一个单元格
                        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            String value = getCellValue(cell);
                            cellList.add(StringUtils.trim(value));
                            if (StringUtils.isNotBlank(StringUtils.trim(value))) {
                                hasValue = true;
                            }
                        }
                    }
                    if (hasValue) {
                        rowList.add(cellList);
                    }
                }
                sheetList.add(rowList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sheetList;
    }


}