package com.mu.utils;

import com.mu.utils.security.DESCoder;
import java.util.Arrays;

public class License {
   public static final byte[] KEY = "UhdOXMskLwxnvbqAxpfkDhekCwsakYFs".getBytes();
   private static final char[] RandomKeys = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

   public static String parseLicense(String licenseStr) throws Exception {
      return new String(DESCoder.decrypt(DESCoder.decryptBASE32(licenseStr), KEY));
   }

   public static String generateLicense(String dataStr) throws Exception {
      return DESCoder.encryptBASE32(DESCoder.encrypt(dataStr.getBytes(), KEY));
   }

   public static String sessionInServer() {
      char[] csn = new char[9];
      String sn = null;
      Arrays.fill(csn, '0');

      for(int i = 0; i < 9; ++i) {
         csn[i] = RandomKeys[(int)(Math.random() * (double)RandomKeys.length)];
      }

      sn = new String(csn);
      return sn;
   }
}
