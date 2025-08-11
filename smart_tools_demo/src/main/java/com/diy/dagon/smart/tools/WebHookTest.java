package com.diy.dagon.smart.tools;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WebHookTest {

    private static String sign(long timestamp, String secret) throws Exception {
       /* long timestamp = System.currentTimeMillis();
        //已设置关键字"test"
        String secret = "this is a security!(test)";
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.getUrlEncoder().encode(signData)), "UTF-8");
        System.out.println("生成报文内容为：" + sign);
        System.out.println("绑定时间戳为：" + timestamp);*/



        //https://im.360teams.com/api/qfin-api/rce-app/robot/send?access_token=e59595ef4c224ab0ad732b2df18973c2382f87642f62432eadd374461b1c38ac







        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return URLEncoder.encode(new String(Base64.getUrlEncoder().encode(signData)), "UTF-8");
    }

    private static Map<String, Object> getTextBody() {
        Map<String, Object> map = new HashMap<>();
        // 消息ID（可选）
        map.put("msgId", System.currentTimeMillis());
        // 消息类型，目前支持 text
        map.put("msgtype", "text");
        // 消息体
        Map<String, String> text = new HashMap<>();
        // 预警消息正文，支持换行符 \n
        text.put("content", "this is a test message for you !");
        map.put("text", text);
        // At用户
        Map<String, Object> at = new HashMap<>();
        // 是否At所有人，若为true，则at预警群内所有人；
        at.put("isAtAll", false);
        // at用户的域账号列表
        at.put("userIds", Arrays.asList("zengguangming-jk"));
        map.put("at", at);
        return map;
    }

    private static Map<String, Object> getMarkdownBody() {
        Map<String, Object> map = new HashMap<>();
        // 消息ID（可选）
        map.put("msgId", System.currentTimeMillis());
        // 消息类型，目前支持 markdown
        map.put("msgtype", "markdown");
        // 消息体
        /**
         * 设置了关键字：test
         */
        Map<String, String> markdown = new HashMap<>();

        //pro



        // 预警消息标题
        markdown.put("title", "服务器硬盘存储达到设定阈值[test]");
        // 预警消息内容
        markdown.put("text", "# 这是支持markdown的文本   \n   ## 标题2    \n   * 列表1   \n  ![alt 啊](https://p5.360teams.com/t0101a93d405e905c6c.jpg)[测试]");
        map.put("markdown", markdown);
        return map;
    }

    public static void main(String[] args) throws Exception {
        // 请注意：这里的webhook请替换为已创建的群机器人WebHook地址
        String webhook =
                "https://im.360teams.com/api/qfin-api/rce-app/robot/send?access_token=e59595ef4c224ab0ad732b2df18973c2382f87642f62432eadd374461b1c38ac";
        // 启用加签校验（若不启用加签校验，则跳过加签过程） start
        long timestamp = System.currentTimeMillis();
        String secret = "0d2359bd58c84bb1b8d597d0f6bb37be0ec2f0dd995d4592a09dcc1c8fd";
        String sign = sign(timestamp, secret);
        webhook += "&timestamp=" + timestamp + "&sign=" + sign;
        // 启用加签校验 end

        // 构造请求消息体
        Map<String, Object> map = getMarkdownBody();
        // 构造POST请求
        HttpPost post = new HttpPost(webhook);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(JSONObject.toJSONString(map), "UTF-8"));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse httpResponse = httpClient.execute(post)) {
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}