package com.diy.dagon.webasr;

import com.nova.DoubaoASR;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class UploadController {

    @GetMapping("/")
    public String index() {
        return "redirect:/index.html";
    }

    @PostMapping(value = "/asr/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> handleUpload(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择一个 WAV 文件上传");
        }
        String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename() == null ? "audio.wav" : multipartFile.getOriginalFilename());
        if (!originalFilename.toLowerCase().endsWith(".wav")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("仅支持 WAV 文件");
        }

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", ".wav");
            try (InputStream in = multipartFile.getInputStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            String jobId = DoubaoASR.submitNew(tempFile.toFile());
            DoubaoASR.query(jobId); // 该方法会将识别结果写到项目根目录的 audio2txt.txt

            File resultFile = new File("audio2txt.txt");
            if (!resultFile.exists()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("识别结果文件未生成");
            }

            String downloadName = "transcript-" + UUID.randomUUID().toString().substring(0, 8) + ".txt";
            InputStreamResource resource = new InputStreamResource(new FileInputStream(resultFile));
            HttpHeaders headers = new HttpHeaders();
            String encodedName = URLEncoder.encode(downloadName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            String contentDisposition = "attachment; filename=\"" + downloadName + "\"; filename*=UTF-8''" + encodedName;
            headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("识别失败: " + e.getMessage());
        } finally {
            if (tempFile != null) {
                try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
            }
        }
    }
}