package com.mu.utils;

import com.mu.utils.buffer.BufferWriter;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zlib {
   private static int cachesize = 1024;

   public static byte[] compressBytes(byte[] input) {
      Deflater compresser = new Deflater();
      byte[] output = null;
      BufferWriter writer = new BufferWriter(input.length);

      try {
         compresser.setInput(input);
         compresser.finish();
         byte[] buf = new byte[cachesize];

         while(!compresser.finished()) {
            int got = compresser.deflate(buf);
            writer.writeBytes(buf, 0, got);
         }

         output = writer.toByteArray();
      } catch (Exception var14) {
         var14.printStackTrace();
      } finally {
         try {
            writer.destroy();
            compresser.end();
         } catch (Exception var13) {
            var13.printStackTrace();
         }

      }

      return output;
   }

   public static byte[] decompressBytes(byte[] input) {
      Inflater decompresser = new Inflater();
      byte[] output = null;
      BufferWriter writer = new BufferWriter(input.length);

      try {
         decompresser.setInput(input);
         byte[] buf = new byte[cachesize];

         while(!decompresser.finished()) {
            int got = decompresser.inflate(buf);
            writer.writeBytes(buf, 0, got);
         }

         output = writer.toByteArray();
      } catch (DataFormatException var16) {
         var16.printStackTrace();
      } catch (Exception var17) {
         var17.printStackTrace();
      } finally {
         try {
            writer.destroy();
            decompresser.end();
         } catch (Exception var15) {
            var15.printStackTrace();
         }

      }

      return output;
   }
}
