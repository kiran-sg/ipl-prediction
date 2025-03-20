package com.ipl.prediction.iplprediction.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String SECRET_KEY = "mySecretKey12345"; // Must match the front-end key
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // AES with CBC mode and PKCS5 padding

    public static String decrypt(String encryptedData) {
        try {
            // Remove any whitespace or invalid characters from the input
            encryptedData = encryptedData.replaceAll("\\s", "");

            // Decode the Base64-encoded encrypted data
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

            // Extract the IV (first 16 bytes) and the actual encrypted data
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[encryptedBytes.length - 16];
            System.arraycopy(encryptedBytes, 0, iv, 0, 16);
            System.arraycopy(encryptedBytes, 16, encrypted, 0, encrypted.length);

            // Initialize the secret key and IV
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Initialize the cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(encrypted);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
