package com.parking.ticket_service.utils;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static javax.crypto.SecretKeyFactory.getInstance;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AESUtils {
    static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    static final int GCM_TAG_LENGTH = 128;
    static final int GCM_IV_LENGTH = 12;
    static final int ITERATION_COUNT = 65536;
    static final int KEY_LENGTH = 256;
    static final int SALT_LENGTH = 16;
    static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    @NonFinal
    @Value("${key.EAS}")
    String passwordFromConfig;

    static String password;

    @PostConstruct
    public void init() {
        password = passwordFromConfig;  // Gán giá trị từ biến không tĩnh vào biến tĩnh
    }

    public static SecretKey generateKeyFromPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = getInstance(ALGORITHM);
        SecretKey tmp = keyFactory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static String encrypt(byte[] data)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {

        byte[] salt = generateSalt();
        SecretKey key = generateKeyFromPassword(password, salt);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] cipherText = cipher.doFinal(data);

        byte[] encryptedDataWithSaltIv = new byte[salt.length + iv.length + cipherText.length];
        System.arraycopy(salt, 0, encryptedDataWithSaltIv, 0, salt.length);
        System.arraycopy(iv, 0, encryptedDataWithSaltIv, salt.length, iv.length);
        System.arraycopy(cipherText, 0, encryptedDataWithSaltIv, salt.length + iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(encryptedDataWithSaltIv);
    }

    public static byte[] decrypt(String encryptedData)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        byte[] salt = new byte[SALT_LENGTH];
        byte[] iv = new byte[GCM_IV_LENGTH];

        System.arraycopy(decodedData, 0, salt, 0, salt.length);
        System.arraycopy(decodedData, SALT_LENGTH, iv, 0, iv.length);

        SecretKey key = generateKeyFromPassword(password, salt);

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        int cipherTextStartIndex = salt.length + iv.length;
        int cipherTextLength = decodedData.length - cipherTextStartIndex;

        return cipher.doFinal(decodedData, cipherTextStartIndex, cipherTextLength);
    }
}

