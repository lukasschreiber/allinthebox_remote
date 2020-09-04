package com.example.allinthebox_remote;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {

    public static String KEY = "IPWsVf7t54CmSHAcitf2hLsTkxSckRcL";
    public static String IV = "jqk8gyF1KoH8T0D1";


    public static String decrypt(String text) {

        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("utf-8"));
            SecretKeySpec newKey = new SecretKeySpec(KEY.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(Base64.decode(text,Base64.DEFAULT)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return "error";
    }
}
