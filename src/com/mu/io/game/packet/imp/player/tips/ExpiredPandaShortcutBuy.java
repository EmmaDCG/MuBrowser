package com.mu.io.game.packet.imp.player.tips;

import com.mu.game.model.item.other.ExpiredItemManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.mall.OpenShortcutBuyAndUse;

public class ExpiredPandaShortcutBuy extends ReadAndWritePacket {
   public ExpiredPandaShortcutBuy(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int type = this.readByte();
      int key = this.readInt();
      int shortcutID = ExpiredItemManager.getShortcutID(type);
      if (shortcutID != -1) {
         OpenShortcutBuyAndUse.sendToClient(player, key, shortcutID);
      }
   }
}
