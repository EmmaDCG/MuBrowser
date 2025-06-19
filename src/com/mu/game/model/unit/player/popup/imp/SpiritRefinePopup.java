package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.spiritOfWar.SpiritRefine;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SpiritRefinePopup extends Popup {
   private int needIngot;

   public SpiritRefinePopup(int id, int needIngot) {
      super(id);
      this.needIngot = needIngot;
   }

   public String getTitle() {
      return MessageText.getText(4036);
   }

   public String getContent() {
      String s = MessageText.getText(4037);
      s = s.replace("%s%", "" + this.needIngot);
      return s;
   }

   public void dealLeftClick(Player player) {
      int result = player.getSpiritManager().refineByIngot(true);
      if (result == 1) {
         SpiritRefine.sendToClient(player, 0);
      } else {
         SystemMessage.writeMessage(player, result);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 40;
   }
}
