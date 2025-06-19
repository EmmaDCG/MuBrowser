package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.friend.Friend;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class DeleteFriendPopup extends Popup {
   private Friend friend;

   public DeleteFriendPopup(int id, Friend friend) {
      super(id);
      this.friend = friend;
   }

   public String getContent() {
      String name = this.friend.getName();
      String msg = null;
      switch(this.friend.getType()) {
      case 0:
         msg = FriendManager.getDelFriendDes();
         break;
      case 1:
         msg = MessageText.getText(1058);
         break;
      case 2:
         msg = MessageText.getText(1059);
      }

      return msg == null ? MessageText.getText(1060) : msg.replace("%s%", name);
   }

   public void dealLeftClick(Player player) {
      player.getFriendManager().doDelFriend(this.friend.getId(), this.friend.getType());
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 37;
   }
}
