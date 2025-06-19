package com.mu.io.game.packet.imp.account;

import com.mu.io.game.packet.WriteOnlyPacket;
import org.jboss.netty.channel.Channel;

public class ConnectionSuccess extends WriteOnlyPacket {
   public ConnectionSuccess() {
      super(100);

      try {
         this.writeBoolean(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void writeConnectionSuccess(Channel channel) {
      ConnectionSuccess cs = new ConnectionSuccess();
      channel.write(cs.toBuffer());
      cs.destroy();
      cs = null;
   }
}
