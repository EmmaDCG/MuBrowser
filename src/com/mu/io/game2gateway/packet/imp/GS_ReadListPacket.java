package com.mu.io.game2gateway.packet.imp;

import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacketFactory;
import com.mu.utils.Zlib;
import com.mu.utils.buffer.BufferReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GS_ReadListPacket extends Game2GatewayPacket {
   private static final Logger logger = LoggerFactory.getLogger(GS_ReadListPacket.class);

   public GS_ReadListPacket(int code, byte[] bytes) {
      super(code, bytes);
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
         Game2GatewayPacket packet = Game2GatewayPacketFactory.getPacket(code, bytes);
         if (packet != null) {
            packet.setC2gChannel(this.getC2gChannel());
            packet.setG2sChannel(this.getG2sChannel());

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
