package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class QuitGangPop extends Popup {
   private boolean isDisbind = false;

   public QuitGangPop(int id, boolean isDisbind) {
      super(id);
      this.isDisbind = isDisbind;
   }

   public String getTitle() {
      return this.isDisbind ? MessageText.getText(9043) : MessageText.getText(9012);
   }

   public String getContent() {
      return this.isDisbind ? MessageText.getText(9044) : MessageText.getText(9013);
   }

   public void dealLeftClick(Player player) {
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(player, 7, this.isDisbind);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 3;
   }
}
