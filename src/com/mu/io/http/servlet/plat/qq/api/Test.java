package com.mu.io.http.servlet.plat.qq.api;

public class Test {
   public static void main(String[] args) {
      String ip = "58.132.170.212";
      long number = ipToLong(ip);
      System.out.println(number + "," + longToIp2(number));
   }

   public static long ipToLong(String ipAddress) {
      long result = 0L;
      String[] ipAddressInArray = ipAddress.split("\\.");

      for(int i = 3; i >= 0; --i) {
         long ip = Long.parseLong(ipAddressInArray[3 - i]);
         result |= ip << i * 8;
      }

      return result;
   }

   public String longToIp(long ip) {
      StringBuilder result = new StringBuilder(15);

      for(int i = 0; i < 4; ++i) {
         result.insert(0, Long.toString(ip & 255L));
         if (i < 3) {
            result.insert(0, '.');
         }

         ip >>= 8;
      }

      return result.toString();
   }

   public static String longToIp2(long ip) {
      return (ip >> 24 & 255L) + "." + (ip >> 16 & 255L) + "." + (ip >> 8 & 255L) + "." + (ip & 255L);
   }
}
