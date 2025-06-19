package com.mu.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA1 {
   private static final String MAC_NAME = "HmacSHA1";
   private static final String ENCODING = "UTF-8";

   public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
      byte[] data = encryptKey.getBytes("UTF-8");
      SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(secretKey);
      byte[] text = encryptText.getBytes("UTF-8");
      return mac.doFinal(text);
   }
}
