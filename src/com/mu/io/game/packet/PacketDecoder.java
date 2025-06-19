package com.mu.io.game.packet;

import com.mu.game.CenterManager;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketDecoder extends FrameDecoder {
   private static Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

   protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
      if (buffer.readableBytes() < 6) {
         return null;
      } else {
         int readerIndex = buffer.readerIndex();
         int dataLength = buffer.getInt(readerIndex);
         int opcode = buffer.getUnsignedShort(readerIndex + 4);
         if (dataLength <= 204800 && dataLength >= 0) {
            if (CenterManager.uploadCheck(channel, dataLength)) {
               return null;
            } else if (buffer.readableBytes() < dataLength + 4) {
               return null;
            } else {
               buffer.skipBytes(6);
               Object decoded = null;

               byte[] decoded2;
               try {
                  decoded2 = new byte[dataLength - 2];
                  buffer.readBytes(decoded2);
               } catch (Exception var9) {
                  var9.printStackTrace();
                  decoded = null;
                  return null;
               }

               ReadAndWritePacket packet = PacketFactory.getPacket(opcode, decoded2);
               if (packet == null) {
                  CenterManager.gameServerActivieOffChannel(channel);
                  logger.error("packet not found ,protocol code = {}", opcode);
               }

               return packet;
            }
         } else {
            logger.error("packet size is too large,protocol code = {},size = {}", opcode, dataLength);
            CenterManager.gameServerActivieOffChannel(channel);
            return null;
         }
      }
   }
}
