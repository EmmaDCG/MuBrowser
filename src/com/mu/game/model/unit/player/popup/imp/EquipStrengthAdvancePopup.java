package com.mu.game.model.unit.player.popup.imp;

public class EquipStrengthAdvancePopup extends EquipStrengthPrimaryPopup {
   public EquipStrengthAdvancePopup(int id, String des, long itemID, long luckyItemID, boolean useProtect, boolean useBind) {
      super(id, des, itemID, luckyItemID, useProtect, useBind);
   }

   public int getType() {
      return 14;
   }
}
