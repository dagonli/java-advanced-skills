//package com.nova.audio;
//import com.tencentcloudapi.common.AbstractModel;
//
//import com.tencentcloudapi.common.Credential;
//import com.tencentcloudapi.common.profile.ClientProfile;
//import com.tencentcloudapi.common.profile.HttpProfile;
//import com.tencentcloudapi.common.exception.TencentCloudSDKException;
//import com.tencentcloudapi.asr.v20190614.AsrClient;
//import com.tencentcloudapi.asr.v20190614.models.*;
//
//public class Sample1
//{
//    public static void main(String [] args) {
//        try{
//            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
//            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性
//            // 以下代码示例仅供参考，建议采用更安全的方式来使用密钥
//            // 请参见：https://cloud.tencent.com/document/product/1278/85305
//            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
//            Credential cred = new Credential(System.getenv("TENCENTCLOUD_SECRET_ID"), System.getenv("TENCENTCLOUD_SECRET_KEY"));
//            // 使用临时密钥示例
//            // Credential cred = new Credential("SecretId", "SecretKey", "Token");
//            // 实例化一个http选项，可选的，没有特殊需求可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("asr.tencentcloudapi.com");
//            // 实例化一个client选项，可选的，没有特殊需求可以跳过
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//            // 实例化要请求产品的client对象,clientProfile是可选的
//            AsrClient client = new AsrClient(cred, "", clientProfile);
//            // 实例化一个请求对象,每个接口都会对应一个request对象
//            VoicePrintEnrollRequest req = new VoicePrintEnrollRequest();
//
//            // 返回的resp是一个VoicePrintEnrollResponse的实例，与请求对象对应
//            VoicePrintEnrollResponse resp = client.VoicePrintEnroll(req);
//            // 输出json格式的字符串回包
//            System.out.println(AbstractModel.toJsonString(resp));
//        } catch (TencentCloudSDKException e) {
//            System.out.println(e.toString());
//        }
//    }
//}