package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;

public class DunTicketPopup extends Popup {
   private int templateId;
   private Item ticket;

   public DunTicketPopup(int id, int templateId, Item ticket) {
      super(id);
      this.templateId = templateId;
      this.ticket = ticket;
   }

   public String getTitle() {
      return "";
   }

   public String getContent() {
      return MessageText.getText(23010).replace("%s%", "#F{c=" + ItemColor.find(this.ticket.getQuality()).getColor() + "}" + this.ticket.getName() + "#F");
   }

   public void dealLeftClick(Player player) {
      DungeonTemplateFactory.getTemplate(this.templateId).writeShortcutBuyTicket(player, 0);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 31;
   }
}
