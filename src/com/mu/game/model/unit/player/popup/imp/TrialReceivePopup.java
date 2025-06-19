package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.trial.TrialReward;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class TrialReceivePopup extends Popup {
   private TrialReward reward = null;

   public TrialReceivePopup(int id, TrialReward reward) {
      super(id);
      this.reward = reward;
   }

   public String getTitle() {
      return MessageText.getText(14030);
   }

   public String getContent() {
      return MessageText.getText(14031).replace("%s%", String.valueOf(this.reward.getConsume()));
   }

   public void dealLeftClick(Player player) {
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 11;
   }
}
