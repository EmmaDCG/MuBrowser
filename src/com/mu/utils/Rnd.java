package com.mu.utils;

import java.util.Random;

public class Rnd {
   private static final Random rnd = new Random();

   public static float get() {
      return rnd.nextFloat();
   }

   public static int get(int n) {
      return (int)Math.floor(rnd.nextDouble() * (double)n);
   }

   public static int get(int min, int max) {
      return min + (int)Math.floor(rnd.nextDouble() * (double)(max - min + 1));
   }

   public static long get(long min, long max) {
      return min + (long)Math.floor(rnd.nextDouble() * (double)(max - min + 1L));
   }

   public static int nextInt(int n) {
      return (int)Math.floor(rnd.nextDouble() * (double)n);
   }

   public static int nextInt() {
      return rnd.nextInt();
   }

   public static double nextDouble() {
      return rnd.nextDouble();
   }

   public static double nextGaussian() {
      return rnd.nextGaussian();
   }

   public static boolean nextBoolean() {
      return rnd.nextBoolean();
   }
}
