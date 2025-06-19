package com.mu.io.game.packet.imp.player.pop;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DealPopup extends ReadAndWritePacket {
   public DealPopup(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int id = this.readInt();
      Popup pop = player.getPopup(id);
      if (pop != null) {
         int type = this.readByte();
         boolean showAgain = this.readBoolean();
         if (showAgain) {
            player.addPopNoAgain(pop.getType());
         }

         switch(type) {
         case 1:
            pop.dealLeftClick(player);
            break;
         case 2:
            pop.dealRightClick(player);
         }

         player.removePopup(id);
         pop.destroy();
      }

   }
}
