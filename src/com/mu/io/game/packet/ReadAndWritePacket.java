package com.mu.io.game.packet;

import com.mu.game.model.unit.player.Player;
import com.mu.utils.buffer.BufferReader;
import java.io.IOException;
import org.jboss.netty.channel.Channel;

public abstract class ReadAndWritePacket extends WriteOnlyPacket {
   protected BufferReader reader = null;
   private Channel channel = null;
   private Player player = null;
   protected boolean processImmediately = false;

   public ReadAndWritePacket(int code, byte[] readBuf) {
      super(code);
      this.reader = new BufferReader(readBuf);
   }

   public final int remaining() {
      return this.reader.remaining();
   }

   public final int readBytes(byte[] bytes) {
      return this.reader.readBytes(bytes);
   }

   public final boolean isProcessImmediately() {
      return this.processImmediately;
   }

   public void setProcessImmediately(boolean processImmediately) {
      this.processImmediately = processImmediately;
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

   public void destroy() {
      super.destroy();
      this.reader.destroy();
      this.reader = null;
      this.channel = null;
      this.player = null;
   }

   public final Channel getChannel() {
      return this.channel;
   }

   public final void setChannel(Channel channel) {
      this.channel = channel;
   }

   public final Player getPlayer() {
      return this.player;
   }

   public final void setPlayer(Player player) {
      this.player = player;
   }

   public abstract void process() throws Exception;
}
