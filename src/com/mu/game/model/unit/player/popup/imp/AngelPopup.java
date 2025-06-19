package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewItemLink;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ShowItemManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.OpenVipShop;

public class AngelPopup extends Popup {
   private Item angelItem = null;
   private long angelID = -1L;

   public AngelPopup(int id, Item angelItem) {
      super(id);
      this.angelItem = angelItem;
      this.angelID = angelItem.getID();
   }

   public String getTitle() {
      String s = MessageText.getText(4028);
      return s;
   }

   public String getContent() {
      String s = MessageText.getText(4029);
      return s;
   }

   public void dealLeftClick(Player player) {
      ShowItemManager.removeAddDestroyItem(this.angelID);
      OpenVipShop.openVipShop(player);
   }

   public void dealRightClick(Player player) {
      ShowItemManager.removeAddDestroyItem(this.angelID);
   }

   public void writeContent(WriteOnlyPacket packet, Player player) throws Exception {
      String tmpStr = this.getContent();
      NewItemLink link = new NewItemLink(0, this.angelItem.getID(), this.angelItem.getName(), this.angelItem.getQuality(), false);
      tmpStr = tmpStr.replace("%s%", link.getContent());
      ChatProcess.writeNewLinkMessage(tmpStr, new NewChatLink[]{link}, packet);
      link.destroy();
      ShowItemManager.addShowItem(this.angelItem);
      this.angelItem = null;
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 30;
   }
}
