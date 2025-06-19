package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.sys.OpenPanel;

public class MallIngotPopup extends Popup {
   public MallIngotPopup(int id) {
      super(id);
   }

   public String getTitle() {
      return MessageText.getText(23009);
   }

   public String getContent() {
      return MessageText.getText(23008);
   }

   public String getLeftButtonContent() {
      return MessageText.getText(23007);
   }

   public void dealLeftClick(Player player) {
      OpenPanel.open(player, 174, 0);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 19;
   }
}
