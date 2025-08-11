package com.nova.audio;

import com.alibaba.fastjson2.JSONObject;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Sample
{
    
    public static void main(String[] args) throws IOException {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性
            // 以下代码示例仅供参考，建议采用更安全的方式来使用密钥
            // 请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            //Credential cred = new Credential(System.getenv("TENCENTCLOUD_SECRET_ID"), System.getenv("TENCENTCLOUD_SECRET_KEY"));
            Credential cred = new Credential("","");



            // 使用临时密钥示例
            // Credential cred = new Credential("SecretId", "SecretKey", "Token");
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("asr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("asr", "2019-06-14", cred, "", clientProfile);
            String params = "{}";

            File file = new File("smart_tools_demo\\src\\resources\\李豫1.wav");
            byte[] audioBytes = new byte[(int) file.length()];

            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(audioBytes);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("Data", Base64.getEncoder().encodeToString(audioBytes));
            map.put("SampleRate", 16000);
            map.put("VoiceFormat", 1);
/*            String resp = client.call("VoicePrintEnroll", JSONObject.toJSONString(map));
            System.out.println(resp);*/



            File file2 = new File("smart_tools_demo\\src\\resources\\李豫2.wav");
            byte[] audioBytes2 = new byte[(int) file2.length()];

            try (FileInputStream fis = new FileInputStream(file2)) {
                fis.read(audioBytes2);
            }

            Map<String, Object> map2 = new HashMap<>();
            map2.put("Data", Base64.getEncoder().encodeToString(audioBytes2));
            map2.put("SampleRate", 16000);
            map2.put("VoiceFormat", 1);
            map2.put("VoicePrintId", "70b66402-8cf8-4a03-a9b7-362db792e3c1");
            String resp2 = client.call("VoicePrintVerify", JSONObject.toJSONString(map2));
            System.out.println(resp2);





        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}