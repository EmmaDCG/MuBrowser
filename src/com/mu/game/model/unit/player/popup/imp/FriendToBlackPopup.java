package com.mu.game.model.unit.player.popup.imp;

import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class FriendToBlackPopup extends Popup {
   private Player other;

   public FriendToBlackPopup(int id, Player other) {
      super(id);
      this.other = other;
   }

   public String getContent() {
      return FriendManager.getFriendToBlackDes().replace("%s%", this.other.getName());
   }

   public void dealLeftClick(Player player) {
      player.getFriendManager().addBlack(this.other);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 38;
   }
}
