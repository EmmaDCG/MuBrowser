package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class ShowRewardItem extends WriteOnlyPacket {
   public ShowRewardItem() {
      super(48006);
   }

   public static void showItems(ArrayList list, Player player, int type) {
      try {
         ShowRewardItem si = new ShowRewardItem();
         si.writeByte(type);
         si.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Item item = (Item)var5.next();
            GetItemStats.writeItem(item, si);
         }

         player.writePacket(si);
         si.destroy();
         si = null;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
