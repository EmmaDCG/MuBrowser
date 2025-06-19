package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.item.Item;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;

public class OpenShortcutBuyAndUse extends WriteOnlyPacket {
   public OpenShortcutBuyAndUse() {
      super(10031);
   }

   public static void sendToClient(Player player, int key, int panelID) {
      try {
         ShortcutBuyPanel panel = ShortcutBuyPanel.getShortcutBuyPanel(panelID);
         if (panel == null) {
            return;
         }

         OpenShortcutBuyAndUse osbu = new OpenShortcutBuyAndUse();
         osbu.writeInt(key);
         osbu.writeUTF(panel.getTitle());
         List itemList = panel.getItemList();
         osbu.writeByte(itemList.size());
         Iterator var7 = itemList.iterator();

         while(var7.hasNext()) {
            Item item = (Item)var7.next();
            GetItemStats.writeItem(item, osbu);
         }

         player.writePacket(osbu);
         osbu.destroy();
         osbu = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
