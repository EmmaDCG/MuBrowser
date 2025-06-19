package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class DungeonQuitPopup extends Popup {
   private String title = null;
   private String content = null;

   public DungeonQuitPopup(int id) {
      super(id);
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getTitle() {
      return this.title == null ? MessageText.getText(14035) : this.title;
   }

   public String getContent() {
      return this.content == null ? MessageText.getText(14036) : this.content;
   }

   public void dealLeftClick(Player player) {
      if (player.isInDungeon()) {
         player.getDungeonMap().getDungeon().exitForInitiative(player, true);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public void destroy() {
      this.title = null;
      this.content = null;
      super.destroy();
   }

   public int getType() {
      return 21;
   }
}
