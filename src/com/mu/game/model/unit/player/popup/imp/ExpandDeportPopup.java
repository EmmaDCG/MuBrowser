package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.item.container.DeportExpandData;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.storage.DeportCount;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ExpandDeportPopup extends Popup {
   private int addedPage;
   private String itemStr;
   private String itemCountStr;
   private String pageStr;

   public ExpandDeportPopup(int id, int addedPage, DeportExpandData data) {
      super(id);
      this.addedPage = addedPage;
      this.itemStr = ItemModel.getModel(data.getItemID()).getName();
      this.itemCountStr = String.valueOf(data.getCount());
      this.pageStr = String.valueOf(data.getPage() + 1);
   }

   public String getTitle() {
      return MessageText.getText(4005);
   }

   public String getContent() {
      String s = MessageText.getText(4006);
      s = s.replaceAll("SS", this.itemCountStr);
      s = s.replaceAll("daoju", this.itemStr);
      s = s.replaceAll("XX", this.pageStr);
      return s;
   }

   public void dealLeftClick(Player player) {
      Storage deport = player.getDepot();
      int result = DeportExpandData.addPage(player);
      if (result == 1) {
         result = deport.expansion(player, this.addedPage);
      }

      DeportCount.sendToClient(player, 4, deport.getPage());
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 10;
   }
}
