package com.mu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
   public static String md5s(String plainText) {
      StringBuffer buf = new StringBuffer("");

      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(plainText.getBytes());
         byte[] b = md.digest();

         for(int offset = 0; offset < b.length; ++offset) {
            int i = b[offset];
            if (i < 0) {
               i += 256;
            }

            if (i < 16) {
               buf.append("0");
            }

            buf.append(Integer.toHexString(i));
         }
      } catch (NoSuchAlgorithmException var6) {
         var6.printStackTrace();
      }

      return buf.toString();
   }
}
