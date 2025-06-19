package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.sys.OpenPanel;

public class PayPopup extends Popup {
   public PayPopup(int id) {
      super(id);
   }

   public String getTitle() {
      return MessageText.getText(1045);
   }

   public String getContent() {
      return MessageText.getText(1043);
   }

   public void dealLeftClick(Player player) {
      OpenPanel.open(player, 174, 0);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public String getLeftButtonContent() {
      return MessageText.getText(1044);
   }

   public int getType() {
      return 35;
   }
}
