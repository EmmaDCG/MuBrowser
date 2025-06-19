package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.RuneRemovePopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class RuneUnMosaic extends ReadAndWritePacket {
   public RuneUnMosaic(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RuneUnMosaic() {
      super(20217, (byte[])null);
   }

   public static void sendToClient(Player player, long itemID, boolean success, int index) {
      try {
         RuneUnMosaic um = new RuneUnMosaic();
         um.writeDouble((double)itemID);
         um.writeBoolean(success);
         if (success) {
            um.writeByte(index);
         }

         player.writePacket(um);
         um.destroy();
         um = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int index = this.readByte();
      RuneRemovePopup popup = new RuneRemovePopup(player.createPopupID(), itemID, index);
      ShowPopup.open(player, popup);
   }
}
