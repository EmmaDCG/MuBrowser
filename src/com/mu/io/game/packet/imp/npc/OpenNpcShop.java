package com.mu.io.game.packet.imp.npc;

import com.mu.game.model.shop.Goods;
import com.mu.game.model.shop.ShopConfigure;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

public class OpenNpcShop extends WriteOnlyPacket {
   public OpenNpcShop() {
      super(10406);
   }

   public void setData(boolean addRepurchage, int shopId, String name, Player player) {
      SortedMap goodMaps = ShopConfigure.getShopItems(shopId);

      try {
         this.writeUTF(name);
         this.writeByte(shopId);
         int labelSize = goodMaps == null ? 0 : goodMaps.size();
         this.writeByte(labelSize);
         if (goodMaps != null) {
            Iterator it = goodMaps.entrySet().iterator();

            while(it.hasNext()) {
               Entry entry = (Entry)it.next();
               int labelID = ((Integer)entry.getKey()).intValue();
               List goodList = (List)entry.getValue();
               this.writeUTF(ShopConfigure.getLalelName(labelID));
               this.writeByte(ShopConfigure.getLabelSort(labelID));
               this.writeShort(goodList.size());
               Iterator var12 = goodList.iterator();

               while(var12.hasNext()) {
                  Goods good = (Goods)var12.next();
                  GetItemStats.writeItem(good.getGoodItem(), this);
               }
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public static void sendToClient(Player player, boolean addRepurchage, int shopId, String name) {
      OpenNpcShop ons = new OpenNpcShop();
      ons.setData(addRepurchage, shopId, name, player);
      player.writePacket(ons);
      ons.destroy();
      ons = null;
   }
}
