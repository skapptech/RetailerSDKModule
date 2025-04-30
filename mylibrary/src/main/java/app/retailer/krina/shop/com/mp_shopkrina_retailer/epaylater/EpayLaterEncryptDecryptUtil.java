package app.retailer.krina.shop.com.mp_shopkrina_retailer.epaylater;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Base64_1;

public class EpayLaterEncryptDecryptUtil {

    public static String createChecksum(String input) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] hash = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    public static String encryptAES256AndBase64(String encryptionKey, String iv, String jsonBody) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new
                SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new
                IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] encbyte = cipher.doFinal(jsonBody.getBytes(StandardCharsets.UTF_8));
        return Base64_1.getEncoder().encodeToString(encbyte);
    }

    private static String decryptBase64EncodedAES256(String encryptionKey, String iv, String inputParam) throws Exception {
        byte[] decodedInput = Base64_1.getDecoder().decode(inputParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new
                SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new
                IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        return new String(cipher.doFinal(decodedInput), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        final String encryptionKey = "BFEE23C36C78CFAE47A1B8515E64C3BF";
        final String iv = "A9A4455FB5415E10";
        final String jsonBody = "abc";
        String checksum = createChecksum(jsonBody);
        System.out.println(checksum);   // BA7816BF8F01CFEA414140DE5DAE2223B00361A396177 A9CB410FF61F20015AD
        String encdata = encryptAES256AndBase64(encryptionKey, iv, jsonBody);
        System.out.println(encdata);    // ISSIswPGau201AyJBKPdgg==
        System.out.println(decryptBase64EncodedAES256(encryptionKey, iv, encdata)); // {'key':'val'}
    }
}