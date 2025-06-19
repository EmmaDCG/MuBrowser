package com.mu.utils.buffer;

import java.io.IOException;

public class Buffer {
   private BufferReader reader;
   private BufferWriter writer;

   public Buffer(byte[] readBuf) {
      this.reader = new BufferReader(readBuf);
      this.writer = new BufferWriter();
   }

   public Buffer(byte[] readBuf, int size) {
      this.reader = new BufferReader(readBuf);
      this.writer = new BufferWriter(size);
   }

   public final int remaining() {
      return this.reader.remaining();
   }

   public final int readBytes(byte[] bytes) {
      return this.reader.readBytes(bytes);
   }

   public final byte readByte() throws IOException {
      return this.reader.readByte();
   }

   public final float readFloat() throws IOException {
      return this.reader.readFloat();
   }

   public final boolean readBoolean() throws IOException {
      return this.reader.readBoolean();
   }

   public final short readShort() throws IOException {
      return this.reader.readShort();
   }

   public final int readInt() throws IOException {
      return this.reader.readInt();
   }

   public final long readLong() throws IOException {
      return this.reader.readLong();
   }

   public final double readDouble() throws IOException {
      return this.reader.readDouble();
   }

   public final int readUnsignedShort() throws IOException {
      return this.reader.readUnsignedShort();
   }

   public final short readUnsignedByte() throws IOException {
      return this.reader.readUnsignedByte();
   }

   public final long readUnsignedInt() throws IOException {
      return this.reader.readUnsignedInt();
   }

   public final String readUTF() throws IOException {
      return this.reader.readUTF();
   }

   public final int getWriteLength() {
      return this.writer.getWriteLength();
   }

   public final byte[] getWriteBuf() {
      return this.writer.getWriteBuf();
   }

   public final void writeByte(int b) throws IOException {
      this.writer.writeByte(b);
   }

   public final void writeBytes(byte[] bytes) throws IOException {
      this.writer.writeBytes(bytes);
   }

   public final void writeBoolean(boolean b) throws IOException {
      this.writer.writeBoolean(b);
   }

   public final void writeShort(int v) throws IOException {
      this.writer.writeShort(v);
   }

   public final void writeInt(int v) throws IOException {
      this.writer.writeInt(v);
   }

   public final void writeFloat(float v) throws IOException {
      this.writer.writeFloat(v);
   }

   public final void writeLong(long v) throws IOException {
      this.writer.writeLong(v);
   }

   public final void writeDouble(double v) throws IOException {
      this.writer.writeDouble(v);
   }

   public final int writeUTF(String str) throws IOException {
      return this.writer.writeUTF(str);
   }

   public final void flush() throws IOException {
      this.writer.flush();
   }

   public final byte[] toByteArray() throws IOException {
      return this.writer.toByteArray();
   }

   public final void destroy() {
      this.reader.destroy();
      this.writer.destroy();
   }
}
