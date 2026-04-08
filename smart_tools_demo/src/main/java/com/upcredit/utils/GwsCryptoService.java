package com.upcredit.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

public class GwsCryptoService {

    private static final String AES_ALGO = "AES/GCM/NoPadding";
    private static final String RSA_ALGO = "RSA/ECB/PKCS1Padding";
    private static final String SIGN_ALGO = "SHA256withRSA";
    private static final int GCM_TAG_LENGTH = 128; // 认证标签长度
    private static final int GCM_IV_LENGTH = 12;  // GCM推荐IV长度

    /**
     * 构建加密请求 (出口网关处理流程)
     */
    public CryptoRequest buildEncryptedRequest(String appId, String bizJson, PrivateKey selfPrivateKey, PublicKey peerPublicKey) throws Exception {

        // 1. 生成随机 AES-256 密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey aesKey = keyGen.generateKey();

        // 2. 生成随机 12 字节 IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        // 3. 使用 AES-256-GCM 加密业务数据
        Cipher aesCipher = Cipher.getInstance(AES_ALGO);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        byte[] encryptedBizData = aesCipher.doFinal(bizJson.getBytes("UTF-8"));

        // 4. 使用对方 RSA 公钥加密 AES 密钥
        Cipher rsaCipher = Cipher.getInstance(RSA_ALGO);
        rsaCipher.init(Cipher.ENCRYPT_MODE, peerPublicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // 5. 准备基础字段
        CryptoRequest request = new CryptoRequest();
        request.setAppId(appId);
        request.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        request.setNonce(UUID.randomUUID().toString().replace("-", ""));
        request.setIv(Base64.getEncoder().encodeToString(iv));
        request.setEncryptedKey(Base64.getEncoder().encodeToString(encryptedAesKey));

        //body
        request.setEncryptedData(Base64.getEncoder().encodeToString(encryptedBizData));




        // 6. 使用本端 RSA 私钥生成签名
        // 签名原串构造建议：appId + timestamp + nonce + encryptedData
        String signContent = request.getAppId() + request.getTimestamp() + request.getNonce() + request.getEncryptedData();
        Signature signature = Signature.getInstance(SIGN_ALGO);
        signature.initSign(selfPrivateKey);
        signature.update(signContent.getBytes("UTF-8"));
        request.setSign(Base64.getEncoder().encodeToString(signature.sign()));

        return request;
    }


    private class CryptoRequest {

        private String appId;
        private String timestamp; // 格式：yyyyMMddHHmmss
        private String nonce;
        private String signType = "RSA";
        private String sign; // Base64编码
        private String encryptedData; // 业务数据加密后，Base64编码
        private String encryptAlgorithm = "AES-256-GCM";
        private String iv; // 12字节IV，Base64编码
        private String encryptedKey; // 补充字段：RSA加密后的AES密钥，Base64编码

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getEncryptedData() {
            return encryptedData;
        }

        public void setEncryptedData(String encryptedData) {
            this.encryptedData = encryptedData;
        }

        public String getEncryptAlgorithm() {
            return encryptAlgorithm;
        }

        public void setEncryptAlgorithm(String encryptAlgorithm) {
            this.encryptAlgorithm = encryptAlgorithm;
        }

        public String getIv() {
            return iv;
        }

        public void setIv(String iv) {
            this.iv = iv;
        }

        public String getEncryptedKey() {
            return encryptedKey;
        }

        public void setEncryptedKey(String encryptedKey) {
            this.encryptedKey = encryptedKey;
        }
    }
}