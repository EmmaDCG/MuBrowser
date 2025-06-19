package com.mu.utils.buffer;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.nio.BufferOverflowException;

public class BufferReader {
   private byte[] readBuf = null;
   private int readPosition = 0;

   public BufferReader(byte[] readBuf) {
      this.readBuf = readBuf;
   }

   public final int remaining() {
      return this.readBuf.length - this.readPosition;
   }

   public final int readBytes(byte[] bytes) {
      if (this.readPosition + bytes.length > this.readBuf.length) {
         (new BufferOverflowException()).printStackTrace();
         return 0;
      } else {
         for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = this.readBuf[this.readPosition++];
         }

         return bytes.length;
      }
   }

   public final byte readByte() throws IOException {
      return this.readBuf[this.readPosition++];
   }

   public final float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readInt());
   }

   public final boolean readBoolean() throws IOException {
      return this.readBuf[this.readPosition++] == 1;
   }

   public final short readShort() throws IOException {
      return (short)(this.readBuf[this.readPosition++] << 8 | this.readBuf[this.readPosition++] & 255);
   }

   public final int readInt() throws IOException {
      int offset = this.readPosition;
      this.readPosition += 4;
      return this.readBuf[offset + 3] & 255 | (this.readBuf[offset + 2] & 255) << 8 | (this.readBuf[offset + 1] & 255) << 16 | (this.readBuf[offset] & 255) << 24;
   }

   public final long readLong() throws IOException {
      int offset = this.readPosition;
      this.readPosition += 8;
      return ((long)this.readBuf[offset] << 56) + ((long)(this.readBuf[offset + 1] & 255) << 48) + ((long)(this.readBuf[offset + 2] & 255) << 40) + ((long)(this.readBuf[offset + 3] & 255) << 32) + ((long)(this.readBuf[offset + 4] & 255) << 24) + (long)((this.readBuf[offset + 5] & 255) << 16) + (long)((this.readBuf[offset + 6] & 255) << 8) + (long)((this.readBuf[offset + 7] & 255) << 0);
   }

   public final double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readLong());
   }

   public final int readUnsignedShort() throws IOException {
      return (this.readBuf[this.readPosition++] & 255) << 8 | this.readBuf[this.readPosition++] & 255;
   }

   public final short readUnsignedByte() throws IOException {
      return (short)(this.readBuf[this.readPosition++] & 255);
   }

   public final long readUnsignedInt() throws IOException {
      return (long)(this.readBuf[this.readPosition++] << 24 | this.readBuf[this.readPosition++] << 16 | this.readBuf[this.readPosition++] << 8 | this.readBuf[this.readPosition++]) & 4294967295L;
   }

   public final String readUTF() throws IOException {
      int utflen = this.readUnsignedShort();
      byte[] bytearr = new byte[utflen];
      char[] chararr = new char[utflen];
      int count = 0;
      int chararr_count = 0;
      System.arraycopy(this.readBuf, this.readPosition, bytearr, 0, utflen);

      int c;
      while(count < utflen) {
         c = bytearr[count] & 255;
         if (c > 127) {
            break;
         }

         ++count;
         chararr[chararr_count++] = (char)c;
      }

      while(count < utflen) {
         c = bytearr[count] & 255;
         byte char2;
         switch(c >> 4) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            ++count;
            chararr[chararr_count++] = (char)c;
            break;
         case 8:
         case 9:
         case 10:
         case 11:
         default:
            throw new UTFDataFormatException("malformed input around byte " + count);
         case 12:
         case 13:
            count += 2;
            if (count > utflen) {
               throw new UTFDataFormatException("malformed input: partial character at end");
            }

            char2 = bytearr[count - 1];
            if ((char2 & 192) != 128) {
               throw new UTFDataFormatException("malformed input around byte " + count);
            }

            chararr[chararr_count++] = (char)((c & 31) << 6 | char2 & 63);
            break;
         case 14:
            count += 3;
            if (count > utflen) {
               throw new UTFDataFormatException("malformed input: partial character at end");
            }

            char2 = bytearr[count - 2];
            int char3 = bytearr[count - 1];
            if ((char2 & 192) != 128 || (char3 & 192) != 128) {
               throw new UTFDataFormatException("malformed input around byte " + (count - 1));
            }

            chararr[chararr_count++] = (char)((c & 15) << 12 | (char2 & 63) << 6 | (char3 & 63) << 0);
         }
      }

      this.readPosition += utflen;
      return new String(chararr, 0, chararr_count);
   }

   public final int getReadLength() {
      return this.readBuf.length;
   }

   public final byte[] getReadBuf() {
      return this.readBuf;
   }

   public final void destroy() {
      this.readBuf = null;
   }
}
