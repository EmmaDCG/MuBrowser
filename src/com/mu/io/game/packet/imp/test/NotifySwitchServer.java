package com.mu.io.game.packet.imp.test;

import com.mu.io.game.packet.WriteOnlyPacket;

public class NotifySwitchServer extends WriteOnlyPacket {
   public NotifySwitchServer(String url, int port) {
      super(104);

      try {
         this.writeUTF(url);
         this.writeShort(port);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
