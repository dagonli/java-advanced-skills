# 企业分类处理工具

## 功能说明

这个工具用于解析CSV文件中的企业数据，根据企业名称中的关键词进行分类，并生成包含分类结果的Excel文件。

## 分类规则

### A类企业
企业名中含有以下关键词之一的企业归为A类：
- 产业园
- 园区
- 软件园
- 科技园
- 开发区
- 产业基地
- 孵化
- 科创
- 工业园
- 物流园

### B类企业
企业名中含有以下关键词之一的企业归为B类：
- 批发
- 市场
- 商贸城
- 广场

## 使用方法

### 1. 直接运行测试类
```bash
java com.diy.excel.EnterpriseClassificationTest
```

### 2. 在代码中使用
```java
EnterpriseClassificationProcessor processor = new EnterpriseClassificationProcessor();
processor.processCsvToExcel("输入文件.csv", "输出文件.xlsx");
```

## 输入文件格式

CSV文件应包含以下列：
- 第一列：企业名称/地址
- 第二列：数量(count)

示例：
```csv
location_prefix,count
龙城镇仲李村,596
黄淮市场西区,193
郑州高新技术产业开发区长椿路,663
```

## 输出文件格式

生成的Excel文件包含以下列：
- 企业名：原始的企业名称
- count：原始的数量
- 类别：A、B或空（未匹配任何分类规则）

## 依赖

- Apache POI 5.2.3 (用于Excel文件处理)
- Java 8+

## 注意事项

1. 确保CSV文件使用UTF-8编码
2. 程序会自动跳过CSV文件的标题行
3. 如果企业名称不匹配任何分类规则，类别字段将为空
4. 生成的Excel文件会自动调整列宽以便查看
