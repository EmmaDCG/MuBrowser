package com.mu.io.game.packet.imp.storage;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.ToDepotPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class DeportOpen extends ReadAndWritePacket {
   public DeportOpen(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DeportOpen() {
      super(20013, (byte[])null);
   }

   public static void openDeport(Player player) {
      DeportOpen open = new DeportOpen();

      try {
         open.writeBoolean(true);
         player.writePacket(open);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      open.destroy();
      open = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      boolean flag = true;
      if (player.getVIPManager().isTimeOut()) {
         flag = false;
      }

      if (flag) {
         player.writePacket(this);
      }

      if (!flag) {
         ShowPopup.open(player, new ToDepotPopup(player.createPopupID()));
      }
   }
}
