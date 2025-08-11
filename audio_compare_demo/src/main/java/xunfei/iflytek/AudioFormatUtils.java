package xunfei.iflytek;

import java.io.*;

public class AudioFormatUtils {
    /**
     * 使用本地 ffmpeg 将 wav 字节流转为 mp3 字节流
     * @param wavBytes wav格式音频数据
     * @return mp3格式音频数据
     */
    public static byte[] wavToMp3(byte[] wavBytes) {
        File tempWav = null;
        File tempMp3 = null;
        try {
            // 1. 写入临时wav文件
            tempWav = File.createTempFile("audio_temp", ".wav");
            try (FileOutputStream fos = new FileOutputStream(tempWav)) {
                fos.write(wavBytes);
            }
            // 2. 创建临时mp3文件
            tempMp3 = File.createTempFile("audio_temp", ".mp3");

            // 3. 构造ffmpeg命令
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-y", // 覆盖输出
                    "-i", tempWav.getAbsolutePath(),
                    "-ar", "16000", // 设置采样率为 16kHz
                    "-ac", "1",     // 设置通道数为单声道
                    "-codec:a", "libmp3lame",
                    "-b:a", "128k",
                    tempMp3.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 4. 读取ffmpeg输出（防止阻塞）
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {}
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg 转码失败，exit code: " + exitCode);
            }

            // 5. 读取mp3文件为byte[]
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 FileInputStream fis = new FileInputStream(tempMp3)) {
                byte[] buffer = new byte[4096];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return baos.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("音频格式转换失败", e);
        } finally {
            if (tempWav != null && tempWav.exists()) tempWav.delete();
            if (tempMp3 != null && tempMp3.exists()) tempMp3.delete();
        }
    }

    // 下面是你的main和工具方法，无需变动
    public static void main(String[] args) throws IOException {
        wavToMp3(read("D:\\diyProject\\java-advanced-skills\\audio_compare_demo\\src\\main\\resources\\files\\儿童1.wav"));
    }
    public static byte[] read(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();
        return data;
    }
    private static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
} 