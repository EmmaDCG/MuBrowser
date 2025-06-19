package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class OpenXSUseItem extends ReadAndWritePacket {
   public OpenXSUseItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int xsId = this.readInt();
      Player player = this.getPlayer();
      PlayerTaskManager ptm = null;
      if (player != null && (ptm = player.getTaskManager()) != null) {
         ptm.openXS(xsId);
      }
   }

   public static void open(Player player, int xsId, boolean open, int remainCount) {
      try {
         OpenXSUseItem oxs = new OpenXSUseItem(40016, (byte[])null);
         oxs.writeInt(xsId);
         oxs.writeBoolean(open);
         oxs.writeShort(remainCount);
         player.writePacket(oxs);
         oxs.destroy();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
