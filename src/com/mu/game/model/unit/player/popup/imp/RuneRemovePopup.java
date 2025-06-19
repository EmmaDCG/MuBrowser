package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.equip.RuneUnMosaic;
import com.mu.io.game.packet.imp.equip.StrengthEquipment;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RuneRemovePopup extends Popup {
   private long itemID;
   private int index;

   public RuneRemovePopup(int id, long itemID, int index) {
      super(id);
      this.itemID = itemID;
      this.index = index;
   }

   public String getTitle() {
      return MessageText.getText(4007);
   }

   public String getContent() {
      return MessageText.getText(4008);
   }

   public void dealLeftClick(Player player) {
      Item item = StrengthEquipment.getForgingItem(player, this.itemID);
      int result = player.getItemManager().deleRune(item, this.index).getResult();
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

      RuneUnMosaic.sendToClient(player, this.itemID, result == 1, this.index);
   }

   public void dealRightClick(Player player) {
      RuneUnMosaic.sendToClient(player, this.itemID, false, this.index);
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 15;
   }
}
