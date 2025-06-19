package com.mu.utils.buffer;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Arrays;

public class BufferWriter {
   private byte[] writeBuf = null;
   private int writeCount = 0;
   private byte[] flushBytes = null;
   private boolean isFlushed = false;

   public BufferWriter() {
      this.writeBuf = new byte[64];
   }

   public BufferWriter(int size) {
      this.writeBuf = new byte[size];
   }

   public final int getWriteLength() {
      return this.writeCount;
   }

   public final byte[] getWriteBuf() {
      return this.writeBuf;
   }

   public final void writeByte(int b) throws IOException {
      int newcount = this.writeCount + 1;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      this.writeBuf[this.writeCount] = (byte)b;
      this.writeCount = newcount;
   }

   public final void writeBytes(byte[] bytes) throws IOException {
      int newcount = this.writeCount + bytes.length;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      System.arraycopy(bytes, 0, this.writeBuf, this.writeCount, bytes.length);
      this.writeCount = newcount;
   }

   public final void writeBytes(byte[] bytes, int srcPos, int length) throws IOException {
      int newcount = this.writeCount + length;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      System.arraycopy(bytes, srcPos, this.writeBuf, this.writeCount, length);
      this.writeCount = newcount;
   }

   public final void writeBoolean(boolean b) throws IOException {
      this.writeByte(b ? 1 : 0);
   }

   public final void writeShort(int v) throws IOException {
      int newcount = this.writeCount + 2;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      this.writeBuf[this.writeCount] = (byte)(v >>> 8 & 255);
      this.writeBuf[this.writeCount + 1] = (byte)(v >>> 0 & 255);
      this.writeCount = newcount;
   }

   public final void writeInt(int v) throws IOException {
      int newcount = this.writeCount + 4;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      this.writeBuf[this.writeCount] = (byte)(v >>> 24 & 255);
      this.writeBuf[this.writeCount + 1] = (byte)(v >>> 16 & 255);
      this.writeBuf[this.writeCount + 2] = (byte)(v >>> 8 & 255);
      this.writeBuf[this.writeCount + 3] = (byte)(v >>> 0 & 255);
      this.writeCount = newcount;
   }

   public final void writeFloat(float v) throws IOException {
      this.writeInt(Float.floatToIntBits(v));
   }

   public final void writeLong(long v) throws IOException {
      int newcount = this.writeCount + 8;
      if (newcount > this.writeBuf.length) {
         this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
      }

      this.writeBuf[this.writeCount] = (byte)((int)(v >>> 56));
      this.writeBuf[this.writeCount + 1] = (byte)((int)(v >>> 48));
      this.writeBuf[this.writeCount + 2] = (byte)((int)(v >>> 40));
      this.writeBuf[this.writeCount + 3] = (byte)((int)(v >>> 32));
      this.writeBuf[this.writeCount + 4] = (byte)((int)(v >>> 24));
      this.writeBuf[this.writeCount + 5] = (byte)((int)(v >>> 16));
      this.writeBuf[this.writeCount + 6] = (byte)((int)(v >>> 8));
      this.writeBuf[this.writeCount + 7] = (byte)((int)(v >>> 0));
      this.writeCount = newcount;
   }

   public final void writeDouble(double v) throws IOException {
      this.writeLong(Double.doubleToLongBits(v));
   }

   public final int writeUTF(String str) throws IOException {
      int strlen = str.length();
      int utflen = 0;
      int num = 0;

      char c;
      for(int i = 0; i < strlen; ++i) {
         c = str.charAt(i);
         if (c >= '\u0001' && c <= '\u007f') {
            ++utflen;
         } else if (c > '\u07ff') {
            utflen += 3;
         } else {
            utflen += 2;
         }
      }

      if (utflen > 65535) {
         throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");
      } else {
         byte[] bytearr = new byte[utflen + 2];
         int var9 = num + 1;
         bytearr[num] = (byte)(utflen >>> 8 & 255);
         bytearr[var9++] = (byte)(utflen >>> 0 & 255);
         int i = 0;
         for(i = 0; i < strlen; ++i) {
            c = str.charAt(i);
            if (c < '\u0001' || c > '\u007f') {
               break;
            }

            bytearr[var9++] = (byte)c;
         }

         for(; i < strlen; ++i) {
            c = str.charAt(i);
            if (c >= '\u0001' && c <= '\u007f') {
               bytearr[var9++] = (byte)c;
            } else if (c > '\u07ff') {
               bytearr[var9++] = (byte)(224 | c >> 12 & 15);
               bytearr[var9++] = (byte)(128 | c >> 6 & 63);
               bytearr[var9++] = (byte)(128 | c >> 0 & 63);
            } else {
               bytearr[var9++] = (byte)(192 | c >> 6 & 31);
               bytearr[var9++] = (byte)(128 | c >> 0 & 63);
            }
         }

         int newcount = this.writeCount + utflen + 2;
         if (newcount > this.writeBuf.length) {
            this.writeBuf = Arrays.copyOf(this.writeBuf, Math.max(this.writeBuf.length << 1, newcount));
         }

         System.arraycopy(bytearr, 0, this.writeBuf, this.writeCount, utflen + 2);
         this.writeCount = newcount;
         return utflen + 2;
      }
   }

   public final void flush() throws IOException {
      if (!this.isFlushed) {
         this.flushBytes = new byte[this.writeCount];
         System.arraycopy(this.writeBuf, 0, this.flushBytes, 0, this.writeCount);
         this.isFlushed = true;
      }
   }

   public final byte[] toByteArray() throws IOException {
      if (!this.isFlushed) {
         this.flush();
      }

      return this.flushBytes;
   }

   public final void destroy() {
      this.writeBuf = null;
      this.flushBytes = null;
   }
}
