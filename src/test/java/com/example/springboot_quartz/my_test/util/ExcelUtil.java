package com.example.springboot_quartz.my_test.util;

import com.example.springboot_quartz.my_test.po.Case;
import com.example.springboot_quartz.my_test.po.Rest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ExcelUtil {

    /**
     * 1、将excel中的数据加载到内存中的对象集合中
     * 2、将列名对应的列序号加载到内存中，后续回填case运行结果时使用
     * @param filePath
     * @param sheetNum
     * @param clz  通用方法，通过class反射机制，将excel数据转换为对象数据
     */
    public static <T> void loadData(String filePath,Integer sheetNum,Class<T> clz){
        try(FileInputStream inputStream = new FileInputStream(new File(filePath))){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetNum - 1);
            String[] fields = null;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                Object object = null;
                if (row!=null){
                    for (int cellNum = 0; cellNum < (int) row.getLastCellNum(); cellNum++) {
                        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
                        //读取标题行数据,将行字段存储下来，以便下面几行的数据反射使用
                        if (rowNum == 0){
                            fields[cellNum] = cellValue.substring(0, cellValue.indexOf("("));
                            int columnIndex = cell.getAddress().getColumn();
                            //将列序号存储下来，以便后续回填case运行结果使用
                            CaseUtil.cellName2CellIndexMap.put(cellValue.substring(0, cellValue.indexOf("(")),columnIndex);
                        }else {
                            object = clz.newInstance();
                            Method method = clz.getMethod("set" + fields[cellNum]);
                            method.invoke(object,cellValue);
                        }
                    }
                    if (object instanceof Case){
                        CaseUtil.caseList.add((Case)object);
                    }else if (object instanceof Rest){
                        RestUtil.restList.add((Rest)object);
                    }
                }
            }
        }catch (Exception e){
            log.error("读取excel数据出错，{}",e);
        }
    }


    public static void writeResultToExcel(String filePath,String sheetNum){
        try(FileInputStream inputStream = new FileInputStream(new File(filePath))){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(Integer.valueOf(sheetNum) - 1);
            for (Map.Entry<String, Map<String,String>> entry : CaseUtil.caseId2resultMap.entrySet()) {
                Row row = sheet.getRow(Integer.valueOf(entry.getKey()));
                for (Map.Entry<String, String> stringStringEntry : entry.getValue().entrySet()) {
                    Cell cell = row.getCell(CaseUtil.cellName2CellIndexMap.get(stringStringEntry.getKey()), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(stringStringEntry.getValue());
                }
            }
            try(FileOutputStream outputStream = new FileOutputStream(new File(""))){
                outputStream.write(inputStream.read());
            }catch (Exception e){

            }
        }catch (Exception e){

        }
    }
}
