package com.diy.excel;

/**
 * 企业分类演示类
 * 展示分类逻辑的工作原理
 */
public class ClassificationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 企业分类演示 ===");
        
        // 测试数据
        String[] testEnterprises = {
            "郑州高新技术产业开发区长椿路",
            "黄淮市场西区", 
            "郸城县白马镇电商产业园D栋第五排",
            "驻马店市驿城区贸易广场",
            "龙城镇仲李村",
            "郑州航空港区沃金商业广场",
            "漯河市召陵区东城办东兴电子产业园区"
        };
        
        EnterpriseClassificationProcessor processor = new EnterpriseClassificationProcessor();
        
        for (String enterprise : testEnterprises) {
            String category = classifyEnterprise(enterprise);
            System.out.println("企业: " + enterprise);
            System.out.println("分类: " + (category.isEmpty() ? "未分类" : category + "类"));
            System.out.println("---");
        }
    }
    
    private static String classifyEnterprise(String enterpriseName) {
        // A类关键词
        String[] categoryAKeywords = {"产业园", "园区", "软件园", "科技园", "开发区", "产业基地", "孵化", "科创", "工业园", "物流园"};
        // B类关键词  
        String[] categoryBKeywords = {"批发", "市场", "商贸城", "广场"};
        
        // 检查A类
        for (String keyword : categoryAKeywords) {
            if (enterpriseName.contains(keyword)) {
                return "A";
            }
        }
        
        // 检查B类
        for (String keyword : categoryBKeywords) {
            if (enterpriseName.contains(keyword)) {
                return "B";
            }
        }
        
        return "";
    }
}
