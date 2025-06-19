package com.mu.io.game.packet.imp.financing;

import com.mu.game.model.financing.FinancingConfigManager;
import com.mu.game.model.financing.FinancingItemData;
import com.mu.game.model.unit.player.popup.imp.FinancingPopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;

public class FinancingBuy extends ReadAndWritePacket {
   public FinancingBuy(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readByte();
      FinancingItemData data = FinancingConfigManager.getItemData(id);
      if (data != null) {
         ShowPopup.open(this.getPlayer(), new FinancingPopup(this.getPlayer().createPopupID(), data));
      }
   }
}
