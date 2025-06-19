package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.item.Item;
import com.mu.game.model.mall.MallConfigManager;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;

public class MallShowItem extends WriteOnlyPacket {
   public MallShowItem() {
      super(45103);
   }

   public static void show(Player player, long itemId, int count) {
      MallItemData data = MallConfigManager.getData(itemId);
      if (data != null) {
         try {
            Item item = data.getItem();
            MallShowItem mi = new MallShowItem();
            GetItemStats.writeItem(item, mi);
            mi.writeInt(count);
            player.writePacket(mi);
            mi.destroy();
            mi = null;
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }
}
