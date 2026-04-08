package com.diy.excel;

/**
 * 企业分类处理测试类
 */
public class EnterpriseClassificationTest {
    
    public static void main(String[] args) {
        System.out.println("开始处理企业分类...");
        
        EnterpriseClassificationProcessor processor = new EnterpriseClassificationProcessor();

        // 设置文件路径
        String csvFilePath = "D:\\diyProject\\java-advanced-skills\\smart_tools_demo\\src\\resources\\企业群结果筛选30.csv";
        String outputExcelPath = "D:\\diyProject\\java-advanced-skills\\smart_tools_demo\\src\\resources\\企业分类结果30.xlsx";
        
        try {
            // 处理文件
            processor.processCsvToExcel(csvFilePath, outputExcelPath);
            
            System.out.println("处理完成！");
            System.out.println("请查看生成的文件：" + outputExcelPath);
            
        } catch (Exception e) {
            System.err.println("处理失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
