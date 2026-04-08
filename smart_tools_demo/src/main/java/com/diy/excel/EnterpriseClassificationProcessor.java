package com.diy.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 企业分类处理类
 * 用于解析CSV文件并根据企业名称进行分类，生成新的Excel文件
 */
public class EnterpriseClassificationProcessor {
    
    // A类关键词：产业园、园区、软件园、科技园、开发区、产业基地、孵化、科创、工业园、物流园
    private static final Set<String> CATEGORY_A_KEYWORDS = new HashSet<>(Arrays.asList(
        "产业园", "园区", "软件园", "科技园", "开发区", "产业基地", "孵化", "科创", "工业园", "物流园"
    ));
    
    // B类关键词：批发、市场、商贸城、广场
    private static final Set<String> CATEGORY_B_KEYWORDS = new HashSet<>(Arrays.asList(
        "批发", "市场", "商贸城", "广场"
    ));
    
    /**
     * 处理CSV文件并生成分类后的Excel文件
     * @param csvFilePath CSV文件路径
     * @param outputExcelPath 输出Excel文件路径
     */
    public void processCsvToExcel(String csvFilePath, String outputExcelPath) {
        try {
            // 读取CSV文件
            List<EnterpriseData> enterpriseList = readCsvFile(csvFilePath);
            
            // 分类处理
            List<EnterpriseData> classifiedList = classifyEnterprises(enterpriseList);
            
            // 生成Excel文件
            generateExcelFile(classifiedList, outputExcelPath);
            
            System.out.println("处理完成！共处理 " + classifiedList.size() + " 条记录");
            System.out.println("输出文件：" + outputExcelPath);
            
        } catch (Exception e) {
            System.err.println("处理过程中发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 读取CSV文件
     */
    private List<EnterpriseData> readCsvFile(String csvFilePath) throws IOException {
        List<EnterpriseData> enterpriseList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // 跳过标题行
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // 解析CSV行
                String[] fields = parseCsvLine(line);
                if (fields.length >= 2) {
                    String enterpriseName = fields[0].trim();
                    String countStr = fields[1].trim();
                    
                    try {
                        int count = Integer.parseInt(countStr);
                        enterpriseList.add(new EnterpriseData(enterpriseName, count));
                    } catch (NumberFormatException e) {
                        System.err.println("无法解析数量字段：" + countStr + "，跳过该行");
                    }
                }
            }
        }
        
        return enterpriseList;
    }
    
    /**
     * 解析CSV行，处理可能包含逗号的字段
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
    
    /**
     * 对企业进行分类
     */
    private List<EnterpriseData> classifyEnterprises(List<EnterpriseData> enterpriseList) {
        List<EnterpriseData> classifiedList = new ArrayList<>();
        
        for (EnterpriseData enterprise : enterpriseList) {
            String category = classifyEnterprise(enterprise.getEnterpriseName());
            enterprise.setCategory(category);
            classifiedList.add(enterprise);
        }
        
        return classifiedList;
    }
    
    /**
     * 根据企业名称进行分类
     */
    private String classifyEnterprise(String enterpriseName) {
        // 检查A类关键词
        for (String keyword : CATEGORY_A_KEYWORDS) {
            if (enterpriseName.contains(keyword)) {
                return "A";
            }
        }
        
        // 检查B类关键词
        for (String keyword : CATEGORY_B_KEYWORDS) {
            if (enterpriseName.contains(keyword)) {
                return "B";
            }
        }
        
        // 如果都不匹配，返回空字符串或"其他"
        return "";
    }
    
    /**
     * 生成Excel文件
     */
    private void generateExcelFile(List<EnterpriseData> enterpriseList, String outputPath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("企业分类结果");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"企业名", "count", "类别"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowNum = 1;
            for (EnterpriseData enterprise : enterpriseList) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(enterprise.getEnterpriseName());
                row.createCell(1).setCellValue(enterprise.getCount());
                row.createCell(2).setCellValue(enterprise.getCategory());
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
            }
        }
    }
    
    /**
     * 企业数据实体类
     */
    public static class EnterpriseData {
        private String enterpriseName;
        private int count;
        private String category;
        
        public EnterpriseData(String enterpriseName, int count) {
            this.enterpriseName = enterpriseName;
            this.count = count;
        }
        
        // Getters and Setters
        public String getEnterpriseName() {
            return enterpriseName;
        }
        
        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        EnterpriseClassificationProcessor processor = new EnterpriseClassificationProcessor();
        
        // 设置文件路径
        String csvFilePath = "src/main/resources/企业群结果筛选.csv";
        String outputExcelPath = "企业分类结果.xlsx";
        
        // 处理文件
        processor.processCsvToExcel(csvFilePath, outputExcelPath);
    }
}
