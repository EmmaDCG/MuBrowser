package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class QuitDungeon extends ReadAndWritePacket {
   public QuitDungeon() {
      super(12005, (byte[])null);
   }

   public QuitDungeon(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static void quit(int dunId, Player player) {
      try {
         QuitDungeon qd = new QuitDungeon();
         qd.writeByte(dunId);
         player.writePacket(qd);
         qd.destroy();
         qd = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.isInDungeon()) {
         player.getDungeonMap().getDungeon().exitForInitiative(player, false);
      }

   }
}
