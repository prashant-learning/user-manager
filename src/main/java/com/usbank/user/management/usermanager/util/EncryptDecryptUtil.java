package com.usbank.user.management.usermanager.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptDecryptUtil {

    public static final String KEY = "QUJDREVGR0hJSktMTU5PUA==";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!%^&*()?><:;{}~$%]).{8,20})";

    private static Cipher ecipher;
    private static Cipher dcipher;
    private static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private static Matcher matcher;


    static {
        try {
            ecipher = Cipher.getInstance("AES");
            SecretKeySpec eSpec = new SecretKeySpec(KEY.getBytes(), "AES");
            ecipher.init(Cipher.ENCRYPT_MODE, eSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            dcipher = Cipher.getInstance("AES");
            SecretKeySpec dSpec = new SecretKeySpec(KEY.getBytes(), "AES");
            dcipher.init(Cipher.DECRYPT_MODE, dSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static String encrypt(String value) {
        byte[] b1 = value.getBytes();
        byte[] encryptedValue;
        try {
            encryptedValue = ecipher.doFinal(b1);
            return Base64.encodeBase64String(encryptedValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static String decrypt(String encryptedValue) {
        byte[] decryptedValue = Base64.decodeBase64(encryptedValue.getBytes());
        byte[] decValue;
        try {
            decValue = dcipher.doFinal(decryptedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean validate(final String password) {
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
