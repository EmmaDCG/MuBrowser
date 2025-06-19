package com.mu.io.game2gateway.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class Game2GatewayPacketDecoder extends FrameDecoder {
   protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
      if (buffer.readableBytes() < 6) {
         return null;
      } else {
         int readerIndex = buffer.readerIndex();
         int dataLength = buffer.getInt(readerIndex);
         int opcode = buffer.getUnsignedShort(readerIndex + 4);
         if (buffer.readableBytes() < dataLength + 4) {
            return null;
         } else {
            buffer.skipBytes(6);
            byte[] decoded = null;

            try {
               decoded = new byte[dataLength - 2];
               buffer.readBytes(decoded);
            } catch (Exception var9) {
               var9.printStackTrace();
            }

            Game2GatewayPacket packet = Game2GatewayPacketFactory.getPacket(opcode, decoded);
            return packet;
         }
      }
   }
}
