package com.mu.io.game.packet;

import com.mu.utils.buffer.BufferWriter;
import java.io.IOException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class WriteOnlyPacket extends Packet {
   protected ChannelBuffer buffer = null;
   protected BufferWriter writer = new BufferWriter();

   public WriteOnlyPacket(int code) {
      super(code);
   }

   public final int getWriteLength() {
      return this.writer.getWriteLength();
   }

   public final byte[] getWriteBuf() {
      return this.writer.getWriteBuf();
   }

   public ChannelBuffer toBuffer() {
      if (this.buffer == null) {
         this.buffer = ChannelBuffers.buffer(this.getWriteLength() + 6);
         this.buffer.writeInt(this.getWriteLength() + 2);
         this.buffer.writeShort(this.getOpcode());
         if (this.getWriteLength() > 0) {
            this.buffer.writeBytes(this.getWriteBuf(), 0, this.getWriteLength());
         }
      }

      return this.buffer;
   }

   public final void writeByte(int b) throws IOException {
      this.writer.writeByte(b);
   }

   public final void writeBytes(byte[] bytes) throws IOException {
      this.writer.writeBytes(bytes);
   }

   public final void writeBytes(byte[] bytes, int srcPos, int length) throws IOException {
      this.writer.writeBytes(bytes, srcPos, length);
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

   public final byte[] toByteArray() throws Exception {
      return this.writer.toByteArray();
   }

   public void destroy() {
      this.writer.destroy();
      this.writer = null;
      this.buffer = null;
   }
}
