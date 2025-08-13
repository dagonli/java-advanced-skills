package com.nova;


import com.alibaba.fastjson2.JSON;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class DoubaoASR {
    static final String SUBMIT_URL = "https://openspeech.bytedance.com/api/v1/vc/submit?appid=%s&language=%s&caption_type=speech&use_itn=True&use_capitalize=True&max_lines=1&words_per_line=15&with_speaker_info=True";
    static final String QUERY_URL = "https://openspeech.bytedance.com/api/v1/vc/query?appid=%s&id=%s";
    static final String LANGUAGE = "zh-CN";
    static final String APPID = "4349306823";
    static final String TOKEN = "-y8u7PljiVts-kOn47Pg_8czZGt4yoLs";

    public static void main(String[] args) throws Exception {

        //换成本地需要解析的音频
        File file = new File("C:\\Users\\liyu-jk\\Desktop\\线下AI获客助手\\第二期\\对话（客户经理二）.wav");

        String job_id = submitNew(file);
        query(job_id);
    }

    public static void query(String job_id) throws IOException {
        OkHttpClient client = createClientTrustAll();

        String url = String.format(QUERY_URL, APPID, job_id);
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", String.format("Bearer; %s", TOKEN))
                .addHeader("Connection", "keep-alive")
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException(response.toString());
        }
        String resultStr = response.body().string();
        System.err.println(resultStr);


        // 保存转换结果
        AudioFileRecognitionResult result = JSON.parseObject(resultStr, AudioFileRecognitionResult.class);
        String transcriptBatch = new Random().nextInt(1000000) + "";
        List<AudioTranscriptCreateDTO> createDTOList = result.getUtterances()
                .stream().map(u -> assembleToCreateDTO(transcriptBatch,  u))
                .collect(Collectors.toList());
        List<String> list = new ArrayList<>(2048);
        for (AudioTranscriptCreateDTO createDTO: createDTOList) {
            list.add("用户_"+createDTO.getSpeaker() + ":" + createDTO.getContent());
        }



        String outputFilePath = "audio2txt.txt";
        writeTranscriptsToFile(list, outputFilePath);
        System.err.println("over");
    }


    public static void writeTranscriptsToFile(List<String> list, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String str : list) {
                writer.write(str);  // 确保你的 DTO 类重写了 toString()
                writer.newLine();
            }
            System.out.println("✅ 写入成功！文件路径：" + filePath);
        } catch (IOException e) {
            System.err.println("❌ 写入失败: " + e.getMessage());
        }
    }


    private static AudioTranscriptCreateDTO assembleToCreateDTO(String transcriptBatch,
                                                         AudioFileRecognitionResult.Utterance utterance) {
        AudioTranscriptCreateDTO createDTO = new AudioTranscriptCreateDTO();
        createDTO.setTranscriptNo(new Random().nextInt(99999090) + "");
        createDTO.setTranscriptBatch(transcriptBatch);
        createDTO.setSessionId("SESSION001");
        createDTO.setStaffNo("SN001");
        createDTO.setStartTime(utterance.getStart_time());
        createDTO.setEndTime(utterance.getEnd_time());
        createDTO.setContent(utterance.getText());
        createDTO.setWords(JSON.toJSONString(utterance.getWords()));
        createDTO.setSpeaker(Optional.ofNullable(utterance.getAttribute())
                .map(AudioFileRecognitionResult.Utterance.Attribute::getSpeaker).orElse(""));
        createDTO.setConfidence(BigDecimal.ZERO);
        return createDTO;
    }


    public static String submitNew(File audioFile) {
        try {
            // 构建请求体（直接以文件作为二进制流上传）
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("audio/wav"),
                    audioFile
            );
            Request request = new Request.Builder()
                    .url(String.format(SUBMIT_URL, APPID, LANGUAGE))
                    .method("POST", requestBody)
                    .addHeader("Accept", "*/*")
                    .addHeader("Authorization", String.format("Bearer; %s", TOKEN))
                    .addHeader("Connection", "keep-alive")
                    .addHeader("content-type", "audio/wav")
                    .build();
            Map<String, Object> resp = null;


            OkHttpClient client = createClientTrustAll();


            try (Response response = client.newCall(request).execute()) {
                if (response.body() != null) {
                    try (InputStream inputStream = response.body().byteStream()) {
                        resp = JSON.parseObject(inputStream, HashMap.class);
                    }
                }
            }
            if (resp == null || (int) resp.get("code") != 0) {
                System.err.println("录音文件识别提交结果返回失败, url={} resp={}"+audioFile.getName()+""+resp);
                throw new IllegalArgumentException("录音文件识别提交结果返回失败 audioFile=" + audioFile.getName());
            }
            System.err.println("录音文件识别提交结果 result={}"+ resp);
            return resp.get("id").toString();
        } catch (IOException e) {
            System.err.println("录音文件识别提交异常"+ e);
            throw new RuntimeException(e);
        }
    }

   public static OkHttpClient createClientTrustAll() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String submit() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("audio/wav");
        RequestBody body = RequestBody.create(mediaType, "{\"url\": \"https://www.iflyrec.com/AudioStreamService/v1/outLinks/2a7e5980004145ea8a01fb73e88b4ddd?action=play\"}");
        Request request = new Request.Builder()
                .url(String.format(SUBMIT_URL, APPID, LANGUAGE))
                .method("POST", body)
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", String.format("Bearer; %s", TOKEN))
                .addHeader("Connection", "keep-alive")
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        Map resp = JSON.parseObject(response.body().byteStream(), HashMap.class);
        System.out.println(resp.toString());
        return resp.get("id").toString();
    }
}