package com.mu.utils;

public class Base32 {
   private static final char[] ALPHABET = new char[]{'Y', '6', 'C', 'O', 'F', 'Z', 'I', 'J', 'B', 'K', 'N', 'L', 'M', 'P', 'R', 'E', 'S', 'W', 'U', 'G', 'X', '2', '8', '4', 'A', 'Q', '5', 'V', 'D', 'T', '9', 'H'};
   private static final byte[] DECODE_TABLE = new byte[128];

   static {
      int i;
      for(i = 0; i < DECODE_TABLE.length; ++i) {
         DECODE_TABLE[i] = -1;
      }

      for(i = 0; i < ALPHABET.length; ++i) {
         DECODE_TABLE[ALPHABET[i]] = (byte)i;
         if (i < 24) {
            DECODE_TABLE[Character.toLowerCase(ALPHABET[i])] = (byte)i;
         }
      }

   }

   public static String encode(byte[] data) {
      char[] chars = new char[data.length * 8 / 5 + (data.length % 5 != 0 ? 1 : 0)];
      int i = 0;
      int j = 0;

      for(int index = 0; i < chars.length; ++i) {
         if (index > 3) {
            int b = data[j] & 255 >> index;
            index = (index + 5) % 8;
            b <<= index;
            if (j < data.length - 1) {
               b |= (data[j + 1] & 255) >> 8 - index;
            }

            chars[i] = ALPHABET[b];
            ++j;
         } else {
            chars[i] = ALPHABET[data[j] >> 8 - (index + 5) & 31];
            index = (index + 5) % 8;
            if (index == 0) {
               ++j;
            }
         }
      }

      return new String(chars);
   }

   public static byte[] decode(String s) throws Exception {
      char[] stringData = s.toCharArray();
      byte[] data = new byte[stringData.length * 5 / 8];
      int i = 0;
      int j = 0;

      for(int index = 0; i < stringData.length; ++i) {
         byte val;
         try {
            val = DECODE_TABLE[stringData[i]];
         } catch (ArrayIndexOutOfBoundsException var8) {
            throw new Exception("Illegal character");
         }

         if (val == -128) {
            throw new Exception("Illegal character");
         }

         int var10001;
         if (index <= 3) {
            index = (index + 5) % 8;
            if (index == 0) {
               var10001 = j++;
               data[var10001] |= val;
            } else {
               data[j] = (byte)(data[j] | val << 8 - index);
            }
         } else {
            index = (index + 5) % 8;
            var10001 = j++;
            data[var10001] = (byte)(data[var10001] | val >> index);
            if (j < data.length) {
               data[j] = (byte)(data[j] | val << 8 - index);
            }
         }
      }

      return data;
   }
}
