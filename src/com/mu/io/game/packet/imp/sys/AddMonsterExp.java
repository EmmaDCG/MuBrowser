package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class AddMonsterExp extends WriteOnlyPacket {
   public AddMonsterExp() {
      super(1018);
   }

   public static void sendToClient(Player player, long exp, long monsterID) {
      try {
         AddMonsterExp ame = new AddMonsterExp();
         ame.writeDouble((double)monsterID);
         ame.writeDouble((double)exp);
         player.writePacket(ame);
         ame.destroy();
         ame = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
