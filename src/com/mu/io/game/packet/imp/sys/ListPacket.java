package com.mu.io.game.packet.imp.sys;

import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.utils.Zlib;
import com.mu.utils.buffer.BufferWriter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListPacket extends WriteOnlyPacket {
   private int packetSize;
   private static Logger logger = LoggerFactory.getLogger(ListPacket.class);

   private ListPacket(int code) {
      super(code);
      this.packetSize = 0;
   }

   public ListPacket() {
      this(60001);
   }

   public static ListPacket forClient() {
      return new ListPacket(60000);
   }

   public void addPacket(WriteOnlyPacket packet) throws Exception {
      this.writeInt(packet.getWriteLength() + 2);
      this.writeShort(packet.getOpcode());
      this.writer.writeBytes(packet.getWriteBuf(), 0, packet.getWriteLength());
      if (this.packetSize++ > 2048) {
         throw new Exception("packet size too long");
      }
   }

   public int getPacketSize() {
      return this.packetSize;
   }

   public ChannelBuffer toBuffer() {
      if (this.buffer == null) {
         try {
            BufferWriter bw = new BufferWriter(2 + this.getWriteLength());
            bw.writeShort(this.packetSize);
            bw.writeBytes(this.writer.toByteArray());
            byte[] compressBytes = Zlib.compressBytes(bw.toByteArray());
            this.buffer = ChannelBuffers.buffer(compressBytes.length + 4 + 2);
            this.buffer.writeInt(compressBytes.length + 2);
            this.buffer.writeShort(this.getOpcode());
            this.buffer.writeBytes(compressBytes);
            compressBytes = null;
            bw.destroy();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return this.buffer;
   }
}
