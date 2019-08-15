package com.starnetsecurity.common.util;

import org.apache.commons.lang.time.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 宏炜 on 2016-03-18.
 */
public class PoiExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PoiExcelUtil.class);
    /**
     * 读取excel返回list<Map>
     * @param filePath
     * @return
     */
    public static List<Map<String,Object>> readExcelToListMap(String filePath){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        boolean isXlsx = false;
        if(filePath.endsWith("xlsx"))
            isXlsx = true;
        try{
            InputStream input = new FileInputStream(filePath);
            Workbook workbook  = null;
            if(isXlsx){
                workbook = new XSSFWorkbook(input);
            }else{
                workbook = new HSSFWorkbook(input);
            }
            Sheet sheet = workbook.getSheetAt(0);     //获得第一个表单
            int totalRow = sheet.getLastRowNum();
            for(int rowIndex = 0; rowIndex <= totalRow;rowIndex ++){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                Row row = sheet.getRow(rowIndex);
                int totalCell = row.getLastCellNum();
                for(int cellIndex = 0;cellIndex < totalCell;cellIndex ++){
                    Cell cell = row.getCell(cellIndex);
                    rowMap.put("value" + (cellIndex + 1),getStringCellValue(cell));
                }
                list.add(rowMap);
            }
        }catch (IOException ex){
            LOGGER.error("导入文件读取失败：文件路径为--"+filePath);
            return null;
        }
        return list;
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                if(strCell.contains(".0")){
                    strCell = strCell.replaceAll("\\.0","");
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                strCell = String.valueOf(cell.getCellFormula());
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    public static void ListMapExpotToExcel(String sheetName,Timestamp start,Timestamp end,List<Map<String,Object>> headList,List<Map<String,Object>> dataList,HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet(sheetName);   // 创建工作表

        //表格标题
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setBorderBottom((short)1);
        titleStyle.setBorderTop((short)1);
        titleStyle.setBorderLeft((short)1);
        titleStyle.setBorderRight((short)1);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell cellTitle = titleRow.createCell(0);
        cellTitle.setCellStyle(titleStyle);
        cellTitle.setCellType(HSSFCell.CELL_TYPE_STRING);
        HSSFRichTextString textTitle = new HSSFRichTextString(sheetName);
        cellTitle.setCellValue(textTitle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headList.size()-1));
        for(int i = 1;i < headList.size(); i++){
            cellTitle = titleRow.createCell(i);
            cellTitle.setCellStyle(titleStyle);
        }

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom((short)1);
        cellStyle.setBorderTop((short)1);
        cellStyle.setBorderLeft((short)1);
        cellStyle.setBorderRight((short)1);


        //表格列宽
        Integer[] rowsWidth = new Integer[headList.size()];
        //表头
        HSSFRow headRow = sheet.createRow(2);
        for(int headIndex = 0;headIndex < headList.size();headIndex ++){
            Map<String,Object> headName = headList.get(headIndex);
            HSSFCell cellHead = headRow.createCell(headIndex);
            HSSFCellStyle headStyle = workbook.createCellStyle();
            headStyle.setBorderBottom((short)1);
            headStyle.setBorderTop((short)1);
            headStyle.setBorderLeft((short)1);
            headStyle.setBorderRight((short)1);
            cellHead.setCellStyle(headStyle);
            cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
            HSSFRichTextString text = new HSSFRichTextString(headName.get("name").toString());
            cellHead.setCellValue(text);
            rowsWidth[headIndex] = text.getString().getBytes().length;
        }


        Integer cellSize = headList.size();
        try{
            for(int rowIndex = 0;rowIndex < dataList.size();rowIndex ++){
                HSSFRow row = sheet.createRow(rowIndex+3);
                Map<String,Object> data = (Map<String,Object>)dataList.get(rowIndex);
                for (int cellIndex = 0;cellIndex < cellSize; cellIndex ++){
                    HSSFCell cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //HSSFRichTextString text = new HSSFRichTextString(String.valueOf(data.get(headList.get(cellIndex).get("key").toString())));
                    HSSFRichTextString text = new HSSFRichTextString();
                    if (CommonUtils.isEmpty(data.get(headList.get(cellIndex).get("key").toString()))){
                        text = new HSSFRichTextString("");
                    }else {
                        text = new HSSFRichTextString(String.valueOf(data.get(headList.get(cellIndex).get("key").toString())));
                    }
                    cell.setCellValue(text);
                    if(text.getString().getBytes().length > rowsWidth[cellIndex]){
                        rowsWidth[cellIndex] = text.getString().getBytes().length;
                    }
                }
            }

        }catch (Exception e) {
            System.out.print("--------"+e);
        }
        Integer totalWidth = 0;
        for(int i = 0;i < headList.size(); i++){
            sheet.setColumnWidth(i,rowsWidth[i] * 256 + 1024);
            totalWidth = totalWidth + rowsWidth[i] + 4;
        }
        HSSFRow secRow = sheet.createRow(1);
        HSSFCell secCell = secRow.createCell(0);
        secCell.setCellType(HSSFCell.CELL_TYPE_STRING);
        String leftString = "编制人员：";
        String rightString = "报表统计周期:：" + CommonUtils.formatTimeStamp("yyyy-MM-dd",start) + " - " + CommonUtils.formatTimeStamp("yyyy-MM-dd",end);
        totalWidth = totalWidth - leftString.getBytes().length - rightString.getBytes().length + headList.size()/2 + headList.size() + 1;
        byte[] bytes = new byte[totalWidth];
        String nullString = new String(bytes);
        HSSFRichTextString secText = new HSSFRichTextString(leftString + nullString + rightString);
        secCell.setCellValue(secText);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headList.size()-1));
        secCell.setCellStyle(cellStyle);
        for(int i = 1;i < headList.size(); i++){
            secCell = secRow.createCell(i);
            secCell.setCellStyle(cellStyle);
        }



        if(workbook !=null){
            try
            {
                String fileName = "Excel-" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void ListMapOutPut(String sheetName,List<Map<String,Object>> headList,List<Map<String,Object>> dataList,HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet(sheetName);   // 创建工作表

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom((short)1);
        cellStyle.setBorderTop((short)1);
        cellStyle.setBorderLeft((short)1);
        cellStyle.setBorderRight((short)1);

        //表格列宽
        Integer[] rowsWidth = new Integer[headList.size()];
        //表头
        HSSFRow headRow = sheet.createRow(0);
        for(int headIndex = 0;headIndex < headList.size();headIndex ++){
            Map<String,Object> headName = headList.get(headIndex);
            HSSFCell cellHead = headRow.createCell(headIndex);
            HSSFCellStyle headStyle = workbook.createCellStyle();
            headStyle.setBorderBottom((short)1);
            headStyle.setBorderTop((short)1);
            headStyle.setBorderLeft((short)1);
            headStyle.setBorderRight((short)1);
            cellHead.setCellStyle(headStyle);
            cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
            HSSFRichTextString text = new HSSFRichTextString(headName.get("name").toString());
            cellHead.setCellValue(text);
            rowsWidth[headIndex] = text.getString().getBytes().length;
        }


        Integer cellSize = headList.size();
        for(int rowIndex = 0;rowIndex < dataList.size();rowIndex ++){
            HSSFRow row = sheet.createRow(rowIndex+1);
            Map<String,Object> data = (Map<String,Object>)dataList.get(rowIndex);
            for (int cellIndex = 0;cellIndex < cellSize; cellIndex ++){
                HSSFCell cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                HSSFRichTextString text;
                if(CommonUtils.isEmpty(data.get(headList.get(cellIndex).get("key").toString()))){
                    text = new HSSFRichTextString("");
                }else{
                    text = new HSSFRichTextString(data.get(headList.get(cellIndex).get("key").toString()).toString());
                }

                cell.setCellValue(text);
                if(text.getString().getBytes().length > rowsWidth[cellIndex]){
                    rowsWidth[cellIndex] = text.getString().getBytes().length;
                }
            }
        }
        for(int i = 0;i < headList.size(); i++){
            sheet.setColumnWidth(i,rowsWidth[i] * 256 + 1024);
        }
        if(workbook !=null){
            try
            {
                String fileName = "Excel-" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static List<Map<String,Object>> readExcelToListOrgMap(String filePath){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        boolean isXlsx = false;
        if(filePath.endsWith("xlsx"))
            isXlsx = true;
        try{
            InputStream input = new FileInputStream(filePath);
            Workbook workbook  = null;
            if(isXlsx){
                workbook = new XSSFWorkbook(input);
            }else{
                workbook = new HSSFWorkbook(input);
            }
            Sheet sheet = workbook.getSheetAt(0);     //获得第一个表单
            int totalRow = sheet.getLastRowNum();
            for(int rowIndex = 0; rowIndex <= totalRow;rowIndex ++){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                Row row = sheet.getRow(rowIndex);
                int totalCell = row.getLastCellNum();
                for(int cellIndex = 0;cellIndex < totalCell;cellIndex ++){
                    Cell cell = row.getCell(cellIndex);
                    rowMap.put("value" + (cellIndex + 1),getStringCellValue(cell));
                }
                list.add(rowMap);
            }
        }catch (IOException ex){
            LOGGER.error("导入文件读取失败：文件路径为--"+filePath);
            return null;
        }
        return list;
    }

   public static String excelDateTodate(int exceldate){
       Calendar c = new GregorianCalendar(1900,0,-1);
       Date d = c.getTime();
       Date _d = org.apache.commons.lang.time.DateUtils.addDays(d, exceldate);  //42605是距离1900年1月1日的天数
       SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd");

       return  myFmt2.format(_d);
   }
}
