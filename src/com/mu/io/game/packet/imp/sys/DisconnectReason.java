package com.mu.io.game.packet.imp.sys;

import com.mu.io.game.packet.WriteOnlyPacket;
import org.jboss.netty.channel.Channel;

public class DisconnectReason extends WriteOnlyPacket {
   public DisconnectReason() {
      super(114);
   }

   public static void pushDisconnect(Channel channel, String str) {
      try {
         DisconnectReason dr = new DisconnectReason();
         dr.writeUTF(str);
         channel.write(dr.toBuffer());
         dr.destroy();
         dr = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
