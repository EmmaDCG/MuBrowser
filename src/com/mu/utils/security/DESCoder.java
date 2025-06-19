package com.mu.utils.security;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public abstract class DESCoder extends Coder {
   public static final String ALGORITHM = "DES";

   private static Key toKey(byte[] key) throws Exception {
      DESKeySpec dks = new DESKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey secretKey = keyFactory.generateSecret(dks);
      return secretKey;
   }

   public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
      Key k = toKey(key);
      Cipher cipher = Cipher.getInstance("DES");
      cipher.init(2, k);
      return cipher.doFinal(data);
   }

   public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
      Key k = toKey(key);
      Cipher cipher = Cipher.getInstance("DES");
      cipher.init(1, k);
      return cipher.doFinal(data);
   }
}
