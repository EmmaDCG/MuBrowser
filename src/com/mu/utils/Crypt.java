package com.mu.utils;

public class Crypt {
   public static byte[] RC4(byte[] b_key, byte[] buf) {
      byte[] state = new byte[256];

      for(int i = 0; i < 256; ++i) {
         state[i] = (byte)i;
      }

      int x = 0;
      int y = 0;
      int index1 = 0;
      int index2 = 0;
      if (b_key != null && b_key.length != 0) {
         int xorIndex;
         byte tmp;
         for(xorIndex = 0; xorIndex < 256; ++xorIndex) {
            index2 = (b_key[index1] & 255) + (state[xorIndex] & 255) + index2 & 255;
            tmp = state[xorIndex];
            state[xorIndex] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
         }

         if (buf == null) {
            return null;
         } else {
            byte[] result = new byte[buf.length];

            for(int i = 0; i < buf.length; ++i) {
               x = x + 1 & 255;
               y = (state[x] & 255) + y & 255;
               tmp = state[x];
               state[x] = state[y];
               state[y] = tmp;
               xorIndex = (state[x] & 255) + (state[y] & 255) & 255;
               result[i] = (byte)(buf[i] ^ state[xorIndex]);
            }

            return result;
         }
      } else {
         return null;
      }
   }

   public static byte[] RC4_Decrypt(byte[] key, byte[] data) {
      return data != null && key != null ? RC4(key, data) : null;
   }
}
