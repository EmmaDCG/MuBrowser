package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class AddToBlackPopup extends Popup {
   private Player other;

   public AddToBlackPopup(int id, Player other) {
      super(id);
      this.other = other;
   }

   public String getContent() {
      return MessageText.getText(1076).replace("%s%", this.other.getName());
   }

   public void dealLeftClick(Player player) {
      player.getFriendManager().addBlack(this.other);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 39;
   }
}
