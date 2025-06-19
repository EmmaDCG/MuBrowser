package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.equip.StoneUnMosaic;

public class UnMosaicStonePopup extends Popup {
   private long itemID;
   private int index;
   private String des;

   public UnMosaicStonePopup(int id, long itemID, int index, String des) {
      super(id);
      this.index = index;
      this.itemID = itemID;
      this.des = des;
   }

   public String getTitle() {
      return MessageText.getText(4019);
   }

   public String getContent() {
      String s = MessageText.getText(4020);
      s = s.replace("%s%", this.des);
      return s;
   }

   public void dealLeftClick(Player player) {
      StoneUnMosaic.doUnMosaic(player, this.itemID, this.index);
   }

   public void dealRightClick(Player player) {
      StoneUnMosaic.sendToClient(player, this.itemID, 5051, this.index);
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 25;
   }
}
