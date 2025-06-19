package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.DunRecoveryInfo;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.game.model.unit.player.popup.Popup;

public class RecoverPopup extends Popup {
   private int dunId;
   private int times;

   public RecoverPopup(int id, int dunId, int times) {
      super(id);
      this.dunId = dunId;
      this.times = times;
   }

   public String getTitle() {
      DunRecoveryInfo info = OfflineManager.getDunRecoveryInfo(this.dunId);
      return info == null ? MessageText.getText(14051) : MessageText.getText(14049).replace("%s%", info.getName());
   }

   public String getContent() {
      DunRecoveryInfo info = OfflineManager.getDunRecoveryInfo(this.dunId);
      if (info == null) {
         return MessageText.getText(14051);
      } else {
         int bindIngot = info.getBindIngot() * this.times;
         int ingot = info.getIngot() * this.times;
         return MessageText.getText(14050).replace("%b%", String.valueOf(bindIngot)).replace("%i%", String.valueOf(ingot)).replace("%t%", String.valueOf(this.times)).replace("%d%", info.getName());
      }
   }

   public void dealLeftClick(Player player) {
      player.getOffLineManager().doRecover(this.dunId, this.times);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 33;
   }
}
