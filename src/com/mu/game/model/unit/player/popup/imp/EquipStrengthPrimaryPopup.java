package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.equip.StrengthEquipment;

public class EquipStrengthPrimaryPopup extends Popup {
   private String des;
   private long itemID;
   private long luckyItemID;
   private boolean useProtect;
   private boolean useBind;

   public EquipStrengthPrimaryPopup(int id, String des, long itemID, long luckyItemID, boolean useProtect, boolean useBind) {
      super(id);
      this.itemID = itemID;
      this.luckyItemID = luckyItemID;
      this.useProtect = useProtect;
      this.useBind = useBind;
      this.des = des;
   }

   public String getTitle() {
      return MessageText.getText(4009);
   }

   public String getContent() {
      return this.des;
   }

   public void dealLeftClick(Player player) {
      StrengthEquipment.doStrength(player, false, this.itemID, this.luckyItemID, this.useProtect, this.useBind);
   }

   public void dealRightClick(Player player) {
      StrengthEquipment.sendToClient(player, this.itemID, false, 2, this.luckyItemID);
   }

   public boolean isShowAgain(Player player) {
      return true;
   }

   public int getType() {
      return 12;
   }
}
