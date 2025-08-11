package com.upcredit.utils;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description RSA utility class, supporting keys of length 2048
 * @Date 2025/3/31 22:50
 * @Author by liyu-jk
 */
public class UpCreditRSAUtils {
    /**
     * encryption algorithm RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * signature algorithms
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private static final String cipherMode = "RSA/ECB/PKCS1Padding";

    /**
     * Public Key (Please replace the real values)
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * Private key (Please replace the real values)
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * Maximum encrypted plaintext size
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * Maximum decrypted plaintext size
     */
    private static final int MAX_DECRYPT_BLOCK = 256;


    public static void main(String[] args) throws Exception {
        /*RSA 2048*/

//        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAswSYu13+yGlDdAUgAxKcJ81Edt04+CjJUuzqmYmO91ubXCcz7cwy6EHfkk++VuZLAXut/sfQa/jlScTOaUgJos67zWJIrifYc1VQqV3y7pG2HeVOJGAuXBzkPXRDXsIVAYRZRFxU++mI3lo8dvOvORWIO1xMH9TJjBzV0UR888qEXeHd1a80qqTVoKawfiy1nVremtbuJIbu5ZSpruM0RAu2rENg0qr4oHmI2bUq3vECrYYPp+kBbp81dDgQDycOrQPr7JEM1ucJZDz2zU0m2UxboNohjAizteoBkEaKj0503e2AUP09ie7knWoZxtPAzolugpbxT3AO1lgbHKL5pwIDAQAB";
//        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCzBJi7Xf7IaUN0BSADEpwnzUR23Tj4KMlS7OqZiY73W5tcJzPtzDLoQd+ST75W5ksBe63+x9Br+OVJxM5pSAmizrvNYkiuJ9hzVVCpXfLukbYd5U4kYC5cHOQ9dENewhUBhFlEXFT76YjeWjx28685FYg7XEwf1MmMHNXRRHzzyoRd4d3VrzSqpNWgprB+LLWdWt6a1u4khu7llKmu4zREC7asQ2DSqvigeYjZtSre8QKthg+n6QFunzV0OBAPJw6tA+vskQzW5wlkPPbNTSbZTFug2iGMCLO16gGQRoqPTnTd7YBQ/T2J7uSdahnG08DOiW6ClvFPcA7WWBscovmnAgMBAAECggEAKzt58w3hIN8i9hrivzs4UPhmh1onju6yp/8lLM0mpKAP5fJlvRDqXmLCLmBptCzLgmEvBO+Wauzh2q3Xt185TIMmoZQRv1VKFZhN8YkJyQmRdKjS9T/xEje7+wdf2bt/PS2MLVErCOc+MYyTO5rf/yYvDz7b93f48IhqLq6einU/l95tbNEc3gXjhSlihuZKr/6KD1++k4AEEBrl5oPa3DBZouhY6HHGCAclNTlzivfvm5WAjdCE3LMlrodC1ACjgGyDZcOIHIPkOcqFLfxIydI0nVEuFPuIWjeyOzGf3EeBjtesiK09SRsK5ddFrDOzby/fpAHWrhiwFmYqv84XgQKBgQDY1e+bZjRCAqbTxzH0uKvVTKvHnNJyc47jh0P02UijVWhrm/d4t9wKH7DYnWBZwZAesYM6XDAzbMYFZ5KLw3EPcIt1fyuMHxeVCz00UH3IEozN8QxWlhrlHik6ik8A9Kqr67Jcs4KeZaumZRWmmX4SRE7jY712O+3QP/AgDAU9wQKBgQDTWgqppLxIiu4MzIzKY5VkhiuvInqX0MJZ0I8kMRvtRh/b34/T+GjT+r1g3AgSHWcwf9nPufwlYWSwoPzGSPhGCF75T2CF4IXEEAnJ4zCJVK7amybgrlAQZXLGXKaIZHr2T6dHQknjJsfYQWKUFde72oxXt/gvjoNK+eCs5GFhZwKBgQC1LBtldkHXnauSa28cEGjScZtdz3Qu2Mrc5RosrJf6kNQMhWaCYOzjMJNsiiIFHKu0WZFR49EKRqo1vdI+IPCIe/qqE7VpAFmN2LQsz8worQck03EBr62NHmRIW2OjYspvlyGSPxK2EjEXeIJcjwc9cAGSELYu4efUBng17pU6gQKBgQCxsKMES27M4pkPA65eveis8iyp+qftGWM81Z5yxCMBkpJYbhXjFZc0mTs8wuC6MiQ+X08FWQ1HdCGOalr6bgDmCEWo/3ZcOA7ebsl8BdkZrKuxOP4vqf3AOzqK0Pxl8Wx7xy4ROAccxc8A3r/9VnvhAPY7DX3Ipd12XKzrTrscgwKBgFCMCYDMMh1ERiImsa5DE4/ndTuI7rILr77JvkfuNbAqTbaF1C9AHYi4WYR/tqwmNRJySjCUuMKavgEdk3V12kz6uQKRqIKb81xClAuYloZufuGGx26wvFPVw6o7ykfbtaaDxdl2Ifplck18+Gu5hETPOdUoIltCGmtA6csNOfkJ";
        /**
         * Base64 here can be used under other packages such as Apache Spring, if it doesn't work,
         * please contact the corresponding development students to get the base64 tool class
         */
        /**
         * This key pair is your own
         */
        Map<String, Object> stringObjectMap = genKeyPair(2048);
        String publicKey_1 = Base64.getEncoder().encodeToString((((RSAPublicKey) stringObjectMap.get(PUBLIC_KEY)).getEncoded()));
        String privateKey_1 = Base64.getEncoder().encodeToString(((RSAPrivateKey) stringObjectMap.get(PRIVATE_KEY)).getEncoded());
        System.out.println("publicKey = " + publicKey_1);
        System.out.println("privateKey = " + privateKey_1);

        /**
         * This key pair is provided by UpCredit, and theoretically, you can only obtain the public key provided by UpCredit
         */
        Map<String, Object> stringObjectMap2 = genKeyPair(2048);
        String publicKey_2 = Base64.getEncoder().encodeToString((((RSAPublicKey) stringObjectMap2.get(PUBLIC_KEY)).getEncoded()));
        String privateKey_2 = Base64.getEncoder().encodeToString(((RSAPrivateKey) stringObjectMap2.get(PRIVATE_KEY)).getEncoded());

        try {
            //Please replace the real values
            String reqData = "Requested business parameters";

            System.out.println("--------------------");

            byte[] reqBytes2 = UpCreditRSAUtils.encryptByPrivateKey(reqData.getBytes(), privateKey_1);
            String reqEncode = Base64.getEncoder().encodeToString(reqBytes2);
            System.out.println("Data encrypted with a private key：" + reqEncode);

            String singnData2 = UpCreditRSAUtils.sign(reqData.getBytes(), privateKey_1);
            System.out.println("Data signed with a private key：" + singnData2);

            // requested parameters
            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("transSeqNo", "123456");
            reqMap.put("partnerCode", "partnerCode");
            reqMap.put("transTime", "1714390213929");
            reqMap.put("bizContent", reqEncode);
            reqMap.put("url", "/api/test");
            reqMap.put("sign", singnData2);


            byte[] reqDecryptBytes2 = UpCreditRSAUtils.decryptByPublicKey(Base64.getDecoder().decode(reqMap.get("bizContent")), publicKey_1);
            System.out.println("Data decrypted by public key：" + new String(reqDecryptBytes2));

            boolean isSign2 = UpCreditRSAUtils.verify(reqDecryptBytes2, publicKey_1, reqMap.get("sign"));
            System.out.println("Verify that the signature is correct：" + isSign2);


            // response
            String repData = "{\"code\":null, \"msg\":null, \"flag\":\"S\", \"data\":{\"a\":\"1\"}}";
            byte[] repBytes2 = UpCreditRSAUtils.encryptByPrivateKey(repData.getBytes(), privateKey_2);
            String repEncode = Base64.getEncoder().encodeToString(repBytes2);
            System.out.println("Data encrypted with a private key：" + repEncode);

            String repSingnData2 = UpCreditRSAUtils.sign(repData.getBytes(), privateKey_2);
            System.out.println("Data signed with a private key：" + singnData2);

            // response parameters
            Map<String, String> repMap = new HashMap<>();
            repMap.put("transSeqNo", "123456");
            repMap.put("partnerCode", "partnerCode");
            repMap.put("transTime", "1714390213929");
            repMap.put("data", repEncode);
            repMap.put("sign", repSingnData2);

            byte[] repDecryptBytes2 = UpCreditRSAUtils.decryptByPublicKey(Base64.getDecoder().decode(repMap.get("data")), publicKey_2);
            System.out.println("Data decrypted by public key：" + new String(repDecryptBytes2));

            boolean isSign3 = UpCreditRSAUtils.verify(repDecryptBytes2, publicKey_2, repMap.get("sign"));
            System.out.println("Verify that the signature is correct：" + isSign3);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param keySize The length of the generated key is usually 1024 or 2048
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        System.out.println("publicKey：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        System.out.println("privateKey：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        return keyMap;
    }


    /**
     * Sign encrypted data
     *
     * @param data       Encrypted data
     * @param privateKey
     * @return The signature generated for the encrypted data
     * @throws Exception
     */

    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }


    /**
     * Signature verification
     *
     * @param data      Data before signature
     * @param publicKey
     * @param sign      Data after signature
     * @return Verify that the signature is correct
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }


    /**
     * Decrypt data using the private key
     *
     * @param encryptedData Data encrypted using the public key
     * @param privateKey
     * @return Decrypted data
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        //Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // Decrypt data in segments
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();


        return decryptedData;
    }

    /**
     * Public key decryption
     *
     * @param encryptedData Data encrypted with a private key
     * @param publicKey     public key
     * @return Decrypted data
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // Decrypt data in segments
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }


    /**
     * Public key encryption
     *
     * @param data      Data that needs to be encrypted
     * @param publicKey public key
     * @return Data encrypted with a public key
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // Encrypt your data
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // Encrypt data in segments
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    /**
     * Private key encryption
     *
     * @param data       Data to be encrypted
     * @param privateKey private key
     * @return Data encrypted with a private key
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(cipherMode);
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // Encrypt data in segments
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


    /**
     * Obtain private key
     *
     * @param keyMap Generated key pair
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


    /**
     * Obtain public key
     *
     * @param keyMap Generated key pair
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }


}
