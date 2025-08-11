package tecent;


import com.alibaba.fastjson2.JSONObject;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AudioBatchComparator {
    private static final String AUDIO_DIR = "audio_compare_demo/src/main/resources/files";
    private static final String OUTPUT_EXCEL = getOutputExcelName();

    private static Map<String, String> cacheSpeakIdMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        Credential cred = new Credential("","");



        // 使用临时密钥示例
        // Credential cred = new Credential("SecretId", "SecretKey", "Token");
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("asr.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        CommonClient client = new CommonClient("asr", "2019-06-14", cred, "", clientProfile);





        List<File> audioFiles = listAudioFiles(AUDIO_DIR);
        List<Object[]> results = new ArrayList<>();
        int index = 1;
        for (int i = 0; i < audioFiles.size(); i++) {
            for (int j = i + 1; j < audioFiles.size(); j++) {
                File file1 = audioFiles.get(i);
                File file2 = audioFiles.get(j);
                double score = 0.0;
                String status = "成功";
                String errorMsg = "";
                try {
                    score = compareAudio(file1, file2, client); // 伪方法
                } catch (Exception e) {
                    status = "失败";
                    errorMsg = e.getMessage();
                }
                String percent = String.format("%.2f%%", score);
                String desc = "";
                if (score >= 90) {
                     desc = "极高相似度 - 很可能是同一个人的声音";
                } else if (score >= 80) {
                    desc = "高相似度 - 可能是同一个人的声音";
                } else if (score >= 70) {
                    desc = "中等相似度 - 可能是相似的声音特征";
                } else if (score >= 50) {
                    desc = "低相似度 - 声音特征有一定差异";
                } else {
                    desc = "极低相似度 - 声音特征差异很大";
                }

                // 逻辑分析
                String name1 = getNameWithoutNumberAndExt(file1.getName());
                String name2 = getNameWithoutNumberAndExt(file2.getName());
                String logicAnalysis = "";
                if (!name1.equals(name2) && score >= 70) {
                    logicAnalysis = "不同人相似度高";
                } else if (name1.equals(name2) && score < 70) {
                    logicAnalysis = "同个人相似度低";
                }

                results.add(new Object[]{
                        index++,
                        file1.getName(),
                        file2.getName(),
                        file1.getPath(),
                        file2.getPath(),
                        score,
                        percent,
                        desc,
                        status,
                        errorMsg,
                        logicAnalysis
                });
            }
        }
        writeExcel(results);
        System.out.println("比对完成，结果已输出到：" + OUTPUT_EXCEL);
        System.err.println("所有注册信息备份：" + JSONObject.toJSONString(cacheSpeakIdMap));
    }

    private static List<File> listAudioFiles(String dir) {
        File folder = new File(dir);
        File[] files = folder.listFiles((d, name) -> name.endsWith(".wav"));
        List<File> list = new ArrayList<>();
        if (files != null) {
            for (File f : files) list.add(f);
        }
        return list;
    }

    // 伪方法：实际应调用音频比对算法
    private static double compareAudio(File f1, File f2, CommonClient client) throws IOException, TencentCloudSDKException {
//        if (1 == 1) {
//            // TODO: 替换为实际比对逻辑
//            return Math.random() * 0.5 + 0.5; // 随机返回0.5~1.0之间的分数
//
//        }



        String fileName1 = f1.getName();
        String voicePrintId1 = "";

        if (cacheSpeakIdMap.get(fileName1) == null) {
            byte[] audioBytes = new byte[(int) f1.length()];

            try (FileInputStream fis = new FileInputStream(f1)) {
                fis.read(audioBytes);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("Data", Base64.getEncoder().encodeToString(audioBytes));
            map.put("SampleRate", 16000);
            map.put("VoiceFormat", 1);
            map.put("SpeakerNick", fileName1.substring(0, fileName1.indexOf(".")));
            String resp = client.call("VoicePrintEnroll", JSONObject.toJSONString(map));
            System.err.println("该声纹未注册过，注册成功返回：" + resp);

            JSONObject jsonObject = JSONObject.parseObject(resp);
            voicePrintId1 = jsonObject
                    .getJSONObject("Response")
                    .getJSONObject("Data")
                    .getString("VoicePrintId");
            cacheSpeakIdMap.put(fileName1, voicePrintId1);

        } else {
            voicePrintId1 = cacheSpeakIdMap.get(fileName1);
            System.err.println("该声纹已注册过，直接拿缓存：" + voicePrintId1);
        }



        byte[] audioBytes2 = new byte[(int) f2.length()];

        try (FileInputStream fis = new FileInputStream(f2)) {
            fis.read(audioBytes2);
        }

        Map<String, Object> map2 = new HashMap<>();
        map2.put("Data", Base64.getEncoder().encodeToString(audioBytes2));
        map2.put("SampleRate", 16000);
        map2.put("VoiceFormat", 1);
        map2.put("VoicePrintId", voicePrintId1);
        String resp2 = client.call("VoicePrintVerify", com.alibaba.fastjson2.JSONObject.toJSONString(map2));
        System.err.println("对比成功，服务返回："+resp2);


        JSONObject jsonObject = JSONObject.parseObject(resp2);
        Double score = jsonObject
                .getJSONObject("Response")
                .getJSONObject("Data")
                .getDouble("Score");

        return score;
    }

    private static void writeExcel(List<Object[]> data) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("比对结果");
        String[] headers = {"序号", "文件1", "文件2", "文件1路径", "文件2路径", "相似度分数", "相似度百分比", "相似度描述", "状态", "错误信息", "逻辑分析"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Object[] rowData = data.get(i);
            for (int j = 0; j < rowData.length; j++) {
                if (rowData[j] instanceof Number) {
                    row.createCell(j).setCellValue(((Number) rowData[j]).doubleValue());
                } else {
                    row.createCell(j).setCellValue(rowData[j] == null ? "" : rowData[j].toString());
                }
            }
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try (FileOutputStream fos = new FileOutputStream(OUTPUT_EXCEL)) {
            wb.write(fos);
        }
        wb.close();
    }

    // 工具方法：提取姓名（去除结尾数字和扩展名）
    private static String getNameWithoutNumberAndExt(String filename) {
        String name = filename;
        int dotIdx = name.lastIndexOf('.');
        if (dotIdx > 0) {
            name = name.substring(0, dotIdx);
        }
        return name.replaceAll("\\d+$", "");
    }

    private static String getOutputExcelName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String datetime = sdf.format(new Date());
        return "audio_compare_demo/src/main/resources/audio_compare_result_" + datetime + ".xlsx";
    }
} 