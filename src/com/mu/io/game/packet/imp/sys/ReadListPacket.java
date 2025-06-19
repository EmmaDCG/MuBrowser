package com.mu.io.game.packet.imp.sys;

import com.mu.io.game.packet.PacketFactory;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Zlib;
import com.mu.utils.buffer.BufferReader;

public class ReadListPacket extends ReadAndWritePacket {
   public ReadListPacket(int code, byte[] readBuf) {
      super(code, readBuf);
      this.processImmediately = true;
   }

   public void process() throws Exception {
      byte[] data = Zlib.decompressBytes(this.reader.getReadBuf());
      BufferReader br = new BufferReader(data);
      int num = br.readShort();

      for(int i = 0; i < num; ++i) {
         int length = br.readInt();
         int code = br.readUnsignedShort();
         byte[] bytes = new byte[length - 2];
         br.readBytes(bytes);
         ReadAndWritePacket packet = PacketFactory.getPacket(code, bytes);
         if (packet != null) {
            packet.setChannel(this.getChannel());
            packet.setPlayer(this.getPlayer());

            try {
               packet.process();
            } catch (Exception var13) {
               var13.printStackTrace();
            } finally {
               packet.destroy();
            }
         }
      }

      br.destroy();
   }
}
