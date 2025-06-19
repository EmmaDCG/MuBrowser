package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class TranseferMasterPopup extends Popup {
   private long rid;

   public TranseferMasterPopup(int id, long rid) {
      super(id);
      this.rid = rid;
   }

   public String getTitle() {
      return MessageText.getText(9067);
   }

   public String getContent() {
      GangMember member = GangManager.getMember(this.rid);
      String name = "";
      if (member != null) {
         name = member.getName();
      }

      return MessageText.getText(9066).replace("%s%", name);
   }

   public void dealLeftClick(Player player) {
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(player, 13, this.rid);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 32;
   }
}
