package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.RedPacketInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class RedPacketPopup extends Popup {
   private RedPacketInfo info = null;

   public RedPacketPopup(int id, RedPacketInfo info) {
      super(id);
      this.info = info;
   }

   public String getTitle() {
      return MessageText.getText(9069);
   }

   public String getContent() {
      return this.info.getType() == 0 ? MessageText.getText(9070).replace("%s%", String.valueOf(this.info.getIngot())) : MessageText.getText(9071).replace("%s%", String.valueOf(this.info.getIngot()));
   }

   public void dealLeftClick(Player player) {
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doSendRedPacket(player, this.info);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 34;
   }
}
