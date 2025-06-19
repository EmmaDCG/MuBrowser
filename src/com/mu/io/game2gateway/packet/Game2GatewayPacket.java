package com.mu.io.game2gateway.packet;

import com.mu.utils.buffer.BufferReader;
import java.io.IOException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

public abstract class Game2GatewayPacket {
   private int opCode = 0;
   protected BufferReader reader = null;
   private Channel c2gChannel = null;
   private Channel g2sChannel = null;
   private ChannelBuffer buffer = null;

   public Game2GatewayPacket(int code, byte[] bytes) {
      this.opCode = code;
      this.reader = new BufferReader(bytes);
   }

   public final Channel getC2gChannel() {
      return this.c2gChannel;
   }

   public final void setC2gChannel(Channel c2gChannel) {
      this.c2gChannel = c2gChannel;
   }

   public final Channel getG2sChannel() {
      return this.g2sChannel;
   }

   public final void setG2sChannel(Channel g2sChannel) {
      this.g2sChannel = g2sChannel;
   }

   public final int getOpCode() {
      return this.opCode;
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

   public final ChannelBuffer toBuffer() {
      if (this.buffer == null) {
         int length = this.reader.getReadLength();
         this.buffer = ChannelBuffers.buffer(length + 6);
         this.buffer.writeInt(length + 2);
         this.buffer.writeShort(this.opCode);
         this.buffer.writeBytes(this.reader.getReadBuf());
      }

      return this.buffer;
   }

   public abstract void process() throws Exception;

   public void destroy() {
      this.c2gChannel = null;
      this.g2sChannel = null;
      this.buffer = null;
      this.reader.destroy();
      this.reader = null;
   }
}
