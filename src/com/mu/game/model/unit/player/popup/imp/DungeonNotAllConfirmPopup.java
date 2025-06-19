package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class DungeonNotAllConfirmPopup extends Popup {
   private int templateId;

   public DungeonNotAllConfirmPopup(int id, int tid) {
      super(id);
      this.templateId = tid;
   }

   public String getTitle() {
      return MessageText.getText(14021);
   }

   public String getContent() {
      return MessageText.getText(14028);
   }

   public void dealLeftClick(Player player) {
      DungeonManager.createAndEnterDungeon(player, this.templateId, true);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 9;
   }
}
