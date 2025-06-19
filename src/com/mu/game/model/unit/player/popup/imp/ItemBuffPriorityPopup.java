package com.mu.game.model.unit.player.popup.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ItemBuffPriorityPopup extends Popup {
   private long itemID;
   private String title;
   private String content;
   private int count;

   public ItemBuffPriorityPopup(int id, String title, String content, long itemID, int count) {
      super(id);
      this.itemID = itemID;
      this.title = title;
      this.content = content;
      this.count = count;
   }

   public String getTitle() {
      return this.title;
   }

   public String getContent() {
      return this.content;
   }

   public void dealLeftClick(Player player) {
      Item item = player.getBackpack().getItemByID(this.itemID);
      if (item == null) {
         SystemMessage.writeMessage(player, 3002);
      } else {
         player.getItemManager().useItem(item, this.count, true);
      }
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 18;
   }
}
