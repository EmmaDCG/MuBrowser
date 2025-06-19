package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class EnterDungeon extends ReadAndWritePacket {
   public EnterDungeon(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public EnterDungeon() {
      super(12004, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int dunId = this.readUnsignedByte();
      Object obj = null;
      if (dunId != 12) {
         DungeonManager.createAndEnterDungeon(player, dunId, obj);
      }
   }

   public static void enterDungeon(Player player, int id, boolean showDynamicMenu) {
      try {
         EnterDungeon ed = new EnterDungeon();
         ed.writeByte(id);
         ed.writeInt(player.getDungeonMap().getDungeon().getPlayerTimeLeft(player));
         ed.writeBoolean(showDynamicMenu);
         player.writePacket(ed);
         ed.destroy();
         ed = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
