package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SelfPositionCorrect extends WriteOnlyPacket {
   public SelfPositionCorrect(int x, int y) {
      super(32008);

      try {
         this.writeInt(x);
         this.writeInt(y);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int x, int y) {
      SelfPositionCorrect spc = new SelfPositionCorrect(x, y);
      player.writePacket(spc);
      spc.destroy();
      spc = null;
   }
}
