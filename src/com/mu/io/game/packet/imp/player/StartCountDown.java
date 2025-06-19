package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.controller.CountdownObject;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class StartCountDown extends WriteOnlyPacket {
   public StartCountDown() {
      super(10015);
   }

   public static void start(Player player, CountdownObject obj) {
      try {
         StartCountDown sd = new StartCountDown();
         sd.writeInt(obj.getTimeLength());
         sd.writeUTF(obj.getCountDownName());
         sd.writeByte(obj.getOrderType());
         player.writePacket(sd);
         sd.destroy();
         sd = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
