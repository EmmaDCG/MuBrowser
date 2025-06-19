package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class DungeonPromptTeamPopup extends Popup {
   private int dunTeamplteId = 0;

   public DungeonPromptTeamPopup(int id, int dunTemplateId) {
      super(id);
      this.dunTeamplteId = dunTemplateId;
   }

   public String getTitle() {
      return MessageText.getText(14021);
   }

   public String getContent() {
      return MessageText.getText(14022);
   }

   public void dealLeftClick(Player player) {
      DungeonManager.createAndEnterDungeon(player, this.dunTeamplteId, true);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 7;
   }
}
