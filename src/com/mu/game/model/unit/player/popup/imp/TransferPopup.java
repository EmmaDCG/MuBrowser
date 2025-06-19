package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class TransferPopup extends Popup {
   private Transfer transfer;

   public TransferPopup(int id, Transfer transfer) {
      super(id);
      this.transfer = transfer;
   }

   public String getTitle() {
      return MessageText.getText(4023);
   }

   public String getContent() {
      return MessageText.getText(4024).replaceFirst("%price%", String.valueOf(this.transfer.getAKeyCompleteExpend())).replaceFirst("%name%", this.transfer.getName());
   }

   public void dealLeftClick(Player player) {
      player.aKeyCompleteTransfer();
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 27;
   }
}
