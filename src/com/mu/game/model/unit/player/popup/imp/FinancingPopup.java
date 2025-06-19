package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.financing.FinancingItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class FinancingPopup extends Popup {
   private FinancingItemData data;

   public FinancingPopup(int id, FinancingItemData data) {
      super(id);
      this.data = data;
   }

   public String getTitle() {
      return MessageText.getText(4021);
   }

   public String getContent() {
      return MessageText.getText(4022).replaceFirst("%price%", String.valueOf(this.data.getPrice())).replaceFirst("%name%", this.data.getName());
   }

   public void dealLeftClick(Player player) {
      player.getFinancingManager().buyItem(this.data);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 26;
   }
}
